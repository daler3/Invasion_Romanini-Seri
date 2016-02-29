package exceptionCondivise;

/**
 * Eccezione lanciata quando un elemento cercato non è presente dove si è cercato.
 *
 *
 */
public class ElementoNonPresenteException extends Exception {

	
	private static final long serialVersionUID = 1L;
	public ElementoNonPresenteException() {
		
	}

	public ElementoNonPresenteException(String message) {
		super(message);
		
	}

	public ElementoNonPresenteException(Throwable cause) {
		super(cause);
		
	}

	public ElementoNonPresenteException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public ElementoNonPresenteException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}
	@Override
	public String toString() {
		return "ElementoNonPresenteException";
	}

}
