package org.onap.portalapp.widget.utils;

import java.nio.charset.Charset;
import java.util.Base64;

public class AuthorizationUtil {

	public boolean authorization(String auth, String security_user, String security_pass){
		if (auth != null && auth.startsWith("Basic")) {		
	        String base64Credentials = auth.substring("Basic".length()).trim();
	        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
	                Charset.forName("UTF-8"));
	        final String[] values = credentials.split(":",2);
	        if(security_user.equals(values[0]) && security_pass.equals(values[1]))
	        	return true;
		}
		return false;
	}
}
