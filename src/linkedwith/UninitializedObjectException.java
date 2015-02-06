package linkedwith;

public class UninitializedObjectException extends Exception {

	private static final long serialVersionUID = 431445672447755658L;

	public UninitializedObjectException() {
		super();
	}

	public UninitializedObjectException(String message) {
		super(message);
	}

	public UninitializedObjectException(Throwable cause) {
		super(cause);
	}

	public UninitializedObjectException(String message, Throwable cause) {
		super(message, cause);
	}

}
