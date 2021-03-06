package client;

import interfacciaGUI.ControllerLimbo;
import interfacciaGUI.ControllerPartita;
import interfacciaGUI.ControllerTavolo;
import interfacciaGUI.ControllerUtente;
import interfacciaGUI.ViewTavolo;

import java.util.ArrayList;

import classiCondivise.BeanGiocatore;
import exceptionCondivise.GiocatoreNonTrovatoException;
import exceptionCondivise.ProblemaAvvioTavoloException;

/**
 * Classe rappresentante un tavolo aperto.
 * @see AccountEStatistiche 
 * @see ControllerTavolo
 */
public class Tavolo extends AccountEStatistiche implements ControllerTavolo {
	
	/**
	 * Numero massimo di giocatori per un tavolo
	 */
	final static int giocatoriMassimi = 6;
	
	/**
	 * Numero minimo di giocatori per avviare un tavolo
	 */
	final static int giocatoriMinimi = 3; 
	
	private ArrayList<String> elencoGiocatori;
	
	/**
	 * Id del tavolo
	 */
	private Integer idTavolo;
	
	/**
	 * Riferimento all'ambasciatore per comunicare con il server
	 */
	private Ambasciatore ambasciatore; 
	
	/**
	 * Riferimento all'interfaccia necessaria per comunicare aggiornamenti alla GUI.
	 * @see Viewtavolo
	 */
	private ViewTavolo vistaTavolo; 

	
	/**
	 * Creiamo localmente il tavolo e ci aggiungiamo alla lista
	 * @param creatoreTavolo Il nome del creatore del tavolo
	 * @param idTavolo ID numerico che viene incrementato dinamicamente
	 */
	public Tavolo(Integer idTavolo){
		elencoGiocatori = new ArrayList<String>(giocatoriMassimi);
		this.idTavolo = idTavolo;
		this.ambasciatore = Ambasciatore.getInstance();
		this.ambasciatore.setTavolo(this);
	}
	
	/**
	 * Setta il riferimento alla View, ovvero l'interfaccia con cui comunicare con la grafica 
	 * @see ViewTavolo
	 */
	public void setVistaTavolo(ViewTavolo vistaTavolo){
		this.vistaTavolo = vistaTavolo;
	}
	

	
	/**
	 * Ci aggiunge all'elenco dei giocatori
	 * @param giocatore 
	 * @return true se veniamo aggiunti, altrimenti false
	 * @throws Exception 
	 */
	public void joinTavolo(String giocatore) throws Exception{
		if(isFull()){
			throw new Exception("Tavolo pieno - Impossibile aggiungere nuovo giocatore");
		}
		else if(isInTavolo(giocatore)){
			throw new Exception("Sono già dentro - NON devo aggiornare");	//Lo stato me lo aggiorna chi mi ci ha messo
		}
		else{
			elencoGiocatori.add(giocatore);
		}
	}
	
	
	/**
	 * Aggiorna la lista dei giocatori
	 * @param listaGiocatori - la nuova lista dei giocatori
	 */
	public void aggiornaListaGiocatoriEView(ArrayList<String> listaGiocatori){
		this.elencoGiocatori = listaGiocatori;
		this.vistaTavolo.aggiornaUtenti(listaGiocatori); //comunico alla grafica
		
	}
	
	/**
	 * Aggiorna la lista dei giocatori
	 * @param listaGiocatori - la nuova lista dei giocatori
	 */
	public void aggiornaListaGiocatori(ArrayList<String> listaGiocatori){
		this.elencoGiocatori = listaGiocatori;
		//this.vistaTavolo.aggiornaUtenti(listaGiocatori);
		//comunico alla grafica
	}
	

	/**
	 * Numero di giocatori connessi
	 * @return il numero dei giocatori connessi
	 */
	public Integer numeroGiocatori(){
		return this.elencoGiocatori.size();
	}
	

	/**
	 * @return l'id del tavolo
	 */
	public Integer getIdTavolo() {
		return idTavolo;
	}
	
	/**
	 * @return l'elenco dei nomi dei giocatori nel tavolo
	 */
	public ArrayList<String> getElencoGiocatori() {
		return elencoGiocatori;
	}
	
	/**
	 * Ci dice se il tavolo è pieno e non si possono aggiungere nuovi giocatori
	 * @return true: il tavolo è pieno - false: si può ancora accedere al tavolo
	 */
	public boolean isFull(){
		if(elencoGiocatori.size() >= giocatoriMassimi)
			return true;
		return false;
	}
	
	private boolean isInTavolo(String giocatore){
		return elencoGiocatori.contains(giocatore);
	}


	/**
	 * Chiamato dall'utente per abbandonare il tavolo. Inoltra la richiesta all'Ambasciatore
	 * @see ControllerTavolo
	 * @see Ambasciatore
	 */
	@Override
	public ControllerLimbo abbandonaTavolo() {
		this.ambasciatore.abbandonaTavolo();
		ControllerLimbo controllerLimbo = new Limbo();
		return controllerLimbo;
	}

	
	/**
	 * Comando di avviare un tavolo
	 * @throws ProblemaAvvioTavoloException  - se c'è un problema nell'avvio del tavolo
	 * @see ControllerTavolo
	 */
	@Override
	public ControllerPartita avviaTavolo(Integer idTavolo) throws ProblemaAvvioTavoloException {
		if(this.ambasciatore.avviaTavolo(idTavolo)){
			ControllerPartita nuovaPartita = new Partita ();
			return nuovaPartita; 
		}
		else{
			return null;
		}
	}
	
	/**
	 * Comando da server di avviare la partita (se qualcun altro l'ha avviata).
	 * Chiama la grafica per aggiornarla.
	 */
	public void startPartita(){
		ControllerPartita nuovaPartita = new Partita();
		this.vistaTavolo.avviaPartita(nuovaPartita);
	}

	/**
	 * Inoltra la richiesta all'ambasciatore
	 * @see Controllertavolo
	 */
	@Override
	public BeanGiocatore getInfoUtente(String nome) throws GiocatoreNonTrovatoException {
		BeanGiocatore infoRichieste = this.ambasciatore.ottieniInfoUtente(nome);
		return infoRichieste;
	}

	@Override
	public void chiusuraServer() {
		ControllerUtente controllerChiusura = new Utente();
		this.vistaTavolo.chiusuraServer(controllerChiusura);
	}
	
	
	
	
}
