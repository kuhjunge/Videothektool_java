import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Diese Klasse repräsentiert die Entität Kunde der DB
 * @author Simon Krause
 * @version 1.0
 *
 */
@DatabaseTable(tableName = "Kunde")
public class Kunde {
	@DatabaseField(id = true)
	private int idKunde;
	@DatabaseField
	private String vorname;
	@DatabaseField
	private String nachname;
	@DatabaseField
	private String strasse;
	@DatabaseField
	private String hausnr;
	@DatabaseField
	private String plz;
	@DatabaseField
	private String stadt;
	@DatabaseField
	private String gebDatum;
	@DatabaseField
	private String telNr;
	@DatabaseField
	private String email;
	
	/**
	 * Default Konstruktor
	 */
	public Kunde(){
		
	}
}
