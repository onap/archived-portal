package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.FunctionalMenuItemWithAppID;

public class FunctionalMenuItemWithAppIDTest {

	public FunctionalMenuItemWithAppID mockFunctionalMenuItemWithAppID(){
		FunctionalMenuItemWithAppID functionalMenuItemWithAppID = new FunctionalMenuItemWithAppID();
	
		functionalMenuItemWithAppID.setRestrictedApp(false);
		functionalMenuItemWithAppID.setUrl("test");
		
		
		return functionalMenuItemWithAppID;
	}
	
	@Test
	public void functionalMenuItemWithAppIDTest(){
		FunctionalMenuItemWithAppID functionalMenuItemWithAppID = mockFunctionalMenuItemWithAppID();

		assertEquals(functionalMenuItemWithAppID.toString(), "FunctionalMenuItem [menuId=null, column=null, text=null, parentMenuId=null, url=test, active_yn=null, appid=null, roles=null, restrictedApp=false]");
	
	}
	
}
