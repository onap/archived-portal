package org.openecomp.portalapp.widget.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.openecomp.portalapp.widget.domain.MicroserviceData;
import org.openecomp.portalapp.widget.domain.MicroserviceParameter;
import org.openecomp.portalapp.widget.domain.RoleApp;
import org.openecomp.portalapp.widget.domain.WidgetCatalog;
import org.openecomp.portalapp.widget.service.InitializationService;
import org.openecomp.portalapp.widget.service.MicroserviceService;
import org.openecomp.portalapp.widget.service.StorageService;
import org.openecomp.portalapp.widget.service.WidgetCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

/**
 * Uploads widget zip archives to Portal.
 */
@Service("initService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
public class InitializationServiceImpl implements InitializationService {

	private static final String BASIC_AUTH = "Basic Authentication";
	private static final String PARAMETER_KEY = "resourceType";
	private static final Logger logger = LoggerFactory.getLogger(InitializationServiceImpl.class);

	@Autowired
	WidgetCatalogService widgetCatalogService;

	@Autowired
	StorageService storageService;

	@Autowired
	MicroserviceService microserviceService;

	@Value("${account.user.name}")
	String account_user;

	@Value("${account.user.password}")
	String account_password;

	@Value("${initialization.widgetData.url}")
	String widgetData_url;

	@Override
	public void initialize() {
		initCommonWidget("News");
		initCommonWidget("Events");
		initCommonWidget("Resources");
	}

	private void initCommonWidget(String name) {

		final String newServiceName = name + " Microservice";

		Long serviceId = microserviceService.getMicroserviceIdByName(newServiceName);

		if (serviceId == null) {
			MicroserviceData newService = new MicroserviceData();
			newService.setName(newServiceName);
			newService.setDesc(name);
			newService.setAppId(1);
			newService.setUrl(widgetData_url);
			newService.setSecurityType(BASIC_AUTH);
			newService.setUsername(account_user);
			newService.setPassword(account_password);
			newService.setActive("Y");
			serviceId = microserviceService.saveMicroserivce(newService);

			MicroserviceParameter parameter = new MicroserviceParameter();
			parameter.setServiceId(serviceId);
			parameter.setPara_key(PARAMETER_KEY);
			String parameter_value = null;
			switch (name.toLowerCase()) {
			case "news":
				parameter_value = "NEWS";
				break;
			case "events":
				parameter_value = "EVENTS";
				break;
			case "resources":
				parameter_value = "IMPORTANTRESOURCES";
				break;
			}
			parameter.setPara_value(parameter_value);
			microserviceService.saveMicroserviceParameter(parameter);
		}

		if (!widgetCatalogService.getWidgetIdByName(name)) {
			WidgetCatalog newWidget = new WidgetCatalog();
			newWidget.setName(name);
			newWidget.setDesc(name);
			newWidget.setAllowAllUser("1");
			String fileLocation = name.toLowerCase() + "-widget.zip";
			newWidget.setFileLocation(fileLocation);
			newWidget.setServiceId(serviceId);
			newWidget.setWidgetRoles(new HashSet<RoleApp>());
			long widgetId = widgetCatalogService.saveWidgetCatalog(newWidget);

			File tmpZipFile = new File("/tmp/" + fileLocation);
			InputStream fileInputStream = null;
			OutputStream outputStream = null;
			try {
				fileInputStream = this.getClass().getClassLoader().getResourceAsStream(fileLocation);
				outputStream = new FileOutputStream(tmpZipFile);
				int read = 0;
				byte[] bytes = new byte[4096];
				while ((read = fileInputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				outputStream.close();
				fileInputStream.close();
			} catch (Exception e) {
				logger.error(
						"Exception occurred while performing InitializationServiceImpl.initCommonWidget in widget microservices. Details:", e);
			}
			storageService.initSave(tmpZipFile, newWidget, widgetId);
			tmpZipFile.delete();
		}
	}
}
