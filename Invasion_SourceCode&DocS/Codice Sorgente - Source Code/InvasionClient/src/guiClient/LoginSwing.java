package guiClient;

import interfacciaGUI.ControllerUtente;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class LoginSwing extends JFrame {

	private static LoginSwing INSTANCE = null; 
	private JPanel contentPane;
	private JPanel pannello;
	private PanelLogin mioLogin;
	private ControllerUtente controllerUtente = null;

	private LoginSwing() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	}
	
	public void setController(ControllerUtente controllerUtente){
		this.controllerUtente = controllerUtente;
	}
	
	/**
	 * Metodo per crea un'istanza della classe Server, se essa ancora non esiste.
	 */
	private synchronized static void createInstance() {
		if(INSTANCE==null)
			INSTANCE = new LoginSwing();
	}
	
	/**
	 * Metodo per ottenere un riferimento alla classe Server
	 * @return l'unica istanza della classe Server
	 */
	public static LoginSwing getInstance(){
		if(INSTANCE == null)
			createInstance();
		return INSTANCE; 
	}
	
	public void createAndShowGUI() {
		mioLogin = new PanelLogin(this.controllerUtente);
		getContentPane().add(mioLogin);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
    }
	
	public void mostraPanel(JPanel panello){
		this.pannello = panello;
		getContentPane().add(this.pannello);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

}

