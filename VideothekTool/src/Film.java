import java.util.Date;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Diese Klasse repräsentiert den View Film_Stammdaten
 * 
 * @author Simon Krause, Chris Deter
 * @version 1.1
 * 
 */
@DatabaseTable(tableName = "Film_Stammdaten")
public class Film {
	@DatabaseField
	protected String titel;
	@DatabaseField
	protected int jahr;
	@DatabaseField
	protected int laufzeit;
	@DatabaseField
	protected double imdb_Wertung;
	@DatabaseField
	protected String plot;
	@DatabaseField
	protected int fsk;
	@DatabaseField
	protected double grundpreis;
	@DatabaseField
	protected double neuheiten_Zuschlag;
	@DatabaseField
	protected Date neu_Bis;
	@DatabaseField
	protected Integer genre_ref;

	/**
	 * default Konstruktor
	 */
	public Film() {

	}

	/**
	 * @param idFilm
	 *            Die ID des Filmes. Wenn keine vorhanden mit 0 Kennzeichnen
	 */
	public Film(String titel, int jahr, int laufzeit,
			double imdbWertung, String plot, int fsk, double grundpreis,
			double neuheitenZuschlag, Integer genre_ref) {
		super();		
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
		return "Filmdatensatz: Titel=" + titel + ", Jahr="
				+ this.jahr + ", Laufzeit=" + laufzeit + ", IMDB="
				+ imdb_Wertung + ", Plot=" + plot + ", FSK=" + fsk
				+ ", Grundpreis=" + grundpreis + ", N-Zuschlag="
				+ neuheiten_Zuschlag + ", Neu bis=" + neu_Bis + ", Genre="
				+ genre_ref + ".\n";
	}

}
