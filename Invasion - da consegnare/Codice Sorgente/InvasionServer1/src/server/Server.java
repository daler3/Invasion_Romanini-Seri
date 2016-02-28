package server;

import interfacceRemote.AcceptInterface;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import rmi.AcceptClass;
import serverGUI.ControllerServer;
import socket.DemoneSocket;
import classiCondivise.BeanGiocatore;
import classiCondivise.BeanTavolo;
import classiCondivise.Classifica;
import mappa.Mappa;
import dbManager.*;
import exceptionCondivise.GiocatoreNonTrovatoException;
import exceptionCondivise.TavoloInesistenteException;


/**
 * Classe che gestisce il lato Server del gioco Invasion
 * Singleton
 * @authors Daniele Romanini - Federico Seri
 */
public class Server implements ControllerServer {
	
	/**
	 * Istanza del Server. 
	 * Se è null creo una nuova istanza 
	 */
	private static Server INSTANCE = null; 
	
	/**
	 * Riferimento al database Manager, per gestire il database
	 */
	private DbManager dbmInvasion; 
	
	/**
	 * È la connessione al db.
	 */
	private Connection connessioneDb;
	
	/**
	 * Lista di giocatori connessi
	 */
	private ArrayList<GiocatoreConnesso> utentiConnessi; 
	
	/**
	 * Lista di tavoli Aperti
	 */
	private ArrayList <TavoloAperto> tavoli; 
	
	/**
	 * Liste di partite in corso
	 */
	private ArrayList <Partita> partite; 
	
	/**
	 * Riferimento al registri dell'rmi.
	 */
	private Registry registry;

	/**
	 * Il riferimento al demone del socket
	 */
	private DemoneSocket demoneSocket;
	
	/**
	 * Host dell'rmi registry.
	 */
	private String host;
	
	/**
	 * Url dell'rmi registry.
	 */
	private String url;
	
	
	
	/**
	 * Costruttore privato
	 * Crea una nuova istanza della classe DbManager, per gestire il database
	 * @throws SQLException 
	 */
	private Server() {
		this.utentiConnessi = new ArrayList<GiocatoreConnesso>();
		this.tavoli = new ArrayList<TavoloAperto>();
		this.partite = new ArrayList<Partita>();
		this.dbmInvasion = new DbManager();
		try {
			this.connessioneDb = dbmInvasion.connessioneDb();
		} catch (InstantiationException e) {
			System.err.println("Problema nell'istanziazione del Database");
			System.exit(0);
		} catch (IllegalAccessException e) {
			System.err.println("Impossibile accedere del Database");
			System.exit(0);
		} catch (ClassNotFoundException e) {
			System.err.println("Impossibile accedere del Database");
			System.exit(0);
		} catch (SQLException e) {
			System.err.println("Database già in uso - Impossibile connettersi");
			System.exit(0);
		}
		//Carico le impostazioni del rmi e del socket dal file di properties

//		Properties propertiesDiEsempio = new Properties();
//		FileInputStream input = new FileInputStream("connessione.properties");
//		propertiesDiEsempio.load(input);
//		//Leggo le Properties
//		input.close();
//		System.out.println(propertiesDiEsempio.getProperty("rmi.host"));
//		
//		
		
		//		FileInputStream f = null;
//		Properties impostazioni = null;
//		try {
////			f = new FileInputStream("../InvasionCondiriso/connessione.properties");
//			f = new FileInputStream("connessione.properties");
//			impostazioni = new Properties();
//			this.host = impostazioni.getProperty("rmi.host");
//			String url1 = impostazioni.getProperty("rmi.urlParte1");
//			String url2 = impostazioni.getProperty("rmi.urlParte2");
//			this.url = url1+host+url2;
//			System.out.println(this.url);
//		} catch (IOException e1) {
//			System.err.println("File di properties non trovato");
//		} finally{
//			//Chiudiamo il file
//			if(f != null){
//				try {f.close();	} catch (IOException e) {}
//			}
//		}
//		this.host = "127.0.0.1";
//		this.url = "rmi://"+this.host+"/Invasion";
//		
		Properties fileProperties = new Properties();
		FileInputStream input;
		try {
			input = new FileInputStream("connessione.properties");
			fileProperties.load(input);
			input.close();
			this.host = fileProperties.getProperty("rmi.host");
			String url1 = fileProperties.getProperty("rmi.urlParte1");
			String url2 = fileProperties.getProperty("rmi.urlParte2");
			this.url = url1+host+url2;
			input.close();
		} catch (FileNotFoundException e) {
			System.err.println("File di properties non trovato - Non posso avviare il server");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Impossibile leggere il file di properties");
		}
//		
	}
	
	
	/**
	 * Metodo per crea un'istanza della classe Server, se essa ancora non esiste.
	 * @throws SQLException 
	 */
	private synchronized static void createInstance() {
		if(INSTANCE==null)
			INSTANCE = new Server();
	}
	
	
	/**
	 * Metodo per ottenere un riferimento alla classe Server
	 * @return l'unica istanza della classe Server
	 * @throws SQLException 
	 */
	public static Server getInstance(){
		if(INSTANCE == null)
			createInstance();
		return INSTANCE; 
	}
	

