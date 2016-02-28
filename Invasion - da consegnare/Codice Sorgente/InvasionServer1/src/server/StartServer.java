package server;

import serverGUI.FrameServer;

/**
 * Classe di avvio del server.
 *
 */
public class StartServer {
	public static void main(String[] args) {
		
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               	//Creiamo l'istanza del server
            	Server mioServer = Server.getInstance();
            	//Avvio la comnunicazione del server
            	mioServer.accendiServer();
            	//Creiamo l'interfaccia grafica
            	new FrameServer(mioServer);
            }
        });
	}
}
