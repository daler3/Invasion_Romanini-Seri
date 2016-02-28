package interfacciaGUI;

import java.util.ArrayList;

import classiCondivise.BeanTavolo;
import exceptionCondivise.TavoloInesistenteException;

/**
 * 
 * @see ControllerAccountEStatistiche
 *
 */
public interface ControllerLimbo extends ControllerAccountStatistiche {

	/**
	 * Fa la richiesta di entrare nel tavolo selezionato
	 * @param idTavolo	Tavolo in cui si vuole entrare
	 * @return	Esito richiesta di accesso al tavolo
	 * @throws TavoloInesistenteException 
	 */
	public ControllerTavolo entraNelTavolo(Integer idTavolo) throws TavoloInesistenteException;
	
	/**
	 * Richiesta di creare un nuovo tavolo
	 * @return il controller del tavolo creato
	 * @see ControllerTavolo
	 */
	public ControllerTavolo creaNuovoTavolo();

	/**
	 * Richiesta di aggiornare l'elenco dei tavoli aperti
	 * @return La lista con le informazioni essenziali dei tavolo aperti
	 * @see Beantavolo
	 */
	public ArrayList<BeanTavolo> aggiornaTavoli();
	
	/**
	 * Setter per il riferimento alla GUI rappresentante il limbo con visibilità di interfaccia
	 * @param vistaLimbo - riferimento alla GUI del limbo con visibilità di interfaccia
	 * @see ViewLimbo
	 */
	public void setVistaLimbo(ViewLimbo vistaLimbo);
	
	
}
