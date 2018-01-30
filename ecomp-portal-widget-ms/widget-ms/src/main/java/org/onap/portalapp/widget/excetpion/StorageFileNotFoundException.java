package org.onap.portalapp.widget.excetpion;

public class StorageFileNotFoundException extends StorageException {

	private static final long serialVersionUID = -930114778119283188L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}