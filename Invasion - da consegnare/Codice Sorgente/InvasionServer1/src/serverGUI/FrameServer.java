package serverGUI;

import guiCondivise.CreaPopup;
import guiCondivise.FrameClassifica;
import guiCondivise.FrameInfoUtente;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;

import classiCondivise.BeanGiocatore;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class FrameServer extends JFrame {

	private JPanel contentPane;
	private JTextField txtNomegiocatore;
	private ControllerServer controllerServer;
	/**
	 * Create the frame.
	 */
	public FrameServer(ControllerServer controller) {
		this.controllerServer = controller;
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 339, 155);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{175, 146, 0};
		gbl_contentPane.rowHeights = new int[]{25, 25, 25, 25, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JButton btnElencoGiocatori = new JButton("elenco Giocatori");
		btnElencoGiocatori.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new FrameGiocatori(controllerServer);
				
			}
		});
		GridBagConstraints gbc_btnElencoGiocatori = new GridBagConstraints();
		gbc_btnElencoGiocatori.fill = GridBagConstraints.BOTH;
		gbc_btnElencoGiocatori.insets = new Insets(0, 0, 5, 5);
		gbc_btnElencoGiocatori.gridx = 0;
		gbc_btnElencoGiocatori.gridy = 0;
		contentPane.add(btnElencoGiocatori, gbc_btnElencoGiocatori);
		
		JButton btnClassifica = new JButton("classifica");
		btnClassifica.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new FrameClassifica(controllerServer.ottieniClassifica());
				} catch (SQLException e1) {
					new CreaPopup("Problema al server");
				}
			}
		});
		GridBagConstraints gbc_btnClassifica = new GridBagConstraints();
		gbc_btnClassifica.fill = GridBagConstraints.BOTH;
		gbc_btnClassifica.insets = new Insets(0, 0, 5, 5);
		gbc_btnClassifica.gridx = 0;
		gbc_btnClassifica.gridy = 1;
		contentPane.add(btnClassifica, gbc_btnClassifica);
		
		JButton btnInfogiocatore = new JButton("infoGiocatore");
		btnInfogiocatore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nome = txtNomegiocatore.getText();
				BeanGiocatore bean = null;
				try {
					bean = controllerServer.ottieniInfoUtente(nome);
				} catch (SQLException e1) {
					new CreaPopup("Problema al DB");
				}
				if(bean == null){
					new CreaPopup("Impossibile trovare il giocatore desiderato");
					return;
				}
				else{
					new FrameInfoUtente(bean);
				}
				
			}
		});
		GridBagConstraints gbc_btnInfogiocatore = new GridBagConstraints();
		gbc_btnInfogiocatore.fill = GridBagConstraints.BOTH;
		gbc_btnInfogiocatore.insets = new Insets(0, 0, 5, 5);
		gbc_btnInfogiocatore.gridx = 0;
		gbc_btnInfogiocatore.gridy = 2;
		contentPane.add(btnInfogiocatore, gbc_btnInfogiocatore);
		
		txtNomegiocatore = new JTextField();
		txtNomegiocatore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtNomegiocatore.setText("");
			}
		});
		txtNomegiocatore.setText("nomeGiocatore");
		GridBagConstraints gbc_txtNomegiocatore = new GridBagConstraints();
		gbc_txtNomegiocatore.fill = GridBagConstraints.BOTH;
		gbc_txtNomegiocatore.insets = new Insets(0, 0, 5, 0);
		gbc_txtNomegiocatore.gridx = 1;
		gbc_txtNomegiocatore.gridy = 2;
		contentPane.add(txtNomegiocatore, gbc_txtNomegiocatore);
		txtNomegiocatore.setColumns(10);
		
		JButton btnPartitegiocate = new JButton("partiteGiocate");
		btnPartitegiocate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new CreaPopup("Sono stato effettuate: "+controllerServer.totalePartiteGiocate()+" partite");
				} catch (SQLException e1) {
					new CreaPopup("Problema al server");
				}
			}
		});
		GridBagConstraints gbc_btnPartitegiocate = new GridBagConstraints();
		gbc_btnPartitegiocate.fill = GridBagConstraints.BOTH;
		gbc_btnPartitegiocate.insets = new Insets(0, 0, 0, 5);
		gbc_btnPartitegiocate.gridx = 0;
		gbc_btnPartitegiocate.gridy = 3;
		contentPane.add(btnPartitegiocate, gbc_btnPartitegiocate);
		
		JButton btnChiudi = new JButton("Chiudi");
		btnChiudi.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		GridBagConstraints gbc_btnChiudi = new GridBagConstraints();
		gbc_btnChiudi.anchor = GridBagConstraints.SOUTHEAST;
		gbc_btnChiudi.gridx = 1;
		gbc_btnChiudi.gridy = 3;
		contentPane.add(btnChiudi, gbc_btnChiudi);
		
		setTitle("Gestione Invasion");
		setVisible(true);
	}
}
