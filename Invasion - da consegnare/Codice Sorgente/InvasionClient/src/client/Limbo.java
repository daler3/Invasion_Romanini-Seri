package client;

import java.util.ArrayList;

import interfacciaGUI.ControllerLimbo;
import interfacciaGUI.ControllerTavolo;
import interfacciaGUI.ControllerUtente;
import interfacciaGUI.ViewLimbo;
import classiCondivise.BeanTavolo;
import exceptionCondivise.TavoloInesistenteException;
/**
 * Contiene le informazioni del giocatore
 * @see ControllerLimbo
 * @see AccountEStatistiche
 */
public class Limbo extends AccountEStatistiche implements ControllerLimbo {
	
	/**
	 * Riferimento alla classe ambasciatore
	 */
	private Ambasciatore ambasciatore; 
	
	/**
	 * Riferimento alla GUI rappresentante il limbo con visibilità di interfaccia
	 * @see ViewLimbo
	 */
	private ViewLimbo vistaLimbo; 
	



	/**
	 * Costruttore del Limbo.
	 * Prende il riferimento all'ambasciatore e passa a questo il suo riferimento
	 */
	public Limbo(){
		super();
		this.ambasciatore = Ambasciatore.getInstance();
		this.ambasciatore.setLimbo(this);
	}

	/**
	 * Setta il riferimento alla GUI rappresentante il limbo con visibilità di interfaccia
	 * @see ViewLimbo
	 */
	public void setVistaLimbo(ViewLimbo vistaLimbo) {
		this.vistaLimbo = vistaLimbo;
	}
	
	/**
	 * Inoltra la richiesta all'ambasciatore che a sua volta la inotrerà al Server
	 * @see ControllerLimbo
	 * 
	 */
	@Override
	public ControllerTavolo entraNelTavolo(Integer idTavolo) throws TavoloInesistenteException {
		ControllerTavolo nuovoTavolo = this.ambasciatore.joinTavolo(idTavolo);
		return nuovoTavolo;
	}

	/**
	 * Inoltra la richiesta all'ambasciatore che a sua volta la inotrerà al Server
	 * @see ControllerLimbo
	 * 
	 */
	@Override
	public ControllerTavolo creaNuovoTavolo() {
		ControllerTavolo nuovoTavolo =  this.ambasciatore.creaTavolo();	//torna il riferimento al tavolo creato	(con visibilità dell'interfaccia ControllerTavolo
		return nuovoTavolo;
	}
	
	/**
	 * @see ControllerLimbo
	 * 
	 */
	@Override
	public ArrayList<BeanTavolo> aggiornaTavoli() {
		ArrayList<BeanTavolo> infoTavoli = this.ambasciatore.richiediListaTavoliAperti();
		return infoTavoli;
	}
	
	/**
	 * Riceve le informazioni aggiornate riguardanti i tavoli aperti dal server e le comuniche alla GUI.
	 * @param infoTavoliAperti - le informazioni riguardanti i tavoli aperti
	 * @see BeanTavolo
	 */
	public void riceviInfoTavoli(ArrayList<BeanTavolo> infoTavoliAperti){
		//comunico alla grafica
		this.vistaLimbo.aggiornaTavoli(infoTavoliAperti);
	}
	
	/**
	 * @see AccountEStatistiche
	 */
	@Override
	public void chiusuraServer(){
		ControllerUtente controllerChiusura = new Utente();
		this.vistaLimbo.chiusuraServer(controllerChiusura);
	}

	
}