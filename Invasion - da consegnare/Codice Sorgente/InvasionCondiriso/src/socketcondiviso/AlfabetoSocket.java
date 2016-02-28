package socketcondiviso;


/**
 * Alfabeto utilizzato per tradurre e interpretare le stringhe scambiate tramite la comunicazione socket
 * 
 */
public class AlfabetoSocket {

	public static final String customString = "DEFAULT";
	public static final String at = "@";
	public static final String dotcomma = ";";
	public static final String escape = "#";
	
	public final static String stringaVoid = "VOID";
	
	public static final String esitoPositivo = "OK";
	public static final String esitoNegativo = "KO";
	public static final String esitoException = "EXCEPTION";
	
	public final static String login = "LOGIN";
	public final static String sqlException = "SQL";
	
	public final static String logout = "LOGOUT";
	public final static String registra = "REGISTRA";
	public final static String cambioPsw = "CAMBIOPSW";
	
	public final static String classifica = "STATISTICHEGENERALI";
	public final static String ottieniInfoUtente = "OTTIENIINFOUTENTE";
	
	public final static String aggiornaTavoli = "AGGIORNATAVOLI";
	public final static String creaTavolo = "CREATAVOLO";
	public final static String joinTavolo = "UNISCITITAVOLO";
	public final static String abbandonaTavolo = "ABBANDONATAVOLO";
	public final static String startTavolo = "AVVIATAVOLO";
	
	public final static String sceltaColore = "COLORESCELTO";
	public final static String posizionamentoArmate = "POSIZIONOARMATE";
	public final static String passo = "PASSO";
	public final static String sposta = "SPOSTA";
	public final static String attacca = "ATTACCA";
	public final static String abbandona = "ABBANDONA";
	
	
	//Da Server a Client
	public final static String infoUtenteAggiornate = "MIEINFOUTENTE";
	public final static String listaTavoliAggiornati = "TAVOLIAGGIORNATI";
	public final static String cambiamentiTavolo = "CAMBIAMENTITAVOLO";
	
	public final static String inizioPartita = "INIZIOPARTITA";
	public final static String ordinePartita = "ORDINE";
	public final static String territoriPerPosizionamento = "POSIZIONAMENTODEFINITIVO";
	public final static String scegliIlTuoColore = "SCEGLITIILCOLORE";
	public final static String aggiornaMappa = "AGGIORNATILAMAPPA";
	public final static String turnoAltrui ="TURNOALTRUI";
	public final static String turnoTuo = "TURNOTUO";
	public final static String attendoMossa = "FAILATUAMOSSA";
	public final static String attaccoEffettuato = "ATTACCOCONFERMATO";
	public final static String sconfittaGiocatore = "SCONFITTAGIOCATORE";
	public final static String ritirataGiocatore = "ABBANDONOGIOCATORE";
	public final static String finePartita = "FINEPARTITA";
	
	public final static String abbandonaPartita = "ABBANDONAPARTITA";
	
}
