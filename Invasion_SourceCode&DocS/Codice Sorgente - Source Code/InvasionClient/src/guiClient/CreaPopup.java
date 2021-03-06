package guiClient;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class CreaPopup {

	int tempoMax = 0;
	int tempoTrascorso = 0;
	final JFrame popup = new JFrame("");
	final JButton bottone = new JButton("Ricevuto");
	public CreaPopup(String frase){
		new CreaPopup(frase, 0);
	}
	
	public CreaPopup(String frase, int tempo){
		popup.setLayout(new FlowLayout());
		JLabel label = new JLabel(frase);
		bottone.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent evt) {
	            popup.dispose();
	        }
	    });
		popup.add(label);
		popup.add(bottone);
		
		if(tempo != 0){
			this.tempoMax = tempo;
			Timer timer = new Timer(1000, new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent evt) {
		            if(tempoMax-tempoTrascorso < 0){
		            	popup.dispose();
		            }
		            else{
		            	bottone.setText(""+(tempoMax-tempoTrascorso));
		            	tempoTrascorso++;
		            }
		        }
		    });
			timer.start();
		}
		
		popup.pack();
		popup.setVisible(true);	
	}
}
