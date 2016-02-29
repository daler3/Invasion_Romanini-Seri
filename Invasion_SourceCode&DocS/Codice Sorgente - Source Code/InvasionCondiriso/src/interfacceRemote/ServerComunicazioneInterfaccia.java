package interfacceRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
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
 * Interfaccia remota di comunicazione lato Server<br>
 * Sono presenti tutti i metodi per ricevere messaggi dal Client tramite comunicazione RMI.
 *
 */
public interface ServerComunicazioneInterfaccia extends Remote{
	
	
	/**
	 * Prova ad eseguire il login richiesto dall'utente.
	 * @param username	username dell'utente
	 * @param password	password dell'utente
	 * @return il BeanGiocatore con le info dell'utente se il login è andato a buon fine; null altrimenti
	 * @throws RemoteException
	 * @throws SQLException 
	 */
	public BeanGiocatore eseguiLogin(String username, String password) throws RemoteException, SQLException;
	
	
	/**
	 * Prova a eseguire la registrazione richiesta dall'utente.
	 * @param username	username dell'utente
	 * @param password	password dell'utente
	 * @return il BeanGiocatore con le info dell'utente se la registrazione è andato a buon fine; null altrimenti
	 * @throws RemoteException
	 */
	public BeanGiocatore eseguiRegistrazione(String username, String password) throws RemoteException;
	
	/**
	 * Metodo chiamato per eseguire il logout.
	 * @return l'esito del logout
	 * @throws RemoteException
	 */
	public boolean eseguiLogout() throws RemoteException;
	
	/**
	 * Metodo per cambiare la password.
	 * @param vecchiaPassword - la vecchia password
	 * @param nuovaPassword - la nuova password
	 * @return true - se il cambio di password è andato a buon fine; false - altrimenti
	 * @throws RemoteException
	 */
	public boolean cambiaPassword(String vecchiaPassword, String nuovaPassword) throws RemoteException;
	
	/**
	 * Ottiene la classifica generale del gioco
	 * @return la classifica generale del gioco
	 * @throws RemoteException
	 */
	public Classifica ottieniClassifica() throws RemoteException;
	
	
	/**
	 * Richiesta di ottenere le informazioni riguardanti un utente specifico.
	 * @param nomeUtente - il nome dell'utente di cui se vogliono le info
	 * @return le info riguardanti l'utente richiesto sotto forma di BeanGiocatore.
	 * @throws RemoteException
	 * @throws GiocatoreNonTrovatoException 
	 */
	public BeanGiocatore ottieniInfoUtente (String nomeUtente) throws RemoteException, GiocatoreNonTrovatoException;
	
	
	/**
	 * Metodo per richiedere la lista dei Tavoli Aperti
	 * @return - la lista con le info dei tavoli aperti (id e numero giocatori)
	 * @throws RemoteException
	 */
	public ArrayList<BeanTavolo> richiediListaTavoliAperti() throws RemoteException;
	
	
	/**
	 * Comando di creare un nuovo tavolo (a cui il creatore si aggiungerà)
	 * @return - l'id del tavolo creato
	 * @throws RemoteException
	 */
	public Integer creaTavolo() throws RemoteException;
	
	
	/**
	 * Comando del client di avviare il tavolo a cui ha preso parte.
	 * @param idTavolo - l'id del tavolo che il client ha richiesto di avviare 
	 * @return true - se il tavolo verrà avviato; false - altrimenti
	 * @throws RemoteException
	 * @throws ProblemaAvvioTavoloException 
	 */
	public boolean avviaTavolo(Integer idTavolo) throws RemoteException, ProblemaAvvioTavoloException;
	
	
	/**
	 * Metodo per aggiungersi a un tavolo. 
	 * Se l'aggiunta va a buon fine cambia lo stato attuale in "InTavolo."
	 * @param idtavolo					L'Id del tavolo a cui il giocatore si vuole aggiungere
	 * @return la lista dei giocatori connessi al tavolo se l'aggiunta è andata a buon fine; null altrimenti
	 * @throws RemoteException
	 * @throws TavoloInesistenteException 
	 */
	public ArrayList<String> joinTavolo(Integer idTavolo) throws RemoteException, TavoloInesistenteException;
	
	
	/**
	 *Comando di abbandonare un tavolo. Fa abbandonare il tavolo al client.
	 *@throws RemoteException
	 */
	public void abbandonaTavolo() throws RemoteException;
	
	/**
	 * Chiamato dal client per comunicare alla partita la sua scelta del colore
	 * @param colore			- il colore scelto
	 * @throws RemoteException
	 */
	public void sceltaColoreEffettuata(Colori colore) throws RemoteException;
	
	/**
	 * Chiamato dal client per comunicare il suo comando di posizionamento delle armate
	 * @param comandoPosizionamento - comando di posizionamento delle armate effettuato dal client sia a inizio partita che a inizio turno
	 * @throws RemoteException
	 */
	public void sceltaArmateInizialeEffettuata(String comandoPosizionamento) throws RemoteException; 
	
	
	/**
	 * Chiamato dal client per passare il turno di una partita in corso
	 * @throws RemoteException
	 */
	public void passaTurno() throws RemoteException;

	/**
	 * Comando del client di effettuare uno spostamento di armate
	 * @param numeroArmateDaSpostare			il numero di armate che il giocatore intende spostare
	 * @param origine							territorio di origine
	 * @param destinazione						territorio di destinazione
	 * @throws RemoteException
	 * @throws TerritorioNonTrovatoException	se uno dei due territori coinvolti non è stato trovato
	 */
	public void comandoSpostaArmate(Integer numeroArmateDaSpostare, String origine, String destinazione) throws RemoteException, TerritorioNonTrovatoException;
	
	/**
	 * Comando del client di effettuare un attacco
	 * @param numeroUnitaAttaccanti			numero delle armate con cui il giocatore intende attaccare
	 * @param tAttaccato					il territorio che si vuole attaccare
	 * @param tAttaccante					il territorio da cui parte l'attacco
	 * @throws RemoteException				problema di connessione
	 * @throws UnitaInsufficientiException	se le unità per l'attacco sono insufficienti
	 */
	public void comandoAttacco(Integer numeroUnitaAttaccanti, String tAttaccato, String tAttaccante) throws RemoteException, UnitaInsufficientiException;
	
	/**
	 * Chiamato dal Client per abbandonare una partita in corso.
	 * @throws RemoteException
	 */
	public void abbandonaPartita() throws RemoteException;
	
	
}
