package interfacciaGUI;

import java.util.ArrayList;

import classiCondivise.BeanTavolo;

public interface ViewLimbo {

	/**
	 * Aggiorniamo i tavoli presenti nell'interfaccia grafica.
	 * @param tavoli	lista con le info dei tavoli aggiornate
	 */
	public void aggiornaTavoli(ArrayList<BeanTavolo> tavoli);
	
	public void chiusuraServer(ControllerUtente controller);
	
}
