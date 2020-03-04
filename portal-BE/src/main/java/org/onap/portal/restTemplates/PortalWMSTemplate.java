package org.onap.portal.restTemplates;

import org.onap.portal.domain.dto.ecomp.WidgetServiceHeaders;
import org.onap.portal.service.WidgetMService;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PortalWMSTemplate {

    private final RestTemplate template = new RestTemplate();

    private final WidgetMService widgetMService;

    @Autowired
    public PortalWMSTemplate(WidgetMService widgetMService) {
        this.widgetMService = widgetMService;
    }


    @SuppressWarnings("rawtypes")
    public ResponseEntity<Long> proxyToDestinationByWidgetId(long widgetId) throws Exception {
        return template.exchange(
            EcompPortalUtils.widgetMsProtocol() + "://"
                + widgetMService.getServiceLocation("widgets-service",
                SystemProperties.getProperty("microservices.widget.local.port"))
                + "/widget/microservices/widgetCatalog/parameters/" + widgetId,
            HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), Long.class);
    }
}
