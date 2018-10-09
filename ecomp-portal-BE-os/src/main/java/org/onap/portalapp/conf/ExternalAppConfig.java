/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.conf;

import static com.att.eelf.configuration.Configuration.MDC_ALERT_SEVERITY;
import static com.att.eelf.configuration.Configuration.MDC_INSTANCE_UUID;
import static com.att.eelf.configuration.Configuration.MDC_SERVER_FQDN;
import static com.att.eelf.configuration.Configuration.MDC_SERVER_IP_ADDRESS;
import static com.att.eelf.configuration.Configuration.MDC_SERVICE_INSTANCE_ID;
import static com.att.eelf.configuration.Configuration.MDC_SERVICE_NAME;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.portalapp.authentication.LoginStrategy;
import org.onap.portalapp.authentication.OpenIdConnectLoginStrategy;
import org.onap.portalapp.authentication.SimpleLoginStrategy;
import org.onap.portalapp.controller.core.LogoutController;
import org.onap.portalapp.controller.core.SDKLoginController;
import org.onap.portalapp.music.conf.MusicSessionConfig;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserApp;
import org.onap.portalapp.portal.interceptor.PortalResourceInterceptor;
import org.onap.portalapp.portal.interceptor.SessionTimeoutInterceptor;
import org.onap.portalapp.portal.listener.HealthMonitor;
import org.onap.portalapp.portal.service.EPLoginService;
import org.onap.portalapp.portal.service.EPLoginServiceImpl;
import org.onap.portalapp.portal.service.ExternalAccessRolesService;
import org.onap.portalapp.portal.service.UserRolesService;
import org.onap.portalapp.portal.transport.ExternalAuthUserRole;
import org.onap.portalapp.portal.transport.ExternalRoleDescription;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EPSystemProperties;
import org.onap.portalapp.scheduler.RegistryAdapter;
import org.onap.portalapp.uebhandler.FunctionalMenuHandler;
import org.onap.portalapp.uebhandler.InitUebHandler;
import org.onap.portalapp.uebhandler.MainUebHandler;
import org.onap.portalapp.uebhandler.WidgetNotificationHandler;
import org.onap.portalsdk.core.conf.AppConfig;
import org.onap.portalsdk.core.conf.Configurable;
import org.onap.portalsdk.core.logging.format.AlarmSeverityEnum;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.objectcache.AbstractCacheManager;
import org.onap.portalsdk.core.onboarding.util.PortalApiConstants;
import org.onap.portalsdk.core.onboarding.util.PortalApiProperties;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.FnMenuService;
import org.onap.portalsdk.core.service.FnMenuServiceImpl;
import org.onap.portalsdk.core.util.CacheManager;
import org.onap.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.onap"}, excludeFilters = {
		@Filter(value = { LogoutController.class, SDKLoginController.class}, type = FilterType.ASSIGNABLE_TYPE) })
