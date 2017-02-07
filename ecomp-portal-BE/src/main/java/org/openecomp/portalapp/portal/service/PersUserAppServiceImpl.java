package org.openecomp.portalapp.portal.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EPUserApp;
import org.openecomp.portalapp.portal.domain.PersUserAppSelection;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;

@Service("persUserAppService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class PersUserAppServiceImpl implements PersUserAppService {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PersUserAppServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;
	@Autowired
	private AdminRolesService adminRolesService;
	@Autowired
	private UserRolesService userRolesService;


	@Override
	public void setPersUserAppValue(EPUser user, EPApp app, boolean select, boolean pending) {
		if (user == null || app == null)
			throw new IllegalArgumentException("setPersUserAppValue: Null values");

		// Find the record for this user-app combo, if any
		String filter = " where user_id = " + Long.toString(user.getId()) + " and app_id = "
				+ Long.toString(app.getId());
		@SuppressWarnings("unchecked")
		List<PersUserAppSelection> persList = dataAccessService.getList(PersUserAppSelection.class, filter, null, null);

		// Key constraint limits to 1 row
		PersUserAppSelection persRow = null;
		if (persList.size() == 1)
			persRow = persList.get(0);
		else
			persRow = new PersUserAppSelection(null, user.getId(), app.getId(), null);

		if (app.getOpen()) {
			// Pending status is not meaningful for open apps.
			if (pending)
				logger.error(EELFLoggerDelegate.errorLogger,
						"setPersUserAppValue: invalid request, ignoring set-pending for open app");

			// Open apps have same behavior for regular and admin users
			if (select) {
				// Selection of an open app requires a record
				persRow.setStatusCode("S"); // show
				dataAccessService.saveDomainObject(persRow, null);
			} else {
				// De-selection of an open app requires no record
				if (persRow.getId() != null)
					dataAccessService.deleteDomainObject(persRow, null);
			}
		} else {
			// Non-open app.

			// Pending overrides select.
			if (pending) {
				persRow.setStatusCode("P");
				dataAccessService.saveDomainObject(persRow, null);
			} else {
				// Behavior depends on Portal (super) admin status, bcos an
				// admin can force an app onto the dashboard.
				boolean isPortalAdmin = adminRolesService.isSuperAdmin(user);
				boolean adminUserHasAppRole = false;
				if (isPortalAdmin) {
					List<EPUserApp> roles = userRolesService.getCachedAppRolesForUser(app.getId(), user.getId());
					adminUserHasAppRole = (roles.size() > 0);
					logger.debug(EELFLoggerDelegate.debugLogger, "setPersUserAppValue: app {}, admin user {}, role count {}",
							app.getId(), user.getId(), roles.size());
				}

				if (select) {
					if (isPortalAdmin && !adminUserHasAppRole) {
						// The special case: portal admin, no role
						persRow.setStatusCode("S"); // show
						dataAccessService.saveDomainObject(persRow, null);
					} else {
						// User has role-based access to the app.
						// Showing an accessible app requires no record.
						if (persRow.getId() != null)
							dataAccessService.deleteDomainObject(persRow, null);
					}
				} // select
				else {
					if (isPortalAdmin && !adminUserHasAppRole) {
						// The special case: portal admin, no role
						if (persRow.getId() != null)
							dataAccessService.deleteDomainObject(persRow, null);
					} else {
						// User has role-based access to the app.
						// Hiding an accessible app requires a record
						persRow.setStatusCode("H"); // hide
						dataAccessService.saveDomainObject(persRow, null);
					}
				} // deselect
			}
		}
	}

}