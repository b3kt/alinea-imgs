package id.alinea.imgs.exception;

public class StorageFileNotFoundException extends StorageException {
    
	private static final long serialVersionUID = -6940347243817671749L;

	public StorageFileNotFoundException(String message) {
		super(message);
	}

	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
