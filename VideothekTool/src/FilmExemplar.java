import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Diese Klasse repräsentiert den View Filmbestand
 * 
 * @author Simon Krause
 * @version 1.1
 */
@DatabaseTable(tableName = "Filmbestand")
public class FilmExemplar {	
	
	@DatabaseField(generatedId = true)
	private int idExemplar;
	@DatabaseField
	private int film_Ref;
	@DatabaseField
	private int medium_Ref;	

	/**
	 * Default-Konstruktor
	 */
	public FilmExemplar() {

	}

	/**
	 * @return the film_Ref
	 */
	public int getFilm_Ref() {
		return film_Ref;
	}

	/**
	 * @return the medium_Ref
	 */
	public int getMedium_Ref() {
		return medium_Ref;
	}

	
	

	
}
