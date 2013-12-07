import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Diese Klasse repräsentiert die Entität MediumArt der DB
 * @author Simon Krause
 * @version 1.0
 */
@DatabaseTable(tableName = "MediumArt")
public class MediumArt {
	@DatabaseField(id = true)
	private int idMedium;
	@DatabaseField
	private String nameMedium;
	@DatabaseField
	private double medienAufschlag;
	
	/**
	 * Default-Konstruktor
	 */
	public MediumArt(){
		
	}

}
