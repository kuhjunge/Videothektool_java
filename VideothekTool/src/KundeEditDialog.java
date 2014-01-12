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
	private JPanel contentPanel = new JPanel();
	private DBController db;

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
		setBounds(100, 100, 450, 259);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(98, 177, 89, 23);
		getContentPane().add(btnNewButton);

		// TODO: Kunde Edit Formular erstellen
	}
	// TODO: Kunde löschen / speichern Funktion 
}