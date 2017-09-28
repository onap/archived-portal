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
package org.openecomp.portalapp.portal.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.ecomp.model.SearchResultItem;

public class SearchResultItemTest {

	public SearchResultItem mockSearchResultItem(){
		SearchResultItem searchResultItem = new SearchResultItem();
				
		searchResultItem.setRowId("test");
		searchResultItem.setCategory("test");
		searchResultItem.setName("test");
		searchResultItem.setTarget("test");
		searchResultItem.setUuid("test");
		
		return searchResultItem;
	}
	
	@Test
	public void searchResultItemTest(){
		SearchResultItem searchResultItem = mockSearchResultItem();
		
		SearchResultItem searchResultItem1 = new SearchResultItem();
		searchResultItem1.setRowId("test");
		searchResultItem1.setCategory("test");
		searchResultItem1.setName("test");
		searchResultItem1.setTarget("test");
		searchResultItem1.setUuid("test");
		
		assertEquals(searchResultItem.getRowId(), searchResultItem.getRowId());
		assertEquals(searchResultItem.getCategory(), searchResultItem.getCategory());
		assertEquals(searchResultItem.getName(), searchResultItem.getName());
		assertEquals(searchResultItem.getTarget(), searchResultItem.getTarget());
		assertEquals(searchResultItem.getUuid(), searchResultItem.getUuid());
		assertEquals(searchResultItem.toString(), searchResultItem.toString());

	}
}
