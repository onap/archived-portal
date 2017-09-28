/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
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
