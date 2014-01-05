import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 */

/**
 * @author Simon Krause
 * @version 1.0
 * Diese Klasse repräsentiert den View Film_ausleihen
 *
 */
@DatabaseTable(tableName = "Film_ausleihen")
public class Verliehen {

	@DatabaseField
	private Date ausleihDatum;
	@DatabaseField
	private Date rueckgabeDatum;
	@DatabaseField
	private Date leihFrist;
	@DatabaseField
	private int rechnungs_Ref;
	@DatabaseField
	private int filmExemplar_Ref;
	
	/**
	 * Default-Konstruktor
	 */
	public Verliehen(){
		
	}
	
}
