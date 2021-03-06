package interfacciaGUI;

import java.sql.SQLException;

/**
 * Tutti i metodi che si hanno a disposizione prima di effettuare il login
 * @author timmy
 *
 */
public interface ControllerUtente {

	/**
	 * Effettua il login con lo username e la password inviate.
	 * @param username	Username con cui fare il login
	 * @param password	Password con cui fare il login
	 * @return	Esito del login: -true: login riuscito; -false: login fallito.
	 * @throws SQLException Problema al DB
	 */
	public ControllerLimbo effettuaLogin(String username, String password) throws SQLException;
	
	/**
	 * Effettua la registrazioni con lo username e la password inviate.
	 * @param username	Username con cui fare la registrazione.
	 * @param password	Password con cui fare la registrazione.
	 * @return	Esito della registrazione: -true: registrazione e login effettuati; false: registrazione fallita.
	 */
	public ControllerLimbo effettuaRegistrazione(String username, String password);
	
	
}
