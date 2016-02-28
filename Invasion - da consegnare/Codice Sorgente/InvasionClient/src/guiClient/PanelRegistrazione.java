package guiClient;

import interfacciaGUI.ControllerLimbo;
import interfacciaGUI.ControllerUtente;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import comunicazione.Client2Server;
import rmi.ClientComunicazione;
import socket.InterpreteClient;
import classiCondivise.BeanTavolo;
import client.Ambasciatore;
import client.SetCommunicationStrategy;
import client.StrategiaComunicazione;

@SuppressWarnings("serial")
public class PanelRegistrazione extends JPanel {
	final JPanel p = this;
	final static int port = 4445;
	final static String host = "127.0.0.1";
	//INFO
	ControllerUtente controllerUtente;
	
	//La griglia
	private GridBagLayout gridBagLayout;
	private GridBagConstraints gbc_lblTitolo;
	private GridBagConstraints gbc_lblUser;
	private GridBagConstraints gbc_usernameField;
	private GridBagConstraints gbc_lblPassword;
	private GridBagConstraints gbc_passwordField;
	private GridBagConstraints gbc_lblPassword_2;
	private GridBagConstraints gbc_passwordField_2;
	private GridBagConstraints gbc_lblProtocollo;
	private GridBagConstraints gbc_rdbtnSocket;
	private GridBagConstraints gbc_rdbtnRmi;
	private GridBagConstraints gbc_btnRegistrazione;
	private GridBagConstraints gbc_btnLogin;
	//Componenti
	private JLabel lblTitolo;
	private JLabel lblUser;
	private JLabel lblPassword;
	private JLabel lblPassword2;
	private JLabel lblProtocollo;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_2;
	private ButtonGroup mioGruppo;
	private JToggleButton rdbtnSocket;
	private JToggleButton rdbtnRmi;
	private JButton btnRegistrazione;
	private JButton btnLogin;
	
	
	
