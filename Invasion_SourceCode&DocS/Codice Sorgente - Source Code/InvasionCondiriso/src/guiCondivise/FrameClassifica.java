package guiCondivise;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import classiCondivise.Classifica;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

@SuppressWarnings("serial")
public class FrameClassifica extends JFrame {

	private JPanel contentPane;
	Integer primiTot = 15;
	Classifica miaClassifica;
	JPanel panel;

	public FrameClassifica(Classifica classifica) {
		this.miaClassifica = classifica;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 831, 526);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{819, 0};
		gbl_contentPane.rowHeights = new int[]{24, 396, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblTitolo = new JLabel("Classifica Generale");
		lblTitolo.setForeground(new Color(0, 153, 0));
		lblTitolo.setFont(new Font("Droid Sans Fallback", Font.BOLD | Font.ITALIC, 18));
		lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblTitolo = new GridBagConstraints();
		gbc_lblTitolo.anchor = GridBagConstraints.NORTH;
		gbc_lblTitolo.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTitolo.insets = new Insets(0, 0, 5, 0);
		gbc_lblTitolo.gridx = 0;
		gbc_lblTitolo.gridy = 0;
		contentPane.add(lblTitolo, gbc_lblTitolo);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{120, 50, 120, 50, 120, 0};
		gbl_panel.rowHeights = new int[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);
		
		JLabel lblPosizione = new JLabel("Posizione Classifica");
		lblPosizione.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblPosizione = new GridBagConstraints();
		gbc_lblPosizione.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblPosizione.anchor = GridBagConstraints.NORTH;
		gbc_lblPosizione.insets = new Insets(0, 0, 5, 5);
		gbc_lblPosizione.gridx = 0;
		gbc_lblPosizione.gridy = 0;
		panel.add(lblPosizione, gbc_lblPosizione);
		
		JLabel label = new JLabel("Nome utente");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.NORTH;
		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);
		
		JLabel label_1 = new JLabel("Punteggio");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.NORTH;
		gbc_label_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 4;
		gbc_label_1.gridy = 0;
		panel.add(label_1, gbc_label_1);
		
		aggiungiUtenti();
		
		
		setTitle("Classifica");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
	}
	
	private void aggiungiUtenti(){
		Integer giocatoriTotali = this.miaClassifica.getGiocatoriInClassifica();
		//Stampiamo al massimo 15 giocatori.
		if(giocatoriTotali<this.primiTot){
			this.primiTot = giocatoriTotali;
		}
		for(int i = 0; i < primiTot; i++){
			Integer posizione = i+1;
			String username = this.miaClassifica.getUsername(posizione);
			Integer punti = this.miaClassifica.getPunteggio(posizione);
			JLabel label_2 = new JLabel(posizione.toString());
			label_2.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gbc_label_2 = new GridBagConstraints();
			gbc_label_2.fill = GridBagConstraints.BOTH;
			gbc_label_2.insets = new Insets(0, 0, 0, 5);
			gbc_label_2.gridx = 0;
			gbc_label_2.gridy = posizione;
			panel.add(label_2, gbc_label_2);
			
			JLabel label_3 = new JLabel(username);
			label_3.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gbc_label_3 = new GridBagConstraints();
			gbc_label_3.anchor = GridBagConstraints.NORTH;
			gbc_label_3.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_3.insets = new Insets(0, 0, 0, 5);
			gbc_label_3.gridx = 2;
			gbc_label_3.gridy = posizione;
			panel.add(label_3, gbc_label_3);
			
			JLabel label_4 = new JLabel(punti.toString());
			label_4.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gbc_label_4 = new GridBagConstraints();
			gbc_label_4.anchor = GridBagConstraints.NORTH;
			gbc_label_4.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_4.gridx = 4;
			gbc_label_4.gridy = posizione;
			panel.add(label_4, gbc_label_4);
		}
		
		
	}
}
