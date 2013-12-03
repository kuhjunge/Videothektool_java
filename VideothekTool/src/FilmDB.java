import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

/**
 * @author Kuhjunge, Simon Krause, Rene Kremer
 * @version 1.0.4
 * 
 */
public class FilmDB {
	private String username = "";
	private String password = "";
	// private String driver = "com.mysql.jdbc.Driver";
	private String url = "";
	private ConnectionSource connectionSource = null;
	private String dbName = "";
	/**
	 * Dao von ORMLite
	 */
	private Dao<Film, String> filmDao;
	private Dao<Genre, String> genreDao;

	/**
	 * Default- Konstruktor
	 */
	public FilmDB() {
		SaveLoader s = new SaveLoader();
		s.read();
		username = s.getUsername();
		password = s.getPassword();
		url = s.getUrl();
		dbName = s.getDbName();
		this.connect();
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
	public void connect() {
		try {
			String databaseUrl = this.url + "/" + this.dbName + "?user="
					+ username + "&password=" + password;

			// create a connection source to our database
			this.connectionSource = new JdbcConnectionSource(databaseUrl);

			// instantiate the dao
			this.filmDao = DaoManager.createDao(this.connectionSource,
					Film.class);
			this.genreDao = DaoManager.createDao(this.connectionSource,
					Genre.class);

		} catch (Exception e) {
			System.out.println(e.toString());
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
	public Map<Integer,String> getGenre() {
		try {
			Map<Integer,String> m = new HashMap<Integer,String>();
			List<Genre> l =  this.genreDao.queryForAll();
			for(Genre g : l)
			{
				m.put(g.getID(), g.getGenre());
			}
			return m;
		} catch (Exception e) {
			System.out.println(e.toString() + " Fehler beim laden der Genre");
			return null;
		}
	}

	/**
	 * Holt einen Film aus der Datenbank mit FilmTitel als Suchkriterium. Es
	 * wird aber nur der erste gefundene Film mit diesem Titel zurückgegeben.
	 * D.h. alle Filmtitel sollten eindeutig sein.
	 * 
	 * @param filmTitel
	 *            Titel des Films
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
			String query = "Select count(m.idExemplar) From Film f, Medienexemplar m, Mediumart ma "
					+ "Where f.idFilm = m. Film_ref And  m.Medium_ref = ma.idMedium "
					+ "and f.Titel = '"
					+ filmTitel
					+ "' and ma.nameMedium = 'DVD'";
			GenericRawResults<String[]> rawResults = filmDao.queryRaw(query);
			List<String[]> ergebnis = rawResults.getResults();

			for (String[] str : ergebnis) {
				for (String s : str) {
					value = Integer.valueOf(s);
				}
			}

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

			String query = "Select count(m.idExemplar) From Film f, Medienexemplar m, Mediumart ma "
					+ "Where f.idFilm = m. Film_ref And  m.Medium_ref = ma.idMedium "
					+ "and f.Titel = '"
					+ filmTitel
					+ "' and ma.nameMedium = 'Blu-Ray'";
			GenericRawResults<String[]> rawResults = filmDao.queryRaw(query);
			List<String[]> ergebnis = rawResults.getResults();

			for (String[] str : ergebnis) {
				for (String s : str) {
					value = Integer.valueOf(s);
				}
			}

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
	 * Diese Methode ruft die StoredProcedure "UnpaidInvoice" auf.
	 * 
	 * @return Gibt einen String aller offenen Rechnungen zurück
	 */
	public String unpaidInvoice() {
		String str = "";
		try {
			String query = "call unpaidInvoice()";
			GenericRawResults<String[]> rawResults = filmDao.queryRaw(query);
			List<String[]> ergebnis = rawResults.getResults();
			String[] arrayResult = new String[ergebnis.size()];
			str += "RechnungsNr\t\tKundenNr\t\tVorname\t\tNachname\t\tRechnungsdatum\tbezahlt am\t\tBetrag\n";
			for (int i = 0; i < ergebnis.size(); i++) {
				arrayResult = ergebnis.get(i);
				for (String s : arrayResult) {
					str += s + "\t\t";
				}
				str += "\n";
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return str;
	}

	/**
	 * Diese Methode schreibt ein Filmobjekt in die Datenbank
	 * 
	 * @return Gibt an ob die Schreiboperation erfolgreich war
	 */
	public boolean writeMovie(Film f) {
		try {
			this.filmDao.createOrUpdate(f);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
