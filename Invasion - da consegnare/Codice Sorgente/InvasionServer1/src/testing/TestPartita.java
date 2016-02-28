package testing;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import mappa.Mappa;
import mappa.Territorio;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import classiCondivise.Colori;
import dbManager.DbManager;
import exceptionCondivise.GiocatoreNonTrovatoException;
import exceptionCondivise.TerritorioNonTrovatoException;
import server.GiocatoreConnesso;
import server.Partita;
import server.Server;

public class TestPartita {


	Partita partita = null;
	GiocatoreConnesso giocatore1 = null;
	GiocatoreConnesso giocatore2 = null;
	GiocatoreConnesso giocatore3 = null;
	Connection connessione = null;
	DbManager db = null;
	Mappa mappa = null;
	
	
	@Before
	public void setUp() throws Exception {
		giocatore1 = new GiocatoreConnesso();
		giocatore2 = new GiocatoreConnesso();
		giocatore3= new GiocatoreConnesso();
		ArrayList<GiocatoreConnesso> listagiocatori = new ArrayList<GiocatoreConnesso>();
		listagiocatori.add(giocatore1);
		listagiocatori.add(giocatore2);
		listagiocatori.add(giocatore3);
		partita = new Partita(listagiocatori, 30);
		db = new DbManager();
		connessione = db.connessioneDb();
		mappa = db.estraiMappa(connessione);
	}

	@After
	public void tearDown() throws Exception {
		partita = null;
		giocatore1= null;
		giocatore2 = null;
		giocatore3 = null;
		db = null;
		connessione = null;
		mappa = null;
	}

	@Test
	public void testSetArmateSuTerritorio() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		Territorio territorio1 = null;
		try {
			territorio1 = mappa.trovaTerritorio("Cita");
		} catch (TerritorioNonTrovatoException e2) {
	
		}
		territorio1.setColorePossessore(Colori.GIALLO);
		territorio1.setUnitaPresenti(10);
		giocatore1.setNumeroArmateDaPosizionare(10);
		giocatore1.setColoreGiocatore(Colori.GIALLO);
		giocatore1.getTerritoriPosseduti().add(territorio1);
		try {
			partita.setArmateSuTerritorio(giocatore1, territorio1, 10);
		} catch (TerritorioNonTrovatoException e1) {

		}
		Integer confronto = 0;
		Integer armateGiocatore = giocatore1.getNumeroArmateDaPosizionare();
		assertEquals(confronto, armateGiocatore);
		
		armateGiocatore = giocatore1.getNumeroArmateDaPosizionare();
		territorio1.setColorePossessore(Colori.BLU);
		giocatore1.setNumeroArmateDaPosizionare(10);
		armateGiocatore = giocatore1.getNumeroArmateDaPosizionare();
		try {
			partita.setArmateSuTerritorio(giocatore1, territorio1, 10);
		} catch (TerritorioNonTrovatoException e) {
			assertNotEquals(confronto, armateGiocatore);
		}
	}

	
	@Test
	public void testGiocatoreAssociatoAlcolore() throws GiocatoreNonTrovatoException {
		giocatore1.setColoreGiocatore(Colori.GIALLO);
		giocatore2.setColoreGiocatore(Colori.BLU);
		assertEquals(giocatore1, partita.giocatoreAssociatoAlcolore(Colori.GIALLO));
		assertNotEquals(giocatore1, partita.giocatoreAssociatoAlcolore(Colori.BLU));
	}


	@Test
	public void testSpostaArmate() throws TerritorioNonTrovatoException {
		Territorio territorio1 = partita.nomeToTerritorio("Congo");
		Territorio territorio2 = partita.nomeToTerritorio("Madagascar");
		Territorio territorio3 = partita.nomeToTerritorio("Africa del Sud");
		territorio1.setColorePossessore(Colori.GIALLO);
		territorio2.setColorePossessore(Colori.GIALLO);
		territorio3.setColorePossessore(Colori.GIALLO);
		giocatore1.setColoreGiocatore(Colori.GIALLO);
		giocatore1.setTuoTurno(true);
		partita.setGiocatoreTurno(giocatore1);
		territorio1.setUnitaPresenti(10);
		territorio3.setUnitaPresenti(1);
		partita.spostaArmate(9, "Congo", "Africa del Sud", giocatore1);
		Integer armateAspettateSuTerritorio = 10;
		assertEquals(territorio3.getUnitaPresenti(), armateAspettateSuTerritorio);
		//dopodiche non è più il turno del giocatore
		territorio1.setUnitaPresenti(10);
		partita.spostaArmate(9, "Congo", "Madagascar", giocatore1);
		armateAspettateSuTerritorio = 19;
		assertNotEquals(territorio2.getUnitaPresenti(), armateAspettateSuTerritorio);
		
		//ora come prima. Ma il territorio2 (Madagascar) non è più Giallo
		giocatore1.setTuoTurno(true);
		partita.setGiocatoreTurno(giocatore1);
		territorio1.setUnitaPresenti(10);
		territorio3.setColorePossessore(Colori.BLU);
		territorio2.setUnitaPresenti(1);
		partita.spostaArmate(9, "Congo", "Madagascar", giocatore1);
		armateAspettateSuTerritorio = 10;
		assertNotEquals(territorio2.getUnitaPresenti(), armateAspettateSuTerritorio);
	}

	
	
}
