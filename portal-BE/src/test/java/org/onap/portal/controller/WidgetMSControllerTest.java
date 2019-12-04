/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 *
 */

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

       final MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

       final HttpServletRequest request = mockitoTestSuite.getMockedRequest();
       final HttpServletResponse response = mockitoTestSuite.getMockedResponse();

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