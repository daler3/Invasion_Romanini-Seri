package guiClient;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CreaPopupExit {

	final JFrame popup = new JFrame("Logout OK");
	
	
	public CreaPopupExit(String frase){
		popup.setLayout(new FlowLayout());
		JLabel label = new JLabel(frase);
		JButton bottone = new JButton("Ricevuto");
		bottone.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            popup.dispose();
	        }
	    });
		popup.add(label);
		popup.add(bottone);
		popup.pack();
		popup.setVisible(true);	
		popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
