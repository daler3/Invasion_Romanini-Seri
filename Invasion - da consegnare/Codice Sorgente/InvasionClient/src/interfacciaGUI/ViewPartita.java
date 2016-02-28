package interfacciaGUI;

import java.util.ArrayList;

import mappa.Mappa;
import mappa.Territorio;
import classiCondivise.BeanGiocatore;
import classiCondivise.Colori;

public interface ViewPartita {
	
	/**
	 * Mi viene comunicato che devo scegliere il colore.
	 * @param timer				Il tempo a disposizione per la scelta.
	 * @param coloriDisponibili	I colori ancora disponibili.
	 */
	public void scegliColore (Integer timer, ArrayList<Colori> coloriDisponibili);
	
	/**
	 * Ricevo l'ordine di gioco.
	 * @param listaInfoGiocatori	Ricevo le info dei giocatori.
	 * @param ordineColori			Ricevo l'ordine dei colori di gioco.
	 */
	public void riceviOrdinePartita(ArrayList<BeanGiocatore>listaInfoGiocatori, ArrayList<Colori>ordineColori);
	
	/**
	 * La partita è iniziata devo posizionare le armate.
	 * @param numeroArmateDaPosizionare	Il numero di armate da posizionare
	 * @param tempoPosizionaArmate		Il tempo a mia disposizione per posizionare le armate.
	 * @param mappaPartita				La mappa con tutte le informazioni.
	 */
	public void riceviTerritoriPosizionaArmate(Integer numeroArmateDaPosizionare, Integer tempoPosizionaArmate, Mappa mappaPartita);
	
	/**
	 * Mi viene comunicato la mappa aggiornata.
	 * @param mappa	La mappa aggiornata.
	 */
	public void riceviMappa(Mappa mappa);
	
	/**
	 * Non è il mio turno, qualcuno ha passato.
	 * @param colore	Il colore del giocatore che sta giocando.
	 * @param username	Il nome del giocatore che sta giocando.
	 */
	public void riceviComunicazioneNuovoTurno(Colori colore, String username);

	/**
	 * È il mio turno, devo posizionare le armate.
	 * @param numeroArmate	Le armate da posizionare.
	 * @param tempo			Il tempo a disposizione per posizionare le armate.
	 */
	public void riceviArmateInizioTurno(Integer numeroArmate, Integer tempo);

	/**
	 * Il serve ci comunica che attende una nostra mossa.
	 * @param tempo	Il tempo che il server attenderà.
	 */
	public void comunicazioneInAttesaMossa(Integer tempo);
	
	/**
	 * Sto attaccando qualcuno.
	 * @param territorioAttaccante	Da dove attacco.
	 * @param territorioAttaccato	Il territorio che subisce l'attacco.
	 * @param risultatoDadiAttacco	Il mio lancio di dadi.
	 */
	public void riceviConfermaMioAttacco(Territorio territorioAttaccante, Territorio territorioAttaccato,
										ArrayList<Integer> risultatoDadiAttacco);
	
	
	/**
	 * Qualcuno mi ha attaccato.
	 * @param territorioAttaccante	Il territorio che mi sta attaccando.
	 * @param territorioAttaccato	Il mio territorio sotto attacco.
	 * @param risultatoDadiAttacco	Quanto ha fatto chi ha attaccato.
	 * @param tempoDifesa			Il tempo a mia disposizione per difendermi.
	 */
	public void riceviAttaccoSubito(Territorio territorioAttaccante, Territorio territorioAttaccato,
									ArrayList<Integer> risultatoDadiAttacco, Integer tempoDifesa);
	
	
	/**
	 * Qualcuno ha attaccato qualcun'altro.
	 * @param coloreAttaccante	Chi ha attaccato.
	 * @param territorioAttaccante	Territorio che attacca.
	 * @param territorioAttaccato	Territorio che si difende.
	 * @param risultatoDadiAttacco	Il risultato dei dadi dell'attacco.
	 */
	public void riceviComunicazioneAttacco(Colori coloreAttaccante, Territorio territorioAttaccante, Territorio territorioAttaccato,
											ArrayList<Integer> risultatoDadiAttacco);	
	
	/**
	 * Un giocatore ha perso la partita
	 * @param giocatoreSconfitto	Il giocatore che ha perso.
	 */
	public void riceviComunicazioneSconfitta(String giocatoreSconfitto);
	
	/**
	 * Hai perso la partita, torna nel limbo.
	 * @param limboSconfitta
	 */
	public void riceviComunicazioneSconfitta(ControllerLimbo limboSconfitta);
	
	/**
	 * Un giocatore ha abbandonato la partita.
	 * @param giocatoreRitirato	Il giocatore che ha abbandonato la partita.
	 */
	public void riceviComunicazioneRitirata(String giocatoreRitirato);
	
	/**
	 * Comunica la classifica finale.
	 * @param classificaFinale	La classifica finale.
	 * @param punteggioPrimo	Vincita del primo.
	 * @param punteggioSecondo	Vincita del secondo.
	 */
	public void riceviClassificaFinale(ArrayList<String> classificaFinale, Integer punteggioPrimo, Integer punteggioSecondo);
	
	public void chiusuraServer(ControllerUtente controller);
	
}
