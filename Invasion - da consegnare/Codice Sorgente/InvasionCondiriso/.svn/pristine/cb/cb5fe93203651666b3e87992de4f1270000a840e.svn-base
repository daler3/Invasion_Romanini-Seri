package classiCondivise;

import java.io.Serializable;

/**
 * 
 * Classe rappresentante le informazioni essenziali di un tavolo (id e numero di utenti), da inviare al client quando si trova nel limbo. 
 *
 */
public class BeanTavolo implements Serializable{

	/**
	 * Defaul serial version UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Id identificativo del tavolo
	 */
	private Integer idTavolo;
	/**
	 * Numero di utenti connessi
	 */
	private Integer numeroUtenti;
	
	/**
	 * Costruttore
	 * @param idTavolo - id del tavolo 
	 * @param numeroUtenti - numero di utenti nel tavolo
	 */
	public BeanTavolo(Integer idTavolo, Integer numeroUtenti) {
		super();
		this.idTavolo = idTavolo;
		this.numeroUtenti = numeroUtenti;
	}
	
	
	public Integer getIdTavolo() {
		return idTavolo;
	}
	
	
	public void setIdTavolo(Integer idTavolo) {
		this.idTavolo = idTavolo;
	}
	
	
	public Integer getNumeroUtenti() {
		return numeroUtenti;
	}
	
	/**
	 * Numero di utenti nel tavolo
	 * @param numeroUtenti
	 */
	public void setNumeroUtenti(Integer numeroUtenti) {
		this.numeroUtenti = numeroUtenti;
	}
	
	@Override
	public String toString() {
		String stringa = idTavolo+";"+numeroUtenti;
		return stringa;
	}
	
}
