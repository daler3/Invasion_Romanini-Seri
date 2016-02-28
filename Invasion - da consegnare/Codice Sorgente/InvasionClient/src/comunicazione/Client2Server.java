package comunicazione;

import java.sql.SQLException;
import java.util.ArrayList;

import classiCondivise.BeanGiocatore;
import classiCondivise.BeanTavolo;
import classiCondivise.Classifica;
import classiCondivise.Colori;
import exceptionCondivise.GiocatoreNonTrovatoException;
import exceptionCondivise.ProblemaAvvioTavoloException;
import exceptionCondivise.TavoloInesistenteException;
import exceptionCondivise.TerritorioNonTrovatoException;
import exceptionCondivise.UnitaInsufficientiException;

/**
 * Interfaccia per la comunicazione lato Client.
 *
 */
public interface Client2Server {
	
	/**
	 * Metodo per effettuare il login.
	 * Il server tenta il login con le informazioni passate.
	 * Il client attende il responso.
	 * @param username
	 * @param password
	 * @return un oggetto BeanGiocatore contenente le info dell'utente in caso di successo del login; null altrimenti
	 * @throws SQLException Problema al Db.
	 */
	public BeanGiocatore effettuaLogin(String username, String password) throws SQLException;
	
	/**
	 * Comando per effettuare il logout.
	 * @return	Esito del logout.
	 */
	public boolean effettuaLogout();
	
	/**
	 * Metodo per effettuare la registrazione.
	 * Il server registra l'utente (se possibile) ed effettua il login.
	 * @param username	username da registrare.
	 * @param password	password da registrare.
	 * @return un oggetto BeanGiocatore contenente le info dell'utente in caso di successo della registrazione; null altrimenti
	 */
	public BeanGiocatore effettuaRegistrazione(String username, String password);
	

	/**
	 * Metodo per cambiare la password. 
	 * @param vecchiaPassword - la vecchia password
	 * @param nuovaPassword - la nuova password
	 * @return true - se il cambio di password è andato a buon fine; false - altrimenti
	 * 
	 */
	public boolean cambiaPassword(String vecchiaPassword, String nuovaPassword); //TODO
	
	
	/**
	 * Richiede la classifica generale del gioco.
	 * @return la classifica generale del gioco
	 * 
	 */
	public Classifica chiediClassifica();
	
	/**
	 * Richiesta di ottenere le informazioni riguardanti un utente specifico.
	 * @param nomeUtente - il nome dell'utente di cui se vogliono le info
	 * @return le info riguardanti l'utente richiesto sotto forma di BeanGiocatore.
	 * @throws GiocatoreNonTrovatoException 
	 * 
	 */
	public BeanGiocatore ottieniInfoUtente(String usernameUtente) throws GiocatoreNonTrovatoException; 
	
	 /** 
	 * Metodo per richiedere la lista dei Tavoli Aperti
	 * @return - la lista con le info dei tavoli aperti (id e numero giocatori)
	 */
	public ArrayList<BeanTavolo> richiediListaTavoliAperti();
	
	
	/**
	 * Comando di creare un nuovo tavolo (a cui il creatore si aggiungerà)
	 * @return - l'id del tavolo creato
	 */
	public Integer creaTavolo();

	/**
	 * Metodo per aggiungersi a un tavolo. 
	 * Se l'aggiunta va a buon fine cambia lo stato attuale in "InTavolo."
	 * @param idtavolo					L'Id del tavolo a cui il giocatore si vuole aggiungere
	 * @return la lista dei  nomi dei giocatori connessi al tavolo se l'aggiunta è andata a buon fine; null altrimenti
	 * @throws TavoloInesistenteException 
	 * 
	 */
	public ArrayList<String> joinTavolo(Integer idTavolo) throws TavoloInesistenteException;
	
	/**
	 *Comando di abbandonare un tavolo. Fa abbandonare il tavolo al client
	 *
	 */
	public void abbandonaTavolo();

	
	/**
	 * Comando di avviare il tavolo a cui ha preso parte.
	 * @param idTavolo - l'id del tavolo che il client ha richiesto di avviare 
	 * @return true - se il tavolo verrà avviato; false - altrimenti
	 * @throws ProblemaAvvioTavoloException 
	 * 
	 */
	public boolean avviaTavolo(Integer idTavolo) throws ProblemaAvvioTavoloException;
	
	/**
	 * Invio il mio colore scelto.
	 * @param colore	Il colore scelto.
	 */
	public void sceltaColoreEffettuata(Colori colore);
	
	/**
	 * Invio le armate scelte.
	 * @param comandoPosizionamento	La stringa delle armate scelte.
	 */
	public void sceltaArmateInizialeEffettuata(String comandoPosizionamento);
	
	/**
	 * Comunico di voler passare il turno.
	 */
	public void passaTurno();
	
	/**
	 * Comando lo spostamento delle armate tra due territori
	 * @param numeroArmateDaSpostare	Il numero di armate da spostare.
	 * @param origine	Territorio di partenza.
	 * @param destinazione	Territorio di arrivo.
	 * @throws TerritorioNonTrovatoException	Impossibile trovare il territorio.
	 */
	public void comandoSpostaArmate(Integer numeroArmateDaSpostare, String origine, String destinazione) throws TerritorioNonTrovatoException;
	
	/**
	 * Comando di attaccare un territorio
	 * @param numeroUnitaAttaccanti	Unità con cui attacco.
	 * @param tAttaccato			Nome del territorio attaccato.
	 * @param tAttaccante			Nome del territorio da cui sferro l'attacco.
	 * @throws UnitaInsufficientiException	Numero di unità insufficienti per l'attacco.
	 */
	public void comandoAttacco(Integer numeroUnitaAttaccanti, String tAttaccato, String tAttaccante) throws UnitaInsufficientiException;
	
	/**
	 * Comunico l'abbandono della partita.
	 */
	public void abbandonaPartita();
	

	
}
