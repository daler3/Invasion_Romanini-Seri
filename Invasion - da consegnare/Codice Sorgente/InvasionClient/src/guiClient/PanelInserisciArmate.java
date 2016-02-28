package guiClient;

import interfacciaGUI.ControllerPartita;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.Timer;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;

import mappa.Mappa;
import mappa.Territorio;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

@SuppressWarnings("serial")
public class PanelInserisciArmate extends JPanel {
	
	private ArrayList<JLabel> arrayLabelNomeTerritorio = new ArrayList<JLabel>();
	private ArrayList<JTextField> arrayNumeroArmateScelte = new ArrayList<JTextField>();
	private ArrayList<String> mieiTerritori = new ArrayList<String>();
	
	private ControllerPartita mioController;
	
	private int tempoTotale = 0;
	private int tempoTrascorso = 0;
	private int numeroArmate = 0;
	
	private String stringTempoRimanente = "Tempo residuo per il posizionamento: ";
	private JLabel lblTempoRimanente;
	
	
	public PanelInserisciArmate(ControllerPartita controller, Integer numArmate, Integer time, Mappa mappaPartita) {
		setBackground(new Color(204, 153, 102));
		this.tempoTotale = time;
		this.mioController = controller;
		estraiTerritori(mappaPartita);
		this.numeroArmate = numArmate;
		setBounds(0, 0, 402, 686);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{258, 149, 0};
		gbl_panel.rowHeights = new int[]{35, 15, 38, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 0, 0, 0, 38, 25, 25, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl_panel);
		
		JLabel label = new JLabel("Posiziona le tue armate");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.RED);
		label.setFont(new Font("Century Schoolbook L", Font.BOLD, 16));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.BOTH;
		gbc_label.gridwidth = 2;
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 0;
		gbc_label.gridy = 1;
		add(label, gbc_label);
		
		lblTempoRimanente = new JLabel(stringTempoRimanente+tempoTotale);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_1.anchor = GridBagConstraints.NORTH;
		gbc_label_1.gridwidth = 2;
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 2;
		add(lblTempoRimanente, gbc_label_1);
		mostraPosizionaArmate();
		
		JButton btnConfermaScelta = new JButton("Conferma Scelta");
		btnConfermaScelta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Iterator<JTextField> itTxt = arrayNumeroArmateScelte.iterator();
				JTextField txtEstratto;
				String nomeTerritorio;
				int i = 0;
				Integer valore = 0;
				String comando = mioController.visualizzaMieInfo().getNomeUtente()+"@";
				while(itTxt.hasNext()){
					nomeTerritorio = arrayLabelNomeTerritorio.get(i).getText();
					i++;
					txtEstratto = itTxt.next();
					try{
						valore = Integer.parseInt(txtEstratto.getText());
					} catch(NumberFormatException x){ 
						valore = 0;
					}
					if(valore == null){
						return;
					}
					comando = comando+nomeTerritorio+":"+valore.intValue()+";";
				}
				mioController.sceltaArmateInizialeEffettuata(comando);
			}
		});
		
		JButton btnPosizionamentoAuto = new JButton("Posizionamento Automatico");
		GridBagConstraints gbc_btnPosAuto = new GridBagConstraints();
		gbc_btnPosAuto.insets = new Insets(0, 0, 5, 0);
		gbc_btnPosAuto.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPosAuto.anchor = GridBagConstraints.NORTH;
		gbc_btnPosAuto.gridwidth = 2;
		gbc_btnPosAuto.gridx = 0;
		gbc_btnPosAuto.gridy = 21;
		//Action listener per il pulsante di posizionamento automatico delle armate
		btnPosizionamentoAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int armate = numeroArmate;
				Iterator<JTextField> itField = arrayNumeroArmateScelte.iterator();
				while(itField.hasNext()){
					itField.next().setText(""+0);
				}
				Integer armateTemp;
				for(int i = 0; armate > 0; i++){
					if(!(i < mieiTerritori.size())){
						i = 0;
					}
					armateTemp = Integer.parseInt(arrayNumeroArmateScelte.get(i).getText());
					arrayNumeroArmateScelte.get(i).setText(""+(armateTemp+1));
					armate--;
				}
			}
		});
		add(btnPosizionamentoAuto, gbc_btnPosAuto);
		
		GridBagConstraints gbc_btnConferma = new GridBagConstraints();
		gbc_btnConferma.insets = new Insets(0, 0, 5, 0);
		gbc_btnConferma.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConferma.anchor = GridBagConstraints.NORTH;
		gbc_btnConferma.gridwidth = 2;
		gbc_btnConferma.gridx = 0;
		gbc_btnConferma.gridy = 22;
		add(btnConfermaScelta, gbc_btnConferma);
		
		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tempoTotale-tempoTrascorso < 0){
					//Non facciamo niente, sarà il server a gestirsi la cosa.
	            }
	            else{
	            	lblTempoRimanente.setText(stringTempoRimanente+(tempoTotale-tempoTrascorso));
	            	tempoTrascorso++;
	            }
				
			}
		});
		timer.start();
	}
	
	private void estraiTerritori(Mappa mappaPartita) {
		Iterator<Territorio> itTerritori = mappaPartita.getListaTerritori().iterator();
		Territorio territorio;
		while(itTerritori.hasNext()){
			territorio = itTerritori.next();
			if(territorio.getColorePossessore() == mioController.getMioColore()){
				mieiTerritori.add(territorio.getNome());
			}
		}
	}

	public void mostraPosizionaArmate(){
		Iterator<String> itTerritori = mieiTerritori.iterator();
		String stringa = "";
		int i = 0;
		while(itTerritori.hasNext()){
			stringa = itTerritori.next();
			JLabel label = new JLabel(stringa);
			GridBagConstraints gbc_label_1 = new GridBagConstraints();
			gbc_label_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_1.anchor = GridBagConstraints.NORTH;
			gbc_label_1.gridwidth = 2;
			gbc_label_1.insets = new Insets(0, 0, 5, 0);
			gbc_label_1.gridx = 0;
			gbc_label_1.gridy = 3+i;
			add(label, gbc_label_1);
			
			JTextField textField = new JTextField(""+0);
			GridBagConstraints gbc_text = new GridBagConstraints();
			gbc_text.fill = GridBagConstraints.HORIZONTAL;
			gbc_text.anchor = GridBagConstraints.NORTH;
			gbc_text.gridwidth = 2;
			gbc_text.insets = new Insets(0, 0, 5, 0);
			gbc_text.gridx = 1;
			gbc_text.gridy = 3+i;
			add(textField, gbc_text);
			
			this.arrayLabelNomeTerritorio.add(label);
			this.arrayNumeroArmateScelte.add(textField);
			i++;
		}
		
		
		
	}
}