@Profile("src")
@EnableAsync
@EnableScheduling
@Import({ MusicSessionConfig.class })
public class ExternalAppConfig extends AppConfig implements Configurable {

	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAppConfig.class);

	@Autowired
	private DataAccessService dataAccessService;

	@Autowired
	private UserRolesService userRolesService;

	@Autowired
	private ExternalAccessRolesService externalAccessRolesService;

	private RegistryAdapter schedulerRegistryAdapter;

	String uebAppKey = PortalApiProperties.getProperty(PortalApiConstants.UEB_APP_KEY); 

	public ViewResolver viewResolver() {
		return super.viewResolver();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/app/fusion/**").addResourceLocations("/app/fusion/");
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		registry.addResourceHandler("/images/**").addResourceLocations("/images/");
		registry.addResourceHandler("/**").addResourceLocations("/public/");
	}

	@PostConstruct
	private void init() {
		String remotecentralizedsystemaccess = SystemProperties.getProperty(EPCommonSystemProperties.REMOTE_CENTRALIZED_SYSTEM_ACCESS);
		try {
			// Loading defaults
			MDC.put(MDC_SERVICE_NAME, EPSystemProperties.ECOMP_PORTAL_BE);
			MDC.put(MDC_SERVER_FQDN, InetAddress.getLocalHost().getHostName());
			MDC.put(MDC_SERVER_IP_ADDRESS, InetAddress.getLocalHost().getHostAddress());
			MDC.put(MDC_SERVICE_INSTANCE_ID, "");
			MDC.put(MDC_ALERT_SEVERITY, AlarmSeverityEnum.INFORMATIONAL.severity());
			MDC.put(MDC_INSTANCE_UUID, SystemProperties.getProperty(SystemProperties.INSTANCE_UUID));
			
			if("true".equalsIgnoreCase(remotecentralizedsystemaccess)){
				importFromExternalAuth();
			}			
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "init failed", e);
		}
	}

	
	private void importFromExternalAuth() throws Exception {
		JSONArray aafAppRoles = new JSONArray();
		JSONArray aafUserList = new JSONArray();
		List<EPApp> appList;
		//to get all centralized apps		
		List<EPApp> centralizedAppList = dataAccessService.executeNamedQuery("getCentralizedApps", null, null);		
		if(centralizedAppList != null && !centralizedAppList.isEmpty()){
			for(int i = 0; i < centralizedAppList.size(); i++){
				//syncRoles(does a sync on functions, roles and role functions)
				externalAccessRolesService.syncApplicationRolesWithEcompDB(centralizedAppList.get(i));
				//retrieve roles based on NS
				aafAppRoles = externalAccessRolesService.getAppRolesJSONFromExtAuthSystem(centralizedAppList.get(i));
				if(aafAppRoles != null && aafAppRoles.length() > 0){
					for(int j = 0; j < aafAppRoles.length(); j++){
						ObjectMapper mapper = new ObjectMapper();
						String name = aafAppRoles.getJSONObject(j).getString("name");
						//String desc = aafAppRoles.getJSONObject(j).getString("description");
						//ExternalRoleDescription externalRoleDescription = mapper.readValue(desc, ExternalRoleDescription.class);
						aafUserList = externalAccessRolesService.getAllUsersByRole(name);	
						if(aafUserList != null && aafUserList.length() > 0){
							for(int k = 0; k < aafUserList.length(); k++){
								EPUser user = null;
								List<EPUser> usersList = null;								
								List<EPUserApp> userRolesList = new ArrayList<>();
								JSONObject userRole = (JSONObject) aafUserList.get(k);
								Gson gson = new Gson();
								ExternalAuthUserRole userRoleObj = gson.fromJson(userRole.toString(), ExternalAuthUserRole.class);
								if(userRoleObj.getUser() != null){
									userRoleObj.setUser(userRoleObj.getUser().substring(0, userRoleObj.getUser().indexOf("@")));
								}							
								//for each role and user in that role, check if user exists in fn_user. If not, add 
								Map<String, String> orgUserId = new HashMap<>();
								orgUserId.put("orgUserIdValue", userRoleObj.getUser());
								usersList = dataAccessService.executeNamedQuery("epUserAppId", orgUserId, null);
								if(usersList != null && !usersList.isEmpty()){
									user = usersList.get(0);
								}							
								if(user == null){
									// add user to fn_user(needs to be revisited after getting user info from AAF PORTAL-172) 								
								}
							}
						}

					}
				}
			}			
		}
	}

	public DataAccessService dataAccessService() {
		return super.dataAccessService();
	}

	@Override
	public String[] tileDefinitions() {
		return super.tileDefinitions();
	}

	public List<String> addTileDefinitions() {
		List<String> definitions = new ArrayList<>();
		definitions.add("/WEB-INF/defs/definitions.xml");
		return definitions;
	}

	@Bean
	public AbstractCacheManager cacheManager() {
		return new CacheManager();
	}

	@Bean
	public SessionTimeoutInterceptor sessionTimeoutInterceptor() {
		return new SessionTimeoutInterceptor();
	}

	@Bean
	public PortalResourceInterceptor portalResourceInterceptor() {
		return new PortalResourceInterceptor();
	}

	@Bean
	public EPLoginService eploginService() {
		return new EPLoginServiceImpl();
	}

	@Bean
	public org.onap.portalsdk.core.auth.LoginStrategy coreLoginStrategy() {
		if ("OIDC".equalsIgnoreCase(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM).trim()))
			return new OpenIdConnectLoginStrategy();
		else
			return new SimpleLoginStrategy();
	}

	@Bean
	public LoginStrategy loginStrategy() {

		if ("OIDC".equalsIgnoreCase(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM).trim()))
			return new OpenIdConnectLoginStrategy();
		else
			return new SimpleLoginStrategy();
	}

	public FnMenuService fnMenuService() {
		return new FnMenuServiceImpl();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionTimeoutInterceptor()).excludePathPatterns("/oid-login", "/portalApi/healthCheck",
				"/portalApi/healthCheck/", "/portalApi/healthCheckSuspend", "/portalApi/healthCheckSuspend/",
				"/portalApi/healthCheckResume", "/portalApi/healthCheckResume/", "/login_external",
				"/login_external.htm*", "login", "/login.htm*", "/auxapi/**/*", "/context/*", "/api*",
				"/single_signon.htm", "/single_signon", "/dashboard", "/OpenSourceLogin.htm");

		registry.addInterceptor(portalResourceInterceptor());

	}

	/**
	 * Creates and returns a new instance of a {@link SchedulerFactoryBean} and
	 * populates it with triggers.
	 *
	 * @return New instance of {@link SchedulerFactoryBean}
	 */

/*	@Bean
	public EPUebHelper epUebHelper() {
		return new EPUebHelper();
	}
*/
	@Bean
	public HealthMonitor healthMonitor() {
		return new HealthMonitor();
	}

	/**
	 * Creates and returns a new instance of a {@link MainUebHandler}.
	 * 
	 * @return New instance of {@link MainUebHandler}.
	 */
	@Bean
	public MainUebHandler mainUebHandler() {
		return new MainUebHandler();
	}

	/**
	 * Creates and returns a new instance of a {@link InitUebHandler}.
	 * 
	 * @return New instance of {@link InitUebHandler}.
	 */
	@Bean
	public InitUebHandler initUebHandler() {
		return new InitUebHandler();
	}

	/**
	 * Creates and returns a new instance of a {@link WidgetNotificationHandler}
	 * .
	 * 
	 * @return New instance of {@link WidgetNotificationHandler}.
	 */
	@Bean
	public WidgetNotificationHandler widgetNotificationHandler() {
		return new WidgetNotificationHandler();
	}

	/**
	 * Creates and returns a new instance of a {@link FunctionalMenuHandler} .
	 * 
	 * @return New instance of {@link FunctionalMenuHandler}.
	 */
	@Bean
	public FunctionalMenuHandler functionalMenuHandler() {
		return new FunctionalMenuHandler();
	}

	/**
	 * Creates and returns a new instance of a {@link SchedulerFactoryBean} and
	 * populates it with triggers.
	 *
	 * @return New instance of {@link SchedulerFactoryBean}
	 * @throws Exception if dataSource fails
	 */
	// APPLICATIONS REQUIRING QUARTZ SHOULD RESTORE ANNOTATION
	@Bean // ANNOTATION COMMENTED OUT
	public SchedulerFactoryBean schedulerFactoryBean() throws Exception {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setConfigLocation(appApplicationContext.getResource("WEB-INF/conf/quartz.properties"));
		scheduler.setDataSource(dataSource());
		scheduler.setTriggers(schedulerRegistryAdapter.getTriggers());
		scheduler.setSchedulerName(getScheduleName());
		return scheduler;
	}

	protected String getScheduleName() {
		final String CRON_SITE_NAME = "cron_site_name";
		String cronSiteVal = "Default";
		try {
			cronSiteVal = SystemProperties.getProperty(CRON_SITE_NAME);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getScheduleName failed", e);
			logger.warn(EELFLoggerDelegate.errorLogger,
					"Cron site name not added in property file, using Default value");
		}

		String cronSiteName = cronSiteVal != null ? cronSiteVal : "";

		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("YYYYMMdd");
		String currentDateStr = dateFormat.format(Calendar.getInstance().getTime());

		return "Scheduler" + "_" + currentDateStr + "_" + cronSiteName;
	}

	/**
	 * Sets the scheduler registry adapter.
	 *
	 * @param schedulerRegistryAdapter
	 *            Scheduler registry adapter
	 */
	@Autowired
	public void setSchedulerRegistryAdapter(final RegistryAdapter schedulerRegistryAdapter) {
		this.schedulerRegistryAdapter = schedulerRegistryAdapter;
	}

}
