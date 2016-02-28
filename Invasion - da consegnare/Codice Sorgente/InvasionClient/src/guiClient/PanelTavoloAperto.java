package guiClient;

import interfacciaGUI.ControllerLimbo;
import interfacciaGUI.ControllerPartita;
import interfacciaGUI.ControllerTavolo;
import interfacciaGUI.ControllerUtente;
import interfacciaGUI.ViewTavolo;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import classiCondivise.BeanGiocatore;
import classiCondivise.BeanTavolo;
import classiCondivise.Classifica;
import classiCondivise.CreareMessaggio;
import exceptionCondivise.GiocatoreNonTrovatoException;
import exceptionCondivise.ProblemaAvvioTavoloException;
import guiClient.immagini.IconLoader;
import guiCondivise.FrameInfoUtente;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressWarnings("serial")
public class PanelTavoloAperto extends JPanel implements ViewTavolo {

	final static int maxGiocatori = 6;
	final JPanel p = this;
	private int idTavolo;
	int numRiga = 0;
	int numColonna = 0;
	
	ControllerTavolo controllerTavolo;
	ArrayList<String> giocatori;
	JPanel panelSalaGiochi;
	JPanel pannelloSuperiore;
	JPanel panelTavoli;
	ArrayList<JButton> bottoniGiocatori = new ArrayList<JButton>(maxGiocatori);
	BeanGiocatore io;
	