	/**
	 * Cerchiamo il nuovo idTavolo
	 * Sync perchè non vogliamo che la lista venga alterata nella ricerca.
	 * @return	L'id del nuovo tavolo.
	 */
	public synchronized Integer ottieniIdNuovoTavolo(){
		Iterator<TavoloAperto> itTavoli = this.tavoli.iterator();
		Integer idNuovoTavolo = 0;
		TavoloAperto tavolo;
		while(itTavoli.hasNext()){
			tavolo = itTavoli.next();
			if(tavolo.getIdTavolo() > idNuovoTavolo){
				idNuovoTavolo = tavolo.getIdTavolo();
			}
		}
		return idNuovoTavolo + 1;
	}
	
	/**
	 * Crea un tavolo su richiesta del client e ritorna il riferimento al tavolo creato
	 * Sync per non alterare la lista dei tavoli.
	 * @param creatoreTavolo	Il creatore del tavolo che verrà aggiunto subito.
	 * @return	il riferimento al tavolo appena creato
	 * @throws TavoloPienoException 
	 */
	public synchronized TavoloAperto creaTavolo(GiocatoreConnesso creatoreTavolo) {
		TavoloAperto nuovoTavolo = new TavoloAperto(ottieniIdNuovoTavolo(), creatoreTavolo);
		//Crea la lista di tavoli
		this.tavoli.add(nuovoTavolo);
		//Comunichiamo a tutti (tranne noi) il nuovo tavolo creato
		inviaListaTavoli(creatoreTavolo);
		return nuovoTavolo;
	}
	
	
//	/**
//	 * Prova ad aggiungere il giocatore alla lista dei giocatori connessi.
//	 * Il synchronized server per modificare l'arrayList di giocatori connessi.
//	 * @param giocatoreConnesso	Il GiocatoreConnesso creato alla connessione del client al server.
//	 * @param username			Lo username inviato per l'identificazione.
//	 * @param password			La password inviata per l'identificazione.
//	 * @return					L'esito dell'accesso
//	 * @throws SQLException		Impossibile effettuare il login.
//	 */
//	public synchronized boolean aggiungiGiocatoreConnesso(GiocatoreConnesso giocatoreConnesso,
//								String username, String password) throws SQLException {
//		if(giocatoreGiaConnesso(username))
//			return false;
//		boolean loginEffettuato = dbmInvasion.loginUtente(connessioneDb, username, password);
//		if(loginEffettuato){
//			//Aggiungiamo le informazioni al giocatore appena connesso
//			riempiBeanGiocatore(giocatoreConnesso, username);
//			//Aggiungiamo il giocatore ai giocatori connessi
//			this.utentiConnessi.add(giocatoreConnesso);
//		}
//		return loginEffettuato;
//	}
	
	
	/**
	 * Prova ad aggiungere il giocatore alla lista dei giocatori connessi.
	 * Il synchronized serve per modificare l'arrayList di giocatori connessi.
	 * @param giocatoreConnesso	Il GiocatoreConnesso creato alla connessione del client al server.
	 * @param username			Lo username inviato per l'identificazione.
	 * @param password			La password inviata per l'identificazione.
	 * @return					L'esito dell'accesso
	 * @throws SQLException		Impossibile effettuare il login.
	 */
	public synchronized boolean aggiungiGiocatoreConnesso(GiocatoreConnesso giocatoreConnesso,
								String username, String password) throws SQLException {
		if(giocatoreGiaConnesso(username))
			return false;
		boolean loginEffettuato = dbmInvasion.loginUtente(connessioneDb, username, password);
		if(loginEffettuato){
			//Aggiungiamo le informazioni al giocatore appena connesso
			riempiBeanGiocatore(giocatoreConnesso, username);
			//Aggiungiamo il giocatore ai giocatori connessi
			this.utentiConnessi.add(giocatoreConnesso);
		}
		return loginEffettuato;
	}
	
	
	
	
	
