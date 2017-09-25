package com.openecomp.portalapp.portal.ecomp.test.model;

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
