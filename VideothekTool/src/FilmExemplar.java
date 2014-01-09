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
	@DatabaseField
	private boolean isVerliehen;

	/**
	 * Default-Konstruktor
	 */
	public FilmExemplar() {

	}
	
	/**
	 * @param isVerliehen the isVerliehen to set
	 */
	public void setVerliehen(boolean isVerliehen) {
		this.isVerliehen = isVerliehen;
	}

	/**
	 * @return the idExemplar
	 */
	public int getIdExemplar() {
		return idExemplar;
	}

	/**
	 * @return the isVerliehen
	 */
	public boolean isVerliehen() {
		return isVerliehen;
	}

	/**
	 * Konstruktor
	 * @param film_Ref
	 * @param medium_Ref
	 */
	public FilmExemplar(int film_Ref, int medium_Ref){		
		this.film_Ref = film_Ref;
		this.medium_Ref = medium_Ref;
		this.isVerliehen = false;
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
