package org.openecomp.portalapp.lm;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class FusionLicenseManagerImplTest {

		
	@Test
	public void FusionLicenseManagerImplTest(){
		FusionLicenseManagerImpl fusionLicenseManagerImpl = new FusionLicenseManagerImpl();
		
		String nvlReturn = null;
		
		int installLicenseInt = fusionLicenseManagerImpl.installLicense();	
		nvlReturn = fusionLicenseManagerImpl.nvl("test");		
		Date expiredDateReturn = fusionLicenseManagerImpl.getExpiredDate();
		
		assertEquals(installLicenseInt, 0);		
		assertEquals(nvlReturn, null); 
		assertEquals(expiredDateReturn, null);	
	
	}
	
}
