package classiCondivise;

import java.util.ArrayList;
import java.util.Iterator;

import mappa.Continente;
import mappa.Mappa;
import mappa.Territorio;
import socketcondiviso.AlfabetoSocket;

public class CreareMessaggio {

	/**
	 * Pulsce il messaggio prima di essere inviato. Qualsiasi informazione prima
	 * di essere messa nella stringa del socket deve essere pulita con questa
	 * funzione.
	 * 
	 * @param nome Parametro da pulire.
	 * @return Il parametro pulito.
	 */
	public static String preparaInserimetoParametro(String nome) {
		String nomeModificato = "";
		for (int i = 0; i < nome.length(); i++) {
			if (nome.charAt(i) == AlfabetoSocket.escape.charAt(0)) {
				nomeModificato = nomeModificato.concat(AlfabetoSocket.escape
						+ AlfabetoSocket.escape);
			} else if (nome.charAt(i) == AlfabetoSocket.dotcomma.charAt(0)) {
				nomeModificato = nomeModificato.concat(AlfabetoSocket.escape
						+ AlfabetoSocket.dotcomma);
			} else {
				String temp = "" + nome.charAt(i);
				nomeModificato = nomeModificato.concat(temp);
			}
		}
		return nomeModificato;
	}

	/**
	 * Rimuove il comando dalla stringa.
	 * 
	 * @param stringaCompleta
	 *            Il messaggio completo.
	 * @return La stringa senza comando.
	 */
	public static String rimuoviComando(String stringaCompleta) {
		Integer posizione = stringaCompleta.indexOf(AlfabetoSocket.at);
		String stringaPulita = stringaCompleta.substring(posizione + 1,
				stringaCompleta.length());
		return stringaPulita;
	}

	/**
	 * Ci da il comando della stringa.
	 * 
	 * @param stringaCompleta
	 *            La stringa completa.
	 * @return Il comando della stringa.
	 */
	public static String estraiComando(String stringaCompleta) {
		Integer posizione = stringaCompleta.indexOf(AlfabetoSocket.at);
		if (posizione == -1) { // È una stringa di solo comando
			posizione = stringaCompleta.length();
		}
		String comando = stringaCompleta.substring(0, posizione);
		return comando;
	}

	/**
	 * Dalla stringa pulita estrae la prima parte del messaggio. La parte
	 * rimossa deve essere decriptata.
	 * 
	 * @param stringaPulita
	 *            La stringa pulita.
	 * @return La prima parte della stringa.
	 */
	private static String estraiPrimaParte(String stringaPulita) {
		Integer posizione = posizioneSeparatore(stringaPulita);
		//Non ci sono separatori => prendo la stringa fino alla fine
		if (posizione == -1) {
			posizione = stringaPulita.length();
		}
		String temp = stringaPulita.substring(0, posizione);
		return temp;
	}

	/**
	 * Dalla stringa pulita estrae la seconda parte del messaggio. La parte
	 * estratta deve essere decriptata.
	 * 
	 * @param stringaPulita
	 *            La stringa pulita.
	 * @return La seconda parte della stringa.
	 */
	public static String estraiSecondaParte(String stringaPulita) {
		Integer posizione = posizioneSeparatore(stringaPulita) + 1;
		if (posizione == 0) {
			posizione = stringaPulita.length();
		}
		String temp = stringaPulita.substring(posizione, stringaPulita.length());
		return temp;
	}

	/**
	 * Ci da la posizione del separatore dotComma.
	 * 
	 * @param stringaPulita La stringa senza comando.
	 * @return La posizione del comando. -1 Se non c'è il separatore.
	 */
	private static Integer posizioneSeparatore(String stringaPulita) {
		String stringaAppoggio = stringaPulita.replaceAll(AlfabetoSocket.escape
				+ AlfabetoSocket.escape, "aa");
		stringaAppoggio = stringaAppoggio.replaceAll(AlfabetoSocket.escape
				+ AlfabetoSocket.dotcomma, "aa");
		int posizione = stringaAppoggio.indexOf(AlfabetoSocket.dotcomma);
		return posizione;
	}

