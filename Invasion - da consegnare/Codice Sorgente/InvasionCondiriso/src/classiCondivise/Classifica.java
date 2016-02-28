package classiCondivise;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Classe rappresentante la partita.
 * @author daniele
 *
 */
public class Classifica implements Serializable {


	private static final long serialVersionUID = 1L;
	private ArrayList<String> ordineRisultato;
	private Hashtable<String, Integer> posizione;
	private Hashtable<String, Integer> punteggio;
	
	public Classifica(Integer numeroGiocatori) {
		this.ordineRisultato = new ArrayList<String>(numeroGiocatori);
		this.posizione = new Hashtable<String, Integer>(numeroGiocatori);
		this.punteggio = new Hashtable<String, Integer>(numeroGiocatori);
	}
	
	/**
	 * Si aggiunge in coda alla classifica un giocatore
	 * @param username	Nome del giocatore
	 * @param punteggio	Punteggio del giocatore
	 */
	public void aggiungiGiocatore(String username, Integer punteggio){
		this.ordineRisultato.add(username);	//Aggiungiamo il giocatore alla fine
		int posizione = this.ordineRisultato.size();
		this.posizione.put(username, posizione);
		this.punteggio.put(username, punteggio);
	}
	
	public void aggiungiGiocatore(String username, Integer punteggio, Integer posizione){
		this.ordineRisultato.add(username);	//Aggiungiamo il giocatore alla fine
		this.posizione.put(username, posizione);
		this.punteggio.put(username, punteggio);
	}
	
	public String getUsername(Integer posizione){
		String username = this.ordineRisultato.get(posizione-1);
		return username;
	}
	
	public Integer getPunteggio(String nome){
		Integer punteggio = this.punteggio.get(nome);
		return punteggio;
	}
	
	public Integer getPunteggio(Integer posizione){
		String username = this.getUsername(posizione);
		return getPunteggio(username);
	}
	
	public Integer getPosizione(String nome){
		Integer posizione = this.posizione.get(nome);
		return posizione;
	}

	public Integer getGiocatoriInClassifica(){
		return ordineRisultato.size();
	}

	@Override
	public String toString() {
		Iterator<String> itClassifica = this.ordineRisultato.iterator();
		//Inizializzo la stringa
		String miaStringa = "";
		//Se ci sono delle statistiche di un giocatore le scarico ad una ad una
		if(itClassifica.hasNext()){
			String usernameOriginale = itClassifica.next();
			//Username pulito
			String usernamePulito = CreareMessaggio.preparaInserimetoParametro(usernameOriginale);
			miaStringa = usernameOriginale+";"+getPosizione(usernamePulito)+";"+getPunteggio(usernamePulito);
			while(itClassifica.hasNext()){
				usernameOriginale = itClassifica.next();
				usernamePulito = CreareMessaggio.preparaInserimetoParametro(usernameOriginale);
				miaStringa = miaStringa+";"+usernamePulito+";"+getPosizione(usernamePulito)+";"+getPunteggio(usernamePulito);
			}
		}
		return miaStringa;
	}

	
}
