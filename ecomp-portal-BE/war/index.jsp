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
 
 response.sendRedirect("welcome.htm");
 
 %>
 
 </body>
 </html>
 