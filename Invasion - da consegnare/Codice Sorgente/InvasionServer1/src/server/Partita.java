package server;


import java.sql.SQLException;
import java.util.*;

import mappa.*;
import classiCondivise.*;
import eccezioni.NumeroGiocatoriErratoException;
import exceptionCondivise.ElementoNonPresenteException;
import exceptionCondivise.GiocatoreNonTrovatoException;
import exceptionCondivise.InserimentoMancanteException;
import exceptionCondivise.TerritorioNonTrovatoException;
import exceptionCondivise.UnitaInsufficientiException;

/**
 * Classe che rappresenta una partita in corso
 * @authors Daniele Romanini - Federico Seri
 * 
 */
public class Partita implements Runnable{
	
	
	protected final static int puntiPrimoSe6 = 40;
	protected final static int puntiSecondoSe6 = 10;
	protected final static int puntiPrimoSe5 = 32; 
	protected final static int puntiSecondoSe5 = 8;
	protected final static int puntiPrimoSe4 = 24; 
	protected final static int puntiSecondoSe4 = 6;
	protected final static int puntiPrimoSe3 = 16;
	protected final static int puntiSecondoSe3 = 4; 
	
	private final static int tempoDifesa = 3;
	private final static int tempoSceltaColore = 10;
	private final static int tempoPosizionaArmateIniziale = 60;
	private final static int tempoPosizionaArmateInizioTurno = 30;
	private final static int tempoMossa = 30;
	
	
	/**
	 * Id della partita
	 */
	private Integer idPartita; 
	
	/**
	 * Riferimento alla classe Server
	 */
	private Server mioServer = null; 
	
	/**
	 * Riferimento alla mappa della partita
	 */
	private Mappa mappa = null;
	
	/**
	 * Collection con i riferimenti ai giocatori iniziali della partita
	 */
	private ArrayList<GiocatoreConnesso> giocatoriIniziali = new ArrayList<GiocatoreConnesso>();
	
	/**
	 * Collection con i riferimenti ai gocatori attuali della partita
	 */
	private ArrayList<GiocatoreConnesso> giocatoriPartita = new ArrayList<GiocatoreConnesso>();
	
	/**
	 * Classifica dei giocatori a fine partita. Saranno aggiunti mano a mano che perdono.
	 */
	private ArrayList<GiocatoreConnesso> classificaGiocatori = new ArrayList<GiocatoreConnesso>();
	
	/**
	 * Lista di giocatori che hanno abbandonato la partita
	 */
	private ArrayList<GiocatoreConnesso> giocatoriRitirati = new ArrayList<GiocatoreConnesso>();
	
	/**
	 * Lista dei colori assegnati ai giocatori in ordine di turni
	 */
	private ArrayList<Colori> coloriInOrdine = new ArrayList<Colori>();
	
	/**
	 * Oggetto di supporto per il posizionamento delle armate a inizio turno
	 */
	private PosizionamentoArmate posizioneIniziale;
	
	/**
	 * Riferimento al giocatore di ogni turno
	 */
	private GiocatoreConnesso giocatoreTurno = null;
	
	/**
	 * Variabile booleana usata per stabilire se un giocatore ha svolto o meno la mossa durante il suo turno.
	 */
	private boolean mossaEseguita;
	
	/**
	 * Colore scelto da un giocatore. Variabile di appoggio da dove, a ogni scelta, si estrarrà il colore di ogni giocatore.
	 */
	private Colori coloreScelto = Colori.NEUTRO; 
	
	/**
	 * Variabile booleana per indicare la fase iniziale della partita
	 */
	private boolean faseIniziale = true; 
	
	/**
	 * Variabile booleana per indicare la fine della partita
	 */
	private boolean fineGioco = false; 
	
	/**
	 * Oggetto sul quale ci mettiamo in lock
	 */
	private Object lock = new Object(); 
	
	/**
	 * Costruttore della classe Partita. 
	 * @param giocatoriPartitaa - Collection con riferimenti ai thread corrispondendi ai giocatori della partita 
	 * @param mioServer - Riferimento alla classe Server
	 * @throws SQLException Impossibile accedere al DB.
	 */
	@SuppressWarnings("unchecked")
	public Partita(ArrayList<GiocatoreConnesso> giocatoriPartita, Integer idPartita) throws SQLException { 
		//prendo il riferimento al Server
		this.mioServer = Server.getInstance();
		//istanzio una nuova Mappa
		this.mappa = mioServer.ottieniMappa(); 
		//li clono perché i riferimenti devono essere diversi e devo gestire le due liste indipendentemente l'una dall'altra
		this.giocatoriPartita = (ArrayList<GiocatoreConnesso>) giocatoriPartita.clone();
		this.giocatoriIniziali = (ArrayList<GiocatoreConnesso>) giocatoriPartita.clone();
		this.classificaGiocatori = new ArrayList<GiocatoreConnesso>(giocatoriPartita.size());
		this.idPartita = idPartita;
	}
	
	/**
	 * Override del metodo run. 
	 * Inizializza la partita e gestisce la successione dei turni.
	 * Si occupa anche di gestire un eventuale chiusura della partita
	 */
	@Override
	public void run(){
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e1) {
			System.err.println("Il Thread è stato Interrotto");
			return; 
		} 
		Boolean continuaPartita = true;
		
