package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.ManifestController;
import org.openecomp.portalapp.portal.service.ManifestService;
import org.openecomp.portalapp.portal.service.ManifestServiceImpl;
import org.openecomp.portalapp.test.framework.MockitoTestSuite;

public class ManifestControllerTest extends MockitoTestSuite{

	
	@Mock
	ManifestService manifestService = new ManifestServiceImpl();
	
	@InjectMocks
	ManifestController  manifestController = new  ManifestController();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	NullPointerException nullPointerException = new NullPointerException();
	
	@Test
	public void getManifestTest() throws IOException
	{
		Map<String, Object> expectedResponse = new HashMap<String, Object>();
		Attributes attributes  = new Attributes();
		expectedResponse.put("test", attributes);
		Mockito.when(manifestService.getWebappManifest()).thenReturn(attributes);
		Map<String, Object> actualResponse = manifestController.getManifest(mockedRequest);
		assertTrue((actualResponse.keySet().toArray().length) == 1);
		
	}
	
	@Test
	public void getManifestExceptionTest() throws IOException
	{
		Mockito.when(manifestService.getWebappManifest()).thenThrow(nullPointerException);
		Map<String, Object> actualResponse = manifestController.getManifest(mockedRequest);
		assertTrue((actualResponse.keySet().toArray().length) == 1);
		assertTrue(actualResponse.get("error").equals("failed to get manifest: java.lang.NullPointerException"));
	}
}