	/**
	 * Ci ritorna il numero di occorrenze del separatore nella stringa.
	 * 
	 * @param stringaPulita  Stringa dove cercare.
	 * @param separatore Il separatore che divide le varie stringhe.
	 * @return Il numero di occorrenze.
	 */
	private static Integer numeroOccorrenze(String stringaPulita,
			String separatore) {
		String stringaAppoggio = stringaPulita.replaceAll(AlfabetoSocket.escape
				+ AlfabetoSocket.escape, "aa");
		stringaAppoggio = stringaAppoggio.replaceAll(AlfabetoSocket.escape
				+ AlfabetoSocket.dotcomma, "aa");
		int lungStringa =  stringaAppoggio.length();
		int numeroAltriCaratteri = stringaAppoggio.replace(separatore, "").length();
		Integer occorrenze = lungStringa - numeroAltriCaratteri;
		return occorrenze;
	}

	/**
	 * Estrae il parametro originale
	 * 
	 * @param parametro Il parametro da estrarre.
	 * @return Il parametro originale.
	 */
	public static String estraiParametro(String parametro) {
		String parametroModificato = parametro;
		parametroModificato = parametroModificato.replace(AlfabetoSocket.escape + AlfabetoSocket.escape, AlfabetoSocket.escape);
		parametroModificato = parametroModificato.replace(AlfabetoSocket.escape + AlfabetoSocket.dotcomma,
				AlfabetoSocket.dotcomma);
		return parametroModificato;
	}

	/**
	 * Rimuove i caratteri di escape aggiunti alla stringaSporca.
	 * @param stringaSporca	La stringa con i caratteri di escape.
	 * @return	Stringa originale.
	 */
	public static String estraiStringaSporca(String stringaSporca){
		String nomeModificato = "";
		int numModifiche = 0;
		//Verifica per vedere se l'ultimo carattere è speciale
		boolean copiareUltimo = true;
		//Il penultimo deve essere diverso da un # e l'ultimo diverso da un # o un ;
		if(stringaSporca.length() >= 2){
			if(stringaSporca.charAt(stringaSporca.length()-2) == AlfabetoSocket.escape.charAt(0)){
				if(stringaSporca.charAt(stringaSporca.length()-1) == AlfabetoSocket.escape.charAt(0) || stringaSporca.charAt(stringaSporca.length()-1) == AlfabetoSocket.dotcomma.charAt(0)){
					//L'ultimo è un carattere speciale
					copiareUltimo = false;
				}
			}
		}
		//Ciclo di pulizia
		for (int i = 0; i < stringaSporca.length()-1-numModifiche; i++) {
			//Caso inserito # -> diventato ##
			if (stringaSporca.charAt(i+numModifiche) == AlfabetoSocket.escape.charAt(0) && stringaSporca.charAt(i+1+numModifiche) == AlfabetoSocket.escape.charAt(0)) {
				nomeModificato = nomeModificato+AlfabetoSocket.escape;
				numModifiche++;
			}
			else if (stringaSporca.charAt(i+numModifiche) == AlfabetoSocket.escape.charAt(0) && stringaSporca.charAt(i+1+numModifiche) == AlfabetoSocket.dotcomma.charAt(0)) {
				nomeModificato = nomeModificato+AlfabetoSocket.dotcomma;
				numModifiche++;
			}
			//Non è stato trovato un carattere speciale, continuiamo
			else{
				nomeModificato = nomeModificato+stringaSporca.charAt(i+numModifiche);
			}
		}
		//Aggiungiamo l'ultimo se non era un carattere speciale
		if(copiareUltimo){
			nomeModificato = nomeModificato+stringaSporca.charAt(stringaSporca.length()-1);
		}
		return nomeModificato;
	}
	