	/**
	 * Proviamo a registrare il giocatore nel DB.
	 * @param username	Nome del giocatore che si vuole registrare.
	 * @param password	Password del giocatore che si vuole registrare.
	 * @return			Esito registrazione.
	 * @throws SQLException	Impossibile registrarsi.
	 */
	public boolean registraNuovoGiocatore(String username, String password) throws SQLException{
		return dbmInvasion.registraUtente(connessioneDb, username, password);
	}
	
	
	/**
	 * Controlliamo se siamo nella lista dei giocatori connessi.
	 * Syncronized perchè non vogliamo interferenze nella lettura della lista.
	 * @param username	Username del giocatore che stiamo cercando
	 * @return	true: Giocatore già connesso;	false: Giocatore disconnesso;
	 */
	private synchronized boolean giocatoreGiaConnesso(String username){
		Iterator<GiocatoreConnesso> itGiocatori = utentiConnessi.iterator();
		while(itGiocatori.hasNext()){
			GiocatoreConnesso giocatore = itGiocatori.next();
			if(giocatore.getMieInfo().getNomeUtente().equals(username))
					return true;
		}
		return false;
	}
	
//	/**
//	 * Comunica un cambiamento nella lista dei tavoli a tutti gli utenti.
//	 * Synchronized per evitare modifiche alla lista dei giocatori connessi.
//	 */
//	public synchronized void inviaListaTavoli(){
//		Iterator<GiocatoreConnesso> itUtenti =  this.utentiConnessi.iterator();
//		GiocatoreConnesso giocatoreTemp; 
//		while(itUtenti.hasNext()){
//			giocatoreTemp = itUtenti.next();
//			giocatoreTemp.comunicaTavoli(this.tavoli);
//			//Il messaggio verrà inoltrato al client solo se si trova nel LIMBO
//		}
//	}
	
	
	/**
	 * Comunica le info della lista dei tavoli a tutti gli utenti.
	 * Synchronized per evitare modifiche alla lista dei giocatori connessi.
	 */
	public synchronized void inviaListaTavoli(){
		Iterator<GiocatoreConnesso> itUtenti =  this.utentiConnessi.iterator();
		GiocatoreConnesso giocatoreTemp; 
		//ottengo le info dei tavoli
		ArrayList<BeanTavolo> infoTavoli = ottieniInfoTavoli();
		while(itUtenti.hasNext()){
			giocatoreTemp = itUtenti.next();
			giocatoreTemp.comunicaBeanTavoli(infoTavoli);
			//Il messaggio viene inoltrato al client solo se si trova nel LIMBO
		}
	}
	
	
	
	/**
	 * Comunica un cambiamento nella lista dei tavoli a tutti gli utenti tranne l'escluso.
	 * Synchronized per evitare modifiche alla lista dei giocatori connessi.
	 * @param giocatoreEscluso	Giocatore non interessato dalla comunicazione 
	 */
	public synchronized void inviaListaTavoli(GiocatoreConnesso giocatoreEscluso){
		Iterator<GiocatoreConnesso> itUtenti =  this.utentiConnessi.iterator();
		GiocatoreConnesso giocatoreTemp; 
		//ottengo le info dei tavoli
		ArrayList<BeanTavolo> infoTavoli = ottieniInfoTavoli();
		while(itUtenti.hasNext()){
			giocatoreTemp = itUtenti.next();
			if(!giocatoreTemp.getUsername().equals(giocatoreEscluso.getUsername())){
				giocatoreTemp.comunicaBeanTavoli(infoTavoli); //comunico al client le info
			}
			//Il messaggio viene inoltrato al client solo se si trova nel LIMBO
		}
	}
	
