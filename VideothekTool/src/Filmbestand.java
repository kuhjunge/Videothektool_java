import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 */

/**
 * @author Kuhjunge
 * 
 */
@DatabaseTable(tableName = "Filmbestand")
public class Filmbestand extends Film {

	@DatabaseField
	private String genre;

	@DatabaseField
	private double medienaufschlag;

	public Filmbestand(int idFilm, String titel, int jahr, int laufzeit,
			double imdbWertung, String plot, int fsk, double grundpreis,
			double neuheitenZuschlag, String genre, double medienaufschlag) {
		if (idFilm > 0)
			this.idFilm = idFilm;
		this.titel = titel;
		this.jahr = jahr;
		this.laufzeit = laufzeit;
		this.imdb_Wertung = imdbWertung;
		this.plot = plot;
		this.fsk = fsk;
		this.grundpreis = grundpreis;
		this.neuheiten_Zuschlag = neuheitenZuschlag;
		this.neu_Bis = null;
		this.genre_ref = 0;
		this.genre = genre;
		this.medienaufschlag = medienaufschlag;

	}

	public Filmbestand() {
	}

	/**
	 * @return the genre
	 */
	public String getStringGenre() {
		return genre;
	}

	/**
	 * @param genre
	 *            the genre to set
	 */
	public void setStringGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * @return the medienaufschlag
	 */
	public double getMedienaufschlag() {
		return medienaufschlag;
	}

	/**
	 * @param medienaufschlag
	 *            the medienaufschlag to set
	 */
	public void setMedienaufschlag(double medienaufschlag) {
		this.medienaufschlag = medienaufschlag;
	}
}
