import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 */

/**
 * @author Simon Krause
 *@version 1.0
 *Diese Klasse repräsentiert die Tabelle Mediumart
 */
@DatabaseTable(tableName = "Mediumart")
public class Medium {
	@DatabaseField(generatedId = true)
	private int idMedium;;
	@DatabaseField
	private String nameMedium;
	@DatabaseField
	private double medienAufschlag;
	
	/**
	 * Default-Konstruktor
	 */
	public Medium(){
		
	}

	/**
	 * @return the idMedium
	 */
	public int getIdMedium() {
		return idMedium;
	}

	/**
	 * @return the nameMedium
	 */
	public String getNameMedium() {
		return nameMedium;
	}

	/**
	 * @return the medienAufschlag
	 */
	public double getMedienAufschlag() {
		return medienAufschlag;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return nameMedium;
	}
			
	
}
