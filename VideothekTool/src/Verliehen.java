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


	/**
	 * @return the idVerliehen
	 */
	public int getIdVerliehen() {
		return idVerliehen;
	}


	/**
	 * @return the ausleihDatum
	 */
	public Date getAusleihDatum() {
		return ausleihDatum;
	}


	/**
	 * @return the leihFrist
	 */
	public Date getLeihFrist() {
		return leihFrist;
	}


	/**
	 * @return the medienExemplar_Ref
	 */
	public int getMedienExemplar_Ref() {
		return medienExemplar_Ref;
	}


	/**
	 * @param rueckgabeDatum the rueckgabeDatum to set
	 */
	public void setRueckgabeDatum(Date rueckgabeDatum) {
		this.rueckgabeDatum = rueckgabeDatum;
	}
	
	/**
	 * @return the rechnung_Ref
	 */
	public int getRechnung_Ref() {
		return rechnung_Ref;
	}

	
	
}
