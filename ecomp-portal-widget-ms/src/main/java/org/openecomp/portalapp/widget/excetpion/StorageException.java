package org.openecomp.portalapp.widget.excetpion;

public class StorageException extends RuntimeException {

	
	private static final long serialVersionUID = 4142203582273176386L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
