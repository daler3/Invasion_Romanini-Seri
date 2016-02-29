package eccezioni;

/**
 * 
 * Eccezione usata quando il numero dei giocatori per l'avvio di un tavolo è errato.
 *
 */
public class NumeroGiocatoriErratoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NumeroGiocatoriErratoException() {
		
	}

	public NumeroGiocatoriErratoException(String message) {
		super(message);
		
	}

	public NumeroGiocatoriErratoException(Throwable cause) {
		super(cause);
		
	}

	public NumeroGiocatoriErratoException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public NumeroGiocatoriErratoException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

}
