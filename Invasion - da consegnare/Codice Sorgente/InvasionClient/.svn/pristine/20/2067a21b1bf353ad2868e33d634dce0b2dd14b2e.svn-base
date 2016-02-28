package interfacciaGUI;

import java.util.ArrayList;

import classiCondivise.BeanGiocatore;
import exceptionCondivise.GiocatoreNonTrovatoException;
import exceptionCondivise.ProblemaAvvioTavoloException;

/**
 * Interfaccia per gestire un tavolo aperto. Usato dalla GUI.
 * @see AccountEStatistiche
 */
public interface ControllerTavolo extends ControllerAccountStatistiche {
	
	/**
	 * Richiesta di abbandonare un tavolo
	 * @return - il controller del limbo
	 * @see ControllerLimbo
	 */
	public ControllerLimbo abbandonaTavolo();

	/**
	 * Richiesta di avviare un tavolo 
	 * @param idTavolo - id del tavolo che si è richiesto di avviare
	 * @return - il Controller Partita se l'avvio del tavolo è andato a buon fine
	 * @throws ProblemaAvvioTavoloException - se c'è stato un problema nell'avvio del tavolo
	 */
	public ControllerPartita avviaTavolo(Integer idTavolo) throws ProblemaAvvioTavoloException;
	
	/**
	 * Getter per l'id del tavolo
	 * @return - l'id del tavolo
	 */
	public Integer getIdTavolo();
	
	/**
	 * Setta il riferimento all'interfaccia grafica con visibilità di interfaccia
	 * @param vistaTavolo - riferimento allla GUI con visibilità di interfaccia
	 * @see ViewTavolo
	 */
	public void setVistaTavolo(ViewTavolo vistaTavolo);
	
	/**
	 * Richiesta dell'elenco dei gocatori presenti in un tavolo
	 * @return - la lista con i nomi dei giocatori presenti nel tavolo
	 */
	public ArrayList<String> getElencoGiocatori();
	
	/**
	 * Richiesta delle info di un utente presente in un tavolo
	 * @param nome - il nome dell'utente di cui si richiedono le info
	 * @return	- le info del giocatore richiesto.
	 * @throws GiocatoreNonTrovatoException - se il giocatore di cui si sono richieste le info non è stato trovatp
	 */
	public BeanGiocatore getInfoUtente(String nome) throws GiocatoreNonTrovatoException;
	
	
}
