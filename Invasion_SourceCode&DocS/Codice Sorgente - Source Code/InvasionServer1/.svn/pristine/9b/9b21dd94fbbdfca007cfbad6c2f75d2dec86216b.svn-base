package dbManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import mappa.Continente;
import mappa.Mappa;
import mappa.Territorio;
import classiCondivise.BeanGiocatore;
import classiCondivise.Classifica;
import exceptionCondivise.TerritorioNonTrovatoException;
public class DbManager {


	private final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
//	private String DBNAME; = "database";
//	private String URL; = "jdbc:derby:"+DBNAME, 
//	private String USER = "admin",
//	private String PSW; = "admin";

	private String DBNAME;
	private String URL; 
	private String USER;
	private String PSW; 
	/**
	 * Crea un connessione al DB.
	 * @return	La connessione al DB.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection connessioneDb() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
//		DBNAME = "database";
//		URL = "jdbc:derby:"+DBNAME;
//		USER = "admin";
//		PSW = "admin";
		
		Class.forName(DRIVER).newInstance();
		
		
		
		Properties fileProperties = new Properties();
		FileInputStream input;
		try {
			
			input = new FileInputStream("db.properties");
			fileProperties.load(input);
			input.close();
			this.DBNAME = fileProperties.getProperty("DBNAME");
			this.URL = fileProperties.getProperty("URL")+this.DBNAME;
			this.USER = fileProperties.getProperty("USER");
			this.PSW = fileProperties.getProperty("PSW");
			input.close();
		} catch (FileNotFoundException e) {
			System.err.println("File di properties non trovato - Non posso avviare il server");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Impossibile leggere il file di properties");
		}
		return DriverManager.getConnection(URL, USER, PSW);
	}
	
	/**
	 * Chiude la connessione al db.
	 * @param connessione	La connessione da chiudere.
	 */
	public void disconnessioneDb(Connection connessione){
		try {
			connessione.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tenta il login con le credenziali scelte. Aggiorna l'ultimo tentativo di login.
	 * @param connessione	La connessione al DB.
	 * @param nomeUtente	Lo username del giocatore.
	 * @param password		La password del giocatore.
	 * @return	true: Login effettuato; false: Credenziali errate;
	 * @throws SQLException	Connessione non valida.
	 */
	public boolean loginUtente(Connection connessione, String nomeUtente, String password) throws SQLException{
		Statement st = connessione.createStatement();
		String miaQuery = "SELECT COUNT(*) "+
							"FROM GIOCATORE "+
							"WHERE NOMEGIOCATORE='"+nomeUtente+"' AND PASSWORD='"+password+"'";
		ResultSet resultSet = st.executeQuery(miaQuery);
		resultSet.next();
		Integer numeroGiocatori = resultSet.getInt(1);
		resultSet.close();
		st.close();
		if(numeroGiocatori >= 1){
			aggiornaAccesso(connessione, nomeUtente);
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Aggiorna l'informazione dell'ultimo accesso alla partita del giocatore.
	 * @param connessione	Connessione al DB.
	 * @param nomeUtente	Username del giocatore che ha effettuato l'accesso.
	 * @throws SQLException	Connessione non valida.
	 */
	private void aggiornaAccesso(Connection connessione, String nomeUtente) throws SQLException{
		Timestamp data = new Timestamp(System.currentTimeMillis());
		String miaQuery = "UPDATE GIOCATORE "+
							"SET DATAULTIMOACCESSO=? "+
							"WHERE NOMEGIOCATORE=?";
		PreparedStatement preparedStatement = connessione.prepareStatement(miaQuery);
		preparedStatement.setTimestamp(1, data);
		preparedStatement.setString(2, nomeUtente);;
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
	
	/**
	 * Salva nel DB la partita appena avviata.
	 * @param connessione	La connessione al DB.
	 * @param giocatori	Lista dei nomi dei giocatori che partecipano alla partita.
	 * @return	L'Id della partita avviata
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public Integer creaPartita(Connection connessione, ArrayList<String> giocatori) throws SQLException{
		Integer idPartita = estraiNumeroPartita(connessione);
		Timestamp data = new Timestamp(System.currentTimeMillis());
		String miaQuery = "INSERT INTO PARTITA (IDPARTITA, DATAPARTITA)"+
							"VALUES (?, ?)";
		PreparedStatement preparedStatement = connessione.prepareStatement(miaQuery);
		preparedStatement.setInt(1,idPartita);
		preparedStatement.setTimestamp(2, data);
		preparedStatement.executeUpdate();
		preparedStatement.close();
		aggiungiPartecipazione(connessione, giocatori, idPartita);
		return idPartita;	//Ritornerò l'id della partita
	}
	
	/**
	 * Aggiunge al DB i partecipanti alla partita.
	 * @param connessione	La connessione al DB.
	 * @param giocatori	Lista dei giocatori che partecipano.
	 * @param partita	La partita a cui partecipano.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	private void aggiungiPartecipazione(Connection connessione, ArrayList<String> giocatori, Integer partita) throws SQLException{
		Iterator<String> itGiocatori = giocatori.iterator();
		String miaQuery = "INSERT INTO PARTECIPAZIONEPARTITA (IDPARTITA, NOMEGIOCATORE, POSIZIONE, PUNTIVINTI) "+
					"VALUES (?, ?, ?, ?)";
		PreparedStatement preparedStatement = connessione.prepareStatement(miaQuery);
		while(itGiocatori.hasNext()){
			preparedStatement.setInt(1, partita);
			preparedStatement.setString(2, itGiocatori.next());
			preparedStatement.setInt(3, -1);
			preparedStatement.setInt(4, 0);
			preparedStatement.executeUpdate();
		}
		preparedStatement.close();
	}
	
	/**
	 * Estrae il numero della nuova partita.
	 * @param connessione	Connessione al DB.
	 * @return	Il numero della partita.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	private Integer estraiNumeroPartita(Connection connessione) throws SQLException{
		Statement st = connessione.createStatement();
		String miaQuery = "SELECT MAX(IDPARTITA)+1 "+
							"FROM PARTITA";
		ResultSet resultSet = st.executeQuery(miaQuery);
		resultSet.next();
		Integer id = resultSet.getInt(1);
		resultSet.close();
		st.close();
		return id;	//Ritorno l'id della partita
	}
	
	/**
	 * Prova ad aggiungere l'utente al DB.
	 * @param connessione	Connessione al DB.
	 * @param nomeNuovoGiocatore	Username del nuovo giocatore.
	 * @param passwordNuovoGiocatore	Password del nuovo giocatore.
	 * @return	true: Registrazione avvenuta con successo; false: Username già in uso;
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public boolean registraUtente(Connection connessione, String nomeNuovoGiocatore, String passwordNuovoGiocatore) throws SQLException{
		if(usernameInUso(connessione, nomeNuovoGiocatore)){	//Impossibile creare il nuovo utente
			return false;
		}
		//Estraggo la data
		Timestamp data = new Timestamp(System.currentTimeMillis());
		//Preparo la query per il nuovo giocatore
		String miaQuery = "INSERT INTO GIOCATORE (NOMEGIOCATORE, PASSWORD, DATAREGISTRAZIONE, DATAULTIMOACCESSO) "+
						"VALUES (?, ?, ?, ?)";
		PreparedStatement preparedStatement = connessione.prepareStatement(miaQuery);
		preparedStatement.setString(1, nomeNuovoGiocatore);
		preparedStatement.setString(2, passwordNuovoGiocatore);
		preparedStatement.setTimestamp(3, data);
		preparedStatement.setTimestamp(4, data);
		//Invio lo statement e lo chiudo
		preparedStatement.executeUpdate();
		preparedStatement.close();
		return true;
	}
	
	/**
	 * Cancella lo username inviato
	 * @param connessione	La connessione al DB
	 * @param nomeGiocatore	Il nome del giocatore da cancellare
	 * @throws SQLException	Problema nell'esecuzione della query
	 */
	public void cancellaUtente(Connection connessione, String nomeGiocatore) throws SQLException{
		String miaQuery = "DELETE FROM GIOCATORE "+
							"WHERE NOMEGIOCATORE = ?";
		PreparedStatement preparedStatement = connessione.prepareStatement(miaQuery);
		preparedStatement.setString(1, nomeGiocatore);
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
	
	/**
	 * Prova a cambiare la password
	 * @param connessione
	 * @param username
	 * @param vecchiaPassword
	 * @param nuovaPassword
	 * @return
	 * @throws SQLException
	 */
	public boolean cambiaPassword(Connection connessione, String username, String vecchiaPassword, String nuovaPassword) throws SQLException{
		if(loginUtente(connessione, username, vecchiaPassword) == false)
			return false;
		else{
			String miaQuery = "UPDATE GIOCATORE "+
					"SET PASSWORD=? "+
					"WHERE NOMEGIOCATORE=?";
			PreparedStatement preparedStatement = connessione.prepareStatement(miaQuery);
			preparedStatement.setString(1, nuovaPassword);
			preparedStatement.setString(2, username);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			return true;
		}
	}
	
	/**
	 * Ci informa se lo username è già in uso
	 * @param connessione	La connessione al DB
	 * @param nomeUtente	Il nome da controllare
	 * @return	-true: Utente già esistente;	-false:Utente inesistente;
	 */
	public boolean usernameInUso(Connection connessione, String nomeUtente){
		String miaQuery = "SELECT COUNT(*) "+
							"FROM GIOCATORE "+
							"WHERE NOMEGIOCATORE = '"+nomeUtente+"'";
		try {
			Statement st = connessione.createStatement();
			ResultSet resultSet = st.executeQuery(miaQuery);
			resultSet.next();
			int numero = resultSet.getInt(1);
			if(numero == 0){
				return false;
			}
		} catch (SQLException e) {
			System.err.println("Impossibile eseguire query");
			e.printStackTrace();
		}
		return true;

	}
	
	/**
	 * L'elenco di tutti i territori presenti sulla mappa.
	 * @param connessione	Connessione del DB
	 * @return	La lista di tutti i territori
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public ArrayList<String> elencoTerritori(Connection connessione) throws SQLException{
		ArrayList<String> territori = new ArrayList<String>();
		String miaQuery="SELECT NOMETERRITORIO "+
							"FROM TERRITORIO "+
							"ORDER BY NOMETERRITORIO ASC ";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		while(resultSet.next()){
			String nomeTerritorio = resultSet.getString(1);
			territori.add(nomeTerritorio);
		}
		resultSet.close();
		st.close();
		return territori;
	}
	
	/**
	 * Elenco dei terriori di un dato continente.
	 * @param connessione	Connessione al DB.
	 * @param continenteDiAppartenenza	Il continente di cui vogliamo i territori.
	 * @return	La lista dei territori cercati.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public ArrayList<String> elencoTerritori(Connection connessione, String continenteDiAppartenenza) throws SQLException{
		ArrayList<String> territori = new ArrayList<String>();
		String miaQuery="SELECT NOMETERRITORIO "+
							"FROM TERRITORIO "+
							"WHERE NOMECONTINENTE='"+continenteDiAppartenenza+"'"+
							"ORDER BY NOMETERRITORIO ASC ";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		while(resultSet.next()){
			String nomeContinente = resultSet.getString(1);
			territori.add(nomeContinente);
		}
		resultSet.close();
		st.close();
		return territori;
	}
	
	public Mappa estraiMappa(Connection connessione) throws SQLException{
		Mappa mappaDaInviare;
		ArrayList<Continente> continenti = estraiContinenti(connessione);
		ArrayList<Territorio> territori = estraiTuttiITerritori(continenti);
		Iterator<Continente> itContinenti = continenti.iterator();
		ArrayList<String> elencoConfini = new ArrayList<String>();
		Iterator<String> itConfini;
		Continente continenteEstratto;
		Territorio territorioEstratto;
		Territorio confinante;
		while(itContinenti.hasNext()){
			//Estraiamo la lista dei continenti e per ogni continente la lista dei territori.
			continenteEstratto = itContinenti.next();
			Iterator<Territorio> itTerritorio = continenteEstratto.getTerritori().iterator();
			while(itTerritorio.hasNext()){
				//Ad ogni territorio mettiamo i confini
				territorioEstratto = itTerritorio.next();
				elencoConfini = elencoConfini(connessione, territorioEstratto.getNome());
				itConfini = elencoConfini.iterator();
				//Inizio a sfogliare la lista dei confini estratta
				while(itConfini.hasNext()){
					try {
						confinante = estraiTerritorioDaContinenti(itConfini.next(), continenti);
						territorioEstratto.aggiungiConfine(confinante);
					} catch (TerritorioNonTrovatoException e) {
						System.err.println("Errore nell'estrazione dei confini");
					}
				}
			}
		}
		mappaDaInviare = new Mappa(continenti, territori);
		//mappaDaInviare.toString();
		return mappaDaInviare;
	}
	
	/**
	 * Estrae il riferimento al territorio dalla lista dei continenti.
	 * @param nomeTerritorio	Il nome del territorio da estrarre.
	 * @param continenti		La lista di tutti i continenti.
	 * @return					Il riferimento al Territorio.
	 * @throws TerritorioNonTrovatoException	Il territorio cercato è inesistente.
	 */
	private Territorio estraiTerritorioDaContinenti(String nomeTerritorio, ArrayList<Continente> continenti) throws TerritorioNonTrovatoException{
		Iterator<Continente> itContinenti = continenti.iterator();
		Continente continenteEstratto;
		Territorio territorioEstratto;
		while(itContinenti.hasNext()){
			continenteEstratto = itContinenti.next();
			Iterator<Territorio> itTerritori = continenteEstratto.getTerritori().iterator();
			//Cerchiamo il territorio
			while(itTerritori.hasNext()){
				territorioEstratto = itTerritori.next();
				if(territorioEstratto.siChiama(nomeTerritorio)){
					return territorioEstratto;
				}
			}
		}
		//Abbiamo sfogliato tutti i territori e non abbiamo trovato nulla con il nome cercato.
		throw new TerritorioNonTrovatoException();
	}
	
	/**
	 * Estraiamo la lista dei riferimenti a tutti i territori.
	 * @param continenti	La lista dei continenti.
	 * @return				La lista dei territori.
	 */
	private ArrayList<Territorio> estraiTuttiITerritori(ArrayList<Continente> continenti){
		ArrayList<Territorio> territori = new ArrayList<Territorio>(42);	//Il senso della vita! Ecco perchè 42, non farti domande.
		Iterator<Continente> itContinenti = continenti.iterator();
		Iterator<Territorio> itTerritori;
		//Estraiamo tutti i Continenti
		while(itContinenti.hasNext()){
			itTerritori = itContinenti.next().getTerritori().iterator();
			//Per ogni continente estraiamo tutti i territori
			while(itTerritori.hasNext()){
				territori.add(itTerritori.next());
			}
		}
		return territori;
	}
	
	/**
	 * Estrae un ArrayList di Continenti.
	 * @param connessione	La connessione al DB.
	 * @return	L'ArrayList di continenti.
	 * @throws SQLException	Impossibile connettersi al DB.
	 */
	public ArrayList<Continente> estraiContinenti(Connection connessione) throws SQLException{
		ArrayList<String> elencoContinenti = elencoContinenti(connessione);
		ArrayList<Integer> elencoBonus = elencoBonusContinenti(connessione);
		Iterator<String> itContinenti = elencoContinenti.iterator();
		ArrayList<Continente> continenti = new ArrayList<Continente>(elencoContinenti.size());
		String continenteNome;
		Continente continente;
		int i = 0;
		while(itContinenti.hasNext()){
			continenteNome = itContinenti.next();
			continente = new Continente(continenteNome, elencoBonus.get(i));
			continente.aggiungiListaTerritori(estraiTerritoriContinente(connessione, continenteNome));
			continenti.add(continente);
			i++;
		}
		return continenti;
	}
	
	/**
	 * Estrae un ArrayList di territori presenti in un continente.
	 * @param connessione	Connessione al db.
	 * @param continente	Continente di appartenenza dei territori.
	 * @return				La lista dei territori.
	 * @throws SQLException 
	 */
	private ArrayList<Territorio> estraiTerritoriContinente(Connection connessione, String continente) throws SQLException{
		ArrayList<String> nomiTerritori = elencoTerritori(connessione, continente);
		Iterator<String> itTerritori = nomiTerritori.iterator();
		ArrayList<Territorio> territori = new ArrayList<Territorio>(nomiTerritori.size());
		while(itTerritori.hasNext()){
			territori.add(new Territorio(itTerritori.next(), continente));
		}
		return territori;
	}
	
	/**
	 * Elenco dei confini di un dato territorio.
	 * @param connessione	Connessione al DB.
	 * @param territorioCentrale	Il territorio di cui cerchiamo i confini.
	 * @return	La lista dei territori confinanti.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public ArrayList<String> elencoConfini(Connection connessione, String territorioCentrale) throws SQLException{
		ArrayList<String> territori = new ArrayList<String>();
		String miaQuery="SELECT NOMETERRITORIO2 "+
							"FROM CONFINE "+
							"WHERE NOMETERRITORIO1='"+territorioCentrale+"'"+
							"ORDER BY NOMETERRITORIO2 ASC ";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		while(resultSet.next()){
			String nomeContinente = resultSet.getString(1);
			territori.add(nomeContinente);
		}
		resultSet.close();
		st.close();
		return territori;
	}
	
	/**
	 * Il numero di continenti presenti.
	 * @param connessione	Connessione al DB.
	 * @return	Il numero di continenti presenti.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public int numeroDiContinenti(Connection connessione) throws SQLException{
		String miaQuery = "SELECT COUNT(*) "+
				"FROM CONTINENTE";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		resultSet.next();
		int risultato = resultSet.getInt(1);
		resultSet.close();
		st.close();
		return risultato;
	}
	
	/**
	 * Elenco dei continenti presenti (ordinati per ordine alfabetico crescente).
	 * @param connessione	Connessione al DB
	 * @return	La lista dei continenti presenti.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public ArrayList<String> elencoContinenti(Connection connessione) throws SQLException{
		ArrayList<String> continenti = new ArrayList<String>(numeroDiContinenti(connessione));
		String miaQuery="SELECT NOMECONTINENTE "+
							"FROM CONTINENTE "+
							"ORDER BY NOMECONTINENTE ASC ";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		while(resultSet.next()){
			String nomeContinente = resultSet.getString(1);
			continenti.add(nomeContinente);
		}
		resultSet.close();
		st.close();
		return continenti;
	}
	
	/**
	 * La lista dei bonus di tutti i continenti (ordinati per ordine alfabetico crescente).
	 * @param connessione	Connessione al DB
	 * @return	La lista dei bonus di ogni continente.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public ArrayList<Integer> elencoBonusContinenti(Connection connessione) throws SQLException{
		ArrayList<Integer> bonus = new ArrayList<Integer>(numeroDiContinenti(connessione));
		String miaQuery="SELECT BONUS "+
				"FROM CONTINENTE "+
				"ORDER BY NOMECONTINENTE ASC ";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		while(resultSet.next()){
			Integer valore = resultSet.getInt(1);
			bonus.add(valore);
		}
		resultSet.close();
		st.close();
		return bonus;
	}
	
	/**
	 * Aggiorna l'utente in seguito ad una partita.
	 * @param connessione			La connessione al DB.
	 * @param username				Lo username del giocatore.
	 * @param idPartita				L'id della partita a cui ha partecipato.
	 * @param punteggioGiocatore	Il punteggio ottenuto dal giocatore nella partita.
	 * @param posizioneClassifica	La posizione nella classifica della partita.
	 * @throws SQLException			Connessione al DB non valida.
	 */
	public void aggiornaUtentePostPartita(Connection connessione, String username, Integer idPartita, Integer punteggioGiocatore, Integer posizioneClassifica) throws SQLException{
		//Preparo la query per il nuovo giocatore
		String miaQuery = "UPDATE PARTECIPAZIONEPARTITA " +
				"SET POSIZIONE = ?, PUNTIVINTI = ? "+
				"WHERE IDPARTITA = ? and NOMEGIOCATORE = ?";
		PreparedStatement preparedStatement = connessione.prepareStatement(miaQuery);
		preparedStatement.setInt(1, posizioneClassifica);
		preparedStatement.setInt(2, punteggioGiocatore);
		preparedStatement.setInt(3, idPartita);
		preparedStatement.setString(4, username);
		//Invio lo statement e lo chiudo
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
	
	/**
	 * Ci da la percentuale di partite vinte da un giocatore.
	 * @param connessione	Connessione al DB
	 * @param username		L'utente di cui vogliamo le statistiche.
	 * @return				Un valore compreso tra 0 e 1.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	private float percentualePartiteVinte(Connection connessione, String username) throws SQLException{
		int totalePartite = totalePartiteGiocate(connessione, username);
		String miaQuery = "SELECT COUNT(*) FROM PARTECIPAZIONEPARTITA WHERE NOMEGIOCATORE = '"+username+"' AND POSIZIONE = 1";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		resultSet.next();
		int risultato = resultSet.getInt(1);
		resultSet.close();
		st.close();
		float risultatoPercentuale = (float)risultato/totalePartite;
		return risultatoPercentuale;
	}
	
	/**
	 * Ci da la percentuale di partite in cui un giocatore è arrivato secondo.
	 * @param connessione	Connessione al DB
	 * @param username		L'utente di cui vogliamo le statistiche.
	 * @return				Un valore compreso tra 0 e 1.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	private float percentualePartiteSecondo(Connection connessione, String username) throws SQLException{
		int totalePartite = totalePartiteGiocate(connessione, username);
		String miaQuery = "SELECT COUNT(*) FROM PARTECIPAZIONEPARTITA WHERE NOMEGIOCATORE = '"+username+"' AND POSIZIONE = 2";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		resultSet.next();
		int risultato = resultSet.getInt(1);
		resultSet.close();
		st.close();
		float risultatoPercentuale = (float)risultato/totalePartite;
		return risultatoPercentuale;
	}

	/**
	 * Ci da la percentuale di partite abbandonate da un giocatore.
	 * @param connessione	Connessione al DB
	 * @param username		L'utente di cui vogliamo le statistiche.
	 * @return				Un valore compreso tra 0 e 1.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	private float percentualePartiteAbbandonate(Connection connessione, String username) throws SQLException{
		int totalePartite = totalePartiteGiocate(connessione, username);
		String miaQuery = "SELECT COUNT(*) FROM PARTECIPAZIONEPARTITA WHERE NOMEGIOCATORE = '"+username+"' AND POSIZIONE = -1";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		resultSet.next();
		int risultato = resultSet.getInt(1);
		resultSet.close();
		st.close();
		float risultatoPercentuale = (float)risultato/totalePartite;
		return risultatoPercentuale;
		
	}

	/**
	 * Il numero di partite giocate da un giocatore.
	 * @param connessione	Connessione al DB
	 * @param username		L'utente di cui vogliamo le statistiche.
	 * @return				Il numero di partite giocate.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public int totalePartiteGiocate(Connection connessione, String username) throws SQLException{
		String miaQuery = "SELECT COUNT(*) FROM PARTECIPAZIONEPARTITA WHERE NOMEGIOCATORE = '"+username+"'";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		resultSet.next();
		int risultato = resultSet.getInt(1);
		resultSet.close();
		st.close();
		return risultato;
	}
	
	/**
	 * Il numero di partite giocate in totale.
	 * @param connessione	Connessione al DB
	 * @return				Il numero di partite giocate.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public int totalePartiteGiocate(Connection connessione) throws SQLException{
		String miaQuery = "SELECT COUNT(*) FROM PARTECIPAZIONEPARTITA";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		resultSet.next();
		int risultato = resultSet.getInt(1);
		resultSet.close();
		st.close();
		return risultato;
	}
	
	/**
	 * Il numero di punti di un giocatore.
	 * @param connessione	Connessione al DB.
	 * @param username		L'utente di cui vogliamo le statistiche.
	 * @return				Il punteggio.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	private int puntiVinti(Connection connessione, String username) throws SQLException{
		String miaQuery = "SELECT SUM(PUNTIVINTI) FROM PARTECIPAZIONEPARTITA WHERE NOMEGIOCATORE = '"+username+"'";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		resultSet.next();
		int risultato = resultSet.getInt(1);
		resultSet.close();
		st.close();
		return risultato;
	}
	
	/**
	 * Ci ritorna la classifica dei giocatori.
	 * @param connessione	Connessione al DB.
	 * @return				La classifica totale
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public Classifica classificaGenerale(Connection connessione) throws SQLException{
		//Contiamo il numero di giocatori
		String miaQuery = "SELECT COUNT(*) FROM GIOCATORE";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		resultSet.next();
		int numeroRighe = resultSet.getInt(1);
		resultSet.close();
		Classifica miaClassifica = new Classifica(numeroRighe);
		//Ci da la classifica di punteggio in ordine decrescente
		miaQuery = "SELECT * "+
				"FROM CLASSIFICAPUNTEGGIO "+
				"UNION "+
				"SELECT * "+
				"FROM FUORICLASSIFICA "+
				"ORDER BY PUNTI DESC, NOME ASC";
		ResultSet resultSet2 = st.executeQuery(miaQuery);
		while(resultSet2.next()){		
			String nome = resultSet2.getString("NOME");
			Integer punti = resultSet2.getInt("PUNTI");
			miaClassifica.aggiungiGiocatore(nome, punti);
		}
		resultSet2.close();
		st.close();
		return miaClassifica;
		/*
		 * CREATE VIEW ClassificaPunteggio (Nome, Punti) AS
		 *SELECT NOMEGIOCATORE AS NG, Sum(PUNTIVINTI) AS A
		 *FROM PARTECIPAZIONEPARTITA AS PP JOIN NUMEROPARTITE AS NP
		 *	ON NOMEGIOCATORE = NP.B
		 *GROUP BY NOMEGIOCATORE
		 *ORDER BY A DESC
		 *
		 *CREATE VIEW FuoriClassifica (Nome, Punti) AS
		 *SELECT NOMEGIOCATORE, 0 AS PUNTEGGIO
		 *FROM GIOCATORE 
		 *WHERE NOMEGIOCATORE NOT IN 
		 *	(SELECT NOME 
		 *	FROM CLASSIFICAPUNTEGGIO) 
		 */
	}
	
	/**
	 * La posizione del giocatore nella classifica generale.
	 * @param connessione	Connessione al DB.
	 * @param username		L'utente di cui vogliamo le statistiche.
	 * @return				La posizione.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	private Integer posizioneInClassifica(Connection connessione, String username) throws SQLException{
		Classifica miaClassifica = classificaGenerale(connessione);
		return miaClassifica.getPosizione(username);
	}
	
	/**
	 * Estrae il Bean dal database con tutte le informazioni dell'utente richiesto.
	 * @param connessione	Connessione al DB.
	 * @param username		Il nome del giocatore di cui vogliamo il Bean.
	 * @return				Il Bean del utente richiesto.
	 * @throws SQLException	Connessione al DB non valida.
	 */
	public BeanGiocatore estraiBean(Connection connessione, String username) throws SQLException{
		Integer punteggio = puntiVinti(connessione, username);
		Integer posizioneClassifica = posizioneInClassifica(connessione, username);
		Integer numeroPartitePartecipate = totalePartiteGiocate(connessione, username);
		float percentualePartiteVinte = percentualePartiteVinte(connessione, username);
		float percentualePartiteSecondo = percentualePartiteSecondo(connessione, username);
		float percentualePartiteAbbandonate = percentualePartiteAbbandonate(connessione, username);
		boolean connesso = true;		
		if(posizioneClassifica == null){
			return null;
		}
		BeanGiocatore giocatore = new BeanGiocatore(username, punteggio, posizioneClassifica, numeroPartitePartecipate, percentualePartiteVinte, percentualePartiteSecondo, percentualePartiteAbbandonate, connesso);
		return giocatore;
	}
	
	/**
	 * Estrae la lista dei giocatori registati ad Invasion.
	 * @return	La lista dei giocatori.
	 * @throws SQLException Errore nel DB.
	 */
	public ArrayList<String> estraiListaGiocatori(Connection connessione) throws SQLException{
		ArrayList<String> elenco = new ArrayList<String>();
		String miaQuery="SELECT NOMEGIOCATORE"+
						"FROM GIOCATORE";
		Statement st = connessione.createStatement();
		ResultSet resultSet = st.executeQuery(miaQuery);
		while(resultSet.next()){
			String nomeGiocatore = resultSet.getString(1);
			elenco.add(nomeGiocatore);
		}
		resultSet.close();
		st.close();
		return elenco;
	}
	
//	public String[] estraiContinenti (Connection connessione) throws SQLException{
//		Integer numeroRighe = numeroDiContinenti(connessione);
//		String miaQuery = "SELECT NOMECONTINENTE "+
//				"FROM CONTINENTE "+
//				"ORDER BY NOMECONTINENTE ASC";
//		Statement st = connessione.createStatement();
//		ResultSet resultSet2 = st.executeQuery(miaQuery);
//		String[] continenti = new String[numeroRighe];
//		for(int i = 0; i < numeroRighe; i++){
//			resultSet2.next();
//			String nome = resultSet2.getString(1);
//			continenti[i] = nome;
//		}
//		resultSet2.close();
//		st.close();
//		return continenti;
//	}
//		
//	public Integer[] estraiBonus (Connection connessione) throws SQLException{
//		Integer numeroRighe = numeroDiContinenti(connessione); 
//		Integer maxContinenti = numeroDiContinenti(connessione);
//		Integer[] bonus = new Integer[maxContinenti];
//		//Ci da la classifica di punteggio in ordine decrescente
//		String miaQuery = "SELECT BONUS "+
//				"FROM CONTINENTE "+
//				"ORDER BY NOMECONTINENTE ASC";
//		Statement st = connessione.createStatement();
//		ResultSet resultSet2 = st.executeQuery(miaQuery);
//		for(int i = 0; i < numeroRighe; i++){
//			resultSet2.next();
//			Integer valore = resultSet2.getInt(1);
//			bonus[i] = valore;
//		}
//		resultSet2.close();
//		st.close();
//		return bonus;
//	}
//	
//	private Integer numeroMassimoTerritori (Connection connessione) throws SQLException{
//		String miaQuery = "SELECT MAX(NUMERO) "+
//				"FROM NUMEROTERRITORI";
//		Statement st = connessione.createStatement();
//		ResultSet resultSet = st.executeQuery(miaQuery);
//		resultSet.next();
//		int risultato = resultSet.getInt(1);
//		resultSet.close();
//		st.close();
//		return risultato;
//	}
	
//	public String[][] estraiTerritori (Connection connessione) throws SQLException{
//		String[] continenti = estraiContinenti(connessione);
//		Integer maxTerritori = numeroMassimoTerritori(connessione);
//		String[][] territori = new String[continenti.length][maxTerritori];
//		String miaQuery = "SELECT NOMETERRITORIO FROM TERRITORIO  "+
//				"WHERE NOMECONTINENTE = ?";
//		PreparedStatement preparedStatement = connessione.prepareStatement(miaQuery);
//		for(int i = 0; i < continenti.length; i++){
//			preparedStatement.setString(1, continenti[i]);
//			ResultSet rs = preparedStatement.executeQuery();
//			int j = 0;
//			while(rs.next()){
//				territori[i][j] = rs.getString(1);
//				System.out.println(territori[i][j]);
//				j++;
//			}
//		}
//		preparedStatement.close();
//		return territori;
//	}
		
//	private Integer numeroMassimoConfini(Connection connessione) throws SQLException{
//		String miaQuery = "SELECT MAX(NUMERO) "+
//		"FROM NUMEROCONFINI";
//		Statement st = connessione.createStatement();
//		ResultSet resultSet = st.executeQuery(miaQuery);
//		resultSet.next();
//		int risultato = resultSet.getInt(1);
//		resultSet.close();
//		st.close();
//		return risultato;
//		
//	}
//	
//	private Integer numeroTotaleTerritori(Connection connessione) throws SQLException{
//		String miaQuery = "SELECT SUM(NUMERO) "+
//		"FROM NUMEROTERRITORI";
//		Statement st = connessione.createStatement();
//		ResultSet resultSet = st.executeQuery(miaQuery);
//		resultSet.next();
//		int risultato = resultSet.getInt(1);
//		resultSet.close();
//		st.close();
//		return risultato;
//	}
	
//	public ArrayList<Territorio> estraiTuttiTerritori (Connection connessione) throws SQLException{
//		Integer numeroRighe = numeroTotaleTerritori(connessione);
//		String miaQuery = "SELECT NOMETERRITORIO1 "+
//				"FROM CONFINE "+
//				"GROUP BY NOMETERRITORIO1 "+
//				"ORDER BY NOMETERRITORIO1 ASC";
//		Statement st = connessione.createStatement();
//		ResultSet resultSet2 = st.executeQuery(miaQuery);
//		ArrayList<Territorio> territori= new ArrayList<Territorio>(numeroRighe);
//		for(int i = 0; i < numeroRighe; i++){
//			resultSet2.next();
//			String nome = resultSet2.getString(1);
//			territori[i] = nome;
//		}
//		resultSet2.close();
//		st.close();
//		return territori;
//	}
	
//	public String[][] estraiConfini (Connection connessione) throws SQLException{
//		String[] tuttiTerritori = estraiTuttiTerritori(connessione);
//		Integer maxConfini = numeroMassimoConfini(connessione);
//		String [][] confini = new String[tuttiTerritori.length][maxConfini];
//		for(int i = 0; i < tuttiTerritori.length; i++){
//			ArrayList<String> confiniStato;
//			confiniStato = elencoConfini(connessione, tuttiTerritori[i]);
//			Iterator<String> itConfini = confiniStato.iterator();
//			int j = 0;
//			while(itConfini.hasNext()){
//				confini[i][j] = itConfini.next();
//				j++;
//			}
//		}
//		return confini;
//	}
}