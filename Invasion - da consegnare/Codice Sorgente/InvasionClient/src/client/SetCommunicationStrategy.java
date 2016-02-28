package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.Properties;

import rmi.ClientComunicazione;
import socket.InterpreteClient;
import comunicazione.Client2Server;

/**
 * 
 * Classe per settare la classe di comunicazione 
 *
 */

public class SetCommunicationStrategy {

	private Integer port;
	private String host;
	
	
	public SetCommunicationStrategy() {
		super();
	}

	
	public void setComunicazione(StrategiaComunicazione comunicazione){
		
		//carico i file di properties
		Properties fileProperties = new Properties();
		FileInputStream input;
		try {
			input = new FileInputStream("connessione.properties");
			fileProperties.load(input);
			input.close();
			this.port = Integer.parseInt(fileProperties.getProperty("socket.port"));
			this.host = fileProperties.getProperty("rmi.host");
			input.close();
		} catch (FileNotFoundException e) {
			System.err.println("File di properties non trovato - Non posso avviare il server");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Impossibile leggere il file di properties");
		}
		
		Client2Server interfacciaComunicazione = null;	
		if(comunicazione == StrategiaComunicazione.RMI){
			try {
				interfacciaComunicazione = new ClientComunicazione();
				Ambasciatore.getInstance().setInterfacciaComunicazione(interfacciaComunicazione);
			} catch (RemoteException e) {
				return;
			}
		}
		else if(comunicazione == StrategiaComunicazione.SOCKET){
			try {
				Socket socket = new Socket(host , port);
				new InterpreteClient(socket);
			} catch (IOException e) {
				System.err.println("Impossibile connettersi a "+host+":"+port);
				return;
			}
		}
	}
	
}
