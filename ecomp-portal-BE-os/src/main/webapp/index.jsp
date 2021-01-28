<%--
  ============LICENSE_START==========================================
  ONAP Portal
  ===================================================================
  Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
  ===================================================================
 
  Unless otherwise specified, all software contained herein is licensed
  under the Apache License, Version 2.0 (the "License");
  you may not use this software except in compliance with the License.
  You may obtain a copy of the License at
 
              http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 
  Unless otherwise specified, all documentation contained herein is licensed
  under the Creative Commons License, Attribution 4.0 Intl. (the "License");
  you may not use this documentation except in compliance with the License.
  You may obtain a copy of the License at
 
              https://creativecommons.org/licenses/by/4.0/
 
  Unless required by applicable law or agreed to in writing, documentation
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 
  ============LICENSE_END============================================
 
  
  --%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Redirected because we can't set the welcome page to a virtual URL. --%>
<%-- <c:redirect url="/login.htm"/>
 --%>
 <html>
 <head>
 <title>ecompportal-BE index.jsp</title>
 </head>
 <body>
 <h2>ECOMP Portal Core</h2>
 This is the ecompportal-BE application, page index.jsp.
 
 <% 
 
 response.sendRedirect("index.htm");
 
 %>
 
 </body>
 </html>
 
