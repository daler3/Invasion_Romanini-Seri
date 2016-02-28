package exceptionCondivise;

/**
 * Ecezione lanciata quando le unità presenti su un territorio sono insufficienti, ad esempio, per effetuare un attacco.
 * 
 *
 */
public class UnitaInsufficientiException extends Exception {


	private static final long serialVersionUID = 1L;

	public UnitaInsufficientiException() {
		  
	}

	public UnitaInsufficientiException(String message) {
		super(message);
		  
	}

	public UnitaInsufficientiException(Throwable cause) {
		super(cause);
		  
	}

	public UnitaInsufficientiException(String message, Throwable cause) {
		super(message, cause);
		  
	}

	public UnitaInsufficientiException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		  
	}
	
	@Override
	public String toString() {
		return "UnitaInsufficientiException";
	}

}
