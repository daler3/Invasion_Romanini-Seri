package mappa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import socketcondiviso.AlfabetoSocket;
import classiCondivise.Colori;
import exceptionCondivise.TerritoriIncompatibiliException;
import exceptionCondivise.UnitaInsufficientiException;



public class Territorio implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nome;
	private String continente;
	private Integer unitaPresenti = 0;
	private Colori colorePossessore = Colori.NEUTRO;
	private ArrayList<Territorio> territoriConfinanti;
	
	/**
	 * Costruttore di un Territorio.
	 * @param nome	Nome del territorio.
	 * @param continente	Nome del continente di appartenenza.
	 */
	public Territorio(String nome, String continente){
		territoriConfinanti = new ArrayList<Territorio>();
		this.nome = nome;
		this.continente = continente;
	}
	
	/**
	 * Aggiunge un nuovo territorio alla lista dei territori confinanti.
	 * Utilizzato solo in fase di creazione del territorio per impostare i confini.
	 * @param territorioConfinante Il territorio confinante
	 */
	public void aggiungiConfine(Territorio territorioConfinante){
		territoriConfinanti.add(territorioConfinante);
	}
	
	public void aggiungiListaConfini(ArrayList<Territorio> confini){
		this.territoriConfinanti = confini;
	}
	
	/**
	 * Ci dice se la stringa passata è il suo nome
	 * @param nomeTerritorio
	 * @return true se è il suo nome, false altrimenti
	 */
	public boolean siChiama(String nomeTerritorio){
		return nome.equals(nomeTerritorio);		
	}
	
	/**
	 * 
	 * @return - lista dei territori confinanti
	 */
	public ArrayList<Territorio> getTerritoriConfinanti (){
		return territoriConfinanti; 
	}
	
	/**
	 * Aggiunge unita' al territorio
	 * @param numeroUnitaDaAggiungere
	 */
	public void aggiungiUnita (Integer numeroUnitaDaAggiungere){
		this.unitaPresenti = this.unitaPresenti + numeroUnitaDaAggiungere; 
	}

	/**
	 * Rimuove unita' al territorio
	 * @param numeroUnitaDaRimuovere
	 * @throws Exception Rimozione di un numero troppo elevato di armate
	 */
	public void rimuoviUnita (Integer numeroUnitaDaRimuovere) throws UnitaInsufficientiException{
		if(this.unitaPresenti < numeroUnitaDaRimuovere){
			throw new UnitaInsufficientiException();
		}
		this.unitaPresenti = this.unitaPresenti - numeroUnitaDaRimuovere; 
	}
	
	/**
	 * Metodo per stabilire se posso o meno spostare le mie unita' su un altro territorio
	 * @param territorio di destinazione
	 * @return -true se posso spostare le mie unita nel territorio passato come parametro -false se non posso
	 */
	public boolean possoSpostare (Territorio territorioDestinazione){
		if(isEnemy(territorioDestinazione)){
			return false;
		}
		ArrayList<Territorio> territoriVisitati = new ArrayList<Territorio>();
		if(!territorioRaggiungibile(territorioDestinazione, territoriVisitati)){
			return false;
		}
		return true; 
	}
	
	/**
	 * Metodo per stabilire se un territorio è raggiungibile dal corrente - Visita DAG
	 * @param territorioDestinazione: territorio di destinazione
	 * @return -true se il territorio di destinazione è raggiungibile dal corrente; -false se il territorio di destinazione non è raggiungibile dal corrente
	 */
	public boolean territorioRaggiungibile(Territorio territorioDestinazione, ArrayList<Territorio> territoriVisitati){
		if(this.territoriConfinanti.contains(territorioDestinazione)){
			return true; 
		}
		territoriVisitati.add(this);
		Iterator<Territorio> confinantiCorrente = this.territoriConfinanti.iterator();
		Territorio territorioConfinante;
		while(confinantiCorrente.hasNext()){
			territorioConfinante = confinantiCorrente.next();
			//Non posso passare da qui
			if(isEnemy(territorioConfinante)){
				continue;
			}
			else {
				//Siamo già passati da qui
				if(territoriVisitati.contains(territorioConfinante))
					continue;
				//È un nuovo territorio
				else{
					if(territorioConfinante.territorioRaggiungibile(territorioDestinazione, territoriVisitati))
						return true;
				}
			}
		}
		return false; //non è raggiungibile
	}
	
	/**
	 * Ci dice se il territorio passato come parametro può essere attaccato da questo.
	 * @param territorioBersaglio	Il territorio che vogliamo attaccare.
	 * @param numeroUnitaAttaccanti	Il numero di unità con cui vogliamo attaccare
	 * @return	true -> il territorio può essere attaccato;	false -> il territorio NON può essere attacato.
	 */
	public boolean possoAttaccareDaQui (Territorio territorioBersaglio, Integer numeroUnitaAttaccanti){
		Colori coloreAttacante = this.getColorePossessore();
		Colori coloreDifensore = territorioBersaglio.getColorePossessore();
		//Non ti puoi attaccare da solo
		if(coloreAttacante == coloreDifensore){
			return false;
		}
		//Non confina
		if(! confina(territorioBersaglio)){
			return false;
		}
		//Il territorio si può solo difendere
		if(this.unitaPresenti <= 1){
			return false;
		}
		//Devi avere almeno un'unità che possa restare in difesa
		if(this.unitaPresenti <= numeroUnitaAttaccanti){
			return false;
		}
		else 
			return true; 
	}
	
	/**
	 * Metodo per controllare se un altro territorio è nemico, ovvero posseduto da un altro giocatore
	 * @param altroTerritorio Territorio con cui fare il confronto
	 * @return -true se l'altro territorio passato come parametro è nemico, -false se non è nemico
	 */
	public boolean isEnemy (Territorio altroTerritorio){
		return isEnemy(altroTerritorio.getColorePossessore());
	}
	
	/**
	 * Metodo per controllare se il territorio selezionato è nemico
	 * @param coloreRichiedente	Il colore di chi fa la richiesta
	 * @return	true se il territorio è nemico, false se si possiede il territorio
	 */
	public boolean isEnemy(Colori coloreRichiedente){
		if(coloreRichiedente != this.colorePossessore)
			return true;
		return false;
	}
	
	/**
	 * Metodo per controllare se un'altro territorio confina col corrente
	 * @param altroTerritorio
	 * @return -true se l'altro territorio passato come parametro confina col corrente -false se non confina
	 */
	public boolean confina (Territorio altroTerritorio){
		if(territoriConfinanti.contains(altroTerritorio))
			return true; 
		return false; 
	}
	
	/**
	 * Metodo che sposta le unita dal territorio corrente al territorio di destinazione
	 * @param numeroArmateDaSpostare Numero di armate che verranno spostate.
	 * @param territorioDestinazione Dove andranno a finire le armate che voglio spostare.
	 * @throws UnitaInsufficientiException 
	 * @throws MappaClientNonAggiornataException Mappa client non aggiornata - reinviare
	 */
	public void spostaUnita (Integer numeroArmateDaSpostare, Territorio territorioDestinazione) throws TerritoriIncompatibiliException, UnitaInsufficientiException{
		if(! possoSpostare(territorioDestinazione)){
			throw new TerritoriIncompatibiliException();
		}
		if(this.getUnitaPresenti() < numeroArmateDaSpostare + 1){
			//throw new Exception("Troppe poche unità presenti per lo spostamento");
			numeroArmateDaSpostare = maxUnitaSpostabili();
		}
		//Se arriviamo fin qui effettuiamo lo spostamento
		this.rimuoviUnita(numeroArmateDaSpostare);
		territorioDestinazione.aggiungiUnita(numeroArmateDaSpostare);
	}
	
	
	public Integer maxUnitaSpostabili(){
		return (this.getUnitaPresenti())-1;
	}
	
	
	public Integer maxUnitaDifesa() throws UnitaInsufficientiException{
		if( getUnitaPresenti() >=2 )
			return 2;
		else if(getUnitaPresenti() == 1)
			return 1;
		else throw new UnitaInsufficientiException();
	}
	
	public Integer maxUnitaAttacco() throws UnitaInsufficientiException{
		if( getUnitaPresenti() > 3 )
			return 3;
		if( getUnitaPresenti() ==3 )
			return 2;
		else if(getUnitaPresenti() == 2)
			return 1;
		else throw new UnitaInsufficientiException();
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getContinente() {
		return continente;
	}

	public Integer getUnitaPresenti() {
		return unitaPresenti;
	}

	public void setUnitaPresenti(Integer unitaPresenti) {
		this.unitaPresenti = unitaPresenti;
	}

	public Colori getColorePossessore() {
		return colorePossessore;
	}

	public void setColorePossessore(Colori colorePossessore) {
		this.colorePossessore = colorePossessore;
	}

	@Override
	public String toString() {
		String messaggio = "";
		String nomeInviato = "";
		String continenteInviato = "";
		int unitaInviate = 0;
		String coloreInviato = Colori.NEUTRO.getNome();
		int numeroConfini = 0;
		if(this.nome != null){
			nomeInviato = this.nome;
		}
		messaggio = messaggio+nomeInviato+AlfabetoSocket.dotcomma;
		if(this.continente != null){
			continenteInviato = this.continente;
		}
		messaggio = messaggio+continenteInviato+AlfabetoSocket.dotcomma;
		if(this.unitaPresenti != null){
			unitaInviate = this.unitaPresenti;
		}
		messaggio = messaggio+unitaInviate+AlfabetoSocket.dotcomma;
		if(this.colorePossessore != null){
			coloreInviato = this.colorePossessore.getNome();
		}
		messaggio = messaggio+coloreInviato+AlfabetoSocket.dotcomma;
		if(this.territoriConfinanti != null){
			numeroConfini = this.territoriConfinanti.size();
			messaggio = messaggio+numeroConfini+AlfabetoSocket.dotcomma;
			Iterator<Territorio> itTerritori = territoriConfinanti.iterator();
			Territorio territorioEstratto;
			while(itTerritori.hasNext()){
				//Estraiamo solo il nome e il continente di appartenenza
				territorioEstratto = itTerritori.next();
				messaggio = messaggio+territorioEstratto.getNome()+AlfabetoSocket.dotcomma+territorioEstratto.getContinente()+AlfabetoSocket.dotcomma;
			}
		}
		else{
			messaggio = messaggio+numeroConfini+AlfabetoSocket.dotcomma;
		}
		//Rimuoviamo l'ultimo ; se presente
		if(messaggio.length() > 0 && messaggio.charAt(messaggio.length()-1) == ';'){
			messaggio = messaggio.substring(0, messaggio.length()-1);
		}
		return messaggio;
	}
	
}
