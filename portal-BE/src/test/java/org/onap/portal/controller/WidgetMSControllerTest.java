package org.onap.portal.controller;

import static org.junit.jupiter.api.Assertions.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.onap.portal.domain.dto.ecomp.PortalRestResponse;
import org.onap.portal.domain.dto.ecomp.PortalRestStatusEnum;
import org.onap.portal.framework.MockitoTestSuite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class WidgetMSControllerTest {

       @Autowired
       WidgetMSController widgetMSController;

       MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

       HttpServletRequest request = mockitoTestSuite.getMockedRequest();
       HttpServletResponse response = mockitoTestSuite.getMockedResponse();

       @Test
       void getServiceLocation() {
              PortalRestResponse<String> expected = new PortalRestResponse<>();
              expected.setMessage("Error!");
              expected.setResponse("Couldn't get the service location");
              expected.setStatus(PortalRestStatusEnum.ERROR);
              PortalRestResponse<String> actual = widgetMSController.getServiceLocation(request, response, "portal");
              assertEquals(expected.getMessage(), actual.getMessage());
              assertEquals(expected.getResponse(), actual.getResponse());
              assertEquals(expected.getStatus(), actual.getStatus());
       }
}