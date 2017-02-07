package org.openecomp.portalapp.portal.service;

import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPUser;

public interface PersUserAppService {

	
	/**
	 * Sets the appropriate code in the user personalization table to indicate
	 * the application is (de)selected and/or pending.
	 * 
	 * @param user
	 *            EP User
	 * @param app
	 *            EP Application
	 * @param select
	 *            True or false
	 * @param select
	 *            True or false
	 */
	void setPersUserAppValue(EPUser user, EPApp app, boolean select, boolean pending);
	
}