	public PanelRegistrazione(ControllerUtente login) {
		this.controllerUtente=login;
		gridBagLayout = new GridBagLayout();
		//Il gruppo dei pulsanti radiobox
		mioGruppo = new ButtonGroup();
		//Impostiamo la dimensione delle colonne-righe e il loro ridimensionamento dinamico
		gridBagLayout.columnWidths = new int[]{20, 60, 60, 60, 20};
		gridBagLayout.rowHeights = new int[]{19, 15, 19, 15, 19, 15, 19, 15, 19, 15, 19, 15, 19};
		gridBagLayout.columnWeights = new double[]{0.0, 0.33, 1.0, 0.33, 0.0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.3, 1.5, 0.0};
		//gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		//Iniziamo ad inserire i componenti
		lblTitolo = new JLabel("Registrati per giocare ad Invasion!");
		lblTitolo.setForeground(Color.RED);
		lblTitolo.setFont(lblTitolo.getFont().deriveFont(lblTitolo.getFont().getStyle() | Font.BOLD, lblTitolo.getFont().getSize() + 4f));
		lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
		gbc_lblTitolo = new GridBagConstraints();
		gbc_lblTitolo.anchor = GridBagConstraints.NORTH;
		gbc_lblTitolo.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTitolo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitolo.gridwidth = 3;
		gbc_lblTitolo.gridx = 1;
		gbc_lblTitolo.gridy = 0;
		add(lblTitolo, gbc_lblTitolo);
		
		lblUser = new JLabel("Username");
		lblUser.setFont(new Font("Dialog", Font.BOLD, 12));
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		gbc_lblUser = new GridBagConstraints();
		gbc_lblUser.fill = GridBagConstraints.BOTH;
		gbc_lblUser.insets = new Insets(0, 0, 5, 5);
		gbc_lblUser.gridx = 2;
		gbc_lblUser.gridy = 1;
		add(lblUser, gbc_lblUser);
		
		usernameField = new JTextField();
		usernameField.setToolTipText("Inserisci il tuo nome");
		usernameField.setHorizontalAlignment(SwingConstants.CENTER);
		usernameField.setColumns(10);
		gbc_usernameField = new GridBagConstraints();
		gbc_usernameField.fill = GridBagConstraints.BOTH;
		gbc_usernameField.insets = new Insets(0, 0, 5, 5);
		gbc_usernameField.gridx = 2;
		gbc_usernameField.gridy = 2;
		add(usernameField, gbc_usernameField);
		usernameField.setColumns(10);
		
		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 12));
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.fill = GridBagConstraints.BOTH;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 2;
		gbc_lblPassword.gridy = 3;
		add(lblPassword, gbc_lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setToolTipText("Inserisci la tua password");
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.fill = GridBagConstraints.BOTH;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 4;
		add(passwordField, gbc_passwordField);
		
		lblPassword2 = new JLabel("Ripeti password");
		lblPassword2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword2.setFont(new Font("Dialog", Font.BOLD, 12));
		gbc_lblPassword_2 = new GridBagConstraints();
		gbc_lblPassword_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword_2.gridx = 2;
		gbc_lblPassword_2.gridy = 5;
		add(lblPassword2, gbc_lblPassword_2);
		
		passwordField_2 = new JPasswordField();
		passwordField_2.setToolTipText("Inserisci nuovamente la tua password");
		passwordField_2.setHorizontalAlignment(SwingConstants.CENTER);
		gbc_passwordField_2 = new GridBagConstraints();
		gbc_passwordField_2.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_2.fill = GridBagConstraints.BOTH;
		gbc_passwordField_2.gridx = 2;
		gbc_passwordField_2.gridy = 6;
		add(passwordField_2, gbc_passwordField_2);
		
		lblProtocollo = new JLabel("Protocollo di connessione");
		lblProtocollo.setFont(new Font("Dialog", Font.BOLD, 12));
		lblProtocollo.setHorizontalAlignment(SwingConstants.CENTER);
		gbc_lblProtocollo = new GridBagConstraints();
		gbc_lblProtocollo.fill = GridBagConstraints.BOTH;
		gbc_lblProtocollo.insets = new Insets(0, 0, 5, 5);
		gbc_lblProtocollo.gridx = 2;
		gbc_lblProtocollo.gridy = 8;
		add(lblProtocollo, gbc_lblProtocollo);
		rdbtnSocket = new JToggleButton("Socket");
		rdbtnSocket.setBackground(Color.LIGHT_GRAY);
		rdbtnSocket.setToolTipText("Comunicazione tramite Socket");
		rdbtnSocket.setSelected(true);
		rdbtnSocket.setHorizontalAlignment(SwingConstants.CENTER);
		mioGruppo.add(rdbtnSocket);
		gbc_rdbtnSocket = new GridBagConstraints();
		gbc_rdbtnSocket.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnSocket.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnSocket.gridx = 1;
		gbc_rdbtnSocket.gridy = 9;
		add(rdbtnSocket, gbc_rdbtnSocket);
		
		btnRegistrazione = new JButton("Passa a Login");
		btnRegistrazione.addActionListener(new PassaLoginListener());
		
		rdbtnRmi = new JToggleButton("RMI");
		rdbtnRmi.setBackground(Color.LIGHT_GRAY);
		rdbtnRmi.setToolTipText("Comunicazione tramite RMI");
		rdbtnRmi.setHorizontalAlignment(SwingConstants.CENTER);
		mioGruppo.add(rdbtnRmi);
		gbc_rdbtnRmi = new GridBagConstraints();
		gbc_rdbtnRmi.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnRmi.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnRmi.gridx = 3;
		gbc_rdbtnRmi.gridy = 9;
		add(rdbtnRmi, gbc_rdbtnRmi);
		gbc_btnRegistrazione = new GridBagConstraints();
		gbc_btnRegistrazione.anchor = GridBagConstraints.NORTH;
		gbc_btnRegistrazione.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRegistrazione.insets = new Insets(0, 0, 5, 5);
		gbc_btnRegistrazione.gridx = 1;
		gbc_btnRegistrazione.gridy = 11;
		add(btnRegistrazione, gbc_btnRegistrazione);
		
		btnLogin = new JButton("Registrami!");
		btnLogin.addActionListener(new EffettuaRegistrazione());
		gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogin.insets = new Insets(0, 0, 5, 5);
		gbc_btnLogin.anchor = GridBagConstraints.NORTH;
		gbc_btnLogin.gridx = 3;
		gbc_btnLogin.gridy = 11;
		add(btnLogin, gbc_btnLogin);
	}
	
	public class PassaLoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Container c = p.getParent();
			PanelLogin mioPanel = null;
			if (c != null) {
				c.remove(p);
				mioPanel = new PanelLogin(controllerUtente);
	            mioPanel.setUser(usernameField.getText());
	            mioPanel.setPassword(new String(passwordField.getPassword()));
	            mioPanel.setToggle(getToggle());
	            c.add(mioPanel);
	            c.revalidate();
			}
		}	
	}
	public class EffettuaRegistrazione implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String nome = usernameField.getText();
			String password1 = new String(passwordField.getPassword());
			String password2 = new String(passwordField_2.getPassword());
			ControllerLimbo controller;
			if(nome.isEmpty()){
				lblTitolo.setText(lblTitolo.getText()+" - Inserire un nome");
			}
			else if(password1.isEmpty()){
				lblTitolo.setText("Inserire una password");
			}
			else if(!passwordCorrispondono(password1, password2)){
				lblTitolo.setText("Password non corrispondenti");
			}
			else{
				//Superati i controlli preliminari
				SetCommunicationStrategy istanziaComunicazione = new SetCommunicationStrategy();
				if(rdbtnRmi.isSelected()){
					istanziaComunicazione.setComunicazione(StrategiaComunicazione.RMI);
				}
				else if(rdbtnSocket.isSelected()){
					istanziaComunicazione.setComunicazione(StrategiaComunicazione.SOCKET);
				}
				controller = controllerUtente.effettuaRegistrazione(nome, password1);
				if(controller != null){
					new CreaPopup("Registrazione effettuata con successo");
					ArrayList<BeanTavolo> tavoli = controller.aggiornaTavoli();
					Container c = p.getParent();
					PanelLimbo mioPanel = null;
					if (c != null) {
						c.remove(p);
						mioPanel = new PanelLimbo(controller, tavoli);
			            c.add(mioPanel);
			            c.revalidate();
					}
				}
				else{
					new CreaPopup("Utente già esistente");
				}
			}
		}
		
		private boolean passwordCorrispondono(String psw1, String psw2){
			return psw1.equals(psw2);
		}
	}
	public void setUser(String username){
		usernameField.setText(username);
	}
	
	public void setPassword(String password){
		passwordField.setText(password);
	}
	public void setToggle(Integer numero){
		if(numero.equals(null))
			return;
		if(numero.equals(1)){
			rdbtnSocket.setSelected(true);
		}
		if(numero.equals(2)){
			rdbtnRmi.setSelected(true);
		}
	}
	public Integer getToggle(){
		if(rdbtnSocket.isSelected())
			return 1;
		if(rdbtnRmi.isSelected())
			return 2;
		return null;
	}
}
