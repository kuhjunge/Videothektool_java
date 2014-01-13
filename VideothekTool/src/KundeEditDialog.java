import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;

/**
 * Diese Klasse verwaltet ein Fenster welches einen Film in die Datenbank
 * einfügen oder ändern kann.
 * 
 * @author Chris Deter
 * 
 */
public class KundeEditDialog extends JDialog {
	private static final long serialVersionUID = 4482694704840588709L;
	private DBController db;
	private JTextField textFieldName;
	private JTextField textFieldGeb;
	private JTextField textFieldTele;
	private JTextField textFieldMail;

	/**
	 * Setzt eine Kundenauswahl in das Fenster!
	 * 
	 * @param kunde
	 *            Das Kundenobjekt des ausgewählten Filmes
	 */
	public void setKunden(Kunde kunde) {
		if (kunde != null) {
			// TODO: Kunde set Methode implementieren
		}
	}

	/**
	 * leert das Kundenmanager Fenster!
	 */
	public void clear() {
		// TODO: Kunde clear Methode implementieren
	}

	/**
	 * Create the dialog.
	 * 
	 * @param videothekFrame
	 */
	public KundeEditDialog(KundenDialog kundenDialog, DBController dbi) {
		
		setResizable(false);
		getContentPane().setLayout(null);
		
		setModal(true);
		setTitle("Kundenmanager");
		db = dbi;
		if (!db.isDBOnline()) {
			dispose();
		}
		setBounds(100, 100, 250, 184);
		getContentPane().setLayout(null);
		
		JLabel lblKundenID = new JLabel("KundenID:");
		lblKundenID.setBounds(10, 11, 224, 14);
		getContentPane().add(lblKundenID);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 36, 88, 14);
		getContentPane().add(lblName);
		
		JLabel lblGeburtstag = new JLabel("Geburtstag:");
		lblGeburtstag.setBounds(10, 61, 88, 14);
		getContentPane().add(lblGeburtstag);
		
		JLabel lblTelefon = new JLabel("Telefonnummer:");
		lblTelefon.setBounds(10, 86, 88, 14);
		getContentPane().add(lblTelefon);
		
		JLabel lblEMail = new JLabel("E-Mail");
		lblEMail.setBounds(10, 111, 88, 14);
		getContentPane().add(lblEMail);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(100, 33, 134, 20);
		getContentPane().add(textFieldName);
		textFieldName.setColumns(10);
		
		textFieldGeb = new JTextField();
		textFieldGeb.setBounds(100, 58, 134, 20);
		getContentPane().add(textFieldGeb);
		textFieldGeb.setColumns(10);
		
		textFieldTele = new JTextField();
		textFieldTele.setBounds(100, 83, 134, 20);
		getContentPane().add(textFieldTele);
		textFieldTele.setColumns(10);
		
		textFieldMail = new JTextField();
		textFieldMail.setBounds(100, 108, 134, 20);
		getContentPane().add(textFieldMail);
		textFieldMail.setColumns(10);

		// TODO: Kunde Edit Formular erstellen
	}
}