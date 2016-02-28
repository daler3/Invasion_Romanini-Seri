package server;

import java.util.Random;

/**
 * Rappresenta un dado
 * 
 * @authors Daniele Romanini - Federico Seri
 *
 */
public class Dado {
	
	/**
	 * Rappresenta il numero di facce del dado
	 */
	private Integer numeroFacce=null; 	
	
	/**
	 * Permetterà di lanciare il dado 
	 */
	private Random lanciatore = null; 
	
	
	/**
	 * Costruttore del dado
	 */
	public Dado (){
		this.numeroFacce=6; 
		lanciatore = new Random (); 
	}
	
	/**
	 * Metodo che permette di lanciare il dado
	 * @return -il numero corrispondente alla faccia uscita in seguito al lancio
	 */
	public Integer lanciaDado(){
		return lanciatore.nextInt(numeroFacce)+1; 
	}
	
	
}
