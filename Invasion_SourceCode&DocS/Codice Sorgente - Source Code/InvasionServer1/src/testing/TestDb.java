package testing;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import classiCondivise.BeanGiocatore;
import dbManager.DbManager;


public class TestDb {
	
	private final static String utenteDelete ="";
	private final static String userLogin ="";
	private final static String pwLogin ="";
	private final static String pwLogin0 ="";
	private final static String userLogin1 ="";
	private final static String pwLogin1 ="";
	private final static String userReg ="";
	private final static String pwReg ="";
	private final static String userReg1 ="";
	private final static String pwReg1 ="";

	DbManager db = null;
	Connection connessioneDb = null;
	

	@Before
	public void setUp() throws Exception {
		db = new DbManager();
		connessioneDb = db.connessioneDb();
	}

	@After
	public void tearDown() throws Exception {
		db = null;
		connessioneDb = null;
	}

	@Test
	public void testLoginUtente() throws SQLException  {
		assertTrue(db.loginUtente(connessioneDb, userLogin, pwLogin));

		assertFalse(db.loginUtente(connessioneDb, userLogin1, pwLogin1));
	}

	@Test
	public void testRegistraUtente() throws SQLException {
		assertFalse(db.registraUtente(connessioneDb, userReg, pwReg));

		assertTrue(db.registraUtente(connessioneDb, userReg1, pwReg1));
		//proviamo a fare il login
		assertTrue(db.loginUtente(connessioneDb, userReg1, pwReg1));
	}
	

	@Test
	public void testCancellaUtente() throws SQLException {
		String username1 = "daler"; //l'utente è già sul database
		String password1 = "daler";
		db.cancellaUtente(connessioneDb, username1);
		//ho cancellato l'utente, provo a fare il login
		assertFalse(db.loginUtente(connessioneDb, username1, password1));
	}

	@Test
	public void testCambiaPassword() throws SQLException {
		db.cambiaPassword(connessioneDb, userLogin, pwLogin, pwLogin0);
		assertTrue(db.loginUtente(connessioneDb, userLogin, pwLogin0));
	}

	@Test
	public void testUsernameInUso() {
		assertTrue(db.usernameInUso(connessioneDb, "q"));
		assertFalse(db.usernameInUso(connessioneDb, "giovannivernia"));
	}


	@Test
	public void testAggiornaUtentePostPartita() throws SQLException {
		ArrayList<String> giocatori = new ArrayList<String>();
		BeanGiocatore bean = db.estraiBean(connessioneDb, "sd");
		Integer partitePrima = bean.getNumeroPartitePartecipate();
		Integer punteggioPrima = bean.getPunteggio();
		giocatori.add("sd");
		giocatori.add("q");
		giocatori.add("fg");
		Integer id = db.creaPartita(connessioneDb, giocatori);
		db.aggiornaUtentePostPartita(connessioneDb, "sd", id, 16, 1);
		BeanGiocatore bean2 = db.estraiBean(connessioneDb, "sd");
		Integer partiteDopo = bean2.getNumeroPartitePartecipate();
		Integer punteggioDopo = bean2.getPunteggio();
		Integer partiteAppoggio = partitePrima +1;
		assertEquals(partiteAppoggio, partiteDopo);
		Integer puntiAppoggio = punteggioPrima +16;
		assertEquals(puntiAppoggio, punteggioDopo);
	}

	@Test
	public void testEstraiBean() throws SQLException {
		String username = "sd";
		BeanGiocatore bean = db.estraiBean(connessioneDb, username);
		assertNotNull(bean);
		String username1 = "a";
		String password = "a";
		db.registraUtente(connessioneDb, username1, password);
		bean = db.estraiBean(connessioneDb, username1);
		assertNotNull(bean);
		assertEquals(username1, bean.getNomeUtente());
	}

}
