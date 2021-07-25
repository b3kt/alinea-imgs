package id.alinea.imgs.exception;

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = -417576616122131636L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
