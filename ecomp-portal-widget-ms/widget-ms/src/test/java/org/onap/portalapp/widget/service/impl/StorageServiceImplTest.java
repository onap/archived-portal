/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.widget.service.impl;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.widget.domain.WidgetCatalog;
import org.onap.portalapp.widget.domain.WidgetFile;
import org.onap.portalapp.widget.excetpion.StorageException;
import org.onap.portalapp.widget.service.WidgetCatalogService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class StorageServiceImplTest {
	
	@InjectMocks
	StorageServiceImpl storageServiceImpl;


	@Mock
	WidgetCatalogService widgetCatalogService;
	
	@Mock
	Session session;
	@Mock
	SessionFactory sessionFactory;
	
	@Mock
	Session currentSession;
	@Mock
	Transaction transaction;
	@Mock
	Criteria criteria;
	
	@Mock
	Criteria widgetCriteria;
	@Mock
	MultipartFile file;
	
	
	@Before
	public void init() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testDeleteWidgetFile() {
		List<WidgetFile> widgetFiles=new ArrayList<>();
		WidgetFile file=new WidgetFile();
		widgetFiles.add(file);
		when(sessionFactory.openSession()).thenReturn(session);
		when(session.createCriteria(WidgetFile.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(widgetFiles);
		when(sessionFactory.getCurrentSession()).thenReturn(currentSession);
		when(currentSession.beginTransaction()).thenReturn(transaction);
		storageServiceImpl.deleteWidgetFile(2l);
	}
	
	@Test
	public void testGetWidgetFile() {
		List<WidgetFile> widgetFiles=new ArrayList<>();
		WidgetFile file=new WidgetFile();
		widgetFiles.add(file);
		when(sessionFactory.openSession()).thenReturn(session);
		when(session.createCriteria(WidgetFile.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(widgetFiles);
		storageServiceImpl.getWidgetFile(2l);
		
	}
	
	@Test
public void testcheckZipFileInvalid() {
		MockMultipartFile mockMultipartFile = new MockMultipartFile(
			    "fileData",
			    "test.zip",
			    "text/plain",
			    "test".getBytes());
	
		storageServiceImpl.checkZipFile(mockMultipartFile);
	}
	
	@Test(expected=StorageException.class)
	public void testcheckZipEmptyFile() {
		when(file.getOriginalFilename()).thenReturn("test.zip");
		when(file.isEmpty()).thenReturn(true);
		storageServiceImpl.checkZipFile(file);	
		
	}
	
	@Test(expected=NullPointerException.class)
	public void testUpdate() {
		
		MockMultipartFile mockMultipartFile = new MockMultipartFile(
			    "fileData",
			    "test.zip",
			    "text/plain",
			    "test".getBytes());
		WidgetCatalog catalog=new WidgetCatalog();
		catalog.setServiceId(2l);
		List<WidgetFile> widgetFiles=new ArrayList<>();
		WidgetFile file=new WidgetFile();
		widgetFiles.add(file);
		when(sessionFactory.openSession()).thenReturn(session);
		when(session.createCriteria(WidgetFile.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(widgetFiles);
		storageServiceImpl.update(mockMultipartFile, catalog, 2l);
		
	
		
	}
	
	@Test
	public void testGetWidgetCatalogContent()throws Exception {
		WidgetCatalog catalog=new WidgetCatalog();
		catalog.setServiceId(2l);
		catalog.setName("test");
		List<WidgetFile> widgetFiles=new ArrayList<>();
		WidgetFile file=new WidgetFile();
		file.setCss("test".getBytes());
		file.setController("test function() Test".getBytes());
		file.setMarkup("Test".getBytes());
		widgetFiles.add(file);
	
		when(sessionFactory.getCurrentSession()).thenReturn(currentSession);
		when(currentSession.beginTransaction()).thenReturn(transaction);
		when(currentSession.createCriteria(WidgetFile.class)).thenReturn(criteria,criteria,criteria);
		when(criteria.list()).thenReturn(widgetFiles);
	//	when(currentSession.createCriteria(WidgetFile.class)).thenReturn(criteria);
	//	when(criteria.list()).thenReturn(widgetFiles);
		when(widgetCatalogService.getWidgetCatalog(2l)).thenReturn(catalog);
		storageServiceImpl.getWidgetCatalogContent(2l);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSaveMultiPartFile() {
		MockMultipartFile mockMultipartFile = new MockMultipartFile(
			    "fileData",
			    "test.zip",
			    "text/plain",
			    "test".getBytes());
		
		WidgetCatalog catalog=new WidgetCatalog();
		catalog.setServiceId(2l);
		catalog.setName("test");
		
		storageServiceImpl.save(mockMultipartFile, catalog, 2l);
		
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInitSave()throws Exception {
		MockMultipartFile mockMultipartFile = new MockMultipartFile(
			    "fileData",
			    "test.zip",
			    "text/plain",
			    "test".getBytes());
		File convFile = new File(mockMultipartFile.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(mockMultipartFile.getBytes());
		fos.close();
		WidgetCatalog catalog=new WidgetCatalog();
		catalog.setServiceId(2l);
		catalog.setName("test");
		storageServiceImpl.initSave(convFile, catalog, 2l);
		convFile.delete();
		
		
	}
	
	@Test
	public void testWidgetFramework()throws Exception {
		List<WidgetFile> widgetFiles=new ArrayList<>();
		WidgetFile file=new WidgetFile();
		file.setCss("test".getBytes());
		file.setController("test function() Test".getBytes());
		file.setMarkup("Test".getBytes());
		file.setFramework("test".getBytes());
		widgetFiles.add(file);
	
		when(sessionFactory.getCurrentSession()).thenReturn(currentSession);
		when(currentSession.beginTransaction()).thenReturn(transaction);
		when(currentSession.createCriteria(WidgetFile.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(widgetFiles);
		storageServiceImpl.getWidgetFramework(2l);
		
	}
	
	
}
