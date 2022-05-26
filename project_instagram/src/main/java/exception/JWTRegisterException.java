package exception;

public class JWTRegisterException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public JWTRegisterException(String message) {
		super(message);
	}

}