	/**
	 * Ottiene una lista con le info dei tavoli aperti da inviare al client
	 * @return - la lista con le info dei tavoli aperti (id e numero giocatori)
	 */
	public synchronized ArrayList<BeanTavolo> ottieniInfoTavoli(){
		Iterator<TavoloAperto> itTavoli = this.tavoli.iterator();
		BeanTavolo infoTemp;
		//alloco lo spazio per la lista tavoli
		ArrayList<BeanTavolo> infoTavoli = new ArrayList<BeanTavolo>(this.tavoli.size()); 
		while(itTavoli.hasNext()){
			//ottengo le info del singolo tavolo
			infoTemp = itTavoli.next().ottieniBeanTavolo(); 
			//aggiungo il tavolo alla lista
			infoTavoli.add(infoTemp);
		}
		return infoTavoli; 
	}
	
	
	/**
	 * Viene chiamato al login o alla registrazione per istanziare le informazioni del giocatore
	 * @param giocatoreConnesso	Giocatore a cui si aggiungono le informazioni.
	 * @param username			Username del giocatore di cui vogliamo le informazioni.
	 * @throws SQLException		Problema al DB.
	 */
	private void riempiBeanGiocatore(GiocatoreConnesso giocatoreConnesso, String username) throws SQLException{
		BeanGiocatore mioBean = dbmInvasion.estraiBean(connessioneDb, username);
		giocatoreConnesso.setMieInfo(mioBean);
	}
	
	
	/**
	 * Viene chiamato da un giocatore che intende effettuare un cambio password.
	 * @param giocatoreConnesso Giocatore che intende cambaire la password
	 * @param vecchiaPassword	la vecchia password del giocatore
	 * @param nuovaPassword		la nuova password del giocatore
	 * @return - true se il cambiamento di password è andato a buon fine; -false altrimenti
	 * @throws SQLException		Problema al DB
	 */
	public boolean cambiaPassword(GiocatoreConnesso giocatoreConnesso, String vecchiaPassword, String nuovaPassword) throws SQLException{
		String username = giocatoreConnesso.getUsername();
		return dbmInvasion.cambiaPassword(this.connessioneDb, username, vecchiaPassword, nuovaPassword);
	}
	
	/**
	 * Serve per ottenere le info di un utente a partire dal suo username
	 * @param nomeUtente Il nome dell'utente di cui si vogliono ottenere le info
	 * @return il bean contente le info dell'utente richieste
	 * @throws GiocatoreNonTrovatoException giocatore non trovato
	 * @throws SQLException 
	 */
	@Override
	public BeanGiocatore ottieniInfoUtente(String nomeUtente){
		BeanGiocatore bean = null;
		try {
			bean = dbmInvasion.estraiBean(connessioneDb, nomeUtente);
		} catch (SQLException e) {
		}
		return bean;
	}
	
	/**
	 * Serve per ottenere le info di un utente 
	 * @param giocatore - il riferimento all'oggetto giocatore, del quale si vogliono ottenere le info 
	 * @return il bean contente le info dell'utente richieste
	 */
	public BeanGiocatore ottieniInfoUtente (GiocatoreConnesso giocatore){
		BeanGiocatore infoUtente = giocatore.getMieInfo().clone();   
		return infoUtente;
	}
	

