package classiCondivise;

import exceptionCondivise.ColoreIrriconoscibileException;

public class StringToColori {

	/**
	 * Ritorna il colore associato alla stringa
	 * @param colore	La stringa che vogliamo interpretare.
	 * @return	Il colore associato.
	 * @throws ColoreIrriconoscibileException Stringa non riconosciuta.
	 */
	public static Colori getColore(String colore) throws ColoreIrriconoscibileException{
		if(colore.toUpperCase().equals(Colori.BLU.getNome().toUpperCase().trim()))
			return Colori.BLU;
		if(colore.toUpperCase().equals(Colori.GIALLO.getNome().toUpperCase().trim()))
			return Colori.GIALLO;
		if(colore.toUpperCase().equals(Colori.NERO.getNome().toUpperCase().trim()))
			return Colori.NERO;
		if(colore.toUpperCase().equals(Colori.NEUTRO.getNome().toUpperCase().trim()))
			return Colori.NEUTRO;
		if(colore.toUpperCase().equals(Colori.ROSSO.getNome().toUpperCase().trim()))
			return Colori.ROSSO;
		if(colore.toUpperCase().equals(Colori.VERDE.getNome().toUpperCase().trim()))
			return Colori.VERDE;
		if(colore.toUpperCase().equals(Colori.VIOLA.getNome().toUpperCase().trim()))
			return Colori.VIOLA;
		throw new ColoreIrriconoscibileException();		
	}

}