		//Inizializzazione della partita
		inizializzaPartita();
		this.faseIniziale = false; //la fase iniziale è terminata
		//ora iniziano i turni
		while(continuaPartita){
			if(! this.fineGioco) //se non è finito il gioco, stabilisco un nuovo turno
				nuovoTurno(); 
			else {
				break;
			}
			while(this.giocatoreTurno.isTuoTurno()){ //fintanto che è il turno di quel giocatore
				if(this.fineGioco){ 
					continuaPartita = false;
					this.giocatoreTurno.setTuoTurno(false);
					break; 
				}
				attendiMossa(); //attendo una mossa dal giocatore
				if(! controlloUtenteConnesso(this.giocatoreTurno)){ //controllo che l'utente sia connesso
					gestisciDisconnessioneUtente(); //se non è connesso gestisco la disconnessione
				}
				if(! continuaIterazioneTurni()){ //controllo se continuare l'iterazione dei turni, ovvero se ci sono utenti sufficienti
					this.fineGioco = true;
				}
				if(this.fineGioco){
					break;
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					break; 
				}
			}
		}
		finePartita(); //fine della partita
		Thread.currentThread().interrupt(); //interrompo il thread
	}
	
	/**
	 * Controllo se devo continuare l'iterazione dei turni.
	 * @return true - se devo continuare; false - altrimenti. Ad esempio se non sono rimasti più giocatori connessi non devo continuare.
	 */
	private boolean continuaIterazioneTurni(){
		Boolean continua = true;;
		if(controlloGiocatoriRimasti()){ //se sono rimasti giocatori controllo la fine della partita, altrimenti non ci sono più giocatori connessi quindi la partita va chiusa
			if(controlloFinePartita()){ //se si è arrivati qui, sono rimasti giocatori, controllo se un giocatore ha vinto, se ha vinto entra in qusto ramo.
				continua = false; 
			} else continua = true;
		} else{
			continua = false; 
		}
		return continua;
	}
	
		
	/**
	 * Setta lo stato della classe rappresentante in giocatori a stato - partita e passa loro il riferimento alla partita in corso.
	 * 
	 */
	private synchronized void settaStatoGiocatori(){
		Iterator<GiocatoreConnesso> itGiocatori = giocatoriPartita.iterator();
		while(itGiocatori.hasNext()){
			itGiocatori.next().setStatoPartita(this); //comunico le info degli utenti di partita a tutti i partecipanti
		}
	}
	
	
	/**
	 * Crea una lista di BeanGiocatore partendo da una lista di GiocatoreConnesso.
	 * @param giocatori - la lista di GiocatoriConnesi
	 * @return la lista di BeanGiocatore che contiene le informazioni degli utenti connessi
	 * @see BeanGiocatore
	 */
	private synchronized ArrayList<BeanGiocatore> ottieniListaBean(ArrayList<GiocatoreConnesso> giocatori){
		Iterator<GiocatoreConnesso> itGiocatori = giocatori.iterator();
		ArrayList<BeanGiocatore> infoGiocatori = new ArrayList<BeanGiocatore>(giocatori.size());
		while(itGiocatori.hasNext()){
			infoGiocatori.add(itGiocatori.next().getMieInfo().clone()); //gli passo un clone
		}
		return infoGiocatori;
	}
	
	
	/**
	 * Stabilisce il nuovo giocatore del turno.
	 */
	private void nuovoTurno(){
		boolean cambiaTurno = false; 
		while(! cambiaTurno && ! this.fineGioco ){
			cambiaTurno = inizializzazioneTurno();
		}
	}
	
	
	/**
	 * Stabilisce il prossimo giocatore del turno nel caso in cui i giocatori siano ancora i giocatori iniziali e inizializza il turno.
	 * Controlla inoltre se l'utente di cui dovrebbe essere il turno è connesso. Ripete il controllo per tutta la fase di inizializzazione del turno, ovvero la comunicazione di questo agli altri giocatori e il ritiro dei rinforzi a inizio turno.
	 * @return true - se il prossimo giocatore nella lista può giocare il turno perché ancora connesso; falase - altrimenti
	 */
	private boolean inizializzazioneTurno(){
		GenericSupport<GiocatoreConnesso> giocatoriSupport = new GenericSupport<GiocatoreConnesso>();
		try {
			this.giocatoreTurno = giocatoriSupport.prossimo(this.giocatoreTurno, this.giocatoriIniziali);
			if(this.giocatoriRitirati.contains(this.giocatoreTurno)){ //disconnessione gia gestita
				return false; 
			}
		} catch (ElementoNonPresenteException e) {
			System.err.println("Il giocatore del turno non era presente tra i giocatori iniziali");
		}
		boolean utenteConnesso = controlloUtenteConnesso(this.giocatoreTurno); //controllo se l'utente che dovrebbe iniziare il turno è connesso
		if (utenteConnesso){
			inizioTurno(this.giocatoreTurno); //imposta solo tuo turno (attributo del giocatore) a true
			comunicaTurno(this.giocatoreTurno.getColoreGiocatore(), this.giocatoreTurno.getUsername());
			ritiraArmateInizioTurno(this.giocatoreTurno); //do al giocatore le armate bonus e aspetto che le posizioni
		} else {
			if(! this.classificaGiocatori.contains(this.giocatoreTurno)) //se non è connesso e non è in classifica giocatori
				gestisciDisconnessioneUtente();
			return false; 
		}
		//comunico la mappa definitiva dopo gli eventuali cambiamenti che il giocatore ha fatto
		comunicaMappa();
		//dopo la fase di ritiro delle armate bonus, potrebbe essersi disconnesso
		//ricontrollo quindi se l'utente è connesso
		utenteConnesso = controlloUtenteConnesso(this.giocatoreTurno); //controllo se l'utente è connesso
		if (utenteConnesso){
			return true; //la fase di inizializzazione turno è andata a buon fine
		} else {
			gestisciDisconnessioneUtente();
			return false; 
		}
	}
	
	/**
	 * Gestisce la disconnessione di un utente dalla partita. 
	 * Aggiunge questo ai giocatori ritirati e lo rimuove da quelli della partita. <br>
	 * Comunica la ritirata di questo agli altri giocatori e setta la variabile di fineGioco a true se i giocatori rimasti in partita sono 1 o meno.
	 */
	private void gestisciDisconnessioneUtente(){
		this.giocatoriRitirati.add(this.giocatoreTurno); //lo aggiungo ai ritirati
		this.giocatoriPartita.remove(this.giocatoreTurno); //lo rimuovo dai giocatori della partita
		this.giocatoreTurno.setTuoTurno(false);
		comunicaRitirata(this.giocatoreTurno.getUsername()); //comunico la sua ritirata
		if(this.giocatoriPartita.size() <= 1){
			this.fineGioco = true;
		}
	}
	
	/**
	 * Controlla se un utente è ancora connesso alla partita
	 * @param giocatore - il giocatore da controllare
	 * @return true - se il giocatore è connesso; false - altrimenti
	 */
	private boolean controlloUtenteConnesso(GiocatoreConnesso giocatore){
		if (giocatore.getMiaPartita()!= this){
			//allora l'utente è disconnesso o comunque non è più in questa partita
			return false;
		}
		return true; 
	}
	
	
	/**
	 * Metodo che inizializza la partita
	 * Inizia stabilendo l'ordine iniziale di gioco, finendo con il comunicare ai giocatori la mappa definitiva, dopo la scelta dei colori, l'assegnazione dei territori, e il posizionamento di tutte le armate
	 * 
	 */
	private void inizializzaPartita() {
		settaStatoGiocatori(); //setto lo stato dei giocatori a in partita
		stabilisciOrdineIniziale(); //stabilisco l'ordine iniziale
		sceltaColore(); //
		comunicaOrdineColori();
		comunicaMappa(); //comunico come è fatta la mappa al client
		assegnaTerritoriIniziali(); //localmente
		try {
			assegnaArmateIniziali();
		} catch (NumeroGiocatoriErratoException e1) {
			System.err.println("E' stata avviata una partita con un numero troppo elevato di armate");
			return; 
		} //localmente
		try {
			faseInizialePosizionamentoArmate();
		} catch (InterruptedException e1) {
			if(Thread.interrupted()){ //se il thread è interrotto, finisco
				System.err.println("Il thread è stato interrotto");
				return; 
			}
		} //comunico al client i suoi territori e le sue armate, e gli chiedo di posizionarle
		controllaPosizionamento();
		comunicaMappa();
	}
	
	
	/**
	 * Setta il flag turno nel giocatore a true, per indicare che il suo turno comincia.
	 * @param giocatore - giocatore che deve iniziare il turno
	 */
	private void inizioTurno(GiocatoreConnesso giocatore){
		giocatore.setTuoTurno(true); //setto il turno (ma non glielo comunico)
	}
	
	
	/**
	 * Comunica a tutti i giocatori (tranne colui di cui è il turno) che il turno di un altro giocatore sta per cominciare
	 * @param colore - colore del giocatore il cui turno sta per cominciare
	 * @param username - username del giocatore il cui turno sta per cominciare
	 */
	private void comunicaTurno(Colori colore, String username){
		Iterator<GiocatoreConnesso> iteratoreGiocatori = this.giocatoriPartita.iterator();
		GiocatoreConnesso giocatoreTemp;
		while(iteratoreGiocatori.hasNext()){
			giocatoreTemp = iteratoreGiocatori.next();
			//se non è il turno di quel giocatore, gli si comunica di chi è il turno che sta per cominciare
			if(!giocatoreTemp.isTuoTurno()){
				if(giocatoreTemp.getMiaPartita() == this)
					giocatoreTemp.comunicaTurno(colore, username);
			}
		}
	}
	
	
	/**
	 * Setta a false il flag di fineTurno di un giocatore.
	 */
	private void fineTurno(){
		this.giocatoreTurno.setTuoTurno(false);	
	}
	

	
	
	/**
	 * Ci mettiamo in attesa della mossa del giocatore per un numero predeterminato di secondi prima di passare.
	 */
	private void attendiMossa(){
		this.mossaEseguita = false;
		comunicaGiocatoreEffettuareMossa();
			attendi(tempoMossa*1000 + 1000);
			if(! controlloUtenteConnesso(this.giocatoreTurno)){
				gestisciDisconnessioneUtente();
				this.mossaEseguita = false; 
			}//se la mossa è eseguita, mi metto in attesa che essa finisca, prima di attenderne una nuova.
			if(this.mossaEseguita){ //se la mossa non è stata eseguita, passa al turno successivo
				attendi();
				if(! controlloUtenteConnesso(this.giocatoreTurno)){
					gestisciDisconnessioneUtente();
					this.mossaEseguita = false; 
				}
			}
			else{
				fineTurno();
			}		
	}
	
	/**
	 * Metodo per mettersi in attesa
	 * @param tempo - il tempo da aspettare
	 */
	private synchronized void attendi(Integer tempo){
		try {
			wait(tempo);
		} catch (InterruptedException e) {
			return;
		}
	}
	
	/**
	 * Metodo per mettersi in attesa
	 */
	private synchronized void attendi(){
		try {
			wait();
		} catch (InterruptedException e) {
			return;
		}
	}
	
	
	/**
	 * Metodo per notificare la fine della mossa alla partita
	 */
	private synchronized void notificaFineMossa(){
		this.mossaEseguita = false; 
		notifyAll();	
	}
	
	/**
	 * Metodo per comunicare al giocatore attuale del turno di effettuare una mossa.
	 */
	private void comunicaGiocatoreEffettuareMossa(){
		if(this.giocatoreTurno.getMiaPartita() == this)
			this.giocatoreTurno.comunicaAttendiMossa(tempoMossa);
	}
	
	
	/**
	 * Metodo per notificare che una mossa è iniziata, bisogna quindi mettersi in attesa della fine di questa.
	 */
	private synchronized void comunicaMossaEseguita(){
		this.mossaEseguita = true;
		notifyAll();	
	}
	
	/**
	 * Metodo per stabilire l'ordine dei turni della partita
	 * Mescola random la lista dei giocatori della partita. 
	 * Chiama poi un metodo che comunica a tutti i giocatori l'ordine stabilito, comunicando i nomi dei partecipanti alla partita. 
	 */
	private synchronized void stabilisciOrdineIniziale(){
		//mescolo la lista dei giocatori per stabilire l'ordine iniziale
		Collections.shuffle(this.giocatoriIniziali); 
	}
	
	
	/** 
	 * Metodo che crea la lista dei nomi dei giocatori della partita
	 * @return - la lista (ordinata in base all'ordine della lista dei giocatori nella classe) dei nomi dei giocatori della partita
	 */
	private synchronized ArrayList<String> creaListaUsername(ArrayList<GiocatoreConnesso> listaGiocatori){
		ArrayList<String> nomiGiocatori = new ArrayList<String>(listaGiocatori.size());
		Iterator<GiocatoreConnesso> iteratoreGiocatori = listaGiocatori.iterator();
		while(iteratoreGiocatori.hasNext()){
			nomiGiocatori.add(iteratoreGiocatori.next().getUsername());
		}
		return nomiGiocatori;
	}
	
		
	/**
	 * Metodo che si occupa di far scegliere il colore ai vari giocatori. 
	 * Nell'ordine di gioco, comunica a ogni giocatore di scegliere il colore di gioco (con un certo tempo).
	 * Se la scelta non viene fatta entro il tempo, si assegna un colore di default al giocatore
	 * Una volta terminata la scelta, si comunica a tutti i giocatore l'ordine dei colori. 
	 */
	private synchronized void sceltaColore(){
		this.coloriInOrdine = new ArrayList<Colori>(this.giocatoriIniziali.size());
		Colori coloreTemp = null; 
		GiocatoreConnesso giocatoreTemp; 
		Iterator<GiocatoreConnesso> iteratoreGiocatori = this.giocatoriIniziali.iterator();
		ArrayList<Colori> coloriDisponibili = riempiListaColori(); 
		while(iteratoreGiocatori.hasNext()){
			giocatoreTemp = iteratoreGiocatori.next();
			//chiamo il metodo per la scelta del colore nella classe Player
			if(giocatoreTemp.getMiaPartita() == this)
				giocatoreTemp.scegliColore(tempoSceltaColore, coloriDisponibili);
			attendiSceltaColore();
			coloreTemp = this.coloreScelto;
			//se mi ritorna null significa che non ha scelto nulla, quindi glielo assegno io di default
			if(coloreTemp == null || coloreTemp == Colori.NEUTRO){
				coloreTemp = coloriDisponibili.get(0); //scelgo il primo nella lista (l'utente non aveva scelto in tempo)
			}
			coloriDisponibili.remove(coloreTemp);
			this.coloriInOrdine.add(coloreTemp);
			giocatoreTemp.setColoreGiocatore(coloreTemp);
			this.coloreScelto = Colori.NEUTRO;
		}
	}
	
	
	/**
	 * Riempie una lista di colori con tutti i colori disponibili
	 * @return - un ArrayList con tutti i colori disponibili
	 */
	private ArrayList<Colori> riempiListaColori(){
		ArrayList<Colori> listaColori = new ArrayList<Colori>(Arrays.asList(Colori.values()));	//Arrays.asList(enum) -> Ritorna una lista con tutti gli enum
		listaColori.remove(Colori.NEUTRO); //rimuovo il neutro dalla lsta dei colori
		return listaColori;
	}
	
	/**
	 * Metodo per mettersi in attesa della scelta del colore di un giocatore, secondo il tempo prestabilito.
	 */
	private synchronized void attendiSceltaColore(){
		try {
			wait(tempoSceltaColore*1000 + 1000);
		} catch (InterruptedException e) {
			return;
		}
	}
	
	/**
	 * Metodo chiamato dal giocatore per comunicare che un colore è stato scelto
	 * @param colore - il colore scelto dal giocatore
	 */
	public synchronized void coloreScelto(Colori colore){
		this.coloreScelto = colore; 
		notifyAll(); 
	}
	
	
	private synchronized void comunicaOrdineColori(){
		ArrayList<BeanGiocatore> listaInfoGiocatori = ottieniListaBean(this.giocatoriIniziali);
		Iterator<GiocatoreConnesso> iteratoreGiocatori = this.giocatoriIniziali.iterator();
		while(iteratoreGiocatori.hasNext()){
			GiocatoreConnesso giocatoreTemp = iteratoreGiocatori.next();
			if(giocatoreTemp.getMiaPartita() == this)
				giocatoreTemp.comunicaOrdinePartita(listaInfoGiocatori, this.coloriInOrdine);
		}
	}
	
	
	/**
	 * Assegna i territori iniziali (senza comunicare nulla ai giocatori).
	 * Mescola una lista di territori e ne assegna, uno per volta, ai giocatori della partita
	 */
	private void assegnaTerritoriIniziali(){
		GiocatoreConnesso giocatoreTemp = null; 
		Territorio territorioTemp;
		ArrayList<Territorio> territori = (mappa.getListaTerritori());
		Iterator<Territorio> iteratoreTerritori = territori.iterator();
		//mescolo la lista di territori
		Collections.shuffle(territori);
		//classe generica dove è implementato il metodo "prossimo", che mi torna il prossimo elemento in un'ArrayList
		//se si è arrivati alla fine della lista, ritorna l'indice del primo elemento.
		//se invece ciò che è preso in ingresso è null, torna il primo elemento della lista.
		GenericSupport<GiocatoreConnesso> giocatoriSupport = new GenericSupport<GiocatoreConnesso>();
		while(iteratoreTerritori.hasNext()){
			try {
				giocatoreTemp=giocatoriSupport.prossimo(giocatoreTemp, this.giocatoriIniziali);
			} catch (ElementoNonPresenteException e) {
				System.err.println("Il giocatore cercato non è stato trovato");
			}
			territorioTemp=iteratoreTerritori.next();
			territorioTemp.setColorePossessore(giocatoreTemp.getColoreGiocatore());
			//aggiungo il territorio ai territori posseduti di quel giocatore
			giocatoreTemp.aggiungiTerritoriPosseduti(territorioTemp);
		}
	}
	

	/**
	 * Metodo che assegna il numero di armate iniziali, in base al numero di giocatori:
	 * - 40 armate se si gioca in 3
	 * - 35 armate se si gioca in 4
	 * - 30 armate se si gioca in 5
	 * - 25 armate se si gioca in 6
	 * @throws NumeroGiocatoriErratoException 
	 */
	private void assegnaArmateIniziali() throws NumeroGiocatoriErratoException{
		Iterator<GiocatoreConnesso> iteratoreGiocatori = this.giocatoriIniziali.iterator();
		GiocatoreConnesso giocatoreTemp;
		int numeroGiocatori = this.giocatoriIniziali.size();
		for(int i=0; i<numeroGiocatori; i++){
			giocatoreTemp = iteratoreGiocatori.next();
			switch(numeroGiocatori){
				case 3: giocatoreTemp.setNumeroArmateDaPosizionare(40);
						continue;
				case 4: giocatoreTemp.setNumeroArmateDaPosizionare(35);
						continue;
				case 5: giocatoreTemp.setNumeroArmateDaPosizionare(30);
						continue;
				case 6: giocatoreTemp.setNumeroArmateDaPosizionare(25);
						continue;
				default: throw new NumeroGiocatoriErratoException();
			}
		}
	}
	

	
	
	private void faseInizialePosizionamentoArmate() throws InterruptedException{		
		this.posizioneIniziale = new PosizionamentoArmate(this.giocatoriIniziali.size()); 
		this.posizioneIniziale.abilitaAggiunta(); 
		comunicaTerritoriEPosizionaArmate(); //mando un messaggio ai giocatori comunicando loro i territori e le armate possedute
		this.posizioneIniziale.attendi(tempoPosizionaArmateIniziale+1); //ora sto in attesa
		this.posizioneIniziale.disabilitaAggiunta();
		estraiSceltaGiocatori();
	}
	
	/**
	 * Estraggo le scelte di posizionamento armate dei giocatori.
	 */
	private void estraiSceltaGiocatori(){
		Iterator<GiocatoreConnesso> itGiocatori = this.giocatoriPartita.iterator();
		GiocatoreConnesso giocatoreTemp; 		
		while(itGiocatori.hasNext()){	
			giocatoreTemp = itGiocatori.next();	
			try{
				estraiSceltaGiocatoreSingolo(giocatoreTemp);
			} catch (InserimentoMancanteException | TerritorioNonTrovatoException e){
				continue; 
			}
		}
	}
	
	/**
	 * Estrae la scelta di posizionamento armate di un singolo giocatore.
	 * @param giocatoreTemp - il giocatore di cui voglio estrarre la scelta
	 * @throws InserimentoMancanteException - se il giocatore non ha inserito
	 * @throws TerritorioNonTrovatoException - se il giocatore ha posizionato armate su un territorio non nella mappa
	 */
	private void estraiSceltaGiocatoreSingolo(GiocatoreConnesso giocatoreTemp) throws InserimentoMancanteException, TerritorioNonTrovatoException{
		String[][] tabellaGiocatore;
		try {
			tabellaGiocatore = this.posizioneIniziale.tabellaPosizionamenti(giocatoreTemp.getUsername());
			for(int i = 0; i < tabellaGiocatore.length; i++){
				String nomeTerritorio = tabellaGiocatore[i][0];
				Integer numeroArmate = Integer.parseInt(tabellaGiocatore[i][1]);
				if(numeroArmate > giocatoreTemp.getNumeroArmateDaPosizionare()){
					numeroArmate = giocatoreTemp.getNumeroArmateDaPosizionare();
				}
				Territorio territorioTemp = nomeToTerritorio(nomeTerritorio);
				territorioTemp.aggiungiUnita(numeroArmate);
				giocatoreTemp.setNumeroArmateDaPosizionare(giocatoreTemp.getNumeroArmateDaPosizionare()-numeroArmate);
				if(giocatoreTemp.getNumeroArmateDaPosizionare() <= 0){
					break;
				}
				//le ho aggiunte tutte sulla mappa, poi la controllo. 
			}
		} catch (ElementoNonPresenteException e) {
			//Il giocatore non aveva inserito il comando
			return;
		}

	}	
		
	
	/**
	 * Chiamata da giocatore per aggiungere il comando di posizionamento nell'apposita classe di supporto
	 * @param comandoPosizionamento - il comando di posizionamento delle armate sotto forma di stringa
	 */
	public void aggiungiPosizionamento(String comandoPosizionamento){
		synchronized(lock){
			this.posizioneIniziale.aggiungiComando (comandoPosizionamento); 
		}
	}
	
	
	
	/**
	 * Comunica ai giocatori la mappa, i territori posseduti e le armate da posizionare a inizio partita.
	 */
	private void comunicaTerritoriEPosizionaArmate(){
		Iterator<GiocatoreConnesso> iteratoreGiocatori = this.giocatoriPartita.iterator();
		GiocatoreConnesso giocatoreTemp;
		while(iteratoreGiocatori.hasNext()){
			//Estraiamo il giocatore
			giocatoreTemp = iteratoreGiocatori.next();
			//questo metodo cominicherà le armate da posizionare, i territori posseduti, e il tempo che si ha per farlo. 
			//i territori posseduti e il numero di armate sono attributi della classe GiocatoreConnesso
			if(giocatoreTemp.getMiaPartita() == this)
				giocatoreTemp.comunicaTerritoriPosizionaArmate(this.mappa, tempoPosizionaArmateIniziale);			
		}

	}
	
	/**
	 * Mettodo per posizionare delle armate su un territorio.
	 * @param giocatore - il giocatore che vuole posizonare le armate
	 * @param territorioPos - il territorio su cui posizionare le armate
	 * @param numeroArmate - numero armate da posizionare
	 * @throws TerritorioNonTrovatoException - se il territorio non è stato trovato
	 */
	public void setArmateSuTerritorio(GiocatoreConnesso giocatore, Territorio territorioPos, Integer numeroArmate) throws TerritorioNonTrovatoException{
		if(territorioPos.isEnemy(giocatore.getColoreGiocatore())){
			throw new TerritorioNonTrovatoException(); //non era il tuo territorio
		}
		territorioPos.aggiungiUnita(numeroArmate);
		giocatore.setNumeroArmateDaPosizionare((giocatore.getNumeroArmateDaPosizionare())-numeroArmate);
	}
	
	
	/**
	 * Estrae i territori vuoti (creandone una lista) da una lista di territori
	 * @param listaTerritori - lista di territori 
	 * @return - i territori vuoti, cioè su cui non sono state posizionate armate, contenuti nella listaTerritori, passata come parametro
	 */
	private ArrayList<Territorio> creaListaTerritoriVuoti(ArrayList<Territorio> listaTerritori){
		Iterator<Territorio> itTerritori = listaTerritori.iterator();
		ArrayList<Territorio> territoriVuoti = new ArrayList<Territorio>();
		Territorio territorioTemp;
		while (itTerritori.hasNext()){
			territorioTemp=itTerritori.next();
			if(territorioTemp.getUnitaPresenti().equals(0)){
				territoriVuoti.add(territorioTemp);
			}
		}
		return territoriVuoti;
	}
	
	
	/**
	 * Metodo che regolarizza il posizionamento delle armate sui territori di un giocatore, nel caso in cui non abbia posizionato tutte le armate assegnatogli e abbia lasciato dei territori vuoti
	 * @param giocatore - il giocatore di cui si vuole regolarizzare il posizionamento delle armate
	 * @param territoriVuoti - la lista dei territori vuoti di quel giocatore
	 */
	private void piuArmatePiuTerritoriVuoti(GiocatoreConnesso giocatore, ArrayList<Territorio> territoriVuoti){
		int x = 1; //numero unita da aggiungere a ogni iterazione
		while(!territoriVuoti.isEmpty() && giocatore.getNumeroArmateDaPosizionare()>0){
			territoriVuoti.get(0).aggiungiUnita(x);
			territoriVuoti.remove(0);
			giocatore.setNumeroArmateDaPosizionare(giocatore.getNumeroArmateDaPosizionare()-x);
		}
	}
	
	
	/**
	 * Metodo che regolarizza il posizionamento delle armate sui territori di un giocatore, nel caso in cui non abbia posizionato tutte le armate assegnatogli, ma non abbia lasciato territori vuoti (cioè abbia posizionato almeno un'armata su ogni territorio)
	 * @param giocatore - il giocatore di cui si vuole regolarizzare il posizionamento delle armate
	 */
	private void piuArmateNoTerritoriVuoti(GiocatoreConnesso giocatore){
		int x = 1;
		while(giocatore.getNumeroArmateDaPosizionare()>0){
			int i = (new Random()).nextInt(giocatore.getTerritoriPosseduti().size());
			giocatore.getUnTerritorio(i).aggiungiUnita(x);
			giocatore.setNumeroArmateDaPosizionare(giocatore.getNumeroArmateDaPosizionare()-x); 
		}
	}
	
	/**
	 * Metodo che regolarizza il posizionamento delle armate sui territori di un giocatore, nel caso in cui abbia posizionato tutte le armate assegnatogli, ma non abbia lasciato territori vuoti, cioè non abbia posizionato almeno un'armata su ogni territorio
	 * @param giocatore - il giocatore di cui si vuol regolarizzare il posizionamento delle armate
	 * @param territoriVuoti - la lista dei territori vuoti di quel giocatore
	 * 
	 */
	private void noArmatePiuTerritoriVuoti(GiocatoreConnesso giocatore, ArrayList<Territorio> territoriVuoti){
		int x = 1; 
		Territorio territorioTemp = null;
		GenericSupport<Territorio> itTerritoriSupport = new GenericSupport<Territorio>();
		while(!territoriVuoti.isEmpty()){
			try {
				territorioTemp = itTerritoriSupport.prossimo (territorioTemp, giocatore.getTerritoriPosseduti());
			} catch (ElementoNonPresenteException e1) {
				continue;
			} 
			if(territorioTemp.getUnitaPresenti().equals(1) || territorioTemp.getUnitaPresenti().equals(0)) //o è 1 o è vuoto
				continue; //passo alla prossima iterazione
			try{
				territorioTemp.rimuoviUnita(x);
				territoriVuoti.get(0).aggiungiUnita(x);
				territoriVuoti.remove(0);
			} catch(UnitaInsufficientiException e){
				continue;
			}			
		}
	}
	
	
	/**
	 * Controlla che il posizionamento delle armate di un singolo giocatore a inizio partita sia corretto e, nel caso non lo sia, lo regolarizza.
	 * @param giocatoreTemp - il giocatore di cui si vuole controllare il posizionamento delle armate
	 * @param territoriVuoti - lista dei territori vuoti, cioè su cui il giocatore non ha posizionato alcuna armata
	 * @throws ElementoNonPresenteException 
	 * @throws UnitaInsufficientiException 
	 * 
	 */
	private void controlloPosizionamentoSingoloGiocatore(GiocatoreConnesso giocatoreTemp, ArrayList<Territorio> territoriVuoti) {
		if(giocatoreTemp.getNumeroArmateDaPosizionare()>0 && territoriVuoti.size()>0){
			//comincio col posizionare le pedine rimanenti sui territori vuoti
			piuArmatePiuTerritoriVuoti(giocatoreTemp, territoriVuoti);
			
		}
		
		if(giocatoreTemp.getNumeroArmateDaPosizionare()>0 && territoriVuoti.size()==0){
			//posiziono le pedine rimanenti a random sui suoi territori
			piuArmateNoTerritoriVuoti(giocatoreTemp);
		}
		
		if(giocatoreTemp.getNumeroArmateDaPosizionare().equals(0) && territoriVuoti.size()>0){
			//se ci sono territori vuoti ma non pu' armate da posizionare, 
			//prendo un'armata da territorio con più di un'armata, la tolgo da quel territorio e la posiziono su un territorio vuoto
			//rimuovo quindi il territorio su cui ho posizionato l'armata dalla lista dei territori vuoti
			noArmatePiuTerritoriVuoti(giocatoreTemp, territoriVuoti); 
		}
	}
	
	
	/**
	 * Controlla che il posizionamento delle armate di tutti i giocatori a inizio partita sia corretto.
	 *
	 */
	private synchronized void controllaPosizionamento() {
		Iterator<GiocatoreConnesso> itGiocatori = this.giocatoriPartita.iterator();
		GiocatoreConnesso giocatoreTemp;
		while(itGiocatori.hasNext()){
			giocatoreTemp = itGiocatori.next();
			ArrayList<Territorio> territoriVuoti = creaListaTerritoriVuoti(giocatoreTemp.getTerritoriPosseduti());
			//a questo punto ho una lista con i territori vuoti
			controlloPosizionamentoSingoloGiocatore(giocatoreTemp, territoriVuoti);
			
		}
	}
	
	
	/**
	 * Si occupa di comunicare la mappa a tutti i giocatori. 
	 * Chiama, per ogni giocatore, il metodo di comunicazione della mappa.
	 */
	public synchronized void comunicaMappa(){
		Iterator<GiocatoreConnesso> iteratoreGiocatori = this.giocatoriPartita.iterator();
		while(iteratoreGiocatori.hasNext()){
			GiocatoreConnesso giocatoreTemp = iteratoreGiocatori.next();
				if(giocatoreTemp.getMiaPartita() == this)
					giocatoreTemp.comunicaMappa(this.mappa);
		}
	}
	
	
	/**
	 * Metodo per controllare se il concorrente corrente deve ritirare il bonus di possesso di tutti i territori dello stesso continente, passato come parametro
	 * @param giocatorePartita - giocatore della partita che inizia il turno
	 * @param continente - il continente da controllare
	 * @return -il valore del bonus corrispondente al continente, se il giocatore possiede tutti i territori del continente; -0 se il giocatore non possiede tutti i territori di quel continente
	 */
	private Integer ritiraBonusContinenteSingolo (GiocatoreConnesso giocatorePartita, Continente continente){
		//controllo se il giocatore passato come parametro possiede tutto il continente
		if(continente.hoBonusContinente(giocatorePartita.getColoreGiocatore())){
			//allora il bonus va ritirato
			return continente.getBonus(); 
		}
		return 0; 
	}
	
	
	/**
	 * Metodo per controllare se il concorrente corrente deve ritirare il bonus di possesso di tutti i territori dello stesso continente
	 * @param giocatorePartita - giocatore della partita che inizia il turno
	 * @return la somma dei bonus derivanti dal possesso di uno o più continenti per intero. Ritorna 0 se il giocatore non possiede tutti i territori di alcun continente 
	 */
	private int ritiraBonusContinenti (GiocatoreConnesso giocatorePartita){
		//lista dei continenti
		ArrayList<Continente> listaContinentiTemp = mappa.getListaContinenti();
		//variabile intera che andrà a contenere l'intero valore del bonus
		int bonus=0; 
		Iterator <Continente> iteratoreContinenti = listaContinentiTemp.iterator();
		while(iteratoreContinenti.hasNext()){
			//sommo l'eventuale bonus derivante dal possesso del continente singolo
			bonus=bonus+ritiraBonusContinenteSingolo(giocatorePartita, iteratoreContinenti.next());
		}
		return bonus; 
	}
	
	/**
	 * Metodo per assegnare il numero di armate a un giocatore a inizio turno, derivante dal numero dei territori posseduti
	 * @param giocatorePartita - giocatore della partita che inizia il turno
	 * @return il numero di armate assegnate a un giocatore derivanti dai territori posseduti:
	 */
	private Integer ritiraArmateTerritoriPosseduti(GiocatoreConnesso giocatorePartita){
		Double numeroTerritori = (double) giocatorePartita.getTerritoriPosseduti().size();
		Double divisore = 3.0;
		//faccio il ceiling
		return (int) Math.ceil(numeroTerritori/divisore);
	}
	
	/**
	 *
	 * Metodo per assegnare il numero di armate a un giocatore a inizio turno
	 * @param giocatorePartita - il giocatore della partita che inizia il turno
	 * @return il numero totale di armate assegnate a un giocatore a inizio turno
	 */
	private void ritiraArmateInizioTurno(GiocatoreConnesso giocatorePartita){
		Integer armateTotali = ritiraArmateTerritoriPosseduti(giocatorePartita) + ritiraBonusContinenti(giocatorePartita);
		giocatorePartita.setNumeroArmateDaPosizionare(armateTotali);

		this.posizioneIniziale = new PosizionamentoArmate(1); //solo per un giocatore 
		this.posizioneIniziale.abilitaAggiunta(); 
		if(giocatorePartita.getMiaPartita() == this)
			giocatorePartita.comunicaArmateInizioTurno(armateTotali, tempoPosizionaArmateInizioTurno); //mando un messaggio al giocatore di posizionare le armate
		try {
			this.posizioneIniziale.attendi(tempoPosizionaArmateInizioTurno+1); //mo sto in attesa
		} catch (InterruptedException e) {
			System.err.println("Eccezione posiz armate");
		} 
		this.posizioneIniziale.disabilitaAggiunta();
		try {
			try {
				estraiSceltaGiocatoreSingolo(giocatorePartita);
			} catch (TerritorioNonTrovatoException e) {
				System.err.println("Il giocatore probabilmente non aveva la mappa aggiornata");
				giocatorePartita.comunicaMappa(mappa);
			}
		} catch (InserimentoMancanteException e) {
			//ha perso le armate
		}	
		giocatorePartita.setNumeroArmateDaPosizionare(0);
	}
	
	
	/**
	 * Metodo per ricavare il riferimento a un oggetto Giocatore, partendo dal suo colore corrente all'interno della partita
	 * @param coloreGiocatore - il colore del giocatore di interesse
	 * @return -Il riferimento al giocatore corrispondente al colore passato come parametro. -null se non viene trovato il giocatore con quel colore
	 * @throws GiocatoreNonTrovatoException Il giocatore non è stato trovato
	 */
	public GiocatoreConnesso giocatoreAssociatoAlcolore (Colori coloreGiocatore) throws GiocatoreNonTrovatoException{
		Iterator<GiocatoreConnesso> iteratoreGiocatori = this.giocatoriIniziali.iterator();
		GiocatoreConnesso giocatoreTemp;
		while(iteratoreGiocatori.hasNext()){
			giocatoreTemp = iteratoreGiocatori.next();
			if(giocatoreTemp.getColoreGiocatore() == coloreGiocatore)
				return giocatoreTemp;  
		}
		throw new GiocatoreNonTrovatoException(); 
	}
	
	
	/**
	 * Metodo per ricavare il riferimento a un oggetto Territorio, partendo dal suo nome
	 * @param nomeTerritorio - il nome del di interesse
	 * @return -Il riferimento al territorio corrispondente al nome passato come parametro. -null se non viene trovato il territorio con quel nome
	 */
	public Territorio nomeToTerritorio (String nomeTerritorio) throws TerritorioNonTrovatoException{
		Iterator<Territorio> iteratoreTerritori = this.mappa.getListaTerritori().iterator();
		Territorio territorioTemp;
		while(iteratoreTerritori.hasNext()){
			territorioTemp = iteratoreTerritori.next();
			if(territorioTemp.getNome().equals(nomeTerritorio))
				return territorioTemp;  
		}
		throw new TerritorioNonTrovatoException();
	}
	
	
	/**
	 * Metodo chiamato dal giocatore per attaccare.
	 * @param numeroUnitaAttaccanti					numero delle unità coinvolte nell'attacco
	 * @param territorioAttaccato					territorio attaccato
	 * @param territorioAttaccante					territorio attaccante
	 * @throws UnitaInsufficientiException 			le unità per l'attacco sono insufficienti
	 * @throws GiocatoreNonTrovatoException 		il giocatore difensore non è stato trovato
	 * @throws TerritorioNonTrovatoException 		uno dei territori coinvolti non è stato trovato	
	 */
	public void attacco(Integer numeroUnitaAttaccanti, String tDifensore, String tAttaccante, GiocatoreConnesso giocatoreAttaccante) throws UnitaInsufficientiException{
		if(this.fineGioco){
			return; 
		}
		Territorio territorioAttaccante;
		Territorio territorioDifensore;
		try {
			territorioAttaccante = nomeToTerritorio(tAttaccante);
			territorioDifensore = nomeToTerritorio(tDifensore);
		} catch (TerritorioNonTrovatoException e) {
			System.err.println("Il territorio non è stato trovato");
			return;
		}
		//Inizializziamo
		Integer [] risultati = new Integer[2];
		boolean conquistato = false; 
		Colori coloreGiocatoreDifensore = territorioDifensore.getColorePossessore();
		GiocatoreConnesso giocatoreDifensore;
		try {
			giocatoreDifensore = giocatoreAssociatoAlcolore(coloreGiocatoreDifensore);			
		} catch (GiocatoreNonTrovatoException e) {
			System.err.println("Il giocatore difensore non è stato trovato tra quelli iniziali della partita");
			return;
		}
		//Controlli
		
		//Attacchiamo con il massimo di unità disponibili	
		if(territorioAttaccante.maxUnitaAttacco() < numeroUnitaAttaccanti){
				numeroUnitaAttaccanti = territorioAttaccante.maxUnitaAttacco();
		}
		//Se non possiamo attaccare desistiamo
		if(! attaccoConsentito(numeroUnitaAttaccanti, territorioDifensore,territorioAttaccante, giocatoreAttaccante)){
			return;
		}
		
		//FINE CONTROLLI - INIZIO ATTACCO
		comunicaMossaEseguita();
		//Lanciamo i dadi
		ArrayList<Integer> risultatoDadiAttacco = lanciaDadi(numeroUnitaAttaccanti);
		Integer numeroUnitaDifesa = territorioDifensore.maxUnitaDifesa();
		ArrayList<Integer> risultatoDadiDifesa = lanciaDadi(numeroUnitaDifesa);
		//Confrontiamo i risultati
		risultati = confrontaRisultati(risultatoDadiAttacco, risultatoDadiDifesa);
		//Applichiamo il risultato dell'attacco: leviamo le armate perse ambo le parti
		modificaTerritori(risultati, territorioDifensore, territorioAttaccante, numeroUnitaAttaccanti);
		//Controlliamo se abbiamo conquistato
		if(! territorioAttaccante.isEnemy(territorioDifensore)){
			conquistato=true;
			giocatoreDifensore.getTerritoriPosseduti().remove(territorioDifensore);
			giocatoreAttaccante.getTerritoriPosseduti().add(territorioDifensore);
		}else 
			conquistato=false;
		//comunica i risultati a tutti
		comunicaRisultatiAttacco(tempoDifesa, territorioAttaccante, territorioDifensore, risultatoDadiAttacco, risultatoDadiDifesa, risultati, conquistato); 
		//comunico la mappa aggiornata
		comunicaMappa();
		if(haPerso(giocatoreDifensore)){
			giocatoreDifensore.setTuoTurno(false);
			//Comunichiamo la sconfitta solo se non si è ritirato - Il ritiro l'abbiamo già comunicato in precedenza
			if(! giocatoriRitirati.contains(giocatoreDifensore)){
				//Quando un giocatore perde lo mettiamo in cima alla classifica
				this.classificaGiocatori.add(0, giocatoreDifensore);
				//Comunichiamo a tutti la sconfitta del giocatore
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.err.println("Il thread è stato interrotto");
				}
				//Rimuoviamo il giocatore dai giocatori della partita
				if(this.giocatoriPartita.size() == 2){ //prima dell'attacco
					this.classificaGiocatori.add(0, giocatoreAttaccante);
					this.fineGioco = true;
				} else {
					comunicaSconfitta(giocatoreDifensore.getUsername());
					this.giocatoriPartita.remove(giocatoreDifensore);
				}
			}
		}
		notificaFineMossa();
	} 
	
	
	/**
	 * Stabilisce se l'attacco è consentito o meno.
	 * @param numeroUnitaAttaccanti			numero delle unità coinvolte nell'attacco
	 * @param territorioAttaccato			territorio attaccato
	 * @param territorioAttaccante			territorio attaccante
	 * @param giocatoreAttaccante			il giocatore che ha richiesto l'attacco
	 * @return								true - se l'attacco è consentito; false - altrimenti
	 * @throws UnitaInsufficientiException	le unità per l'attacco sono insufficienti
	 */
	private boolean attaccoConsentito(Integer numeroUnitaAttaccanti, Territorio territorioAttaccato, Territorio territorioAttaccante, GiocatoreConnesso giocatoreAttaccante) throws UnitaInsufficientiException{
		//Controllo che sia il tuo turno
		if(! giocatoreAttaccante.isTuoTurno()){
			return false;
		}
		//Controlliamo che il giocatore attaccante possegga il territorio
		if(! giocatoreAttaccante.getTerritoriPosseduti().contains(territorioAttaccante)){
			return false;
		}
		//Non possiamo attaccarci un nostro stato
		if(giocatoreAttaccante.getTerritoriPosseduti().contains(territorioAttaccato)){
			return false;
		}
		//Controlliamo che i due territori siano compatibili per l'attacco
		if(! territorioAttaccante.possoAttaccareDaQui(territorioAttaccato, numeroUnitaAttaccanti)){
			return false;
		}
		if(numeroUnitaAttaccanti > territorioAttaccante.maxUnitaAttacco()){
			numeroUnitaAttaccanti = territorioAttaccante.maxUnitaAttacco();
		}
		return true;
	}
	
	/**
	 * Metodo chiamato dal giocatore per spostare le armate (e quindi passate).
	 * @param numeroArmateDaSpostare			le armate da spostare
	 * @param origine							il territorio di origine
	 * @param destinazione						il territorio di destinazione
	 * @param giocatore							il giocatore che richiede lo spostamento
	 * @throws TerritorioNonTrovatoException 	uno dei due territori (origine o destinazione) non è stato trovato	
	 */
	public void spostaArmate(Integer numeroArmateDaSpostare, String origine, String destinazione, GiocatoreConnesso giocatore) throws TerritorioNonTrovatoException{
		if(this.fineGioco){
			return; 
		}
		if(!giocatore.isTuoTurno()){
			return;
		}
		Territorio territorioOrigine = nomeToTerritorio(origine);
		Territorio territorioDestinazione = nomeToTerritorio(destinazione);
		synchronized(lock){
			comunicaMossaEseguita();
			try {
				territorioOrigine.spostaUnita(numeroArmateDaSpostare, territorioDestinazione);
				//fineTurno();
			} catch (Exception e) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} finally{
				comunicaMappa();
			}
			if(! this.faseIniziale)
				this.posizioneIniziale.notifica(); 
			fineTurno();
			notificaFineMossa();	//Una volta che abbiamo spostato le armate passiamo il turno.
		}
	}
	
	/**
	 * Metodo chiamato dal giocatore per passare il turno.
	 * @param giocatore - il giocatore che ha passato il turno
	 */
	public void passaTurno(GiocatoreConnesso giocatore){
		if(this.fineGioco){
			return; 
		}
		if(!giocatore.isTuoTurno()){
			System.err.println("Non puoi passare quando non è il tuo turno");
			return;
		}
		synchronized(lock){
			comunicaMossaEseguita();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(! this.faseIniziale)
				this.posizioneIniziale.notifica(); //nell'eventualità che passi prima di aver finito di posizionare le armate
			fineTurno();
			notificaFineMossa();
		}
	}
	
	/**
	 * Comunicazione dei risultati di un attacco
	 * @param tempoDifesa 			il tempo che la difesa ha per difendersi
	 * @param territorioAttaccante 	il territorio attaccante
	 * @param territorioAttaccato	il territorio attaccato
	 * @param risultatoDadiAttacco	il risultato dei dadi dell'attacco
	 * @param risultatoDadiDifesa	il risultato dei dadi della difesa
	 * @param risultati				il numero di armate sconfitte dall'attacco in posizione 0, il numero di armate perse dall'attacco in posiaiozne 1
	 * @param conquistato			true - se il territorio è stato conquistato; false - altrimenti
	 */
	public synchronized void comunicaRisultatiAttacco(Integer tempoDifesa, Territorio territorioAttaccante, Territorio territorioAttaccato, ArrayList<Integer> risultatoDadiAttacco, ArrayList<Integer> risultatoDadiDifesa, Integer [] risultati, boolean conquistato){
		Iterator<GiocatoreConnesso> itGiocatoriPartita = this.giocatoriPartita.iterator();
		while(itGiocatoriPartita.hasNext()){
			GiocatoreConnesso giocatoreTemp = itGiocatoriPartita.next();
			if(giocatoreTemp.getMiaPartita() == this)
				giocatoreTemp.comunicaAttacco(tempoDifesa, territorioAttaccante, territorioAttaccato, risultatoDadiAttacco, risultatoDadiDifesa, risultati, conquistato);
		}
	}
	
	

	/**
	 * Modifica i territori in seguito a un attacco
	 * @param risultati 			il numero di armate sconfitte dall'attacco in posizione 0, il numero di armate perse dall'attacco in posiaiozne 1
	 * @param territorioAttaccato	il territorio attaccato
	 * @param territorioAttaccante	il territorio attaccante
	 * @param numeroUnitaAttaccanti il numero delle unità attaccanti
	 */
	private void modificaTerritori(Integer [] risultati, Territorio territorioAttaccato, Territorio territorioAttaccante, Integer numeroUnitaAttaccanti){
		//Rimuoviamo le unità perse da attaccante e difensore
		try {
			territorioAttaccato.rimuoviUnita(risultati[0]);
			territorioAttaccante.rimuoviUnita(risultati[1]);
		} catch (UnitaInsufficientiException e) {
			System.err.println("Rimozione di un numero troppo elevato di armate");
		}
		//Se conquistiamo il territorio spostiamo le nostre armate nel territorio conquistato
		if(territorioAttaccato.getUnitaPresenti().intValue() <= 0){
			//Lo facciamo nostro
			territorioAttaccato.setColorePossessore(territorioAttaccante.getColorePossessore());
			//Spostiamo nel territorio appena conquistato le unità sopravvissute all'attacco
			territorioAttaccato.setUnitaPresenti(numeroUnitaAttaccanti-risultati[1]);
			//Rimuoviamo dal territorio attaccante le unità spostate nel nuovo territorio
			try {
				territorioAttaccante.rimuoviUnita(numeroUnitaAttaccanti-risultati[1].intValue());
			} catch (UnitaInsufficientiException e) {
				System.err.println("Rimosse troppe unità dopo la conquista");
			}
		}
	}
	
	/**
	 * Confronta i risultati di un attacco
	 * @param attacco	ArrayList contente i dadi dell'attacco.
	 * @param difesa	ArrayList contente i dadi dell'attacco.
	 * @return Integer[0] -> unità distrutte alla difesa;	Integer[1] -> unità distrutte all'attacco.
	 */
	private Integer[] confrontaRisultati(ArrayList<Integer> attacco, ArrayList<Integer> difesa){
		//Estraiamo il numero di dadi lanciati e in minDadi mettiamo il minor numero di dadi estratti
		Integer numeroDadiAttacco = attacco.size();
		Integer numeroDadiDifesa = difesa.size();
		Integer minDadi = Math.min(numeroDadiAttacco, numeroDadiDifesa);
		//Ordiniamo le due liste dal numero più piccolo al più grande
		Collections.sort(attacco);
		Collections.sort(difesa);
		//array destinato a contenere i risultati dell'attacco
		//in prima posizione unità sconfitte, in seconda unità perse
		Integer [] risultati = {0, 0}; 
		Integer attaccoTemp;
		Integer difesaTemp;
		for(int i=1; i <=minDadi; i++){
			//Estraiamo dal dado più grande al più piccolo
			attaccoTemp = attacco.get(attacco.size()-i);
			difesaTemp = difesa.get(difesa.size()-i);
			if(attaccoTemp > difesaTemp){
				distruggiArmataDifensore(risultati);
			} else distruggiArmataAttaccante(risultati);
		}
		return risultati; 
	}
	
	/**
	 *  Aggiunge un'unità in posizione 0 all'ArrayList che conterrà i risultati di un attacco. In posizione 1 ci saranno le armate perse dell'attacco.
	 * @param risultato - riferimento all'ArrayList che conterrà i risultati dell'attacco
	 */
	private void distruggiArmataAttaccante(Integer[] risultato){
		risultato[1]++;
	}
	
	/**
	 * Aggiunge un'unità in posizione 0 all'ArrayList che conterrà i risultati di un attacco. In posizione 0 ci saranno le armate sconfitte dall'attacco.
	 * @param risultato - riferimento all'ArrayList che conterrà i risultati dell'attacco
	 */
	private void distruggiArmataDifensore(Integer[] risultato){
		risultato[0]++;
	}
	
	

	/**
	 * Lancio dei dadi.
	 * @param numeroDadi - numero dei dadi da lanciare
	 * @return ArrayList<Integer> con il risultato del dadi
	 */
	private ArrayList<Integer> lanciaDadi(Integer numeroDadi){
		ArrayList<Integer> risultatoDadi = new ArrayList<Integer>(numeroDadi);
		for(int i=0; i<numeroDadi; i++){
			Dado dado = new Dado();
			risultatoDadi.add(dado.lanciaDado());
		}
		return risultatoDadi;
	}
	
	
	/**
	 * Controlla se sono rimasti dei giocatori nella partita
	 * @return true - se ci sono ancora giocatori; false - altrimenti
	 */
	private boolean controlloGiocatoriRimasti(){
		if(this.giocatoriPartita.size() == 0){
			return false;
		} else return true;
	}
	
	
	/**
	 * Metodo per controllare se una partita è finita, ovvero se un giocatore ha conquistato tutti i territori della mappa.
	 * @return -true: se la partita è terminata; -false: altrimenti 
	 */
	public boolean controlloFinePartita(){
		//Controlliamo se siamo gli ultimi rimasti in partita
		if(this.giocatoriPartita.size() == 1)
			return true;	//Siamo gli ultimi, la partita deve finire.
		//Controlliamo se possediamo tutti i territori
		Iterator<Continente> itContinenti = this.mappa.getListaContinenti().iterator();
		Colori coloreTemp1, coloreTemp2; 
		//il metodo coloreBonusContinente() torna il colore 
		coloreTemp1 = itContinenti.next().coloreBonusContinente();
		//se già un solo continente non è posseduto da un solo giocatore, la partita non è sicuramente terminata
		if(coloreTemp1 == null) 
			return false; 
		while(itContinenti.hasNext()){
			coloreTemp2 = itContinenti.next().coloreBonusContinente();
			//Se i colori di due continenti sono diversi, o se, come prima, un continente non è posseduto da qualcuno, la partita non è sicuramente terminate
			//poichè, per terminare, tutti i continenti devono essere posseduti da un unico giocatore
			if(coloreTemp1 != coloreTemp2 || coloreTemp2 == null){
				return false;
			}
		}
		return true; 
	}
	
	
	/**
	 * Metodo per gestire la fine della partita
	 */
	private void finePartita() {
		Server mioServer = Server.getInstance();
		GiocatoreConnesso vincitore;
		if(giocatoriPartita.size() == 1){
			vincitore = giocatoriPartita.get(0);
			this.classificaGiocatori.add(0, vincitore); //ora ho la classifica dei giocatori completa
			//comunico la fine della partita ai giocatori
			comunicaFinePartita();
			try {
				mioServer.chiudiPartita(this.classificaGiocatori, this.giocatoriIniziali.size(), this.idPartita);
			} catch (SQLException e) {
				System.err.println("Non posso salvare la partita sul Db per un problema");
			}
		} else if (giocatoriPartita.size() == 0){
			try {
				mioServer.chiudiPartita(this.classificaGiocatori, this.giocatoriIniziali.size(), this.idPartita);
			} catch (SQLException e) {
				System.err.println("Non posso salvare la partita sul Db per un problema");
			}
		}
	}
	
	/**
	 * Controlla se un giocatore ha perso la partita
	 * @param giocatore - il giocatore da controllare
	 * @return true - se il giocatore ha perso; false - altrimenti
	 */
	private boolean haPerso(GiocatoreConnesso giocatore){
		return giocatore.getTerritoriPosseduti().isEmpty();
	}
	
	
	/**
	 * Metodo per comunicare la fine della partita (con i risultati) ai giocatori della partita rimasti 
	 * @param nomeVincitore - il nome del vincitore della partita
	 * @param punteggi - lista con i punteggi dei vari giocatori, ordinati secondo l'ordine di gioco
	 */
	private void comunicaFinePartita(){
		int punteggioPrimo = punteggioPrimo(this.giocatoriIniziali.size());
		int punteggioSecondo = punteggioSecondo(this.giocatoriIniziali.size());
		Iterator<GiocatoreConnesso> itGiocatori = this.giocatoriPartita.iterator();
		while(itGiocatori.hasNext()){
			GiocatoreConnesso giocatoreTemp = itGiocatori.next();
			if(giocatoreTemp.getMiaPartita() == this)
				giocatoreTemp.fineDellaPartita(creaListaUsername(this.classificaGiocatori),  punteggioPrimo, punteggioSecondo); 
		}
	}

	/**
	 * 
	 * @return - il riferimento al giocatore attuale del turno
	 */
	public GiocatoreConnesso getGiocatoreTurno() {
		return giocatoreTurno;
	}

	/**
	 * Setta il giocatore del turno
	 * @param giocatoreTurno - il giocatore del turno
	 */
	public void setGiocatoreTurno(GiocatoreConnesso giocatoreTurno) {
		this.giocatoreTurno = giocatoreTurno;
	}
	

	/**
	 * Comunico ai giocatori rimasti la sconfitta di un giocatore
	 * @param giocatorePerdente - il nome del giocatore che ha perso
	 */
	private void comunicaSconfitta(String giocatorePerdente){
		Iterator<GiocatoreConnesso> itGiocatori = giocatoriPartita.iterator();
		while(itGiocatori.hasNext()){
			GiocatoreConnesso giocatoreTemp = itGiocatori.next();
			if(giocatoreTemp.getMiaPartita() == this)
				giocatoreTemp.comunicaSconfitta(giocatorePerdente);
		}
	}
	
	
	/**
	 * Comunico ai giocatori rimasti la ritirata di un giocatore.
	 * @param giocatoreRitirato - nome del giocatore che si è ritirato
	 */
	private void comunicaRitirata(String giocatoreRitirato){
		Iterator<GiocatoreConnesso> itGiocatori = this.giocatoriPartita.iterator();
		while(itGiocatori.hasNext()){
			GiocatoreConnesso giocatoreTemp = itGiocatori.next();
			if(giocatoreTemp.getMiaPartita() == this)
				giocatoreTemp.comunicaRitirata(giocatoreRitirato);
		}
	}
	
	
	/**
	 * Rimuove il giocatore su richiesta del client dalla partita
	 * @param giocatore	Giocatore da rimuovere.
	 * @throws SQLException Problema al DB
	 */
	public void disconnettiUtente(GiocatoreConnesso giocatore) {
		//Lo rimuoviamo dalla partita e lo mettiamo nei ritirati
		rimuoviUtenteConnessoDaLista(giocatore);
		comunicaRitirata(giocatore.getUsername());
		//Se non ci sono più utenti connessi chiudiamo la partita.
		if(this.giocatoriPartita.size() == 0){
			Server mioServer = Server.getInstance();
			try {
				mioServer.chiudiPartita(this.classificaGiocatori, this.giocatoriIniziali.size(), this.idPartita);
			} catch (SQLException e) {
				System.err.println("Non posso salvare la partita sul Db per un problema");
			}
		}
	}

	
	/**
	 * Aggiunge un utente alla lista dei giocatori ritirati
	 * @param giocatore
	 */
	private synchronized void rimuoviUtenteConnessoDaLista(GiocatoreConnesso giocatore){
		this.giocatoriRitirati.add(giocatore);
	}
	
	/**
	 * Getter per la lista dei giocatori della partita
	 * @return -la lista dei giocatori attuali della partita
	 */
	public ArrayList<GiocatoreConnesso> getGiocatoriPartita() {
		return giocatoriPartita;
	}

	/**
	 * Getter l'id della partita
	 * @return l'id della partita
	 */
	public Integer getIdPartita() {
		return idPartita;
	}
	
	
	/**
	 * Metodo statico per ottenere il punteggio del primo classificato
	 * @param numeroGiocatori - il numero di giocatori della partita (che non si sono ritirati)
	 * @return il punteggio del primo classificato
	 */
	public static int punteggioPrimo(int numeroGiocatori){
		if(numeroGiocatori == 3)
			return puntiPrimoSe3;
		if(numeroGiocatori == 4)
			return puntiPrimoSe4;
		if(numeroGiocatori == 5)
			return puntiPrimoSe5;
		if(numeroGiocatori == 6)
			return puntiPrimoSe6;
		return 0; //non puo' essere per le regole del gioco
	}
	
	/**
	 * Metodo statico per ottenere il punteggio del secondo classificato
	 * @param numeroGiocatori - il numero di giocatori della partita (che non si sono ritirati)
	 * @return il punteggio del secondo classificato
	 */
	public static int punteggioSecondo(int numeroGiocatori){
		if(numeroGiocatori == 3)
			return puntiSecondoSe3;
		if(numeroGiocatori == 4)
			return puntiSecondoSe4;
		if(numeroGiocatori == 5)
			return puntiSecondoSe5;
		if(numeroGiocatori == 6)
			return puntiSecondoSe6;
		return 0; //non puo' essere per le regole del gioco
	}
	
	