	/**
	 * Ottiene la classifica del gioco, richiedendola al Db.
	 * @return La classifica generale del gioco.
	 * @throws SQLException 	Problema al Db
	 */
	@Override
	public Classifica ottieniClassifica() throws SQLException{
		return dbmInvasion.classificaGenerale(connessioneDb);
	}
	
	
	/**
	 * Metodo per ricavare il riferimento a un oggetto Giocatore, partendo dal suo Username 
	 * @param nomeGiocatore - lo username del giocatore di interesse
	 * @return -Il riferimento al giocatore corrispondente allo username passato come parametro. .-null se non viene trovato il giocatore con quel username
	 * @throws GiocatoreNonTrovatoException Il giocatore non è stato trovato
	 */
	public GiocatoreConnesso usernameToPlayer (String nomeGiocatore) throws GiocatoreNonTrovatoException {
		Iterator<GiocatoreConnesso> iteratoreGiocatori = this.utentiConnessi.iterator();
		GiocatoreConnesso giocatoreTemp;
		while(iteratoreGiocatori.hasNext()){
			giocatoreTemp = iteratoreGiocatori.next();
			if(giocatoreTemp.getUsername().equals(nomeGiocatore))
				return giocatoreTemp;  
		}
		throw new GiocatoreNonTrovatoException();   
	}
	
	
	/**
	 * Metodo per rimuovere un giocatore dalla lista dei giocatori connessi
	 * @param giocatoreDaRimuovere - il giocatore da rimuovere dalla lista dei giocatoi connessi
	 */
	public boolean rimuoviDaConnessi(GiocatoreConnesso giocatoreDaRimuovere){
		boolean esitoRimozione = this.utentiConnessi.remove(giocatoreDaRimuovere); 
		return esitoRimozione;
	}
	
	
	/**
	 * Metodo che aggiunge un giocatore a un tavolo.
	 * @param giocatoreDaAggiungere  	Giocatore da aggiungere al tavolo
	 * @param idtavolo					L'Id del tavolo a cui il giocatore si vuole aggiungere
	 * @return la lista dei giocatori connessi al tavolo se l'aggiunta è andata a buon fine; null altrimenti
	 * @throws TavoloPienoException 
	 */
	public synchronized TavoloAperto joinTavolo(GiocatoreConnesso giocatoreDaAggiungere, Integer idtavolo) throws TavoloInesistenteException{
		TavoloAperto tavoloTemp;
		tavoloTemp = idToTavolo(idtavolo); //ricavo il riferimento al tavolo
		boolean esito = tavoloTemp.aggiungiGiocatore(giocatoreDaAggiungere);
		if(esito){
			inviaListaTavoli(giocatoreDaAggiungere);
		}
		else throw new TavoloInesistenteException();
		return tavoloTemp;
	}
	
	
	/**
	 * Ci da il riferimento del tavolo a partire dal suo id.
	 * Sync perchè non vogliamo interferenza nella ricerca del tavolo.
	 * @param idtavolo	Id del tavolo ricercato.
	 * @return	Il riferimento al tavolo cercato.
	 * @throws TavoloInesistenteException	Tavolo inesistente.
	 */
	private synchronized TavoloAperto idToTavolo (Integer idtavolo) throws TavoloInesistenteException{
		Iterator<TavoloAperto> iteratoreTavoli = this.tavoli.iterator();
		TavoloAperto tavoloTemp;
		while(iteratoreTavoli.hasNext()){
			tavoloTemp = iteratoreTavoli.next();
			if(tavoloTemp.getIdTavolo().equals(idtavolo))
				return tavoloTemp;  
		}
		throw new TavoloInesistenteException(); //se arriva qui, non c'era il tavolo con quell'Id
	}
	
	
	
	/**
	 * Rimuoviamo il tavolo e comunichiamo a tutti gli utenti nel limbo il cambiamento
	 * @param tavolo	Il tavolo da rimuovere.
	 */
	public void rimuoviTavoloAperto(TavoloAperto tavolo){
		this.tavoli.remove(tavolo);
		inviaListaTavoli();
	}
	
	
	/**
	 * Rimuoviamo il tavolo e comunichiamo a tutti gli utenti nel limbo il cambiamento
	 * @param tavolo	Il tavolo da rimuovere.
	 */
	public synchronized void rimuoviTavoloAperto(TavoloAperto tavolo, GiocatoreConnesso giocatoreEscluso){
		this.tavoli.remove(tavolo);
		inviaListaTavoli(giocatoreEscluso);
	}
	
	/**
	 * Comunica al server i riferimenti al registry e al demone socket.
	 * @param r	Il registry dell'rmi.
	 * @param s	Il demone socket.
	 */
	private void setComunicazione(Registry r, DemoneSocket s){
		this.registry = r;
		this.demoneSocket = s;
	}
	
	/**
	 * Ferma la comnunicazione del server.
	 */
	public void spegniServer(){
		try {
			this.registry.unbind(url);
		} catch (RemoteException | NotBoundException e) {}
		this.demoneSocket.ferma();
	}
	
