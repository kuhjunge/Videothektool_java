import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.BoxLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;

public class FilmBestandAendernDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5110370406318458045L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	/**
	 * Oberfenster, das diesen Dialog aufruft,
	 * wird genutzt um den Table aus Oberfenster zu aktualisieren,
	 * wenn hier Änderungen kommen
	 */
	private VideothekFrame topFrame;

	/**
	 * DBController
	 */
	private DBController db;
	/**
	 * Ausgewählter Film
	 */
	private int idFilm = -1;
	private JSpinner spinner_1;
	private JComboBox comboBox;

	/**
	 * Create the dialog.
	 */
	public FilmBestandAendernDialog(DBController db, VideothekFrame topFrame) {
		addComponentListener(new ComponentAdapter() {
			/**
			 * Aufruf des Dialogs
			 */
			@Override
			public void componentShown(ComponentEvent arg0) {
				updateUI();
			}

		});
		this.db = db;
		this.topFrame = topFrame;
		setTitle("Anzahl Filialexemplare \u00E4ndern");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 500, 100);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			textField = new JTextField();
			textField.setToolTipText("Ausgew\u00E4hlter Film");
			textField.setEditable(false);
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		{
			comboBox = new JComboBox();
			contentPanel.add(comboBox);
		}
		{
			spinner_1 = new JSpinner();
			spinner_1.setModel(new SpinnerNumberModel(0, 0, 99, 1));
			contentPanel.add(spinner_1);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					/**
					 * Änderungen übernehmen
					 */
					public void actionPerformed(ActionEvent arg0) {
						int value = abfrageDialog("Änderungen übernehmen?");
						if (value == JOptionPane.YES_OPTION) {
							writeValues();
						}
						if (value != JOptionPane.CANCEL_OPTION) {
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					/**
					 * Ohne Änderungen zurück
					 */
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * Der Ausgewählte Film aus anderer Klasse heraus kann gesetzt werden
	 * 
	 * @param idFilm
	 */
	public void setFilm(int idFilm) {
		this.idFilm = idFilm;
	}

	/**
	 * Die UI wird aktualisiert
	 */
	private void updateUI() {
		// alte Werte zurücksetzen
		textField.setText("");
		comboBox.removeAllItems();
		// Werte neu setzen
		textField.setText(db.getFilm(idFilm).getTitel());
		List<Medium> medium = db.getMedium();
		for (Medium str : db.getMedium()) {
			comboBox.addItem(str.getNameMedium());
		}
		spinner_1.setValue(db.getAnzahlPraesent(idFilm, medium.get(0)
				.getIdMedium()));

		comboBox.addItemListener(new ItemListener() {
			/**
			 * Aufruf, wenn Medium geändert wird
			 * 
			 * @param itemEvent
			 */
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					String str = (String) itemEvent.getItem();
					spinner_1.setValue(db.getAnzahlPraesent(idFilm, db
							.getMedium(str).getIdMedium()));
				}
			}

		});

	}

	/**
	 * Die Änderungen werden geschrieben
	 */
	private void writeValues() {
		Medium medium = db.getMedium((String) comboBox.getSelectedItem());
		try {
			int value = (int) spinner_1.getValue()
					- db.getAnzahl(idFilm, medium.getIdMedium());
			if (value > 0) {
				for (int i = 0; i < value; i++) {
					db.addExemplar(idFilm, medium.getIdMedium());
				}
			}
			if (value < 0) {
				//Lösche solange, bis Differenz == 0;
				for (int a = 0; a < -value; a++) {
					List<FilmExemplar> exemplare = db.getExemplare(idFilm,
							medium.getIdMedium(), false);
					String[] possibilities = new String[exemplare.size() + 1];
					Film film = db.getFilm(exemplare.get(0).getFilm_Ref());

					possibilities[0] = "nichts löschen";
					for (int i = 1; i < possibilities.length; i++) {
						possibilities[i] = film.getTitel() + " - ID: "
								+ exemplare.get(i - 1).getIdExemplar();
					}
					String s = (String) JOptionPane.showInputDialog(this,
							"Welches Exemplar des Films wollen Sie löschen",
							"Löschen eines Exemplars",
							JOptionPane.PLAIN_MESSAGE, null, possibilities,
							"nichts löschen");

					// Wenn eine Auswahl getroffen wurde, lösche dieses Exemplar
					if ((s != null) && !s.isEmpty()
							&& !s.equals("nichts löschen")) {
						int i = s.lastIndexOf("- ID: ") + 6;
						String str = s.substring(i);
						db.deleteExemplar(Integer.parseInt(str));
					}					
				}
				//Aktualisiere den Table aus VideothekFrame
				topFrame.setTableValues();
			}
		} catch (Exception e) {
			System.out.println(e.toString()
					+ " Fehler beim Schreiben der neuen Anzahl");
		}	
	}

	/**
	 * diese Methode öffnet einen FrageDialog
	 * 
	 * @param str
	 * @return
	 */
	private int abfrageDialog(String str) {
		return JOptionPane.showConfirmDialog(this, str);
	}

}
