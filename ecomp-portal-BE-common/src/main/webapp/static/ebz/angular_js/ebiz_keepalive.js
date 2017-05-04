function keepAlive(){
		alert("<%=VSPUtils.getEbizKeepAliveURL(request)%>");
		$.ajax({
			dataType: 'jsonp',
			url: "<%=VSPUtils.getEbizKeepAliveURL(request)%>", 
			jsonpCallback: 'JsonpCallback'		
		});
}