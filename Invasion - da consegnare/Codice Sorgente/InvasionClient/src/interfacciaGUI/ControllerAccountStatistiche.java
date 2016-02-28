package interfacciaGUI;

import classiCondivise.BeanGiocatore;
import classiCondivise.Classifica;
import exceptionCondivise.GiocatoreNonTrovatoException;

public interface ControllerAccountStatistiche {
	
	/**
	 * Richiesta di cambio password
	 * @param vecchiaPassword
	 * @param nuovaPassword
	 * @return
	 */
	public boolean cambiaPassword(String vecchiaPassword, String nuovaPassword);
	
	/**
	 * Richiesta di visuaizzazione della classifica
	 * @return - la classifica di gioco
	 */
	public Classifica visualizzaClassifica(); 
	
	/**
	 * Richiesta di visualizzazione delle info di un giocatore
	 * @param nomeGiocatore - il nome del giocatore di cui si vogliono richiedere le info
	 * @return	- le info del giocatore
	 * @throws GiocatoreNonTrovatoException - se il giocatore di cui si sono richieste le info non è stato trovato
	 */
	public BeanGiocatore visualizzaInfoGiocatore(String nomeGiocatore) throws GiocatoreNonTrovatoException;
	
	/**
	 * Metodo per visualizzare le info personali
	 * @return - le info personali
	 */
	public BeanGiocatore visualizzaMieInfo();
	
	/**
	 * Richiesta di logout da parte del giocatore
	 * @return estito del logout
	 */
	public boolean logout();
	
}