	/**
	 * Estrae un BeanGiocatore dalla stringa inviata.
	 * 
	 * @param stringaPulita
	 *            La stringa inviata senza il comando.
	 * @return Il bean associato alla stringa.
	 */
	public static BeanGiocatore estraiBeanGiocatore(String stringaPulita) {
		String username = estraiStringa(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);

		Integer punteggio = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);

		Integer posizioneClassifica = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);

		Integer numeroPartite = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);

		float percVinte = estraiFloat(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);

		float percSecondo = estraiFloat(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);

		float percAbbandono = estraiFloat(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);

		boolean connesso = estraiBoolean(stringaPulita);

		return new BeanGiocatore(username, punteggio, posizioneClassifica,
				numeroPartite, percVinte, percSecondo, percAbbandono, connesso);
	}

	/**
	 * Estrae un BeanTavolo dalla stringa inviata.
	 * 
	 * @param stringaPulita
	 *            La stringa inviata senza il comando.
	 * @return Il bean associato alla stringa.
	 */
	public static BeanTavolo estraiBeanTavolo(String stringaPulita) {
		Integer idTavolo = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);

		Integer numeroUtenti = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);

		return new BeanTavolo(idTavolo, numeroUtenti);
	}

	/**
	 * Rimuove il primo bean tavolo trovato dalla stringa passata.
	 * @param stringaPulita	La stringa che contiene il bean.
	 * @return	La striga senza il bean.
	 */
	public static String scorriBeanTavolo(String stringaPulita) {
		estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		return stringaPulita;
	}
	
	/**
	 * Rimuove il primo bean giocatore trovato dalla stringa passata.
	 * @param stringaPulita	La stringa che contiene il bean.
	 * @return	La striga senza il bean.
	 */
	public static String scorriBeanGiocatore(String stringaPulita){
		estraiStringa(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiFloat(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiFloat(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiFloat(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiBoolean(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		return stringaPulita;
	}
	
	/**
	 * Rimuove il primo territorio trovato dalla stringa passata.
	 * @param stringaPulita	La stringa che contiene il territorio.
	 * @return	La striga senza il territorio.
	 */
	public static String scorriTerritorio(String stringaPulita){
		estraiStringa(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiStringa(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiColori(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		Integer numeroConfini = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		for(int i = 0; i < numeroConfini; i++){
			estraiStringa(stringaPulita);
			stringaPulita = estraiSecondaParte(stringaPulita);
			estraiStringa(stringaPulita);
			stringaPulita = estraiSecondaParte(stringaPulita);
		}
		return stringaPulita;
	}
	
	/**
	 * Rimuove il primo continente che trova.
	 * @param stringaPulita	La stringa con il continente.
	 * @return	La stringa senza continente.
	 */
	public static String scorriContinente(String stringaPulita){
		estraiStringa(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		Integer numeroTerritori = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		for(int i = 0; i < numeroTerritori; i++){
			estraiTerritorio(stringaPulita);
			stringaPulita = scorriTerritorio(stringaPulita);
		}
		return stringaPulita;
	}
	
	/**
	 * Rimuove la prima mappa che trova.
	 * @param stringaPulita	La stringa con la mappa.
	 * @return	La stringa senza mappa.
	 */
	public static String scorriMappa(String stringaPulita){
		int numeroContinenti = 0;
		int numeroTerritori = 0;
		numeroContinenti = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		for(int i = 0; i < numeroContinenti; i++){
			estraiContinente(stringaPulita);
			stringaPulita = scorriContinente(stringaPulita);
		}
		numeroTerritori = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		for(int i = 0; i < numeroTerritori; i++){
			estraiTerritorio(stringaPulita);
			stringaPulita = scorriTerritorio(stringaPulita);
		}
		return stringaPulita;
	}
	/**
	 * Estrae un ArrayList BeanTavolo dalla stringa inviata.
	 * 
	 * @param stringaPulita
	 *            La stringa inviata senza il comando.
	 * @return L'ArrayList associato alla stringa.
	 */
	public static ArrayList<BeanTavolo> estraiArrayListBeanTavolo(
			String stringaPulita) {
		Integer numeroTavoli = (numeroOccorrenze(stringaPulita, AlfabetoSocket.dotcomma) + 1) / 2; //TODO:
																									// non
																									// va
		ArrayList<BeanTavolo> tavoli = new ArrayList<BeanTavolo>(numeroTavoli);
		BeanTavolo tavoloTemp;
		for (int i = 0; i < numeroTavoli; i++) {
			tavoloTemp = estraiBeanTavolo(stringaPulita);
			stringaPulita = scorriBeanTavolo(stringaPulita);
			tavoli.add(tavoloTemp);
		}
		return tavoli;
	}

	/**
	 * Estrae un ArrayList String dalla stringa inviata.
	 * 
	 * @param stringaPulita La stringa inviata senza il comando.
	 * @return L'ArrayList associato alla stringa.
	 */
	public static ArrayList<String> estraiArrayListStringhe(String stringaPulita) {
		ArrayList<String> listaStringhe = new ArrayList<String>();
		Integer numeroStringhe = (numeroOccorrenze(stringaPulita, AlfabetoSocket.dotcomma) + 1) / 1;

		for (int i = 0; i < numeroStringhe; i++) {
			String primaStringa = CreareMessaggio.estraiStringa(stringaPulita);
			stringaPulita = CreareMessaggio.estraiSecondaParte(stringaPulita);
			listaStringhe.add(primaStringa);
		}
		return listaStringhe;
	}

	/**
	 * Prepara un arraylist all'invio tramite socket.
	 * @param lista	L'arrayList da mettere in stringa.
	 * @return	La stringa contenente l'arraylist.
	 */
	public static String impacchettaArrayListStringhe(ArrayList<String> lista) {
		String stringaTemp = "";
		String stringaDiRitorno = "";
		Iterator<String> itStringhe = lista.iterator();
		if (itStringhe.hasNext()) {
			stringaTemp = preparaInserimetoParametro(itStringhe.next());
			stringaDiRitorno = stringaTemp;
		}
		while (itStringhe.hasNext()) {
			stringaTemp = preparaInserimetoParametro(itStringhe.next());
			stringaDiRitorno = stringaDiRitorno + AlfabetoSocket.dotcomma + stringaTemp;
		}
		return stringaDiRitorno;
	}

	/**
	 * Estrae la classifica dalla stringa inviata
	 * 
	 * @param stringaPulita
	 *            La stringa inviata senza il comando.
	 * @return La classifica.
	 */
	public static Classifica estraiClassifica(String stringaPulita) {
		Integer numeroUtenti = (numeroOccorrenze(stringaPulita, AlfabetoSocket.dotcomma) + 1) / 3;
		Classifica classifica = new Classifica(numeroUtenti);
		for (int i = 0; i < numeroUtenti; i++) {
			String username = estraiStringa(stringaPulita);
			stringaPulita = estraiSecondaParte(stringaPulita);
			//Non la usiamo questa posizione, se non ha mai giocato è null e quindi non è affidabile
			estraiInteger(stringaPulita);
			stringaPulita = estraiSecondaParte(stringaPulita);
			Integer punteggio = estraiInteger(stringaPulita);
			stringaPulita = estraiSecondaParte(stringaPulita);

			classifica.aggiungiGiocatore(username, punteggio, i+1);
		}
		return classifica;
	}

	/**
	 * Estrae una stringa dal messaggio.
	 * 
	 * @param stringaPulita
	 *            La stringa senza il comando.
	 * @return La stringa estratta.
	 */
	public static String estraiStringa(String stringaPulita) {
		String stringa = estraiPrimaParte(stringaPulita);
		//Puliamo la stringa estratta nel caso abbia caratteri speciali
		stringa = CreareMessaggio.estraiStringaSporca(stringa);
		return stringa;
	}

	/**
	 * Estrae un valore Integer dalla stringa letta.
	 * 
	 * @param stringaPulita	 La stringa senza il comando. Se non viene riconosciuta stampa 0.
	 * @return L'Integer letto.
	 */
	public static Integer estraiInteger(String stringaPulita) {
		Integer numero = 0;
		try{
			numero = Integer.parseInt(estraiPrimaParte(stringaPulita));
		} catch(NumberFormatException e){}
		return numero;
	}

	/**
	 * Estrae un float con massimo due cifre dopo la virgola.
	 * @param floatDaPulire	Il float che vogliamo pulire
	 * @param cifreDopoLaVirgola	Il numero di cifre dopo la virgola che vogliamo
	 * @return	Un float con il numero massimo di cifre dopo la virgola indicato.
	 */
	public static float floatPulito(float floatDaPulire, int cifreDopoLaVirgola){
		if(cifreDopoLaVirgola <= 0)
			return 0;
		if(floatDaPulire == 0)
			return 0;
		float appoggio = floatDaPulire*10*cifreDopoLaVirgola;
		double temp = Math.ceil(appoggio);
		return (float) (temp/(10*cifreDopoLaVirgola));
	}
	
	/**
	 * Estrae un valore float dalla stringa letta.
	 * 
	 * @param stringaPulita
	 *            La stringa senza il comando.
	 * @return Il float letto.
	 */
	public static float estraiFloat(String stringaPulita) {
		return Float.parseFloat(estraiPrimaParte(stringaPulita));
	}

	/**
	 * Se viene letto TRUE ritorna true; false altrimenti
	 * 
	 * @param stringaPulita
	 *            La stringa senza comando.
	 * @return Il valore boolean.
	 */
	public static boolean estraiBoolean(String stringaPulita) {
		if (estraiPrimaParte(stringaPulita).toUpperCase().equals("TRUE"))
			return true;
		return false;
	}

	/**
	 * Estrae un colore dalla stringa passata
	 * @param stringaPulita	La stringa da cui estrarre il colore.
	 * @return	Il colore estratto.
	 */
	public static Colori estraiColori(String stringaPulita){
		String nomeColore = estraiPrimaParte(stringaPulita);
		if(Colori.BLU.getNome().toUpperCase().equals(nomeColore.toUpperCase())){
			return Colori.BLU;
		}
		if(Colori.GIALLO.getNome().toUpperCase().equals(nomeColore.toUpperCase())){
			return Colori.GIALLO;
		}
		if(Colori.NERO.getNome().toUpperCase().equals(nomeColore.toUpperCase())){
			return Colori.NERO;
		}
		if(Colori.NEUTRO.getNome().toUpperCase().equals(nomeColore.toUpperCase())){
			return Colori.NEUTRO;
		}
		if(Colori.ROSSO.getNome().toUpperCase().equals(nomeColore.toUpperCase())){
			return Colori.ROSSO;
		}
		if(Colori.VERDE.getNome().toUpperCase().equals(nomeColore.toUpperCase())){
			return Colori.VERDE;
		}
		if(Colori.VIOLA.getNome().toUpperCase().equals(nomeColore.toUpperCase())){
			return Colori.VIOLA;
		}
		System.err.println("Colore non riconosciuto");
		return null;
	}
	
	/**
	 * Estrae un territorio da una stringa pulita.
	 * @param stringaPulita	La stinga da cui estrarre il territorio.
	 * @return	Il territorio estratto.
	 */
	public static Territorio estraiTerritorio(String stringaPulita){
		String nomeTerritorio = estraiStringa(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		String nomeContinente = estraiStringa(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		Integer unitaPresenti = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		Colori coloreProprietario = estraiColori(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		Integer numeroConfini = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		ArrayList<Territorio> listaConfini= new ArrayList<Territorio>(numeroConfini);
		for(int i = 0; i < numeroConfini; i++){
			String nomeTerrEstratto = estraiStringa(stringaPulita);
			stringaPulita = estraiSecondaParte(stringaPulita);
			String nomeContEstratto = estraiStringa(stringaPulita);
			stringaPulita = estraiSecondaParte(stringaPulita);
			listaConfini.add(new Territorio(nomeTerrEstratto, nomeContEstratto));
		}
		//Creiamo il territorio da inviare
		Territorio territorioEstratto = new Territorio(nomeTerritorio, nomeContinente);
		//Ci mettiamo i confini
		territorioEstratto.aggiungiListaConfini(listaConfini);
		//Ci mettiamo le unità
		territorioEstratto.setUnitaPresenti(unitaPresenti);
		//Ci mettiamo il colore del proprietario
		territorioEstratto.setColorePossessore(coloreProprietario);
		return territorioEstratto;
	}
	
	/**
	 * Estrae un Continente.
	 * @param stringaPulita	La stringa da cui estrarre il continente.
	 * @return	Il continente estratto.
	 */
	public static Continente estraiContinente(String stringaPulita){
		String nomeContinente = estraiStringa(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		Integer bonus = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		Integer numeroTerritori = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		ArrayList<Territorio> territori = new ArrayList<Territorio>(numeroTerritori);
		Territorio territorioTemp;
		for(int i = 0; i < numeroTerritori; i++){
			territorioTemp = estraiTerritorio(stringaPulita);
			stringaPulita = scorriTerritorio(stringaPulita);
			territori.add(territorioTemp);
		}
		//Creiamo il continente
		Continente continenteEstratto = new Continente(nomeContinente, bonus);
		//Inseriamo i suoi territori
		continenteEstratto.aggiungiListaTerritori(territori);
		return continenteEstratto;
	}
	
	/**
	 * Estrae la Mappa.
	 * @param stringaPulita	La stringa da cui estrarre la Mappa.
	 * @return	La mappa estratta.
	 */
	public static Mappa estraiMappa(String stringaPulita){
		int numeroContinenti = 0;
		int numeroTerritori = 0;
		ArrayList<Continente> continenti = new ArrayList<Continente>();
		ArrayList<Territorio> territori = new ArrayList<Territorio>();
		Continente continenteEstratto;
		Territorio territorioEstratto;
		numeroContinenti = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		for(int i = 0; i < numeroContinenti; i++){
			continenteEstratto = estraiContinente(stringaPulita);
			stringaPulita = scorriContinente(stringaPulita);
			continenti.add(continenteEstratto);
		}
		numeroTerritori = estraiInteger(stringaPulita);
		stringaPulita = estraiSecondaParte(stringaPulita);
		for(int i = 0; i < numeroTerritori; i++){
			territorioEstratto = estraiTerritorio(stringaPulita);
			stringaPulita = scorriTerritorio(stringaPulita);
			territori.add(territorioEstratto);
		}
		Mappa mappaEstratta = new Mappa(continenti, territori);
		return mappaEstratta;
	}
	
	/**
	 * Genera un messaggio con responso positivo
	 * 
	 * @param messaggioAllegato
	 *            Messaggio da allegare.
	 * @return Il messaggio pronto ad essere inviato.
	 */
	public static String responsoPositivo(String messaggioAllegato) {
		return AlfabetoSocket.esitoPositivo + AlfabetoSocket.at + messaggioAllegato;
	}

	/**
	 * Genera un messaggio con responso negativo.
	 * 
	 * @param messaggioAllegato
	 *            Messaggio da allegare.
	 * @return Il messaggio pronto ad essere inviato.
	 */
	public static String responsoNegativo(String messaggioAllegato) {
		return AlfabetoSocket.esitoNegativo + AlfabetoSocket.at + messaggioAllegato;
	}

	/**
	 * Genera un messaggio di eccezione.
	 * @param nomeException	Il nome dell'eccezione da inoltrare.
	 * @return 				Il messaggio pronto ad essere inviato.
	 */
	public static String responsoException(String nomeException) {
		return AlfabetoSocket.esitoException + AlfabetoSocket.at + nomeException;
	}
	

}
