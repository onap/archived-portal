package org.openecomp.portalapp.widget.service;

import java.util.List;

import org.openecomp.portalapp.widget.domain.RoleApp;
import org.openecomp.portalapp.widget.domain.WidgetCatalog;

public interface WidgetCatalogService {
	
	List<WidgetCatalog> getWidgetCatalog();
	
	List<WidgetCatalog> getUserWidgetCatalog(String loginName);

	WidgetCatalog getWidgetCatalog(Long widgetCatalogId);
	
	void deleteWidgetCatalog(long WidgetCatalogId);
	
	long saveWidgetCatalog(WidgetCatalog newWidgetCatalog);

	void updateWidgetCatalog(Long widgetCatalogId, WidgetCatalog newWidgetCatalog);	
	
	Long getServiceIdByWidget(Long widgetCatalogId);
	
	boolean getWidgetIdByName(String newWidgetName);
	
	List<WidgetCatalog> getWidgetsByServiceId(Long serviceId);
	

}
