package client;

import java.sql.SQLException;

import interfacciaGUI.ControllerLimbo;
import interfacciaGUI.ControllerUtente;

/**
 * 
 * @see ControllerUtente
 *
 */
public class Utente implements ControllerUtente {
	private Ambasciatore ambasciatore = null;
	
	public Utente(){
		super();
		this.ambasciatore = Ambasciatore.getInstance();
	}
	/**
	 * Effettua il login
	 * @param username Username del giocatore
	 * @param password Password del giocatore
	 * @return True: login riuscito; false: username in uso
	 * @throws SQLException 
	 * 
	 */
	private boolean login(String username, String password) throws SQLException{
		boolean esitoComunicazione = this.ambasciatore.login(username, password);
		return esitoComunicazione;
	}
	
	
	/**
	 * 
	 * @param username Username del giocatore
	 * @param password Password del giocatore
	 * @return True: login riuscito; false: username in uso
	 */
	private boolean registazione(String username, String password){
		boolean esitoComunicazione = this.ambasciatore.registrazione(username, password);
		return esitoComunicazione;
	}

	
	@Override
	public ControllerLimbo effettuaLogin(String username, String password) throws SQLException {
		if(login(username, password)){
			ControllerLimbo controllerLimbo = new Limbo();
			return controllerLimbo;
		}
		return null;
	}


	@Override
	public ControllerLimbo effettuaRegistrazione(String username, String password) {
		if(registazione(username, password)){
			ControllerLimbo controllerLimbo = new Limbo();
			return controllerLimbo;
		}
		return null;
	}
}
