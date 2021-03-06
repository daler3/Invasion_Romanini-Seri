package mappa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import socketcondiviso.AlfabetoSocket;
import exceptionCondivise.TerritorioNonTrovatoException;
/**
 * Rappresenta la mappa di gioco
 * 
 *
 */
public class Mappa implements Serializable {
	

	private static final long serialVersionUID = 1L;
	private ArrayList<Continente> listaContinenti = new ArrayList<Continente>();
	private ArrayList<Territorio> listaTerritori = new ArrayList<Territorio>();

	
	public Mappa(String[] continenti, Integer[] bonus, String[][] territori, String[][] confina){
		this.listaContinenti = new ArrayList<Continente>(continenti.length);
		this.listaTerritori = new ArrayList<Territorio>(territori.length);
		//Ciclo che crea tutti i continenti e i territori
		for(int i = 0; i<continenti.length; i++){
			listaContinenti.add(new Continente(continenti[i], bonus[i]));
			for(int j = 0; j<territori[i].length; j++){
				//Creiamo lo stato dandogli nome e password
				if(territori[i][j] == null)
					break;
				else{
					Territorio territorioTemporaneo = new Territorio(territori[i][j], continenti[i]);
					listaTerritori.add(territorioTemporaneo);
				}
			}
		}
		//Ciclo che definisce tutti i confini
		Territorio territorioTemporaneo = null;
		for(int i = 0; i<continenti.length; i++){	//Sfogliamo tutti i continenti
			for(int j = 0; j<territori[i].length; j++){	//Sfogliamo tutti gli stati
				for(int k=0; k<confina[i].length; k++){
					try {
						territorioTemporaneo = trovaTerritorio(territori[i][j]);
					} catch (TerritorioNonTrovatoException e) {	}
					listaContinenti.get(i).getTerritori().get(j).getTerritoriConfinanti().add(territorioTemporaneo);
				}
			}
		}	
	}
	
	/**
	 * Costruttore della mappa
	 * @param continenti 	- lista dei continenti
	 * @param territori		- lista dei territorio
	 */
	public Mappa(ArrayList<Continente> continenti, ArrayList<Territorio> territori){
		this.listaContinenti = continenti;
		this.listaTerritori = territori;
	}
	
	/**
	 * Cerca il territorio con il nome passato come parametro
	 * @param nome Il nome del territorio ricercato.
	 * @return il territorio che è stato trovato - null se non viene trovato 
	 * @throws TerritorioNonTrovatoException 
	 */
	public Territorio trovaTerritorio(String nome) throws TerritorioNonTrovatoException{
		Territorio territorioTrovato = null;
		Iterator<Continente> itContinente = this.listaContinenti.iterator();
		while(itContinente.hasNext()){
			territorioTrovato = itContinente.next().trovaTerritorio(nome);
			if(territorioTrovato != null)	//Se non è null il risultato della ricerca ci torna il valore trovato
				return territorioTrovato;
		}
	throw new TerritorioNonTrovatoException();
	}

	
	/**
	 * La lista dei territori presenti nella mappa
	 * @return I territori presenti nella mappa. (Ogni modifica è permanente)
	 */
	public ArrayList<Territorio> getListaTerritori() {
		return this.listaTerritori;
	}
	
	/**
	 * Ci da una copia dei continenti (I continenti, una volta inizializzati sono immutabili)
	 * @return La copia dei continenti presenti in mappa (Ogni modifica è solo locale)
	 */
	public ArrayList<Continente> getListaContinenti() {
		return this.listaContinenti;
	}

	@Override
	public String toString() {
		int numeroContinenti = 0;
		int numeroTerritori = 0;
		String messaggio = "";
		//Inseriamo la lista dei continenti
		if(this.listaContinenti != null){
			numeroContinenti = listaContinenti.size();
			messaggio = messaggio+numeroContinenti+AlfabetoSocket.dotcomma;
			Iterator<Continente> itContinente = listaContinenti.iterator();
			while(itContinente.hasNext()){
				//Finisce già con il punto e virgola
				messaggio = messaggio+itContinente.next().toString()+AlfabetoSocket.dotcomma;
			}
		}
		else{
			messaggio = messaggio+numeroContinenti+AlfabetoSocket.dotcomma;
		}
		//Inseriamo la lista dei territori
		if(this.listaTerritori != null){
			numeroTerritori = listaTerritori.size();
			messaggio = messaggio+numeroTerritori+AlfabetoSocket.dotcomma;
			Iterator<Territorio> itTeritorio = listaTerritori.iterator();
			while(itTeritorio.hasNext()){
				//Finisce già con il punto e virgola
				messaggio = messaggio+itTeritorio.next().toString()+AlfabetoSocket.dotcomma;
			}
		}
		else{
			messaggio = messaggio+numeroTerritori+AlfabetoSocket.dotcomma;
		}
		//Rimuoviamo l'ultimo ; se presente
		if(messaggio.length() > 0 && messaggio.charAt(messaggio.length()-1) == ';'){
			messaggio = messaggio.substring(0, messaggio.length()-1);
		}
		return messaggio;
	}
	
	
	
	
	
}