	public void accendiServer(){
		//Avviamo il socket
		DemoneSocket demoneSocket = new DemoneSocket();
		Thread demone = new Thread(demoneSocket);
		demone.setDaemon(true);
		demone.start();
		System.out.println("Comunicazione socket ON");
		//Avvio RMI
		Registry registry = null;
		try{
			if (System.getSecurityManager() == null){
				System.setSecurityManager(new SecurityManager());
			}
			AcceptInterface stub = new AcceptClass();
			registry = LocateRegistry.getRegistry(host); 
			registry.rebind(url, stub);
			Thread threadAccept = new Thread();
			threadAccept.start();
		} catch(RemoteException e){	}
		System.out.println("Comunicazione RMI ON");
		//Assegnamo al server i riferimenti al demone
		setComunicazione(registry, demoneSocket);
	}
	
	/** 
	 * Metodo che crea la lista dei nomi di giocatori a partire dai loro riferimenti
	 * @return - la lista (ordinata in base all'ordine della lista dei giocatori nella classe) dei nomi dei giocatori della partita
	 */
	private ArrayList<String> creaListaUsername(ArrayList<GiocatoreConnesso> giocatori){
		ArrayList<String> nomiGiocatoriPartita = new ArrayList<String>(giocatori.size());
		Iterator<GiocatoreConnesso> iteratoreGiocatori = giocatori.iterator();
		while(iteratoreGiocatori.hasNext()){
			String nomeTemp = iteratoreGiocatori.next().getUsername(); 
			nomiGiocatoriPartita.add(nomeTemp);
		}
		return nomiGiocatoriPartita;
	}
	
	
	/**
	 * Inizializza una nuova partita, rimuove il tavolo corrispondente, e fa quindi partire la partita, dopo aver comunicato ai giocatori l'imminente inizio di essa. 
	 * Sync per conservare l'integrità della lista di tavoli e della lista delle partite
	 * @param tavoloTemp - Il tavolo che sarà chiuso per essere convertito in Partita.
	 * @throws SQLException Problema al DB
	 * @throws GiocatoreNonTrovatoException 
	 */
	public synchronized void inizializzaPartita(TavoloAperto tavoloTemp, String  nomeCreatoreTavolo) throws SQLException, GiocatoreNonTrovatoException{
		ArrayList<GiocatoreConnesso> giocatoriPartita = tavoloTemp.getGiocatoriInTavolo();//giocatori della futura partita
		ArrayList<String> listaNomiGiocatori = creaListaUsername(giocatoriPartita);
		//ottengo l'Id della nuova partita
		Integer idPartita = dbmInvasion.creaPartita(connessioneDb, listaNomiGiocatori);
		//costruisco un nuovo oggetto di tipo Partita
		Partita nuovaPartita = new Partita(giocatoriPartita, idPartita);
		//rimuovo il tavolo aperto
		rimuoviTavoloAperto(tavoloTemp);
		//aggiungo la partita alla lista locale di partite
		this.partite.add(nuovaPartita); 
		//ricavo il riferimento al creatore del tavolo
		GiocatoreConnesso giocatoreEscluso = usernameToPlayer(nomeCreatoreTavolo);
		//comunico a tutti che la partita sta per cominciare, tranne al creatore del tavolo (lui lo saprà tramite valore di ritorno del suo comando "avviaTavolo"
		comunicoInizioPartita(giocatoriPartita, giocatoreEscluso);
		//creo un nuovo Thread per far girare la partita e lo faccio partire
		Thread threadPartita = new Thread(nuovaPartita); 
		threadPartita.start();
	}
	

