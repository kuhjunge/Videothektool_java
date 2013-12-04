import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Diese Klasse repräsentiert die Entität MedienExemplar der DB
 * @author Simon Krause
 * @version 1.0
 */
@DatabaseTable(tableName = "Medienexemplar")
public class MedienExemplar {
	@DatabaseField(id = true)
	private int idExemplar;
	@DatabaseField
	private int film_ref;
	@DatabaseField
	private int medium_ref;
	
	/**
	 * Default-Konstruktor
	 */
	public MedienExemplar(){
		
	}

}
