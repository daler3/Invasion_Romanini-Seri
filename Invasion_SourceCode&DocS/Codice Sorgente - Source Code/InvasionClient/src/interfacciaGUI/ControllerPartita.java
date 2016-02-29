package interfacciaGUI;
import classiCondivise.BeanGiocatore;
import classiCondivise.Colori;

import java.util.*;

import exceptionCondivise.TerritorioNonTrovatoException;
import exceptionCondivise.UnitaInsufficientiException;
import mappa.Mappa;

public interface ControllerPartita extends ControllerAccountStatistiche {

	/**
	 * Il colore scelto
	 * @param colore	Il colore scelto.
	 */
	public void sceltaColoreEffettuata(Colori colore);
	
	/**
	 * Quando creiamo il controller partita dobbiamo impostare anche la view.
	 * @param viewPartita	Il riferimento al pannello appena creato.
	 */
	public void setVistaPartita(ViewPartita viewPartita);
	
	/**
	 * Richiedo il mio colore
	 * @return	Il mio colore.
	 */
	public Colori getMioColore();
	
	/**
	 * Richiediamo informazioni sul giocatore.
	 * @param nome	Nome del giocatore.
	 * @return		Informazioni del giocatore.
	 */
	public BeanGiocatore getInfoGiocatore(String nome);
	
	
	/**
	 * Invio il comando di posizionamento.<br>Esempio:<b>nomeUtente@nomeTerr:numArmate;nomeTerr:numArmate;[...]</b>
	 * @param comandoPosizionamento
	 */
	public void sceltaArmateInizialeEffettuata(String comandoPosizionamento);
	
	/**
	 * Passo il turno.
	 */
	public void passaTurno();
	
	/**
	 * Chiedo di spostare le armate.
	 * @param numeroArmateDaSpostare	Numero di armate da spostare.
	 * @param origine					Nome del territorio di partenza.
	 * @param destinazione				Nome del territorio di arrivo.
	 */
	public void comandoSpostaArmate(Integer numeroArmateDaSpostare, String origine, String destinazione) throws TerritorioNonTrovatoException;
	
	//io attacco
	/**
	 * Richiedo di attaccare.
	 * @param numeroUnitaAttaccanti	Le unità con cui voglio attaccare.
	 * @param tAttaccato			Il nome del territorio che voglio attaccare.
	 * @param tAttaccante			Il nome del territorio che si deve difendere
	 * @throws UnitaInsufficientiException

	 */
	public void comandoAttacco(Integer numeroUnitaAttaccanti, String tAttaccato, String tAttaccante) throws UnitaInsufficientiException;
	
	/**
	 * Richiedo il risultato dei dadi del difensore.
	 * @return	I dadi del difensore.
	 */
	public ArrayList<Integer> getDadiDifesa();
	
	/**
	 * Ottengo le unità distrutte dopo un attacco.
	 * @return	Numero di unità perse.
	 */
	public Integer getUnitaDifesaSconfitte();
	
	/**
	 * Ottengo le unità perse dopo un attacco.
	 * @return	Numero di unità distrutte.
	 */
	public Integer getUnitaAttaccoPerse();
	
	/**
	 * Per chiedere se il territorio attaccato è stato conquistato.
	 * @return
	 */
	public boolean isTerritorioAttaccatoConquistato();
	
	/**
	 * Mi comunica la mappa post attacco. Da richiedere dopo la difesa di un utente.
	 * @return	La mappa aggiornata.
	 */
	public Mappa fineDifesa();
	
	/**
	 * Richiedo la mappa in caso di refresh manuale.
	 * @return La mappa aggiornata.
	 */
	public Mappa getMappa();
	
	/**
	 * Permette di abbandonare la partita.
	 * @return	Restituisce il controller Limbo per richiedere i tavoli disponibili.
	 */
	public ControllerLimbo abbandonaPartita();
	

}
