package org.openecomp.portalapp.portal.service;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.service.ManifestServiceImpl;

public class ManifestServiceImplTest {

	@Mock
	ServletContext context;

	@Mock
	ServletContext context1 = null;

	@InjectMocks
	ManifestServiceImpl manifestServiceImpl = new ManifestServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	NullPointerException nullPointerException = new NullPointerException();

	@Test
	public void getWebappManifestTest() throws IOException {
		final String MANIFEST_RESOURCE_PATH = "/META-INF/MANIFEST.MF";
		InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
		Mockito.when(context.getResourceAsStream(MANIFEST_RESOURCE_PATH)).thenReturn(inputStream);
		Attributes attributes = manifestServiceImpl.getWebappManifest();
		assertTrue(attributes.size() == 0);
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void getWebappManifestExceptionTest() throws IOException {
		final String MANIFEST_RESOURCE_PATH = "/META-INF/MANIFEST.MF";
		InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
		Mockito.when(context1.getResourceAsStream(MANIFEST_RESOURCE_PATH)).thenThrow(nullPointerException);
		Attributes attributes = manifestServiceImpl.getWebappManifest();
		assertTrue(attributes.size() == 0);
	}
}
