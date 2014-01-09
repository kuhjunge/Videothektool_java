import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 */

/**
 * @author Simon Krause
 * @version 1.0
 * Diese Klasse repräsentiert den View Rechnung_view
 *
 */
@DatabaseTable(tableName = "Rechnung_view")
public class Rechnung {
	
	@DatabaseField(generatedId = true)
	private int idRechnung;
	@DatabaseField
	private double betrag;
	@DatabaseField
	private Date bezahl_Datum;
	@DatabaseField
	private int kunde_Ref;
	
	/**
	 * Default-Konstruktor
	 */
	public Rechnung(){
		
	}
	
	/**
	 * Konstruktor
	 * @param betrag
	 * @param bezahl_Datum
	 * @param kunde_Ref
	 */
	public Rechnung(double betrag, Date bezahl_Datum, int kunde_Ref){
		this.betrag = betrag;
		this.bezahl_Datum = bezahl_Datum;
		this.kunde_Ref = kunde_Ref;
	}

	/**
	 * @return the idRechnung
	 */
	public int getIdRechnung() {
		return idRechnung;
	}
	
	
	
}
