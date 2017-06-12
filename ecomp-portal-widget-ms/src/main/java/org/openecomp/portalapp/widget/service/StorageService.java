package org.openecomp.portalapp.widget.service;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.openecomp.portalapp.widget.domain.ValidationRespond;
import org.openecomp.portalapp.widget.domain.WidgetCatalog;
import org.openecomp.portalapp.widget.domain.WidgetFile;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void deleteWidgetFile(long widgetId);
    
    WidgetFile getWidgetFile(long widgetId);
     
    String getWidgetMarkup(long widgetId) throws UnsupportedEncodingException;
    
    String getWidgetController(long widgetId) throws UnsupportedEncodingException;
    
    String getWidgetFramework(long widgetId) throws UnsupportedEncodingException;
    
    String getWidgetCSS(long widgetId) throws UnsupportedEncodingException;
    
    ValidationRespond checkZipFile(MultipartFile file);
    
    void save(MultipartFile file, WidgetCatalog newWidget, long widgetId);
    
    void initSave(File file, WidgetCatalog newWidget, long widgetId);
    
    void update(MultipartFile file, WidgetCatalog newWidget, long widgetId);
    
	byte[] getWidgetCatalogContent(long widgetId) throws Exception;
}
