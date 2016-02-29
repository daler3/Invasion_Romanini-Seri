package exceptionCondivise;


/**
 * Eccezione lanciata da partita quando un comando dell'utente (che si aspettava) non è stato trovato.
 * @author daniele
 *
 */
public class InserimentoMancanteException extends Exception {


	private static final long serialVersionUID = 1L;

	public InserimentoMancanteException() {
		super();
	}

	@Override
	public String toString() {
		return "InserimentoMancanteException";
	}
}
