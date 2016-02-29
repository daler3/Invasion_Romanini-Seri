package classiCondivise;

import java.io.Serializable;
/**
 * Classe che rappresenta le Info di un utente
 *
 */
public class BeanGiocatore implements Cloneable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Nome dell'utente
	 */
	private String nomeUtente; 
	
	/**
	 * Punteggio totale dell'utente al momento
	 */
	private int punteggio; 
	
	/**
	 * Posizione in classifica dell'utente
	 */
	private int posizioneClassifica; 
	
	/**
	 * Numero delle partite a cui l'utente ha preso parte
	 */
	private int numeroPartitePartecipate; 
	
	/**
	 * Percentuale di partite vinte
	 */
	private float percentualePartiteVinte; 
	
	/**
	 * Percentuale di partite vinte
	 */
	private float percentualePartiteSecondo; 
	
	/**
	 * Percentuale di partite abbandonate
	 */
	private float percentualePartiteAbbandonate; 
	
	/**
	 * Attributo che indica se il giocatore è connesso o meno
	 */
	private boolean connesso;

	
	/**
	 * Costruttore
	 * @param nomeUtente
	 * @param punteggio
	 * @param posizioneClassifica
	 * @param numeroPartitePartecipate
	 * @param percentualePartiteVinte
	 * @param percentualePartiteSecondo
	 * @param percentualePartiteAbbandonate
	 * @param connesso
	 * @param percentualePartiteSecondo 
	 */
	public BeanGiocatore(String nomeUtente, int punteggio,
			int posizioneClassifica, int numeroPartitePartecipate,
			float percentualePartiteVinte, float percentualePartiteSecondo, float percentualePartiteAbbandonate,  boolean connesso) {
		this.nomeUtente = nomeUtente;
		this.punteggio = punteggio;
		this.posizioneClassifica = posizioneClassifica;
		this.numeroPartitePartecipate = numeroPartitePartecipate;
		this.percentualePartiteVinte = percentualePartiteVinte;
		this.percentualePartiteSecondo = percentualePartiteSecondo;
		this.percentualePartiteAbbandonate = percentualePartiteAbbandonate;
		this.connesso = connesso;
	}
	
	
	@Override
	public BeanGiocatore clone(){
		BeanGiocatore nuoveInfoClone= new BeanGiocatore(this.nomeUtente, this.punteggio, this.posizioneClassifica, this.numeroPartitePartecipate,
											this.percentualePartiteVinte, this.percentualePartiteSecondo, this.percentualePartiteAbbandonate,
											this.connesso);		
		return nuoveInfoClone;
		
	}


	public String getNomeUtente() {
		return nomeUtente;
	}


	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}


	public int getPunteggio() {
		return punteggio;
	}


	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}


	public int getPosizioneClassifica() {
		return posizioneClassifica;
	}


	public void setPosizioneClassifica(int posizioneClassifica) {
		this.posizioneClassifica = posizioneClassifica;
	}


	public int getNumeroPartitePartecipate() {
		return numeroPartitePartecipate;
	}


	public void setNumeroPartitePartecipate(int numeroPartitePartecipate) {
		this.numeroPartitePartecipate = numeroPartitePartecipate;
	}

	
	public float getPercentualePartiteVinte() {
		return percentualePartiteVinte;
	}
	
	public void setPercentualePartiteVinte(float percentualePartiteVinte) {
		this.percentualePartiteVinte = percentualePartiteVinte;
	}
	

	public float getPercentualePartiteSecondo() {
		return percentualePartiteVinte;
	}
	
	
	public void setPercentualePartiteSecondo(float percentualePartiteSecondo) {
		this.percentualePartiteVinte = percentualePartiteSecondo;
	}
	
	
	public float getPercentualePartiteAbbandonate() {
		return percentualePartiteAbbandonate;
	}


	public void setPercentualePartiteAbbandonate(float percentualePartiteAbbandonate) {
		this.percentualePartiteAbbandonate = percentualePartiteAbbandonate;
	}


	public boolean isConnesso() {
		return connesso;
	}


	public void setConnesso(boolean connesso) {
		this.connesso = connesso;
	}


	@Override
	public String toString() {
		String daInviare = CreareMessaggio.preparaInserimetoParametro(nomeUtente)+";"+
				punteggio+";"+
				posizioneClassifica+";"+
				numeroPartitePartecipate+";"+
				percentualePartiteVinte+";"+
				percentualePartiteSecondo+";"+
				percentualePartiteAbbandonate+";"+
				connesso;
		return daInviare;
	} 
	
	

}
