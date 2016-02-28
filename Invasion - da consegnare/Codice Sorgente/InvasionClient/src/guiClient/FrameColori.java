package guiClient;

import interfacciaGUI.ControllerPartita;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;

import classiCondivise.Colori;
import classiCondivise.StringToColori;
import exceptionCondivise.ColoreIrriconoscibileException;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

@SuppressWarnings("serial")
public class FrameColori extends JFrame {

	private JPanel contentPane;
	private int tempoTrascorso = 0;
	private int tempoTotale = 0;
	private int numeroGriglia = 0;
//	private ArrayList<JButton> bottoni;
	private ControllerPartita controller;
	
	public FrameColori(Integer tempoMax, ArrayList<Colori> coloriResidui, ControllerPartita controller) {
//		bottoni = new ArrayList<JButton>(coloriResidui.size());
		this.controller = controller;
		this.tempoTotale = tempoMax;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 605, 235);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{85, 85, 85, 85, 85, 85, 0};
		gbl_contentPane.rowHeights = new int[]{41, 15, 85, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblColori = new JLabel("Colori");
		lblColori.setFont(new Font("Noto Sans UI", Font.BOLD, 16));
		lblColori.setForeground(Color.RED);
		lblColori.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblColori = new GridBagConstraints();
		gbc_lblColori.fill = GridBagConstraints.BOTH;
		gbc_lblColori.insets = new Insets(0, 0, 5, 0);
		gbc_lblColori.gridwidth = 6;
		gbc_lblColori.gridx = 0;
		gbc_lblColori.gridy = 0;
		contentPane.add(lblColori, gbc_lblColori);
		
		final JLabel lblTempoResiduo = new JLabel("Scegli il colore entro: "+tempoTotale+" secondi");
		lblTempoResiduo.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_tempoResiduo = new GridBagConstraints();
		gbc_tempoResiduo.fill = GridBagConstraints.HORIZONTAL;
		gbc_tempoResiduo.insets = new Insets(0, 0, 5, 0);
		gbc_tempoResiduo.gridwidth = 6;
		gbc_tempoResiduo.gridx = 0;
		gbc_tempoResiduo.gridy = 1;
		contentPane.add(lblTempoResiduo, gbc_tempoResiduo);
		
		inserisciPulsanti(coloriResidui);
		Timer timer = null;
		timer = new Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(tempoTrascorso < tempoTotale){
					tempoTrascorso++;
					lblTempoResiduo.setText("Scegli il colore entro: "+(tempoTotale-tempoTrascorso)+" secondi");
				}
				else{
					dispose();
				}				
			}
		});
		
		timer.start();
	}
	
	public void inserisciPulsanti(ArrayList<Colori> colore){
		Iterator<Colori> itColore = colore.iterator();
		Colori coloreEstratto;
		while(itColore.hasNext()){
			coloreEstratto = itColore.next();
			//Se mi viene passato il colore neutro non lo mostro
			if(coloreEstratto.equals(Colori.NEUTRO)){
				continue;
			}
			JButton bottone = new JButton(coloreEstratto.getNome());
			bottone.setActionCommand(coloreEstratto.toString());
			bottone.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						controller.sceltaColoreEffettuata(StringToColori.getColore(e.getActionCommand()));
					} catch (ColoreIrriconoscibileException e1) {
						//Colore non riconosciuto.
						System.err.println("Non ho riconosciuto il colore: "+e.getActionCommand());
						controller.sceltaColoreEffettuata(Colori.NEUTRO);
					}
					dispose();
					
				}
			});
			bottone.setBackground(new Color(coloreEstratto.getR(), coloreEstratto.getG(), coloreEstratto.getB()));
			if(coloreEstratto.getForeground()){
				bottone.setForeground(new Color(255,255,255));
			}
			bottone.setOpaque(true);
			bottone.setBorderPainted(false);
			GridBagConstraints gbc_btnRosso = new GridBagConstraints();
			gbc_btnRosso.fill = GridBagConstraints.BOTH;
			gbc_btnRosso.insets = new Insets(0, 0, 0, 5);
			gbc_btnRosso.gridx = numeroGriglia;
			gbc_btnRosso.gridy = 2;
			contentPane.add(bottone, gbc_btnRosso);
			numeroGriglia++;
		}
		
	}
}
