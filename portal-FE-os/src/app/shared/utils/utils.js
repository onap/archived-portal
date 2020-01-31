function getCookies() {
    var cookies = { };
    if (document.cookie && document.cookie != '') {
        var split = document.cookie.split(';');
        for (var i = 0; i < split.length; i++) {
            var name_value = split[i].split("=");
            name_value[0] = name_value[0].replace(/^ /, '');
            cookies[decodeURIComponent(name_value[0])] = decodeURIComponent(name_value[1]);
        }
    }
    return cookies;
}

function getContextRoot(){
	var pathName = window.location.pathname;
	var pathArray = pathName.split( '/' );
	var contextRoot='';
	if(pathArray.length!=0 && pathArray.length>=1)
		contextRoot = pathArray[1];
	return contextRoot;
}