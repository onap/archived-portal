package org.onap.portal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.onap.portal.domain.db.ep.EpMicroservice;
import org.onap.portal.domain.db.ep.EpMicroserviceParameter;
import org.onap.portal.domain.db.ep.EpWidgetCatalogParameter;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.restTemplates.PortalWMSTemplate;
import org.onap.portal.service.microservice.EpMicroserviceService;
import org.onap.portal.service.widgetCatalogParameter.EpWidgetCatalogParameterService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class MicroserviceProxyService {

    private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MicroserviceProxyService.class);

    private static final String BASIC_AUTH = "Basic Authentication";
    private static final String NO_AUTH = "No Authentication";
    private static final String COOKIE_AUTH = "Cookie based Authentication";
    private static final String QUESTION_MARK = "?";
    private static final String ADD_MARK = "&";

    private RestTemplate template = new RestTemplate();

    private final EpMicroserviceService microserviceService;
    private final EpWidgetCatalogParameterService widgetParameterService;

    private final PortalWMSTemplate portalWMSTemplate;

    @Autowired
    public MicroserviceProxyService(EpMicroserviceService microserviceService,
        EpWidgetCatalogParameterService widgetParameterService, WidgetMService widgetMService,
        PortalWMSTemplate portalWMSTemplate) {
        this.microserviceService = microserviceService;
        this.widgetParameterService = widgetParameterService;
        this.portalWMSTemplate = portalWMSTemplate;
    }

    public String proxyToDestination(long serviceId, FnUser user, HttpServletRequest request) throws Exception {
        // get the microservice object by the id
        Optional<EpMicroservice> data = microserviceService.getById(serviceId);
        // No such microservice available
        // can we return a better response than null?
        return data
            .map(epMicroservice -> authenticateAndRespond(epMicroservice, request, composeParams(epMicroservice, user)))
            .orElse(null);
    }

    public String proxyToDestinationByWidgetId(long widgetId, FnUser user, HttpServletRequest request)
        throws Exception {
        ResponseEntity<Long> ans = portalWMSTemplate.proxyToDestinationByWidgetId(widgetId);
        Long serviceId = ans.getBody();
        // get the microservice object by the id
        Optional<EpMicroservice> data = microserviceService.getById(serviceId);
        // No such microservice available
        if (!data.isPresent()) {
            return null;
        }
        List<EpMicroserviceParameter> params = composeParams(data.get(), user);
        for (EpMicroserviceParameter p : params) {
            EpWidgetCatalogParameter userValue = widgetParameterService.getUserParamById(widgetId, user.getId(),
                p.getId());
            if (userValue != null) {
                p.setParaValue(userValue.getUserValue());
            }
        }
        return authenticateAndRespond(data.get(), request, params);
    }

    private String authenticateAndRespond(EpMicroservice data, HttpServletRequest request,
        List<EpMicroserviceParameter> params) throws HttpClientErrorException, IllegalArgumentException {
        String response = null;
        switch (data.getSecurityType()) {
            case NO_AUTH: {
                HttpEntity<String> entity = new HttpEntity<>(headersForNoAuth());
                String url = microserviceUrlConverter(data, params);
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "authenticateAndRespond: Before making no authentication call: {}", url);
                response = template.exchange(url, HttpMethod.GET, entity, String.class).getBody();
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "authenticateAndRespond: No authentication call response: {}",
                    response);
                break;
            }
            case BASIC_AUTH: {
                // encoding the username and password
                String plainCreds;
                try {
                    plainCreds = data.getUsername() + ":" + decryptedPassword(data.getPassword());
                } catch (Exception e) {
                    logger.error("authenticateAndRespond failed to decrypt password", e);
                    throw new IllegalArgumentException("Failed to decrypt password", e);
                }
                byte[] plainCredsBytes = plainCreds.getBytes();
                byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
                String base64Creds = new String(base64CredsBytes);

                HttpEntity<String> entity = new HttpEntity<>(headersForBasicAuth(request, base64Creds));

                String url = microserviceUrlConverter(data, params);
                try {
                    response = template.exchange(url, HttpMethod.GET, entity, String.class).getBody();
                } catch (HttpClientErrorException e) {
                    logger.error("authenticateAndRespond failed for basic security url " + url, e);
                    throw e;
                }
                break;
            }
            case COOKIE_AUTH: {
                HttpEntity<String> entity = new HttpEntity<>(headersForCookieAuth(request));
                String url = microserviceUrlConverter(data, params);
                try {
                    response = template.exchange(url, HttpMethod.GET, entity, String.class).getBody();
                } catch (HttpClientErrorException e) {
                    logger.error("authenticateAndRespond failed for cookie auth url " + url, e);
                    throw e;
                }
                break;
            }
        }

        return response;
    }

    private String decryptedPassword(String encryptedPwd) throws Exception {
        String result = "";
        if (encryptedPwd != null && encryptedPwd.length() > 0) {
            try {
                result = CipherUtil.decryptPKC(encryptedPwd,
                    SystemProperties.getProperty(SystemProperties.Decryption_Key));
            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger, "decryptedPassword failed", e);
                throw e;
            }
        }

        return result;
    }

    private String microserviceUrlConverter(EpMicroservice data, List<EpMicroserviceParameter> params) {
        String url = data.getEndpointUrl();
        for (int i = 0; i < params.size(); i++) {
            if (i == 0) {
                url += QUESTION_MARK;
            }
            url += params.get(i).getParaKey() + "=" + params.get(i).getParaValue();
            if (i != (params.size() - 1)) {
                url += ADD_MARK;
            }
        }

        return url;
    }

    private HttpHeaders headersForNoAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    // TODO: why is this generically named cookie used?
    private final static String Cookie = "Cookie";

    private HttpHeaders headersForBasicAuth(HttpServletRequest request, String base64Creds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String rawCookie = request.getHeader(Cookie);
        if (rawCookie != null) {
            headers.add(Cookie, rawCookie);
        }
        return headers;
    }

    private HttpHeaders headersForCookieAuth(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String rawCookie = request.getHeader(Cookie);
        if (rawCookie != null) {
            headers.add(Cookie, rawCookie);
        }
        return headers;
    }

    private List<EpMicroserviceParameter> composeParams(EpMicroservice data, FnUser user) {
        List<EpMicroserviceParameter> params = new ArrayList<>(data.getEpMicroserviceParameters());
        EpMicroserviceParameter userIdParam = new EpMicroserviceParameter();
        userIdParam.setParaKey("userId");
        userIdParam.setParaValue(user.getOrgUserId());
        params.add(userIdParam);
        return params;
    }
}
