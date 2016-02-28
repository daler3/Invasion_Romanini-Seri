package guiClient;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class PanelError extends JPanel {

	final JPanel p = this;
	private GridBagLayout gridBagLayout;
	
	private JLabel lblTitolo;
	private JButton btnEsci;
//	private JButton btnRiprova;
	private JTextPane txtpnMotivazione;
	
	private GridBagConstraints gbc_lblTitolo;
	private GridBagConstraints gbc_btnEsci;
//	private GridBagConstraints gbc_btnRiprova;
	private GridBagConstraints gbc_txtpnMotivazione;
	
	
	
	public PanelError(String motivazione) {
		if(motivazione.equals(null)){
			motivazione = "Errore";
		}
		
		gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		
		lblTitolo = new JLabel("Errore comunicazione");
		gbc_lblTitolo = new GridBagConstraints();
		gbc_lblTitolo.insets = new Insets(0, 0, 5, 0);
		gbc_lblTitolo.gridwidth = 7;
		gbc_lblTitolo.gridx = 0;
		gbc_lblTitolo.gridy = 0;
		add(lblTitolo, gbc_lblTitolo);
		
		txtpnMotivazione = new JTextPane();
		txtpnMotivazione.setText("Motivazione\n"+motivazione);
		gbc_txtpnMotivazione = new GridBagConstraints();
		gbc_txtpnMotivazione.gridwidth = 5;
		gbc_txtpnMotivazione.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnMotivazione.gridx = 1;
		gbc_txtpnMotivazione.gridy = 1;
		add(txtpnMotivazione, gbc_txtpnMotivazione);
		
		btnEsci = new JButton("Esci");
		gbc_btnEsci = new GridBagConstraints();
		btnEsci.addActionListener(new EsciListener());
		gbc_btnEsci.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEsci.insets = new Insets(0, 0, 0, 5);
		gbc_btnEsci.gridx = 1;
		gbc_btnEsci.gridy = 2;
		add(btnEsci, gbc_btnEsci);
		
//		btnRiprova = new JButton("Riprova");
//		btnRiprova.addActionListener(new RiprovaListener());
//		gbc_btnRiprova = new GridBagConstraints();
//		gbc_btnRiprova.fill = GridBagConstraints.HORIZONTAL;
//		gbc_btnRiprova.insets = new Insets(0, 0, 0, 5);
//		gbc_btnRiprova.gridx = 5;
//		gbc_btnRiprova.gridy = 2;
//		add(btnRiprova, gbc_btnRiprova);
			
	}

	public class EsciListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Container c = p.getParent();
			 if (c != null) {
				 c.remove(p);             
			 }
			 System.exit(ABORT);
		}	
	}
	
	public class RiprovaListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Container c = p.getParent();
			 if (c != null) {
				 c.remove(p);
				 c.revalidate();
			 }
			 
			 //System.exit(ABORT);
		}	
	}
	
	
}
