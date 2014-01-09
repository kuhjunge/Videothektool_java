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

	@DatabaseField(generatedId = true)
	private int idVerliehen;
	@DatabaseField
	private Date ausleihDatum;
	@DatabaseField
	private Date rueckgabeDatum;
	@DatabaseField
	private Date leihFrist;
	@DatabaseField
	private int rechnung_Ref;
	@DatabaseField
	private int medienExemplar_Ref;
	
	/**
	 * Default-Konstruktor
	 */
	public Verliehen(){
		
	}
	

	/**
	 * Konstruktor
	 * @param ausleihDatum
	 * @param rueckgabeDatum
	 * @param leihFrist
	 * @param rechnungs_Ref
	 * @param medienExemplar_Ref
	 */
	public Verliehen(Date ausleihDatum, Date leihFrist,
			int rechnung_Ref, int medienExemplar_Ref) {
		super();
		this.ausleihDatum = ausleihDatum;		
		this.leihFrist = leihFrist;
		this.rechnung_Ref = rechnung_Ref;
		this.medienExemplar_Ref = medienExemplar_Ref;
	}
	
	
}
