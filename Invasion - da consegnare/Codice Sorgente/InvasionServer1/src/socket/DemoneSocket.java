package socket;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import server.GiocatoreConnesso;

public class DemoneSocket implements Runnable {

	private Integer port = 4446;
	private boolean continuaCiclo = true;
	
	@Override
	public void run() {
		//carico i file di properties
		Properties fileProperties = new Properties();
		FileInputStream input;
		try {
			input = new FileInputStream("connessione.properties");
			fileProperties.load(input);
			input.close();
			this.port = Integer.parseInt(fileProperties.getProperty("socket.port"));
			input.close();
		} catch (FileNotFoundException e) {
			System.err.println("File di properties non trovato - Non posso avviare il server");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Impossibile leggere il file di properties");
		}
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		InterpreteServer comunicazione = null;
//		
        try {
        	//Inizializzo il server socket
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Non posso fare listen sulla porta: "+port);
            System.exit(1);
        }
        
    	continuaCiclo = true;
    	ArrayList<InterpreteServer> tasklist = new ArrayList<InterpreteServer>();
    	Iterator<InterpreteServer> iteratore = null;
        try {
			while(continuaCiclo){
				//Restiamo in attesa di un Client
				Thread.sleep(500);
	        	clientSocket = serverSocket.accept();
	        	if(continuaCiclo){
	        		//Creo il giocatore
	        		GiocatoreConnesso giocatore = new GiocatoreConnesso();
	        		//Creo l'interprete del lettore
	        		InterpreteServer mioInterprete = new InterpreteServer(clientSocket, giocatore);
	        		//Passo l'interfaccia di comunicazione al giocatore
	        		giocatore.assegnaInterfacciaComunicazione(mioInterprete); //la metterei direttamente i interprete
	        		//Fine creazione collegamento
		            
		            //Inserisco l'interprete nella lista
		            tasklist.add(mioInterprete);
		            
		            //Elimino tutti i thread terminati dalla lista
		            iteratore = tasklist.iterator();
	        		while(iteratore.hasNext()){
	        			//Rimuovo i Lettori morti
	        			comunicazione = iteratore.next();
	        			if(!comunicazione.isAlive()){	//Controlliamo tramite interprete se il lettore è vivo
	        				iteratore.remove();
	        			}
	        		}
	        	}else{
	        		//Rifiuto la connessione
	        		clientSocket.close();
	        		//lancio un interrupt a tutti i Lettori per chiuderli
	        		iteratore = tasklist.iterator();
	        		while(iteratore.hasNext()){
	        			comunicazione = iteratore.next();
	        			if(comunicazione.isAlive()){
	        				comunicazione.killLettore();
	        			}
	        		}
	        	}
        	}
            
        } catch (IOException e) {
            System.err.println("Accept fallita.");
        } catch (InterruptedException e) {
			this.continuaCiclo = false;
		} finally{
        	if(!serverSocket.isClosed()){
				try { serverSocket.close();	} catch (IOException e) {}
			}
        }
        

	}

	public void ferma() {
		this.continuaCiclo = false;
		Thread.currentThread().interrupt();
	}
	
	

}
