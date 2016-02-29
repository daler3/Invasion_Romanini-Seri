package interfacceRemote;

import java.rmi.*;

/**
 * 
 * Interfaccia remota del gestore delle accept. (comunicazione RMI).
 *
 */
public interface AcceptInterface extends Remote {
	//public ServerComunicazioneInterfaccia login (ClientComunicazioneInterfaccia client2, String name, String password) throws RemoteException;
	
	/**
	 * Metodo chiamato da parte client per iniziare la connessione con il server. 
	 * @param client - l'interfaccia di comunicazione remota, il cui oggetto che la implementa ha chiamato questo metodo, con lo scopo di stabilire una connessione con il server.
	 * @return l'interfaccia di comunicazione remota che servirà al client per comunicare con il server e viceversa. 
	 */
	public ServerComunicazioneInterfaccia inizioConnessione (ClientComunicazioneInterfaccia client) throws RemoteException;

}
