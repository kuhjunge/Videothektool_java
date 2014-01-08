import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

/**
 * @author Kuhjunge, Simon Krause, Rene Kremer
 * @version 1.0.5
 * 
 */
public class DBController {
	
	private final int DVD = 1;
	private final int BLURAY = 2;
	
	private int globalright = 3;
	/**
	 * User, der sich in der DB anmeldet
	 */
	private String username = "";

	/**
	 * Passwort des Users
	 */
	private String password = "";

	/**
	 * URL der Datenbank-Verbindung
	 */
	private String url = "";

	/**
	 * ConnectionSource von ORMLite
	 */
	private ConnectionSource connectionSource = null;

	/**
	 * Name des DB-Schemas
	 */
	private String dbName = "";

	/**
	 * Dao für View Film_Stammdaten
	 */
	private Dao<Film, String> filmDao;

	/**
	 * Dao für View Filmbestand
	 */
	private Dao<FilmExemplar, String> bestandDao;

	/**
	 * Dao für Genre-Entität der DB
	 */
	private Dao<Genre, String> genreDao;

	/**
	 * Dao für View Kundendaten
	 */
	private Dao<Kunde, String> kundeDao;
	
	/**
	 * Dao für View Verliehen
	 */
	private Dao<Verliehen, String> verliehenDao;

	/**
	 * Default- Konstruktor Es wird keine Verbindung zur Datenbank aufgebaut.
	 */
	public DBController() {
		username = SaveLoader.getUsername();
		password = SaveLoader.getPassword();
		url = SaveLoader.getUrl();
		dbName = SaveLoader.getDbName();
	}

