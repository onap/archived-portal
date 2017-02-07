/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.conf;

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
import java.util.List;

import javax.annotation.PostConstruct;

import org.openecomp.portalapp.authentication.LoginStrategy;
import org.openecomp.portalapp.authentication.OpenIdConnectLoginStrategy;
import org.openecomp.portalapp.authentication.SimpleLoginStrategy;
import org.openecomp.portalapp.portal.interceptor.PortalResourceInterceptor;
import org.openecomp.portalapp.portal.interceptor.SessionTimeoutInterceptor;
import org.openecomp.portalapp.portal.listener.HealthMonitor;
import org.openecomp.portalapp.portal.service.EPLoginService;
import org.openecomp.portalapp.portal.service.EPLoginServiceImpl;
import org.openecomp.portalapp.portal.ueb.EPUebHelper;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.scheduler.RegistryAdapter;
import org.openecomp.portalapp.uebhandler.FunctionalMenuHandler;
import org.openecomp.portalapp.uebhandler.InitUebHandler;
import org.openecomp.portalapp.uebhandler.MainUebHandler;
import org.openecomp.portalapp.uebhandler.WidgetNotificationHandler;
import org.openecomp.portalsdk.core.conf.AppConfig;
import org.openecomp.portalsdk.core.conf.Configurable;
import org.openecomp.portalsdk.core.controller.LogoutController;
import org.openecomp.portalsdk.core.logging.format.AlarmSeverityEnum;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.objectcache.AbstractCacheManager;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.service.FnMenuService;
import org.openecomp.portalsdk.core.service.FnMenuServiceImpl;
import org.openecomp.portalsdk.core.util.CacheManager;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.openecomp"}, excludeFilters = {
		@Filter(value = { LogoutController.class }, type = FilterType.ASSIGNABLE_TYPE) })
@Profile("src")
@EnableAsync
@EnableScheduling
public class ExternalAppConfig extends AppConfig implements Configurable {
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAppConfig.class);

	private RegistryAdapter schedulerRegistryAdapter;

	public ViewResolver viewResolver() {
		return super.viewResolver();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		registry.addResourceHandler("/images/**").addResourceLocations("/images/");
		registry.addResourceHandler("/**").addResourceLocations("/public/");
	}

	@PostConstruct
	private void init() {
		try {
			//Loading defaults
			MDC.put(MDC_SERVICE_NAME, EPSystemProperties.ECOMP_PORTAL_BE);
			MDC.put(MDC_SERVER_FQDN, InetAddress.getLocalHost().getHostName());
			MDC.put(MDC_SERVER_IP_ADDRESS, InetAddress.getLocalHost().getHostAddress());
			MDC.put(MDC_SERVICE_INSTANCE_ID, "");
			MDC.put(MDC_ALERT_SEVERITY, AlarmSeverityEnum.INFORMATIONAL.toString());
			MDC.put(MDC_INSTANCE_UUID, SystemProperties.getProperty(SystemProperties.INSTANCE_UUID));
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
	}
	
	/*
	 * /
	 * 
	 * @PostConstruct // file://${catalina.home}/conf/log4j.properties public
	 * void initLog4j() throws FileNotFoundException { try { URL[] classpathurls
	 * = ((URLClassLoader)
	 * (Thread.currentThread().getContextClassLoader())).getURLs(); for (URL url
	 * : classpathurls) { System.out.println(url.getFile().toString()); }
	 * Log4jConfigurer.initLogging(
	 * "file://${catalina.home}/conf/log4j.properties"); } catch
	 * (FileNotFoundException e) { ((URLClassLoader)
	 * (Thread.currentThread().getContextClassLoader())).getURLs();
	 * Log4jConfigurer.initLogging("classpath:../conf/log4j.properties"); } } /
	 **/

	public DataAccessService dataAccessService() {
		return super.dataAccessService();
	}

	public String[] tileDefinitions() {
		return super.tileDefinitions();
	}

	public List<String> addTileDefinitions() {
		List<String> definitions = new ArrayList<String>();
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
	public LoginStrategy loginStrategy(){
		
		if(SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM).trim().equalsIgnoreCase("OIDC"))
			return new OpenIdConnectLoginStrategy();
		else
			return new SimpleLoginStrategy();
	}
	
	public FnMenuService fnMenuService(){
		return new FnMenuServiceImpl();
	}
	

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// registry.addInterceptor(new
		// StaticResourcesInterceptor()).addPathPatterns("/index.htm",
		// "/applicationsHome", "/widgetsHome", "/admins", "/users",
		// "/applications", "/widgets");
		// Excludes login/logout pages and REST endpoints used by other
		// application servers.
		
		
		registry.addInterceptor(sessionTimeoutInterceptor()).excludePathPatterns("/oid-login", "/portalApi/healthCheck","/portalApi/healthCheck/",
				"/portalApi/healthCheckSuspend","/portalApi/healthCheckSuspend/", "/portalApi/healthCheckResume","/portalApi/healthCheckResume/",
				"/login_external","/login_external.htm*", "login", "/login.htm*", "/auxapi/*", "/context/*",
				"/api*", "/single_signon.htm", "/single_signon","/dashboard", "/OpenSourceLogin.htm");
		
		registry.addInterceptor(portalResourceInterceptor());
		
	}

	/**
	 * Creates and returns a new instance of a {@link SchedulerFactoryBean} and
	 * populates it with triggers.
	 *
	 * @return New instance of {@link SchedulerFactoryBean}
	 * @throws Exception
	 */

	@Bean
	public EPUebHelper epUebHelper() {
		return new EPUebHelper();
	}

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
	 * @throws Exception
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
		try{
			cronSiteVal = SystemProperties.getProperty(CRON_SITE_NAME);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			logger.warn(EELFLoggerDelegate.errorLogger, "Cron site name not added in property file, using Default value");
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
	 */
	@Autowired
	public void setSchedulerRegistryAdapter(final RegistryAdapter schedulerRegistryAdapter) {
		this.schedulerRegistryAdapter = schedulerRegistryAdapter;
	}
	
	
	
}
