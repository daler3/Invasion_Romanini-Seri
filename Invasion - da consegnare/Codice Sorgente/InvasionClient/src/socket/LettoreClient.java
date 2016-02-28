package socket;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

import socketcondiviso.AlfabetoSocket;

/**
 * Lettore che si occupa dell'invio e della ricezione dei messaggi via socket da parte del client.
 * @author timmy
 *
 */
public class LettoreClient implements Runnable {

	private InterpreteClient interprete;
	private boolean continua;
	private Socket socket = null;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private String stringaLetta = AlfabetoSocket.customString;
	
	/**
	 * Instanzia un lettore con il socket appena aperto.
	 * @param interprete	L'interprete con cui comunica il lettore.
	 * @param mioSocket		Il socket di comunicazione con il client.
	 */
	public LettoreClient (InterpreteClient interprete, Socket mioSocket){
				
		this.interprete = interprete;
		this.continua = true;
		this.socket = mioSocket;
        try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Impossibile creare comunicazione tramite Socket");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
        //Ciclo di lettura continua
		while(continua){
			String stringaLetta = null;
			String stringaDaInviare = null;
			try {
				stringaLetta = reader.readLine();
				if(stringaLetta.toUpperCase().startsWith(AlfabetoSocket.esitoPositivo) || stringaLetta.toUpperCase().startsWith(AlfabetoSocket.esitoNegativo) || stringaLetta.toUpperCase().startsWith(AlfabetoSocket.esitoException)){
					aggiornaStringa(stringaLetta);
				}
				else{	//Caso comunicazione ricevuta dal client
					stringaDaInviare = interprete.interpreta(stringaLetta);
					writer.println(stringaDaInviare);	//Risposta al client - Anche se VOID comunica al client
					writer.flush();
				}
			} catch (IOException e) {
				continua = false; 
				this.interprete.disconnetti();
				Thread.currentThread().interrupt();
				
			} catch (NullPointerException e){
				continua = false; 
				this.interprete.disconnetti();
				Thread.currentThread().interrupt();
				
				
				
				//e.printStackTrace();
				//TODO gestire un eventuale null del client che spegne la connessione
			}
		}

	}
	
	/**
	 * Invia un comando e attende indefinitamente la risposta del client prima di procedere.
	 * @param comandoDaInviare	Il comando da inviare al client.
	 * @return	La risposta del client.
	 */
	public synchronized String inviaComando(String comandoDaInviare){
		//Riportiamo il campo al valore di default
				this.stringaLetta = AlfabetoSocket.customString;
				//Inviamo la comunicazione al client
				writer.println(comandoDaInviare);
				writer.flush();
				//Attendiamo la risposta dell'utente
				if(stringaLetta.equals(AlfabetoSocket.customString)){
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//Ci salviamo la risposta, ripristiniamo la stringa di default e ritorniamo quello che abbiamo letto
				String risposta = stringaLetta;
				this.stringaLetta = AlfabetoSocket.customString;
				return risposta;
	}
	
	/**
	 * Metodo per notificare la lettura del messaggio
	 * @param rispostaClient	Il messaggio letto
	 */
	public synchronized void aggiornaStringa(String rispostaClient){
		stringaLetta = rispostaClient;
		notifyAll();
	}

}
