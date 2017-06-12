package org.openecomp.portalapp.widget.listener;

import org.openecomp.portalapp.widget.service.InitializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class WidgetEventListener implements ApplicationListener<ApplicationReadyEvent>{

	private static final Logger logger = LoggerFactory.getLogger(WidgetEventListener.class);
	
	@Autowired
	InitializationService initializationService;
	
	@Value("${initialization.default.widgets}")
	String init_default_widget;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		logger.debug("Listening event " + event.toString());
		try {
			if(Boolean.parseBoolean(init_default_widget))
				initializationService.initialize();
		} catch (Exception e) {
			logger.error("Exception occurred while performing WidgetsCatalogController.initializeWidgetCatalog in widget microservices. Details:" + e.getMessage());
		}
		
	}
	
}
