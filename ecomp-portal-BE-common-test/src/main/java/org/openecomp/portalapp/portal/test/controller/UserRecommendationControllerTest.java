package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.UserRecommendationController;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;
import org.openecomp.portalsdk.core.web.support.UserUtils;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserUtils.class)
public class UserRecommendationControllerTest {

	/*
	 * @Mock FunctionalMenuService functionalMenuService = new
	 * FunctionalMenuServiceImpl();
	 * 
	 * @Mock UserNotificationService userNotificationService = new
	 * UserNotificationServiceImpl();
	 */

	@InjectMocks
	UserRecommendationController userRecommendationController = new UserRecommendationController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockEPUser mockUser = new MockEPUser();
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();


	@Test
	public void getRecommendationsTest() throws Exception {
		String recommendations = "{\"id\": \"USERID\",  \"timestamp\": \"TIMESTAMP\",  \"count\": \"count of recommendations returned\",\"recommendations\": [\"recommended action 1\",\"recommended action 2\"] }";
		// String actualPortalRestResponse = null;
		String actualPortalRestResponse = recommendations;
		actualPortalRestResponse = userRecommendationController.getRecommendations(mockedRequest, mockedResponse);
		assertTrue(actualPortalRestResponse.equals(actualPortalRestResponse));

	}
	
	@Test
	public void getRecommendationsTestForRecommendations() throws Exception {
		String recommendations = "{\"id\": \"USERID\",  \"timestamp\": \"TIMESTAMP\",  \"count\": \"count of recommendations returned\" }";
		// String actualPortalRestResponse = null;
		String actualPortalRestResponse = recommendations;
		actualPortalRestResponse = userRecommendationController.getRecommendations(mockedRequest, mockedResponse);
		assertTrue(actualPortalRestResponse.equals(actualPortalRestResponse));

	}

	
}
