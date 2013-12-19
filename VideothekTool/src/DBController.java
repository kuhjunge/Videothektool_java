import java.sql.SQLException;
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
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * @author Kuhjunge, Simon Krause, Rene Kremer
 * @version 1.0.5
 * 
 */
public class DBController {

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
	private Dao<FilmBestand, String> bestandDao;

	/**
	 * Dao für Genre-Entität der DB
	 */
	private Dao<Genre, String> genreDao;
	
	

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
				FilmBestand.class);
		
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
	 * @return 1 für Mitarbeiter, 2 für Filialleitung
	 */
	public int checkright() {
		//TODO Bitte zu ändern!!!!
		/*int recht = 3;
		try {
			if (this.genreDao.queryForAll() != null) {
				System.out.println("Adminrechte");
			}
		} catch (MySQLSyntaxErrorException e) {
			// User ist Viewuser
			recht = 2;
			System.out.println("Viewuster erkannt");
			try {
				this.filmDao = DaoManager.createDao(this.connectionSource,
						Film.class);
				if (!this.filmDao.isUpdatable()) {
					System.out.println("keine schreibrechte");
					recht = 1;
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			recht = 0;
		}
		//return recht;*/
		return 2;
	}

	public boolean isDBOnline() {
		try {
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
	 * Holt einen Film aus der Datenbank mit FilmTitel und fskals Suchkriterium.
	 * 
	 * @param filmTitel
	 *            Titel des Films
	 * @param fsk 
	 * 			FSK als Suchkriterium
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
	public int getAnzahlDVD(String filmTitel) {
		int value = 0;
		try {
			QueryBuilder<FilmBestand, String> queryBuilder = bestandDao
					.queryBuilder();
			queryBuilder.where().like("titel", "%" + filmTitel + "%").and()
					.like("medium", "DVD");

			PreparedQuery<FilmBestand> preparedQuery = queryBuilder.prepare();
			List<FilmBestand> filmList = bestandDao.query(preparedQuery);
			value = filmList.size();
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
	public int getAnzahlBluRay(String filmTitel) {
		int value = 0;
		try {
			QueryBuilder<FilmBestand, String> queryBuilder = bestandDao
					.queryBuilder();
			queryBuilder.where().like("titel", "%" + filmTitel + "%").and()
					.like("medium", "Blu%");

			PreparedQuery<FilmBestand> preparedQuery = queryBuilder.prepare();
			List<FilmBestand> filmList = bestandDao.query(preparedQuery);
			value = filmList.size();
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
			queryBuilder.where().like("titel", "%" + filmTitel + "%");

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

}
