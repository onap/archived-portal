package org.openecomp.portalapp.lm;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.junit.Test;

public class LicenseableClassImplTest {

	@Test
	public void licenseableClassImplTest() throws FileNotFoundException{
		LicenseableClassImpl licenseableClassImpl = new LicenseableClassImpl();
		
		String appNameReturn = licenseableClassImpl.getApplicationName();
		java.io.InputStream inputStream = licenseableClassImpl.getPublicKeystoreAsInputStream();
		String aliasReturn = licenseableClassImpl.getAlias();
		String pswdReturn = licenseableClassImpl.getKeyPasswd();
		String pkPswd = licenseableClassImpl.getPublicKeystorePassword();
		String cpPswd = licenseableClassImpl.getCipherParamPassword();
		
		assertEquals(appNameReturn, "");
		assertEquals(inputStream, null);
		assertEquals(aliasReturn, "");
		assertEquals(pswdReturn, "");
		assertEquals(pkPswd, "");
		assertEquals(cpPswd, "");
		
	}
}
