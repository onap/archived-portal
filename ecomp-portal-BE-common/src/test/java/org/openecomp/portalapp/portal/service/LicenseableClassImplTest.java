package org.openecomp.portalapp.portal.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.lm.LicenseableClassImpl;

public class LicenseableClassImplTest {
	
	@InjectMocks
	LicenseableClassImpl licenseableClassImpl= new LicenseableClassImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getApplicationNameTest()
	{		
		assertEquals(licenseableClassImpl.getApplicationName(), "");
	}
	@Test
	public void getPublicKeystoreAsInputStreamTest() throws IOException
	{		
		assertNull(licenseableClassImpl.getPublicKeystoreAsInputStream());
	}
	@Test
	public void getAliasTest()
	{		
		assertEquals(licenseableClassImpl.getAlias(), "");
	}
	@Test
	public void getKeyPasswdTest()
	{		
		assertEquals(licenseableClassImpl.getKeyPasswd(), "");
	}
	@Test
	public void getPublicKeystorePasswordTest()
	{		
		assertEquals(licenseableClassImpl.getPublicKeystorePassword(), "");
	}
	@Test
	public void getCipherParamPasswordTest()
	{		
		assertEquals(licenseableClassImpl.getPublicKeystorePassword(), "");
	}
	@Test
	public void getClassToLicenseTest()
	{		
		assertEquals(licenseableClassImpl.getClassToLicense(), LicenseableClassImpl.class);
	}
	
	
}
