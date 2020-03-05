package org.onap.portal.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.servlet.ServletContext;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@Service
@EnableAspectJAutoProxy
public class ManifestService {

    @Autowired
    ServletContext context;

    public Attributes getWebappManifest() throws IOException {
        EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ManifestService.class);
        // Path to resource on classpath
        final String MANIFEST_RESOURCE_PATH = "/META-INF/MANIFEST.MF";
        // Manifest is formatted as Java-style properties
        try {
            InputStream inputStream = context.getResourceAsStream(MANIFEST_RESOURCE_PATH);
            Manifest manifest = new Manifest(inputStream);
            inputStream.close();
            return manifest.getMainAttributes();
        } catch (IOException e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getWebappManifest: failed to read/find manifest");
            throw e;
        }
    }
}
