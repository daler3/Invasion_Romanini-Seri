package mappa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import socketcondiviso.AlfabetoSocket;
import classiCondivise.Colori;
import exceptionCondivise.TerritorioNonTrovatoException;

/**
 * 
 *Classe che rappresenta un continente
 *
 */
public class Continente implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nome;
	private Integer bonus; 
	/**
	 * Lista dei territori contenuti nel continente
	 */
	private ArrayList<Territorio> territori = null;
	
	public Continente(String nome, Integer bonus){
		territori = new ArrayList<Territorio>();
		this.nome = nome;
		this.bonus = bonus; 
	}
	
	/**
	 * Aggiunge un territorio al continente
	 * @param mioTerritorio - il territorio da aggiungere
	 */
	public void aggiungiTerritorio(Territorio mioTerritorio){
		territori.add(mioTerritorio);
	}
	
	/**
	 * Aggiunge una lista di territori al continente
	 * @param territori - la lista dei territori da aggiungere
	 */
	public void aggiungiListaTerritori(ArrayList<Territorio> territori){
		this.territori = territori;
	}
	
	/**
	 * Ricerca il territorio desiderato e lo ritorna se lo trova
	 * @param nome
	 * @return Il territorio se viene trovato, null in caso contrario.
	 * @throws TerritorioNonTrovatoException Territorio non trovato
	 */
	public Territorio trovaTerritorio(String nome) {
		Iterator<Territorio> itTerritori = this.territori.iterator();
		while(itTerritori.hasNext()){
			Territorio territorioTemp = itTerritori.next();
			if(territorioTemp.siChiama(nome)){
				return territorioTemp;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * Metodo che controlla se i territori del continente sono interamente posseduti da un giocatore, quindi sono dello stesso colore
	 * @return -true se i territori del continente sono dello stesso colore, cioè appartengono allo stesso giocatore, e quindi il bonus va ritirato 
	 * 		   -false se i territori del continente non sono tutti dello stesso colore
	 */
	public boolean hoBonusContinente(Colori colore){
		Iterator <Territorio> itTerritori = this.territori.iterator();
		while(itTerritori.hasNext()){
			Territorio territorioTemp = itTerritori.next();
			if(territorioTemp.isEnemy(colore))
				return false;
		}
		return true;
	}

	/**
	 * Metodo che ritorna il colore di un continente, se il continente è posseduto per intero da qualcuno.
	 * @return - il colore del possessore del continente (se qualche giocatore possiede il continente per intero; 
	 * 		   - null, se il continente non è posseduto da nessuno
	 */
	public Colori coloreBonusContinente(){
		//iteratore dei territori facenti parte del continente
		Iterator <Territorio> itTerritori = this.territori.iterator();
		Colori coloreTemp1, coloreTemp2; 
		coloreTemp1=itTerritori.next().getColorePossessore();
		//itero su tutti i territori
		while(itTerritori.hasNext()){
			coloreTemp2=itTerritori.next().getColorePossessore();
			//se solo due territori hanno colore diverso, il continenti non ha un unico colore, quindi il metodo può terminare
			if(coloreTemp1!=coloreTemp2)
				return null;
			coloreTemp1=coloreTemp2; 
		}
		return coloreTemp1;
	}
	
	
	/**
	 * 
	 * @return - il nome del continente
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Setta il nome del continente
	 * @param nome - il nome del continente
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * 
	 * @return - il bonus corrispondente al continente
	 */
	public Integer getBonus() {
		return bonus;
	}

	/**
	 * Setta il bonus del continente
	 * @param bonus - il bonus del continente
	 */
	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	/**
	 * 
	 * @return - la lista dei territori del continente
	 */
	public ArrayList<Territorio> getTerritori() {
		return territori;
	}

	/**
	 * Setta la lista dei territori del continente
	 * @param territori - la lista dei territori del continente
	 */
	public void setTerritori(ArrayList<Territorio> territori) {
		this.territori = territori;
	}

	@Override
	public String toString() {
		String messaggio = "";
		String nomeInviato = "";
		int bonusInviato = 0;
		int numTerritori = 0;
		//Preparo i parametri da inviare
		//nome
		if(this.nome != null){
			nomeInviato = this.nome;
		}
		messaggio = messaggio+nomeInviato+AlfabetoSocket.dotcomma;
		//bonus
		if(this.bonus != null){
			bonusInviato = this.bonus;
		}
		messaggio = messaggio+bonusInviato+AlfabetoSocket.dotcomma;
		//territori
		if(this.territori != null){
			numTerritori = territori.size();
			messaggio = messaggio+numTerritori+AlfabetoSocket.dotcomma;
			Iterator<Territorio> itTerritorio = territori.iterator();
			while(itTerritorio.hasNext()){
				//Finisce già con il punto e virgola
				messaggio = messaggio+itTerritorio.next().toString()+AlfabetoSocket.dotcomma;
			}
		}
		else{
			messaggio = messaggio+numTerritori+AlfabetoSocket.dotcomma;
		}
		//Rimuoviamo l'ultimo ; se presente
		if(messaggio.length() > 0 && messaggio.charAt(messaggio.length()-1) == ';'){
			messaggio = messaggio.substring(0, messaggio.length()-1);
		}
		return messaggio;
	}
	
}
