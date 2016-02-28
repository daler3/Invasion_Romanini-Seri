package interfacciaGUI;
/**
 * Tutti i metodi che modificano l'interfaccia grafica
 * 
 *
 */
public interface UpdateView {

	
	/**
	 * Disabilita tutti i pulsanti per la gestione partita in quanto non è più il nostro turno.
	 */
	public void disabilitaPulsantiPartita();
	
	/**
	 * Abilita tutti i pulsanti che permettono di posizionare i rinforzi.
	 * -Pulsanti selezione stati;
	 * -TextBox numero armate
	 * -Pulsante posiziona armate
	 * -Pulsante posizionaRandom
	 * -Pulsante noRinforzi
	 */
	public void abilitaPulsantiRinforzi();
	
	/**
	 * Abilita i pulsanti per effettuare l'attacco:
	 * -Pulsanti selezione stati
	 * -Pulsante attacca
	 * -Pulsante passaTurno
	 */
	public void abilitaPulsantiAttacco();
	
	/**
	 * Mostra i pulsanti per la selezione del numero delle armate attaccanti:
	 * -Label in cui si chiede di scegliere il numero di armate attaccanti
	 * -Toggle Button, in numero pari a armateMassime
	 * -Pulsante Annulla
	 * @param armateMassime	Il massimo numero di armate che possono attaccare
	 */
	public void sceltaArmateAttaccenti(Integer armateMassime);
	
	/**
	 * Fa apparire un popup che permette di lanciare i dadi per la difesa
	 */
	public void popupDifesa();
	
	/**
	 * Fa apparire un popup con il numero selezionato di dadi che danno il valore selezionato.
	 * @param numeroDadi	Dadi da far apparire
	 * @param valoreDadi	Risultato da mostrare
	 */
	public void popupDadi(Integer numeroDadi, int[] valoreDadi);
	
	//TODO Separare le interfacce?
	//Pulsanti per il limbo
	
	/**
	 * Cancella il tavolo dalla lista dei tavoli presenti
	 * @param idTavolo	Tavolo da cancellare
	 */
	public void cancellaTavolo(Integer idTavolo);
	
	/**
	 * Aggiunge alla lista dei tavoli il tavolo scelto
	 * @param idTavolo	Tavolo da aggiungere
	 */
	public void aggiugiTavolo(Integer idTavolo);
	
	/**
	 * Abilita tutti i pulsanti per la selezione del tavolo e i tavoli.
	 */
	public void mostraPulsantiTavoli();
	
	/**
	 * Nasconde tutti i pulsanti per la selezione del tavolo e i tavoli.
	 */
	public void nascondiPulsantiTavoli();
	
	
	/**
	 * Mostra il timeout della partita che sta per iniziare
	 * @param numeroDiSecondi	Numero di secondi all'inizio della partita
	 */
	public void mostraTimeoutInizioPartita(Integer numeroDiSecondi);
	
	/**
	 * Aggiorna il timeout della partita che sta per iniziare (Ad esempio si è unito un nuovo giocatore)
	 * @param numeroDiSecondi	Numero di secondi all'inizio della partita
	 */
	public void riavviaTimeout(Integer numeroDiSecondi);
	
	/**
	 * Spegne il timout - Non ci sono abbastanza giocatori per iniziare a giocare
	 */
	public void disabilitaTimeout();
	
}