	public PanelTavoloAperto(ControllerTavolo controllerTavolo, ArrayList<String> giocatori, int idTavolo) {
		this.controllerTavolo = controllerTavolo;
		this.giocatori = giocatori;
		this.idTavolo = idTavolo;
		//Interfaccia generale
		setMinimumSize(new Dimension(800, 800));
		setPreferredSize(new Dimension(800, 800));
		GridBagLayout grigliaEsterna = new GridBagLayout();
		grigliaEsterna.columnWidths = new int[]{1315};
		grigliaEsterna.rowHeights = new int[]{232, 390};
		grigliaEsterna.columnWeights = new double[]{1.0};
		grigliaEsterna.rowWeights = new double[]{1.0, 2.0};
		setLayout(grigliaEsterna);
		//Pannello superiore
		pannelloSuperiore = new JPanel();
		pannelloSuperiore.setBackground(Color.WHITE);
		GridBagConstraints gbc_pannelloSuperiore = new GridBagConstraints();
		gbc_pannelloSuperiore.fill = GridBagConstraints.BOTH;
		gbc_pannelloSuperiore.insets = new Insets(0, 0, 5, 0);
		gbc_pannelloSuperiore.gridx = 0;
		gbc_pannelloSuperiore.gridy = 0;
		add(pannelloSuperiore, gbc_pannelloSuperiore);
		pannelloSuperiore.setLayout(null);
		//Pannellino immagine
		JPanel panelImmagine = new JPanel();
		panelImmagine.setBounds(12, 12, 208, 208);
		pannelloSuperiore.add(panelImmagine);
		panelImmagine.setLayout(null);
		
		JLabel lblUsername = new JLabel(controllerTavolo.visualizzaMieInfo().getNomeUtente());
		lblUsername.setForeground(UIManager.getColor("OptionPane.errorDialog.border.background"));
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(0, 12, 208, 15);
		panelImmagine.add(lblUsername);
		
		JLabel lblFoto = new JLabel(IconLoader.imageIcon("user2.png"));
		lblFoto.setBounds(10, 39, 186, 169);
		panelImmagine.add(lblFoto);
		
		JButton btnStatistiche = new JButton("Classifica");
		btnStatistiche.addActionListener(new FrameClassificaListener());
		btnStatistiche.setBounds(580, 12, 208, 25);
		pannelloSuperiore.add(btnStatistiche);
		
		JButton btnCambiopassword = new JButton("Cambio Password");
		btnCambiopassword.setBounds(580, 61, 208, 25);
		pannelloSuperiore.add(btnCambiopassword);
		
		JLabel label = new JLabel("Numero Partite");
		label.setBounds(238, 39, 146, 15);
		pannelloSuperiore.add(label);
		
		JLabel label_1 = new JLabel("Punteggio");
		label_1.setBounds(238, 66, 146, 15);
		pannelloSuperiore.add(label_1);
		
		JLabel label_2 = new JLabel("Pos. Classifica");
		label_2.setBounds(238, 12, 146, 15);
		pannelloSuperiore.add(label_2);
		
		JLabel label_3 = new JLabel("Partite vinte");
		label_3.setBounds(238, 98, 146, 15);
		pannelloSuperiore.add(label_3);
		
		JLabel label_4 = new JLabel("Partite secondo");
		label_4.setBounds(238, 125, 146, 15);
		pannelloSuperiore.add(label_4);
		
		JLabel label_5 = new JLabel("Partite ritirato");
		label_5.setBounds(238, 152, 146, 15);
		pannelloSuperiore.add(label_5);
		
		this.io = controllerTavolo.visualizzaMieInfo();
		
		JLabel lblA = new JLabel(""+io.getPosizioneClassifica());
		lblA.setBounds(451, 12, 70, 15);
		pannelloSuperiore.add(lblA);
		
		JLabel label_6 = new JLabel(""+io.getNumeroPartitePartecipate());
		label_6.setBounds(451, 39, 70, 15);
		pannelloSuperiore.add(label_6);
		
		JLabel label_7 = new JLabel(""+io.getPunteggio());
		label_7.setBounds(451, 66, 70, 15);
		pannelloSuperiore.add(label_7);
		
		
		JLabel label_8 = new JLabel(""+(CreareMessaggio.floatPulito(io.getPercentualePartiteVinte(), 4)*100)+"%");
		label_8.setBounds(451, 98, 70, 15);
		pannelloSuperiore.add(label_8);
		
		JLabel label_9 = new JLabel(""+(CreareMessaggio.floatPulito(io.getPercentualePartiteSecondo(), 4)*100)+"%");
		label_9.setBounds(451, 125, 70, 15);
		pannelloSuperiore.add(label_9);
		
		JLabel label_10 = new JLabel(""+(CreareMessaggio.floatPulito(io.getPercentualePartiteAbbandonate(), 4)*100)+"%");
		label_10.setBounds(451, 152, 70, 15);
		pannelloSuperiore.add(label_10);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setBounds(580, 147, 208, 25);
		btnLogout.addActionListener(new LogoutListener());
		pannelloSuperiore.add(btnLogout);
		
		JButton btnAvviaPartita = new JButton("Avvia Partita");
		btnAvviaPartita.addActionListener(new AvviaPartitaListener());
		btnAvviaPartita.setBounds(238, 221, 249, 25);
		pannelloSuperiore.add(btnAvviaPartita);
		
		JButton btnAbbandonaTavolo = new JButton("Abbandona Tavolo");
		btnAbbandonaTavolo.addActionListener(new AbbandonaTavoloListener());
		btnAbbandonaTavolo.setBounds(539, 221, 249, 25);
		pannelloSuperiore.add(btnAbbandonaTavolo);
		//Pannello inferiore
		panelSalaGiochi = new JPanel();
		panelSalaGiochi.setBackground(new Color(51, 51, 51));
		GridBagConstraints gbc_panelTavoli_1 = new GridBagConstraints();
		gbc_panelTavoli_1.fill = GridBagConstraints.BOTH;
		gbc_panelTavoli_1.gridx = 0;
		gbc_panelTavoli_1.gridy = 1;
		add(panelSalaGiochi, gbc_panelTavoli_1);
		GridBagLayout gbl_panelSalaGiochi = new GridBagLayout();
		gbl_panelSalaGiochi.columnWidths = new int[]{1339, 0};
		gbl_panelSalaGiochi.rowHeights = new int[]{64, 314, 0};
		gbl_panelSalaGiochi.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelSalaGiochi.rowWeights = new double[]{1.0, 5.0, Double.MIN_VALUE};
		panelSalaGiochi.setLayout(gbl_panelSalaGiochi);
		
		JLabel lblSalaGiochi = new JLabel("Tavolo numero "+this.idTavolo);
		lblSalaGiochi.setBackground(new Color(102, 51, 0));
		lblSalaGiochi.setForeground(new Color(255, 255, 255));
		lblSalaGiochi.setFont(new Font("DejaVu Serif Condensed", Font.BOLD | Font.ITALIC, 16));
		lblSalaGiochi.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblSalaGiochi = new GridBagConstraints();
		gbc_lblSalaGiochi.fill = GridBagConstraints.BOTH;
		gbc_lblSalaGiochi.insets = new Insets(0, 0, 5, 0);
		gbc_lblSalaGiochi.gridx = 0;
		gbc_lblSalaGiochi.gridy = 0;
		panelSalaGiochi.add(lblSalaGiochi, gbc_lblSalaGiochi);
			//Pannello dei tavoli
			panelTavoli = new JPanel();
			panelTavoli.setBackground(new Color(51, 153, 0));
				//Lo aggiungiamo alla sala giochi
				GridBagConstraints gbc_panelTavoli_2 = new GridBagConstraints();
				gbc_panelTavoli_2.fill = GridBagConstraints.BOTH;
				gbc_panelTavoli_2.gridx = 0;
				gbc_panelTavoli_2.gridy = 1;
				panelSalaGiochi.add(panelTavoli, gbc_panelTavoli_2);
			//Impostiamo lo stile del pannello
			GridBagLayout gbl_panel_2 = new GridBagLayout();
			gbl_panel_2.columnWidths = new int[] {5, 5, 5, 5};	//Dimensione minima larghezza colonne
			gbl_panel_2.rowHeights = new int[] {43, 5, 43, 5};	//Dimensione minima altezza righe
			gbl_panel_2.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0};
			gbl_panel_2.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0};
			panelTavoli.setLayout(gbl_panel_2);
			
			
		//Componenti del tavolo
		Iterator<String> itgiocatori = this.giocatori.iterator();
		while(itgiocatori.hasNext()){
			String giocatore = itgiocatori.next();
			aggiungiGiocatore(giocatore);
		}
		LoginSwing.getInstance().setMinimumSize(new Dimension(850, 750));
		LoginSwing.getInstance().repaint();
	}
	
	public void aggiungiGiocatore(String giocatore){
		JButton btnGiocatore = new JButton(giocatore, IconLoader.imageIcon("persona.png"));
		btnGiocatore.setForeground(new Color(102, 204, 51));
		btnGiocatore.setBackground(new Color(255, 255, 255));
		btnGiocatore.setActionCommand(giocatore);
		btnGiocatore.addActionListener(new chiediInfoListener());
		GridBagConstraints gbc_btnGiocatore = new GridBagConstraints();
		gbc_btnGiocatore.fill = GridBagConstraints.BOTH;
		gbc_btnGiocatore.gridwidth = 2;
		gbc_btnGiocatore.insets = new Insets(0, 0, 5, 5);
		gbc_btnGiocatore.gridx = numColonna;
		gbc_btnGiocatore.gridy = numRiga+1;
		panelTavoli.add(btnGiocatore, gbc_btnGiocatore);
		//Aggiunge il bottone all'arraylist
		bottoniGiocatori.add(btnGiocatore);
		//Ci spostiamo alla prossima casella per disegnare il bottone
		prossimaCasella();
				
	}
	private void prossimaCasella(){
		if(numRiga==2){
			numRiga = 0;
			numColonna+=2;
		}
		else{
			numRiga++;
		}
	}
	
	/**
	 * Rimuove tutti i tavoli.
	 */
	private void resetPosizione(){
		this.numColonna = 0;
		this.numRiga = 0;
		Iterator<JButton> itBottoni = this.bottoniGiocatori.iterator();
		JButton bottoneTemp;
		while(itBottoni.hasNext()){
			bottoneTemp = itBottoni.next();
			bottoneTemp.setVisible(false);
			bottoneTemp.setEnabled(false);
		}
		Container c = p.getParent();
		c.revalidate();
		c.invalidate();
		c.repaint();
	}
	

	
	public class AvviaPartitaListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			ControllerPartita controllerPartita = null;
			try {
				controllerPartita = controllerTavolo.avviaTavolo(idTavolo);
			} catch (ProblemaAvvioTavoloException e) {
				new CreaPopup("Problema con l'avvio del tavolo");
			}
			if(controllerPartita != null){
				Container c = p.getParent();
				PanelPartita mioPanel = null;
				if (c != null) {
					new CreaPopup("Inizio partita - Attendi che gli altri giocatori scelgano il colore", 4);
					c.remove(p);
					mioPanel = new PanelPartita(controllerPartita);
					controllerPartita.setVistaPartita(mioPanel);
		            c.add(mioPanel);
		            c.revalidate();
		            c.repaint();
				}
			}
			else{
				new CreaPopup("Attendi che siano pronti anche gli altri giocatori", 4);
			}
		}	
	}
	
	public class AbbandonaTavoloListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//Abbandona il tavolo
			ControllerLimbo controllerLimbo = controllerTavolo.abbandonaTavolo();
			//Chiedo le informazioni del Limbo
			ArrayList<BeanTavolo> tavoli = controllerLimbo.aggiornaTavoli();
			//Avvio il pannello
			Container c = p.getParent();
			PanelLimbo mioPanel = null;
			if (c != null) {
				c.remove(p);
				mioPanel = new PanelLimbo(controllerLimbo, tavoli);
				//Preparo la view
				controllerLimbo.setVistaLimbo(mioPanel);
	            c.add(mioPanel);
	            c.revalidate();
			}
		}	
	}
	
	public class FrameClassificaListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Classifica classifica = controllerTavolo.visualizzaClassifica();
			//Creo il frame
			new FrameClassifica(classifica);
	   	}	
	}
	
	public class LogoutListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(controllerTavolo.logout()){
				LoginSwing.getInstance().dispose();
			}
			else{
				new CreaPopup("Impossibile fare il logout! Aiuto liberatemi!");
			}
		}	
	}
	
	public class CambioPasswordListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFrame frameCambioPass = new FrameCambioPsw(controllerTavolo);
			frameCambioPass.setTitle("Cambio Password");
			frameCambioPass.setSize(800, 800);
			frameCambioPass.setLocationRelativeTo(null);
			frameCambioPass.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frameCambioPass.pack();
			frameCambioPass.setVisible(true);
		}
	}

	@Override
	public void aggiornaUtenti(ArrayList<String> utenti) {
		resetPosizione();
		Iterator<String> giocatori = utenti.iterator();
		String giocatoreTemp;
		while(giocatori.hasNext()){
			giocatoreTemp = giocatori.next();
			aggiungiGiocatore(giocatoreTemp);
		}
		Container c = p.getParent();
		c.revalidate();
	}

	@Override
	public void avviaPartita(ControllerPartita controllerPartita) {
		//Fa la stessa cosa di premere su Avvia partita per il Creatore
		Container c = p.getParent();
		PanelPartita mioPanel = null;
		if (c != null) {
			new CreaPopup("Attendi che gli altri giocatori scelgano il colore", 3);
			c.remove(p);
			mioPanel = new PanelPartita(controllerPartita);
			controllerPartita.setVistaPartita(mioPanel);
            c.add(mioPanel);
            c.revalidate();
            c.repaint();
		}
	}
	
	public class chiediInfoListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			BeanGiocatore giocatore = null;
			try {
				giocatore = controllerTavolo.getInfoUtente(arg0.getActionCommand());
			} catch (GiocatoreNonTrovatoException e) {
				new CreaPopup("Giocatore non trovato");
			}
			if(giocatore != null){
				new FrameInfoUtente(giocatore);
			}
			else{
				new CreaPopup("Impossibile trovare informazioni sul giocatore richiesto");
			}
		}
	}
	
	@Override
	public void chiusuraServer(ControllerUtente controller) {
		Container c = p.getParent();
		if(c != null){
			c.remove(p);
			LoginSwing simpleapp = LoginSwing.getInstance();
			simpleapp.setController(controller);
			simpleapp.mostraPanel(new PanelLogin(controller));
		}
	}
	
}