	/**
	 * Comunico l'inizio della partita a tutti tranne al giocatore escluso dalla comunicazione, che sarà il creatore del tavolo.
	 * @param giocatoreEscluso - il giocatore escluso dalla comunicazione (il creatore del tavolo).
	 */
	public void comunicoInizioPartita(ArrayList<GiocatoreConnesso> listaGiocatoriTavolo, GiocatoreConnesso giocatoreEscluso){
		Iterator<GiocatoreConnesso> itGiocatoriTavolo = listaGiocatoriTavolo.iterator();
		GiocatoreConnesso giocatoreTemp; 
		while(itGiocatoriTavolo.hasNext()){
			giocatoreTemp = itGiocatoriTavolo.next();
			//se il prossimo giocatore non è il giocatore escluso, invio questo messaggio. 
			if (!giocatoreTemp.equals(giocatoreEscluso)){
				giocatoreTemp.comunicazioneInizioPartita(); 
			}
		}
	}
	
	
//	/**
//	 * Crea una lista di BeanGiocatore partendo da una lista di GiocatoreConnesso.
//	 * @param giocatori - la lista di GiocatoriConnesi
//	 * @return la lista di BeanGiocatore che 
//	 */
//	private ArrayList<BeanGiocatore> ottieniListaBean(ArrayList<GiocatoreConnesso> giocatori){
//		Iterator<GiocatoreConnesso> itGiocatori = giocatori.iterator();
//		ArrayList<BeanGiocatore> infoGiocatori = new ArrayList<BeanGiocatore>(giocatori.size());
//		while(itGiocatori.hasNext()){
//			infoGiocatori.add(itGiocatori.next().getMieInfo().clone()); //gli passo un clone
//		}
//		return infoGiocatori;
//	}
	
//	//review
//	public ArrayList<BeanGiocatore> getInfoPartecipanti(){
//		ArrayList<BeanGiocatore> infoGiocatori = new ArrayList<BeanGiocatore>(this.giocatoriIniziali.size());
//		Iterator<GiocatoreConnesso> itGiocIniz = this.giocatoriIniziali.iterator();
//		while(itGiocIniz.hasNext()){
//			infoGiocatori.add(itGiocIniz.next().getMieInfo().clone()); //gli passo un clone
//		}
//		return infoGiocatori;
//	}
	
	
	
	/**
	 * Chiude una partita e salva le informazioni relative nel Db. 
	 * @param classificaPartita - la classifica finale della partita che si è conclusa. 
	 * @throws SQLException Problema al DB
	 */
	public synchronized void chiudiPartita(ArrayList<GiocatoreConnesso> classificaPartita, Integer numeroGiocatori, Integer idPartita) throws SQLException{
		Iterator<GiocatoreConnesso> itClassifica = classificaPartita.iterator();
		int punteggioPrimo = Partita.punteggioPrimo(numeroGiocatori);
		int punteggioSecondo = Partita.punteggioSecondo(numeroGiocatori);
		GiocatoreConnesso giocatoreTemp; 
		String username;
		int punteggioGiocatore = 0; 
		int contClassifica = 1; //per tenere traccia della posizione in classifica 
		while(itClassifica.hasNext()){
			giocatoreTemp = itClassifica.next();
			username = giocatoreTemp.getUsername();
			if(contClassifica == 1)
				punteggioGiocatore = punteggioPrimo; 
			if(contClassifica == 2)
				punteggioGiocatore = punteggioSecondo; 
			if(contClassifica > 2){
				punteggioGiocatore = 0;
			}
			dbmInvasion.aggiornaUtentePostPartita(connessioneDb, username, idPartita, punteggioGiocatore, contClassifica);
			punteggioGiocatore = 0; 
			contClassifica ++;
		}
	}
	
	/**
	 * Crea la mappa e la invia a chi ne ha fatto richiesta.
	 * @return	La mappa.
	 * @throws SQLException	Impossibile accedere al DB.
	 */
	public synchronized Mappa ottieniMappa() throws SQLException{
		Mappa nuovaMappa = dbmInvasion.estraiMappa(connessioneDb);
		return nuovaMappa;
	}
	
	/**
	 * @see ControllerServer
	 */
	@Override
	public synchronized ArrayList<String> ottieniListaGiocatori(){
		ArrayList<String> nomiUtentiConnessi = new ArrayList<String>();
		nomiUtentiConnessi = creaListaUsername(utentiConnessi);
		return nomiUtentiConnessi;
	}
	
	/**
	 * Interroga il db.
	 * @see ControllerServer
	 */
	@Override
	public ArrayList<String> ottieniListaRegistrati() throws SQLException{
		return this.dbmInvasion.estraiListaGiocatori(connessioneDb);
	}
	
	/**
	 * Interroga il db.
	 * @see ControllerServer
	 */
	@Override
	public int totalePartiteGiocate() throws SQLException {
		return this.dbmInvasion.totalePartiteGiocate(connessioneDb);
	}
	
}
