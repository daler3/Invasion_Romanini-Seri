package client;

import classiCondivise.BeanGiocatore;
import classiCondivise.Classifica;
import interfacciaGUI.ControllerAccountStatistiche;
import exceptionCondivise.GiocatoreNonTrovatoException;

public abstract class AccountEStatistiche implements ControllerAccountStatistiche{

	private Ambasciatore ambasciatore;
	
	/**
	 * Costruttore
	 */
	public AccountEStatistiche() {
		this.ambasciatore = Ambasciatore.getInstance();
	}

	
	/**
	 * Comunica con l'ambasciatore, che inoltrerà la richiesta al Server
	 * @see ControllerAccountEStatistiche
	 */
	@Override
	public boolean cambiaPassword(String vecchiaPassword, String nuovaPassword) {
		boolean esito = this.ambasciatore.cambiaPassword(vecchiaPassword, nuovaPassword);
		return esito; 
	}
	
	
	/**
	 * Comunica con l'ambasciatore, che inoltrerà la richiesta al Server
	 * @see ControllerAccountEStatistiche
	 */
	@Override
	public Classifica visualizzaClassifica() {
		Classifica classifica = this.ambasciatore.ottieniClassifica();
		return classifica; 
	}

	
	/**
	 * Comunica con l'ambasciatore, che inoltrerà la richiesta al Server
	 * @see ControllerAccountEStatistiche
	 */
	@Override
	public BeanGiocatore visualizzaInfoGiocatore(String nomeGiocatore) throws GiocatoreNonTrovatoException {
		BeanGiocatore infoGiocatore = this.ambasciatore.ottieniInfoUtente(nomeGiocatore);
		return infoGiocatore;
	}


	/**
	 * Comunica con l'ambasciatore per richiedere le informazioni personali.
	 * 
	 */
	@Override
	public BeanGiocatore visualizzaMieInfo() {
		BeanGiocatore mieInfo = this.ambasciatore.getMieInfo();
		return mieInfo; 
	}

	/**
	 * Comunica con l'ambasciatore, che inoltrerà la richiesta al Server
	 * @see ControllerAccountEStatistiche
	 */
	@Override
	public boolean logout() {
		boolean esitoLogout = this.ambasciatore.eseguiLogout();
		return esitoLogout;
	}
	
	/**
	 * Metodo per comunicare la chiusura del Server
	 */
	public abstract void chiusuraServer();
	

}
