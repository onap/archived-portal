package org.onap.portal.restTemplates;

import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AAFTemplate {

    private final RestTemplate template = new RestTemplate();

    public ResponseEntity<String> addPortalAdminInAAF(HttpEntity<String> addUserRole){
        return template.exchange(
            SystemProperties.getProperty(
                EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                + "userRole",
            HttpMethod.POST, addUserRole, String.class);
    }

    public void deletePortalAdminFromAAF(final String name, final String extRole, final HttpEntity<String> addUserRole){
        template.exchange(
            SystemProperties.getProperty(
                EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                + "userRole/" + name + "/" + extRole,
            HttpMethod.DELETE, addUserRole, String.class);
    }
}
