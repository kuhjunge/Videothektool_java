import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Diese Klasse repräsentiert den View Filmbestand
 * 
 * @author Simon Krause
 * @version 1.0
 */
@DatabaseTable(tableName = "Filmbestand")
public class FilmBestand {
	@DatabaseField//(id = true)
	protected int idFilm;
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
	protected String genre;
	@DatabaseField
	protected String medium;

	/**
	 * Default-Konstruktor
	 */
	public FilmBestand() {

	}

}
