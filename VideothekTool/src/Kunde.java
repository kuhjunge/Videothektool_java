import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Diese Klasse repräsentiert den View Kundendaten
 * @author Simon Krause
 * @version 1.0
 */
@DatabaseTable(tableName = "Kundendaten")
public class Kunde {
	
	@DatabaseField(generatedId = true)
	private int idKunde;
	@DatabaseField
	private String nachname;
	@DatabaseField
	private String vorname;
	@DatabaseField
	private Date geburtsDatum;
	@DatabaseField
	private String telefonnummer;
	@DatabaseField
	private String email;
	
	/**
	 * Default-Konstruktor
	 */
	public Kunde(){
		this("","",new Date(0),"","");
	}
	
	public Kunde(String name, String vorname, Date geb, String tel, String email){
		this.nachname = name;
		this.vorname = vorname;
		this.geburtsDatum = geb;
		this.telefonnummer = tel;
		this.email = email;
	}
	
	public String getName() {
		return nachname;
	}
	public String getVorname() {
		return vorname;
	}
	public Date getGeburtsDatum() {
		return geburtsDatum;
	}
	public String getTelefonnummer() {
		return telefonnummer;
	}
	public String getEmail() {
		return email;
	}
	
}
