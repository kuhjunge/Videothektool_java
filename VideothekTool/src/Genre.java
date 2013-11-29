import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Datentypklasse für Genre Verwaltung
 * @author Kuhjunge
 * @version 1.0
 */
@DatabaseTable(tableName = "Genre")
public class Genre {
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Genre [idGenre=" + idGenre + ", Name=" + Name + "]";
	}

	@DatabaseField(id = true)
	private int idGenre;
	@DatabaseField
	private String Name;
	
	/**
	 * Datentypklasse für Genre Verwaltung
	 * @param id Die ID
	 * @param Name Der Name des Genres
	 */
	public Genre(int id, String name)
	{
		this.idGenre = id;
		this.Name = name;
	}
	
	public Genre()
	{
		// Für DAO
	}
	
	public String getGenre() {
		return this.Name;
	}
	
	public Integer getID() {
		return this.idGenre;
	}
}
