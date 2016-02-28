package server;

import java.util.ArrayList;

import exceptionCondivise.ElementoNonPresenteException;

/**
 * Classe generica di supporto, dove sono implementati metodi generici 
 *
 */

public class GenericSupport<E> {

	/**
	 * Prendendo in ingresso un elemento di una lista, torna l'elemento successivo.<br>
	 * Se l'elemento in ingresso è null, ritorna il primo elemento della lista.<br>
	 * Se l'elemento preso in ingresso è l'ultimo della lista, ritorna il primo elemento della lista.<br>
	 * @param var 		Elemento contenuto in una lista.
	 * @param lista 	La lista.
	 * @return 			Il prossimo elemento nella lista.
	 * @throws ElementoNonPresenteException	
	 */
	public synchronized E prossimo(E var, ArrayList<E> lista) throws ElementoNonPresenteException{
		if(var == null){
			return lista.get(0);
		}
		//Non esiste il prossimo elemento
		if (! lista.contains(var)){
			throw new ElementoNonPresenteException(); //elemento mancante nella lista
		}
		int totaleGiocatori = lista.size();
		//se come parametro gli passo un oggetto nullo, mi ritorna il primo della lista
		int posizione = lista.indexOf(var);
		if(posizione+1 == totaleGiocatori){	//Siamo arrivati alla fine del ciclo
			return lista.get(0);	//Ci ritorna il primo giocatore
		}
		else{
			return lista.get(posizione+1);
		}		
	}
	
	
	
}