	/**
	 * Schließen der Verbindung
	 */
	public void close() {
		if (this.connectionSource != null) {
			try {
				this.connectionSource.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Verbinden durch ORMLite mit Datenbank
	 */
	public void connect() throws SQLException {
		String databaseUrl = this.url + "/" + this.dbName + "?user=" + username
				+ "&password=" + password;

		// create a connection source to our database
		this.connectionSource = new JdbcConnectionSource(databaseUrl);

		// instantiate the dao
		this.filmDao = DaoManager.createDao(this.connectionSource, Film.class);
		this.genreDao = DaoManager
				.createDao(this.connectionSource, Genre.class);
		this.bestandDao = DaoManager.createDao(this.connectionSource,
				FilmExemplar.class);
		this.kundeDao = DaoManager
				.createDao(this.connectionSource, Kunde.class);
		this.verliehenDao = DaoManager.createDao(this.connectionSource, Verliehen.class);

		this.filmDao.isTableExists(); // Erzeugt Fehler bei fehlerhafter
										// Verbindung
		globalright = checkright();
		if (globalright < 3) {
			// Wenn admin
		} else if (globalright < 2) {
			// Wenn Viewuser (mit schreibrechten)
		} else {
			// alles andere TODO: Schreibrechte für View richtig abprüfen
		}
				
	}

	/**
	 * Methode überprüft, ob Filialleiter-Rechte vorhanden sind
	 * 
	 * @return 1 für Mitarbeiter, 2 für Filialleitung
	 */
	public int checkright() {
		// TODO Bitte zu ändern!!!!
		/*
		 * int recht = 3; try { if (this.genreDao.queryForAll() != null) {
		 * System.out.println("Adminrechte"); } } catch
		 * (MySQLSyntaxErrorException e) { // User ist Viewuser recht = 2;
		 * System.out.println("Viewuster erkannt"); try { this.filmDao =
		 * DaoManager.createDao(this.connectionSource, Film.class); if
		 * (!this.filmDao.isUpdatable()) {
		 * System.out.println("keine schreibrechte"); recht = 1; } } catch
		 * (SQLException e1) { e1.printStackTrace(); } } catch (SQLException e)
		 * { recht = 0; } //return recht;
		 */
		return 2;
	}

	public boolean isDBOnline() {
		try {//TODO überprüfen, ob diese Methode gebraucht wird
			return this.filmDao.isTableExists();
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Gibt eine List<Film> zurück. Es wird im FilmDao.queryForAll() aufgerufen
	 * 
	 * @return
	 */
	public List<Film> getFilme() {
		try {
			return this.filmDao.queryForAll();
		} catch (Exception e) {
			System.out.println(e.toString() + " Fehler beim laden der Filme");
			return null;
		}
	}

	/**
	 * Gibt eine List<Genre> zurück. Es wird im FilmDao.queryForAll() aufgerufen
	 * 
	 * @return
	 */
	public Map<Integer, String> getGenre() {
		try {
			Map<Integer, String> m = new HashMap<Integer, String>();
			List<Genre> l = this.genreDao.queryForAll();
			for (Genre g : l) {
				m.put(g.getID(), g.getGenre());
			}
			return m;
		} catch (Exception e) {
			System.out.println(e.toString() + " Fehler beim laden der Genre");
			return null;
		}
	}

	/**
	 * Holt einen Film aus der Datenbank mit FilmTitel und fsk als Suchkriterium.
	 * 
	 * @param filmTitel
	 *            Titel des Films
	 * @param fsk
	 *            FSK als Suchkriterium
	 * @return gibt einen Film zurück
	 */
	public List<Film> getFilme(String filmTitel, String fsk) {
		List<Film> filmList = new LinkedList<Film>();
		try {
			QueryBuilder<Film, String> queryBuilder = filmDao.queryBuilder();
			if (!filmTitel.isEmpty()) {
				queryBuilder.where().like("titel", "%" + filmTitel + "%").and()
						.le("fsk", Integer.valueOf(fsk));
			} else {
				queryBuilder.where().le("fsk", Integer.valueOf(fsk));
			}
			PreparedQuery<Film> preparedQuery = queryBuilder.prepare();
			filmList = filmDao.query(preparedQuery);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return filmList;
	}

	/**
	 * Gibt die Anzahl der vorhandenen Exemplare als DVD zurück
	 * 
	 * @param filmTitel
	 *            zu suchender Film
	 * @return Rückgabe der Anzahl der Filme als DVD
	 */
	public int getAnzahlDVD(int idFilm) {
		int value = 0;
		try {
			QueryBuilder<FilmExemplar, String> qB2 = bestandDao.queryBuilder();
			qB2.where().eq("film_Ref", idFilm)
				.and().eq("medium_Ref", DVD);
			List<FilmExemplar> exemplare = bestandDao.query(qB2.prepare());				
			value = exemplare.size();	
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return value;
	}

	/**
	 * Gibt die Anzahl der vorhandenen Exemplare als Blu-Ray eines FilmTitels
	 * zurück
	 * 
	 * @param filmTitel
	 *            zu suchender Film
	 * @return Rückgabe der Anzahl der Filme als Blu-Rays
	 */
	public int getAnzahlBluRay(int idFilm) {
		int value = 0;
		try {			
			QueryBuilder<FilmExemplar, String> qB2 = bestandDao.queryBuilder();
			qB2.where().eq("film_Ref", idFilm)
				.and().eq("medium_Ref", BLURAY);
			List<FilmExemplar> exemplare = bestandDao.query(qB2.prepare());				
			value = exemplare.size();				
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return value;
	}

	/**
	 * Gibt eine Liste der vorhandenen MedienArten zurück
	 * 
	 * @return
	 */
	public List<String> getMediumArt() {
		List<String> liste = new LinkedList<String>();
		try {
			String query = "select nameMedium from Mediumart";
			GenericRawResults<String[]> rawResults = filmDao.queryRaw(query);
			List<String[]> ergebnis = rawResults.getResults();

			for (String[] str : ergebnis) {
				for (String s : str) {
					liste.add(s);
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return liste;
	}

	/**
	 * Diese Methode gibt einen Film zurück, der mit dem Übergabeparameter des
	 * Titels übereinstimmt
	 * 
	 * @param filmTitel
	 *            zu suchender Filmtitel
	 * @return gibt den passenden Film zurück
	 */
	public Film getFilm(String filmTitel) {
		List<Film> filmList = new LinkedList<Film>();
		try {
			QueryBuilder<Film, String> queryBuilder = filmDao.queryBuilder();
			queryBuilder.where().eq("titel", filmTitel);

			PreparedQuery<Film> preparedQuery = queryBuilder.prepare();
			filmList = filmDao.query(preparedQuery);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return filmList.get(0);
	}
	
	/**
	 * Diese Methode gibt einen Film zurück, anhand seiner id
	 * @param idFilm
	 * @return
	 */
	public Film getFilm(int idFilm) {
		List<Film> filmList = new LinkedList<Film>();
		try {
			QueryBuilder<Film, String> queryBuilder = filmDao.queryBuilder();
			queryBuilder.where().eq("idFilm", idFilm);

			PreparedQuery<Film> preparedQuery = queryBuilder.prepare();
			filmList = filmDao.query(preparedQuery);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return filmList.get(0);
	}
	
	

	/**
	 * Diese Methode schreibt ein Filmobjekt in die Datenbank
	 * 
	 * @return Gibt an ob die Schreiboperation erfolgreich war
	 */
	public boolean writeMovie(Film f) {
		try {
			this.filmDao.create(f);
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Fehler beim Schreibzugriff",
					"Fehler", JOptionPane.OK_OPTION);
			return false;
		}
	}

	/**
	 * Diese Methode gibt eine Liste aller Kunden zurück
	 * 
	 * @return
	 */
	public List<Kunde> getKunden() {
		try {
			return this.kundeDao.queryForAll();
		} catch (Exception e) {
			System.out.println(e.toString() + " Fehler beim laden der Kunden");
			return null;
		}
	}

	/**
	 * Diese Methode gibt die Anzahl alle nicht verliehenen DVD eines Film zurück
	 * @param idFilm
	 * @return
	 */
	public int getAnzahlDVDPraesent(int idFilm) {
		int value = 0;
		try{
			QueryBuilder<FilmExemplar, String> qB2 = bestandDao.queryBuilder();
			qB2.where().eq("film_Ref", idFilm)
				.and().eq("medium_Ref", DVD)
				.and().eq("isVerliehen", false);
			List<FilmExemplar> exemplare = bestandDao.query(qB2.prepare());				
			
			value = exemplare.size();			
						
		}catch(Exception e){
			System.out.println(e.toString());
		}
		return value;
	}

	/**
	 * Diese Methode gibt die Anzahl alle nicht verliehenen BluRays eines Film zurück
	 * @param idFilm
	 * @return
	 */
	public int getAnzahlBluRayPraesent(int idFilm) {
		int value = 0;
		try{
			QueryBuilder<FilmExemplar, String> qB2 = bestandDao.queryBuilder();
			qB2.where().eq("film_Ref", idFilm)
				.and().eq("medium_Ref", BLURAY)
				.and().eq("isVerliehen", false);
			List<FilmExemplar> exemplare = bestandDao.query(qB2.prepare());				
			
			value = exemplare.size();				
						
		}catch(Exception e){
			System.out.println(e.toString());
		}
		return value;
	}

	/**
	 * Diese Methode schreibt die neue Anzahl an DVD in die Datenbank,
	 * d.h. es werden neue Exemplare vom Typ DVD des Films angelegt.
	 * bzw. es werden Exemplare gelöscht, die nicht ausgeliehen sind
	 * @param anzahl
	 * @param idFilm
	 * @return Rückgabe von -1, wenn mehr gelöscht werden soll, als nicht ausgeliehen vorhanden ist
	 */
	public int writeAnzahlDVD(int anzahl, int idFilm) throws Exception{
		//filmDao.create(f);
		int bestand = this.getAnzahlDVD(idFilm);
		int dif = anzahl - bestand;		
		//füge neue Exemplare hinzu		
		if(dif > 0){			
			for(int i = 0; i < dif; i++){
				bestandDao.create( new FilmExemplar(idFilm, DVD) );
			}
		}
		else if(dif < 0){			
			if(-dif <= this.getAnzahlDVDPraesent(idFilm) ){
				QueryBuilder<FilmExemplar, String> qB2 = bestandDao.queryBuilder();
				qB2.where().eq("film_Ref", idFilm)
					.and().eq("medium_Ref", DVD)
					.and().eq("isVerliehen", false);
				List<FilmExemplar> exemplare = bestandDao.query(qB2.prepare());				
				for(int i = 0; i < (-dif); i++){
					bestandDao.delete(exemplare.get(i));
				}
			}
			else{
				return -1;
			}
		}
		return 0;
	}
	
	/**
	 * Diese Methode schreibt die neue Anzahl an BluRay in die Datenbank,
	 * d.h. es werden neue Exemplare vom Typ BluRay des Films angelegt.
	 * bzw. es werden Exemplare gelöscht, die nicht ausgeliehen sind
	 * @param anzahl
	 * @param idFilm
	 * @return Rückgabe von -1, wenn mehr gelöscht werden soll, als nicht ausgeliehen vorhanden ist
	 */
	public int writeAnzahlBluRay(int anzahl, int idFilm) throws Exception{
		//filmDao.create(f);
		int bestand = this.getAnzahlBluRay(idFilm);
		int dif = anzahl - bestand;		
		//füge neue Exemplare hinzu		
		if(dif > 0){			
			for(int i = 0; i < dif; i++){
				bestandDao.create( new FilmExemplar(idFilm, BLURAY) );
			}
		}
		else if(dif < 0){			
			if(-dif <= this.getAnzahlDVDPraesent(idFilm) ){
				QueryBuilder<FilmExemplar, String> qB2 = bestandDao.queryBuilder();
				qB2.where().eq("film_Ref", idFilm)
					.and().eq("medium_Ref", BLURAY)
					.and().eq("isVerliehen", false);
				List<FilmExemplar> exemplare = bestandDao.query(qB2.prepare());				
				for(int i = 0; i < (-dif); i++){
					bestandDao.delete(exemplare.get(i));
				}
			}
			else{
				return -1;
			}
		}
		return 0;
	}
	
	/**
	 * Diese Methode löscht, sofern kein Film mehr ausgeliehen ist, alle Exemplare und den Filmtitel aus dem Sortiment
	 * @param idFilm
	 */
	public void deleteFilm(int idFilm){
		try{
			if( this.getAnzahlBluRay(idFilm) == this.getAnzahlBluRayPraesent(idFilm) 
			&& this.getAnzahlDVD(idFilm) == this.getAnzahlDVDPraesent(idFilm) ){				
				QueryBuilder<FilmExemplar, String> qB2 = bestandDao.queryBuilder();
				qB2.where().eq("film_Ref", idFilm);					
				List<FilmExemplar> exemplare = bestandDao.query(qB2.prepare());
				
				bestandDao.delete(exemplare);
				filmDao.deleteById(String.valueOf(idFilm));
			}

		}catch(Exception e){
			System.out.println(e.toString()+ "Fehler beim Löschen eines Films und deren Exemplaren");
		}
	}
	
}
