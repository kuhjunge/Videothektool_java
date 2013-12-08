import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Diese Klasse repräsentiert den View offeneRechnungen
 * @author Simon Krause
 * @version 1.0
 */
@DatabaseTable(tableName = "offeneRechnung")
public class unPaidInvoice {
	@DatabaseField
	private Date rechnung_vom;
	@DatabaseField
	private double rechnungsBetrag;
	@DatabaseField
	private String nachname;
	@DatabaseField
	private String vorname;
	@DatabaseField
	private String telefonnummer;
	@DatabaseField
	private String email;
	
	/**
	 * Default-Konstuktor
	 */
	public unPaidInvoice(){
		
	}

	public Date getRechnung_vom() {
		return rechnung_vom;
	}

	public double getRechnungsBetrag() {
		return rechnungsBetrag;
	}

	public String getNachname() {
		return nachname;
	}

	public String getVorname() {
		return vorname;
	}

	public String getTelefonnummer() {
		return telefonnummer;
	}

	public String getEmail() {
		return email;
	}

}
