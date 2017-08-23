package org.openecomp.portalapp.portal.framework;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openecomp.portalapp.portal.test.controller.AppCatalogControllerTest;
import org.openecomp.portalapp.portal.test.controller.AppContactUsControllerTest;
import org.openecomp.portalapp.portal.test.controller.AppsControllerExternalRequestTest;
import org.openecomp.portalapp.portal.test.controller.AppsControllerTest;
import org.openecomp.portalapp.portal.test.controller.BEPropertyReaderControllerTest;
import org.openecomp.portalapp.portal.test.controller.BasicAuthAccountControllerTest;
import org.openecomp.portalapp.portal.test.controller.CommonWidgetControllerTest;
import org.openecomp.portalapp.portal.test.controller.ConsulClientControllerTest;
import org.openecomp.portalapp.portal.test.controller.DashboardSearchResultControllerTest;
import org.openecomp.portalapp.portal.test.controller.ExternalAppsRestfulControllerTest;
import org.openecomp.portalapp.portal.test.controller.FunctionalMenuControllerTest;
import org.openecomp.portalapp.portal.test.controller.GetAccessControllerTest;
import org.openecomp.portalapp.portal.test.controller.ManifestControllerTest;
import org.openecomp.portalapp.portal.test.controller.MicroserviceControllerTest;
import org.openecomp.portalapp.portal.test.controller.MicroserviceProxyControllerTest;
import org.openecomp.portalapp.portal.test.controller.PortalAdminControllerTest;
import org.openecomp.portalapp.portal.test.controller.RolesApprovalSystemControllerTest;
import org.openecomp.portalapp.portal.test.controller.TicketEventControllerTest;
import org.openecomp.portalapp.portal.test.controller.UserControllerTest;
import org.openecomp.portalapp.portal.test.controller.UserNotificationControllerTest;
import org.openecomp.portalapp.portal.test.controller.UserRolesControllerTest;
import org.openecomp.portalapp.portal.test.controller.WebAnalyticsExtAppControllerTest;
import org.openecomp.portalapp.portal.test.controller.WidgetsCatalogMarkupControllerTest;



/**
 * 
 * 
 * Create the Test class in ecmop-portal-BE-common-test and extend from MockitoTestSuite
 * Add the class in Suite to test it
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
UserRolesControllerTest.class,
FunctionalMenuControllerTest.class,
AppCatalogControllerTest.class,
AppContactUsControllerTest.class,
UserNotificationControllerTest.class,
TicketEventControllerTest.class,
CommonWidgetControllerTest.class,
ConsulClientControllerTest.class,
GetAccessControllerTest.class,
AppsControllerTest.class,
BasicAuthAccountControllerTest.class,
DashboardSearchResultControllerTest.class,
PortalAdminControllerTest.class,
ManifestControllerTest.class,
BEPropertyReaderControllerTest.class,
WebAnalyticsExtAppControllerTest.class,
AppsControllerExternalRequestTest.class,
WidgetsCatalogMarkupControllerTest.class,
RolesApprovalSystemControllerTest.class,
MicroserviceProxyControllerTest.class,
MicroserviceControllerTest.class,
ExternalAppsRestfulControllerTest.class,
UserControllerTest.class
})
public class MockTestSuite {

}

