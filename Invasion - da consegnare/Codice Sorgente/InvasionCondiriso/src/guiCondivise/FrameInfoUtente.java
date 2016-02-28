package guiCondivise;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import classiCondivise.BeanGiocatore;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

@SuppressWarnings("serial")
public class FrameInfoUtente extends JFrame {

	private JPanel contentPane;
	private BeanGiocatore giocatore;
	
	public FrameInfoUtente(BeanGiocatore giocatore) {
		this.giocatore = giocatore;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 255);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{205, 67, 169, 0};
		gbl_contentPane.rowHeights = new int[]{46, 45, 15, 15, 15, 15, 15, 15, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNomeUtente = new JLabel(giocatore.getNomeUtente());
		lblNomeUtente.setFont(new Font("Century Schoolbook L", Font.BOLD, 16));
		lblNomeUtente.setForeground(new Color(255, 0, 0));
		lblNomeUtente.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblNomeUtente = new GridBagConstraints();
		gbc_lblNomeUtente.fill = GridBagConstraints.VERTICAL;
		gbc_lblNomeUtente.insets = new Insets(0, 0, 5, 0);
		gbc_lblNomeUtente.gridwidth = 3;
		gbc_lblNomeUtente.gridx = 0;
		gbc_lblNomeUtente.gridy = 0;
		contentPane.add(lblNomeUtente, gbc_lblNomeUtente);
		
		JLabel label = new JLabel("Pos. Classifica");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.NORTH;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 2;
		contentPane.add(label, gbc_label);
		
		JLabel label_1 = new JLabel(""+giocatore.getPosizioneClassifica());
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.NORTH;
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 2;
		gbc_label_1.gridy = 2;
		contentPane.add(label_1, gbc_label_1);
		
		JLabel label_2 = new JLabel("Numero Partite");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.NORTH;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 3;
		contentPane.add(label_2, gbc_label_2);
		
		JLabel label_3 = new JLabel(""+giocatore.getNumeroPartitePartecipate());
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.NORTH;
		gbc_label_3.insets = new Insets(0, 0, 5, 0);
		gbc_label_3.gridx = 2;
		gbc_label_3.gridy = 3;
		contentPane.add(label_3, gbc_label_3);
		
		JLabel label_4 = new JLabel("Punteggio");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.anchor = GridBagConstraints.NORTH;
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 4;
		contentPane.add(label_4, gbc_label_4);
		
		JLabel label_5 = new JLabel(""+giocatore.getPunteggio());
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.anchor = GridBagConstraints.NORTH;
		gbc_label_5.insets = new Insets(0, 0, 5, 0);
		gbc_label_5.gridx = 2;
		gbc_label_5.gridy = 4;
		contentPane.add(label_5, gbc_label_5);
		
		JLabel label_6 = new JLabel("Partite vinte");
		GridBagConstraints gbc_label_6 = new GridBagConstraints();
		gbc_label_6.anchor = GridBagConstraints.NORTH;
		gbc_label_6.insets = new Insets(0, 0, 5, 5);
		gbc_label_6.gridx = 0;
		gbc_label_6.gridy = 5;
		contentPane.add(label_6, gbc_label_6);
		
		JLabel label_7 = new JLabel(""+(giocatore.getPercentualePartiteVinte()*100)+"%");
		GridBagConstraints gbc_label_7 = new GridBagConstraints();
		gbc_label_7.anchor = GridBagConstraints.NORTH;
		gbc_label_7.insets = new Insets(0, 0, 5, 0);
		gbc_label_7.gridx = 2;
		gbc_label_7.gridy = 5;
		contentPane.add(label_7, gbc_label_7);
		
		JLabel label_9 = new JLabel("Partite secondo");
		GridBagConstraints gbc_label_9 = new GridBagConstraints();
		gbc_label_9.anchor = GridBagConstraints.NORTH;
		gbc_label_9.insets = new Insets(0, 0, 5, 5);
		gbc_label_9.gridx = 0;
		gbc_label_9.gridy = 6;
		contentPane.add(label_9, gbc_label_9);
		
		JLabel label_8 = new JLabel(""+(giocatore.getPercentualePartiteSecondo()*100)+"%");
		GridBagConstraints gbc_label_8 = new GridBagConstraints();
		gbc_label_8.anchor = GridBagConstraints.NORTH;
		gbc_label_8.insets = new Insets(0, 0, 5, 0);
		gbc_label_8.gridx = 2;
		gbc_label_8.gridy = 6;
		contentPane.add(label_8, gbc_label_8);
		
		JLabel label_10 = new JLabel("Partite ritirato");
		GridBagConstraints gbc_label_10 = new GridBagConstraints();
		gbc_label_10.anchor = GridBagConstraints.NORTH;
		gbc_label_10.insets = new Insets(0, 0, 0, 5);
		gbc_label_10.gridx = 0;
		gbc_label_10.gridy = 7;
		contentPane.add(label_10, gbc_label_10);
		
		JLabel label_11 = new JLabel(""+(giocatore.getPercentualePartiteAbbandonate()*100)+"%");
		GridBagConstraints gbc_label_11 = new GridBagConstraints();
		gbc_label_11.anchor = GridBagConstraints.NORTH;
		gbc_label_11.gridx = 2;
		gbc_label_11.gridy = 7;
		contentPane.add(label_11, gbc_label_11);
		
		setTitle("Statistiche "+this.giocatore.getNomeUtente());
		setSize(800, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
		
	}
}
