/*
 * ============LICENSE_START=======================================================
 * ONAP  PORTAL
 * ================================================================================
 * Copyright 2018 TechMahindra
 *=================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */
package org.onap.portalapp.portal.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.SharedContext;
import org.onap.portalsdk.core.service.DataAccessService;

public class SharedContextServiceImplTest {

	@Mock
	DataAccessService dataAccessService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@InjectMocks
	SharedContextServiceImpl sharedContextServiceImpl = new SharedContextServiceImpl();

	MockEPUser mockUser = new MockEPUser();

	@Test
	public void getSharedContextsTest() {
		String contextId = "test";
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion contextIdCrit = Restrictions.eq("context_id", contextId);
		restrictionsList.add(contextIdCrit);
		List<SharedContext> contextsList = new ArrayList<>();
		SharedContext sharedContext = new SharedContext();
		contextsList.add(sharedContext);
		Mockito.when((List<SharedContext>) dataAccessService.getList(SharedContext.class, null))
		.thenReturn(contextsList);
		sharedContextServiceImpl.getSharedContexts("test");
	}

	@Test
	public void getSharedContextsTest_usingKey() {
		String contextId = "test";
		String key = "key";
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion contextIdCrit = Restrictions.eq("context_id", "test");
		Criterion keyCrit = Restrictions.eq("ckey", "key");
		restrictionsList.add(contextIdCrit);
		restrictionsList.add(keyCrit);
		List<SharedContext> contextsList = new ArrayList<>();
		SharedContext sharedContext = new SharedContext();
		contextsList.add(sharedContext);
		Mockito.when((List<SharedContext>) dataAccessService.getList(SharedContext.class, null))
		.thenReturn(contextsList);
		sharedContextServiceImpl.getSharedContext("test", "key");
	}

	@Test
	public void addSharedContextTest() {
		SharedContext context = new SharedContext();
		context.setContext_id("test");
		context.setCkey("key");
		context.setCvalue("demo");
		context.setId(1l);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(context,  null);
		sharedContextServiceImpl.addSharedContext("test", "key", "demo");
	}

	@Test
	public void saveSharedContext() {
		SharedContext context = new SharedContext();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(context,  null);
		sharedContextServiceImpl.saveSharedContext(context);//("test", "key", "demo");
	}

	@Test
	public void deleteSharedContextsTest() {
		List<SharedContext> contextsList = new ArrayList<>();
		SharedContext sharedContext = new SharedContext();
		contextsList.add(sharedContext);
		String contextId = "test";
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion contextIdCrit = Restrictions.eq("context_id", contextId);
		restrictionsList.add(contextIdCrit);
		Mockito.when((List<SharedContext>) dataAccessService.getList(SharedContext.class, null))
		.thenReturn(contextsList);
		sharedContextServiceImpl.deleteSharedContexts("test");
	}

	@Test
	public void deleteSharedContextTest() {
		SharedContext context = new SharedContext();
		Mockito.doNothing().when(dataAccessService).deleteDomainObject(context,  null);
		sharedContextServiceImpl.deleteSharedContext(context);
	}

	@Test
	public void expireSharedContextsTest() {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("2018-02-22 10:00:00"); 
		Date expiredDateTime = new Date(System.currentTimeMillis() - 3600 * 1000);
		final String whereClause = " create_time < '" + dateFormat.format(expiredDateTime) + "'";
		Mockito.doNothing().when(dataAccessService).deleteDomainObjects(SharedContext.class, whereClause, null);
		sharedContextServiceImpl.expireSharedContexts(3600);
	}
}
