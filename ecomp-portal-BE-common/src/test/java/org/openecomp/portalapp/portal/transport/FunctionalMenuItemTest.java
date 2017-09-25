package org.openecomp.portalapp.portal.transport;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItem;

public class FunctionalMenuItemTest {
	
	public FunctionalMenuItem mockFunctionalMenuItem(){
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		
		List<Integer> roles = new ArrayList<Integer>();
		
		functionalMenuItem.setRestrictedApp(false);
		functionalMenuItem.setUrl("test");
		functionalMenuItem.setRoles(roles);
		
		return functionalMenuItem;
	}
	
	@Test
	public void functionalMenuItemTest(){
		FunctionalMenuItem functionalMenuItem = mockFunctionalMenuItem();
		
		FunctionalMenuItem functionalMenuItem1 = mockFunctionalMenuItem();
		
		List<Integer> roles = new ArrayList<Integer>();
		
		functionalMenuItem1.setRestrictedApp(false);
		functionalMenuItem1.setUrl("test");
		functionalMenuItem1.setRoles(roles);
		
		assertEquals(functionalMenuItem.getRoles(), functionalMenuItem1.getRoles());
		assertEquals(functionalMenuItem.toString(), "FunctionalMenuItem [menuId=null, column=null, text=null, parentMenuId=null, url=test, active_yn=null, appid=null, roles=[], restrictedApp=false]");
		// assertTrue(functionalMenuItem.normalize(), functionalMenuItem1.normalize());
	}

}
