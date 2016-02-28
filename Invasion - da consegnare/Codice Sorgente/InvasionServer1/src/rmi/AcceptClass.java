package rmi;

import interfacceRemote.AcceptInterface;
import interfacceRemote.ClientComunicazioneInterfaccia;
import interfacceRemote.ServerComunicazioneInterfaccia;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject; 



/**
 * Gestore delle Accept. (Comunicazione RMI).
 *
 */

@SuppressWarnings("unused")
public class AcceptClass extends UnicastRemoteObject implements AcceptInterface{
	
	
	
	/**
	 * Costruttore del gestore delle Accept.
	 * @throws RemoteException	Problema di connessione remota
	 */
	public AcceptClass() throws RemoteException {
		super();
	}


	private static final long serialVersionUID = 1L;

	
	/**
	 * Metodo chiamato da parte client per iniziare la connessione con il server. 
	 * @param client - l'interfaccia di comunicazione remota, il cui oggetto che la implementa ha chiamato questo metodo, con lo scopo di stabilire una connessione con il server.
	 * @return l'interfaccia di comunicazione remota che servirà al client per comunicare con il server e viceversa. 
	 * @see AcceptInterface
	 */
	@Override
	public ServerComunicazioneInterfaccia inizioConnessione(ClientComunicazioneInterfaccia client) throws RemoteException {
		ServerComunicazione classePerClient = new ServerComunicazione(client);
		Thread threadPerClient = new Thread(classePerClient);
		threadPerClient.start();
		classePerClient.setNuovoGiocatore();
		return classePerClient; 
	}
	
	
	
}
	
	
	
	
	
	
	
	
	
	
	


	











