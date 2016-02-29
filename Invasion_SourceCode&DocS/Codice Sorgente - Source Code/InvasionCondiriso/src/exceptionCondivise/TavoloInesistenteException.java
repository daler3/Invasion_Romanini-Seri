package exceptionCondivise;

/**
 * 
 * Eccezione generata quando il tavolo cercato non esiste o non è più accessibile
 * 
 *
 */
public class TavoloInesistenteException extends Exception {


	private static final long serialVersionUID = 1L;

	public TavoloInesistenteException() {
		 
	}

	public TavoloInesistenteException(String message) {
		super(message);
		 
	}

	public TavoloInesistenteException(Throwable cause) {
		super(cause);
		 
	}

	public TavoloInesistenteException(String message, Throwable cause) {
		super(message, cause);
		 
	}

	public TavoloInesistenteException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		 
	}

	@Override
	public String toString() {
		return "TavoloInesistenteException";
	}
}
