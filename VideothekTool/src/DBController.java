import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

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
	 * Dao für Tabelle Mediumart
	 */
	private Dao<Medium, String> mediumDao;

	/**
	 * Dao für View Verliehen
	 */
	private Dao<Verliehen, String> verliehenDao;

	/**
	 * Dao für View Rechnung
	 */
	private Dao<Rechnung, String> rechnungDao;

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
		this.verliehenDao = DaoManager.createDao(this.connectionSource,
				Verliehen.class);
		this.mediumDao = DaoManager.createDao(this.connectionSource,
				Medium.class);
		this.rechnungDao = DaoManager.createDao(this.connectionSource,
				Rechnung.class);

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
		// TODO Experimentelle Funktion, bitte ausgiebig testen (Bugs an Chris
		// melden)

		int recht = 2;
		try {
			if (this.genreDao.queryForAll() != null) {
				System.out.println("Adminrechte");
			}
		} catch (MySQLSyntaxErrorException e) { // User ist Viewuser recht = 2;
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
		} // return recht;

		return recht;
	}

	/**
	 *  Datenbankstatus Prüftool. Wird im AddMovieDialog.java gebraucht.
	 * 
	 * @return Statuswert ob Datenbank Online ist
	 */
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
	 * Holt einen Film aus der Datenbank mit FilmTitel und fsk als
	 * Suchkriterium.
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
						.ge("fsk", Integer.valueOf(fsk));
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
	 * Gibt die Anzahl der vorhandenen Exemplare einer Mediumsart zurück
	 * 
	 * @param idFilm
	 *            zu suchender Film
	 * @param idMedium
	 *            die MediumsArt
	 * @return Rückgabe der Anzahl der Filme als DVD
	 */
	public int getAnzahl(int idFilm, int idMedium) {
		int value = 0;
		try {
			QueryBuilder<FilmExemplar, String> qb = bestandDao.queryBuilder();
			qb.where().eq("film_Ref", idFilm).and().eq("medium_Ref", idMedium);
			List<FilmExemplar> exemplare = bestandDao.query(qb.prepare());
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
	public List<Medium> getMedium() {
		try {
			return this.mediumDao.queryForAll();

		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	/**
	 * Diese Methode gibt das Medium anhand seines Namens zurück
	 * 
	 * @param mediumArt
	 * @return
	 */
	public Medium getMedium(String mediumArt) {
		try {
			QueryBuilder<Medium, String> qb = mediumDao.queryBuilder();
			qb.where().eq("nameMedium", mediumArt);

			List<Medium> list = mediumDao.query(qb.prepare());
			return list.get(0);

		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	/**
	 * Diese Methode gibt das Medium anhand seiner ID zurück
	 * 
	 * @param idMedium
	 * @return
	 */
	public Medium getMedium(int idMedium) {
		try {
			QueryBuilder<Medium, String> qb = mediumDao.queryBuilder();
			qb.where().eq("idMedium", idMedium);

			List<Medium> list = mediumDao.query(qb.prepare());
			return list.get(0);

		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
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
	 * 
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
	 * Diese Methode gibt einen Kunden anhand seiner ID zurück
	 * 
	 * @param idKunde
	 * @return
	 */
	public Kunde getKunde(int idKunde) {
		try {
			QueryBuilder<Kunde, String> qB = kundeDao.queryBuilder();
			qB.where().eq("idKunde", idKunde);
			List<Kunde> kunden = kundeDao.query(qB.prepare());
			return kunden.get(0);
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	/**
	 * Diese Methode gibt alle nicht verliehenen Exemplare des Film eines
	 * Mediums zurück
	 * 
	 * @param idFilm
	 *            Exemplare dieses Films
	 * @param idMedium
	 *            und dieses Mediums
	 * @return
	 */
	public int getAnzahlPraesent(int idFilm, int idMedium) {
		int value = 0;
		try {
			QueryBuilder<FilmExemplar, String> qb = bestandDao.queryBuilder();
			qb.where().eq("film_Ref", idFilm).and().eq("medium_Ref", idMedium)
					.and().eq("isVerliehen", false);
			List<FilmExemplar> exemplare = bestandDao.query(qb.prepare());

			value = exemplare.size();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return value;
	}

	/**
	 * Diese Methode löscht, sofern kein Film mehr ausgeliehen ist, alle
	 * Exemplare und den Filmtitel aus dem Sortiment
	 * 
	 * @param idFilm
	 */
	public void deleteFilm(int idFilm) {
		try {
			List<Medium> list = this.getMedium();
			boolean status = true;
			for (Medium value : list) {
				if (getAnzahl(idFilm, value.getIdMedium()) != getAnzahlPraesent(
						idFilm, value.getIdMedium())) {
					status = false;
				}
			}
			if (status) {
				QueryBuilder<FilmExemplar, String> qB2 = bestandDao
						.queryBuilder();
				qB2.where().eq("film_Ref", idFilm);
				List<FilmExemplar> exemplare = bestandDao.query(qB2.prepare());

				bestandDao.delete(exemplare);
				filmDao.deleteById(String.valueOf(idFilm));
			}

		} catch (Exception e) {
			System.out.println(e.toString()
					+ "Fehler beim Löschen eines Films und deren Exemplaren");
		}
	}

	/**
	 * Diese Methode fügt ein Exemplar des Mediums und Films hinzu
	 * 
	 * @param idFilm
	 * @param idMedium
	 */
	public void addExemplar(int idFilm, int idMedium) {
		try {
			bestandDao.create(new FilmExemplar(idFilm, idMedium));
		} catch (Exception e) {
			System.out.println(e.toString()
					+ "Fehler bei erzeugen neuer Exemplare");
		}
	}

	/**
	 * Gibt eine Liste von Film von Medium zurück, ob verliehen oder nicht
	 * 
	 * @param idFilm
	 * @param idMedium
	 * @param isVerliehen
	 * @return
	 */
	public List<FilmExemplar> getExemplare(int idFilm, int idMedium,
			boolean isVerliehen) {
		List<FilmExemplar> exemplare = new LinkedList<FilmExemplar>();
		try {
			QueryBuilder<FilmExemplar, String> qb = bestandDao.queryBuilder();
			qb.where().eq("film_Ref", idFilm).and().eq("medium_Ref", idMedium)
					.and().eq("isVerliehen", isVerliehen);

			exemplare = bestandDao.query(qb.prepare());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return exemplare;
	}

	/**
	 * Diese Methode löscht ein Filmexemplar
	 * 
	 * @param idExemplar
	 */
	public void deleteExemplar(int idExemplar) {
		try {
			bestandDao.deleteById(String.valueOf(idExemplar));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * Diese Methode gibt ein FilmExemplar anhand seiner ID zurück
	 * 
	 * @param idExemplar
	 * @return
	 */
	public FilmExemplar getExemplar(int idExemplar) {
		List<FilmExemplar> list = new LinkedList<FilmExemplar>();
		try {
			QueryBuilder<FilmExemplar, String> qb = bestandDao.queryBuilder();
			qb.where().eq("idExemplar", idExemplar);
			list = bestandDao.query(qb.prepare());
			return list.get(0);
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	/**
	 * Diese Methode erzeugt einen neuen Eintrag in Verliehen für ein bestimmtes
	 * FilmExemplar für eine bestimmte Rechnung, und setzt die
	 * Leihfrist(RückgabeDatum) und das heutige Datum sowie wird der Flag
	 * isVerliehen des Exemplares gesetzt
	 * 
	 * @param idExemplar
	 * @param leihfrist
	 * @param idRechnung
	 */
	public void writeVerliehen(int idExemplar, Date leihfrist, int idRechnung) {
		try {
			// Berechnung java.sql.Date
			java.util.Date date = new java.util.Date();
			DateFormat df_sql = new SimpleDateFormat("yyyy-MM-dd");
			String str2 = df_sql.format(date);
			java.sql.Date date_sql = java.sql.Date.valueOf(str2);
			// --
			Verliehen obj = new Verliehen(date_sql, leihfrist, idRechnung,
					idExemplar);
			verliehenDao.create(obj);

			// setze isVerliehenFlag des Exemplars
			FilmExemplar exemplar = bestandDao.queryForId(String
					.valueOf(idExemplar));
			exemplar.setVerliehen(true);
			bestandDao.update(exemplar);

		} catch (Exception e) {
			System.out.println(e.toString()
					+ "Fehler beim Schreiben von Verleihen");
		}
	}

	/**
	 * Diese Methode schreibt eine Rechnung, für einen Kunden mit einem Betrag
	 * und gibt seine ID zurück
	 * 
	 * @param idKunde
	 * @param betrag
	 * @return
	 */
	public int writeRechnung(int idKunde, double betrag) {
		try {
			// Berechnung java.sql.Date
			java.util.Date date = new java.util.Date();
			DateFormat df_sql = new SimpleDateFormat("yyyy-MM-dd");
			String str2 = df_sql.format(date);
			java.sql.Date date_sql = java.sql.Date.valueOf(str2);

			Rechnung rechnung = new Rechnung(betrag, date_sql, idKunde);
			rechnungDao.create(rechnung);

			return rechnung.getIdRechnung();
		} catch (Exception e) {
			System.out.println(e.toString()
					+ "Fehler beim Schreiben der Rechnung");
			return -1;
		}
	}

	/**
	 * Diese Methode gibt alle Exemplare zurück, die entweder verliehen oder
	 * nicht sind
	 * 
	 * @param isVerliehen
	 * @return
	 */
	public List<FilmExemplar> getExemplar(boolean isVerliehen) {
		List<FilmExemplar> exemplare = new LinkedList<FilmExemplar>();
		try {
			QueryBuilder<FilmExemplar, String> qb = bestandDao.queryBuilder();
			qb.where().eq("isVerliehen", isVerliehen);
			exemplare = bestandDao.query(qb.prepare());
			return exemplare;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	/**
	 * Diese Methode gibt alle Verliehen-Datensätze zurück, die noch kein
	 * RückgabeDatum haben
	 * 
	 * @return
	 */
	public List<Verliehen> getVerliehen() {
		List<Verliehen> list = new LinkedList<Verliehen>();
		try {
			QueryBuilder<Verliehen, String> qb = verliehenDao.queryBuilder();
			qb.where().isNull("rueckgabeDatum");
			list = verliehenDao.query(qb.prepare());
			return list;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	/**
	 * Diese Methode gibt den zu einer Rechnung gehörende KundenID zurück
	 * 
	 * @param idRechnung
	 * @return
	 */
	public int getKundeRechnung(int idRechnung) {
		int value = -1;
		try {
			Rechnung rechnung = rechnungDao.queryForId(String
					.valueOf(idRechnung));
			value = rechnung.getKunde_Ref();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return value;
	}

	/**
	 * Diese Methode setzt das RueckgabeDatum eines Verliehen-Obj
	 * 
	 * @param idVerliehen
	 * @param rueckgabeDate
	 */
	public void updateVerliehen(int idVerliehen, java.sql.Date rueckgabeDate) {
		try {
			Verliehen verliehen = verliehenDao.queryForId(String
					.valueOf(idVerliehen));
			verliehen.setRueckgabeDatum(rueckgabeDate);
			verliehenDao.update(verliehen);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * Diese Methode setzt den isVerliehenFlag von FilmExemplar auf false
	 * 
	 * @param idExemplar
	 */
	public void updateExemplar(int idExemplar) {
		try {
			FilmExemplar exemplar = bestandDao.queryForId(String
					.valueOf(idExemplar));
			exemplar.setVerliehen(false);
			bestandDao.update(exemplar);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * Diese Methode gibt ein Verliehen-Obj zurück, das sich durch Suche von
	 * offenem Rueckgabedatum und idFilmExemplar ergibt
	 * 
	 * @param idExemplar
	 * @return
	 */
	public Verliehen getVerliehen(int idExemplar) {
		try {
			QueryBuilder<Verliehen, String> qb = verliehenDao.queryBuilder();
			qb.where().isNull("rueckgabeDatum").and()
					.eq("medienExemplar_Ref", idExemplar);

			List<Verliehen> list = verliehenDao.query(qb.prepare());
			return list.get(0);
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

}