//	/**
//	 * Prendendo in ingresso un elemento di una lista, torna l'elemento successivo.<br>
//	 * Se l'elemento in ingresso è null, ritorna il primo elemento della lista.<br>
//	 * Se l'elemento preso in ingresso è l'ultimo della lista, ritorna il primo elemento della lista.<br>
//	 * @param var 		Elemento contenuto in una lista.
//	 * @param lista 	La lista.
//	 * @return 			Il prossimo elemento nella lista.
//	 * @throws ElementoNonPresenteException	
//	 */
//	public synchronized GiocatoreConnesso prossimo(GiocatoreConnesso var, ArrayList<GiocatoreConnesso> lista) throws ElementoNonPresenteException{
//		if(var == null){
//			return lista.get(0);
//		}
//		//Non esiste il prossimo elemento
//		if (! lista.contains(var)){
//			throw new ElementoNonPresenteException(); //elemento mancante nella lista
//		}
//		int totaleGiocatori = lista.size();
//		//se come parametro gli passo un oggetto nullo, mi ritorna il primo della lista
//		int posizione = lista.indexOf(var);
//		if(posizione+1 == totaleGiocatori){	//Siamo arrivati alla fine del ciclo
//			return lista.get(0);	//Ci ritorna il primo giocatore
//		}
//		else{
//			return lista.get(posizione+1);
//		}		
//	}	
}
	
