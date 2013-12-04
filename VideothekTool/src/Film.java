import java.util.Date;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Diese Klasse repräsentiert die Entität Film der DB
 * @author Simon Krause, Chris Deter
 * @version 1.1
 * 
 */
@DatabaseTable(tableName = "Film")
public class Film {
	@DatabaseField(id = true)
	private int idFilm;
	@DatabaseField
	private String titel;
	@DatabaseField
	private int jahr;
	@DatabaseField
	private int laufzeit;
	@DatabaseField
	private double imdb_Wertung;
	@DatabaseField
	private String plot;
	@DatabaseField
	private int fsk;
	@DatabaseField
	private double grundpreis;
	@DatabaseField
	private double neuheiten_Zuschlag;
	@DatabaseField
	private Date neu_Bis;
	@DatabaseField
	private Integer genre_ref;

	/**
	 * default Konstruktor
	 */
	public Film() {

	}

	/**
	 * @param idFilm
	 *            Die ID des Filmes. Wenn keine vorhanden mit 0 Kennzeichnen
	 */
	public Film(int idFilm, String titel, int jahr, int laufzeit,
			double imdbWertung, String plot, int fsk, double grundpreis,
			double neuheitenZuschlag, Integer genre_ref) {
		super();
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
		this.genre_ref = genre_ref;
	}

	
	
	public String getTitel() {
		return this.titel;
	}

	/**
	 * @return the id
	 */
	public int getID() {
		return this.idFilm;
	}
	
	/**
	 * @return the id
	 */
	public int getGenre() {
		return this.genre_ref;
	}

	/**
	 * @return the year
	 */
	public int getJahr() {
		return this.jahr;
	}

	/**
	 * @return the laufzeit
	 */
	public int getLaufzeit() {
		return laufzeit;
	}

	/**
	 * @return the imdbwertung
	 */
	public double getImdb_Wertung() {
		return imdb_Wertung;
	}

	/**
	 * @return the plot
	 */
	public String getPlot() {
		return plot;
	}

	/**
	 * @return the fsk
	 */
	public int getFsk() {
		return fsk;
	}

	/**
	 * 
	 * @return GrundPreis
	 */
	public double getGrundPreis() {
		return this.grundpreis;
	}

	/**
	 * toString-Methode
	 */
	@Override
	public String toString() {
		return "Filmdatensatz: ID=" + idFilm + ", Titel=" + titel + ", Jahr="
				+ this.jahr + ", Laufzeit=" + laufzeit + ", IMDB="
				+ imdb_Wertung + ", Plot=" + plot + ", FSK=" + fsk
				+ ", Grundpreis=" + grundpreis + ", N-Zuschlag="
				+ neuheiten_Zuschlag + ", Neu bis=" + neu_Bis + ", Genre="
				+ genre_ref + ".\n";
	}

}
