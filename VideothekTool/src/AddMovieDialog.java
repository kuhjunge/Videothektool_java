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
public class AddMovieDialog extends JDialog {

	private static final long serialVersionUID = -4477118282765121472L;
	private JPanel contentPanel = new JPanel();
	private JTextField textFieldTitle;
	private JTextField textFieldYear;
	private JTextField textFieldPlaytime;
	private JTextField textFieldIMDB;
	private JTextField textFieldFSK;
	private JTextField textFieldBasicCost;
	private JTextField textFieldNewCost;
	private JTextField textFieldNewto;
	private JComboBox<String> comboBoxGenre = new JComboBox<String>();
	private JTextArea txtrStory;
	private Film f = null;
	private JButton btnFilmHinzufgen = new JButton("Film hinzuf\u00FCgen");
	private int filmID = 0;
	private DBController db;
	Map<Integer, String> genre;
	/**
	 * Oberfenster, das diesen Dialog aufruft,
	 * wird genutzt um den Table aus Oberfenster zu aktualisieren,
	 * wenn hier Änderungen kommen
	 */
	private VideothekFrame topFrame;

	/**
	 * Setzt eine Filmauswahl in das Fenster!
	 * 
	 * @param film
	 *            Das Filmobjekt des ausgewählten Filmes
	 */
	public void setFilm(Film film) {
		if (film != null) {
			this.f = film;
			textFieldTitle.setText(f.getTitel());
			textFieldYear.setText(f.getJahr() + "");
			textFieldPlaytime.setText(f.getLaufzeit() + "");
			textFieldIMDB.setText(f.getImdb_Wertung() + "");
			txtrStory.setText(f.getPlot());
			textFieldFSK.setText(f.getFsk() + "");
			textFieldBasicCost.setText("0");
			textFieldNewCost.setText("0");
			this.comboBoxGenre.setSelectedItem(genre.get(f.getGenre()));			
			this.btnFilmHinzufgen.setText("Film aendern");
		}
	}

	/**
	 * leert das Filmmanager Fenster!
	 */
	public void clear() {
		this.f = null;
		textFieldTitle.setText("");
		textFieldYear.setText("");
		textFieldPlaytime.setText("");
		textFieldIMDB.setText("");
		txtrStory.setText("");
		textFieldFSK.setText("");
		textFieldBasicCost.setText("0");
		textFieldNewCost.setText("0");
		this.comboBoxGenre.setSelectedItem("1");
		filmID = 0;
		this.btnFilmHinzufgen.setText("Film hinzuf\u00FCgen");
	}

	/**
	 * Create the dialog.
	 * 
	 * @param videothekFrame
	 */
	public AddMovieDialog(VideothekFrame frame, DBController dbi) {
		setResizable(false);
		setModal(true);
		setTitle("Filmmanager");
		db = dbi;
		this.topFrame = frame;
		genre = db.getGenre();		
		if (!db.isDBOnline()) {
			dispose();
		}
		setBounds(100, 100, 450, 259);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{

			btnFilmHinzufgen.addActionListener(new ActionListener() {
				//@SuppressWarnings("deprecation")
				public void actionPerformed(ActionEvent e) {
					try {
						int genreid = 1;
						for (Map.Entry<Integer, String> me : genre.entrySet()) {
							if (me.getValue() == comboBoxGenre
									.getSelectedItem()) {
								genreid = me.getKey();
								break;
							}
						}
						f = new Film(								
								textFieldTitle.getText(),
								Integer.parseInt(textFieldYear.getText()),
								Integer.parseInt(textFieldPlaytime.getText()),
								Double.parseDouble(textFieldIMDB.getText()),
								txtrStory.getText(),
								Integer.parseInt(textFieldFSK.getText()),
								Double.parseDouble(textFieldBasicCost.getText()),
								Double.parseDouble(textFieldNewCost.getText()),
								genreid);
						db.writeMovie(f);
						//Aktualisieren des Tables in VideothekFrame
						topFrame.setTableValues();
						dispose();
					} catch (Exception fehler) {
						System.out.println("Fehler beim Eingabeformat!");
					}
				}
			});
			btnFilmHinzufgen.setBounds(220, 194, 107, 23);
			contentPanel.add(btnFilmHinzufgen);
		}

		textFieldTitle = new JTextField();
		textFieldTitle.setBounds(66, 11, 166, 20);
		contentPanel.add(textFieldTitle);
		textFieldTitle.setColumns(10);

		JLabel lblTitel = new JLabel("Titel:");
		lblTitel.setBounds(10, 14, 46, 14);
		contentPanel.add(lblTitel);

		JLabel lblJahr = new JLabel("Jahr:");
		lblJahr.setBounds(10, 39, 46, 14);
		contentPanel.add(lblJahr);

		JLabel lblLaufzeit = new JLabel("Laufzeit:");
		lblLaufzeit.setBounds(10, 64, 46, 14);
		contentPanel.add(lblLaufzeit);

		JLabel lblImdbwertung = new JLabel("imdb_Wertung:");
		lblImdbwertung.setBounds(10, 89, 74, 14);
		contentPanel.add(lblImdbwertung);

		JLabel lblPlot = new JLabel("Plot:");
		lblPlot.setBounds(10, 114, 46, 14);
		contentPanel.add(lblPlot);

		JLabel lblFsk = new JLabel("FSK:");
		lblFsk.setBounds(242, 120, 46, 14);
		contentPanel.add(lblFsk);

		JLabel lblGrundpreis = new JLabel("Grundpreis");
		lblGrundpreis.setBounds(242, 14, 85, 14);
		contentPanel.add(lblGrundpreis);

		JLabel lblNzuschlag = new JLabel("N-Zuschlag:");
		lblNzuschlag.setBounds(242, 39, 73, 14);
		contentPanel.add(lblNzuschlag);

		JLabel lblNeuBis = new JLabel("Neu Bis:");
		lblNeuBis.setBounds(242, 64, 46, 14);
		contentPanel.add(lblNeuBis);

		JLabel lblGenre = new JLabel("Genre:");
		lblGenre.setBounds(242, 89, 46, 14);
		contentPanel.add(lblGenre);

		textFieldYear = new JTextField();
		textFieldYear.setColumns(10);
		textFieldYear.setBounds(146, 36, 86, 20);
		contentPanel.add(textFieldYear);

		textFieldPlaytime = new JTextField();
		textFieldPlaytime.setColumns(10);
		textFieldPlaytime.setBounds(146, 61, 86, 20);
		contentPanel.add(textFieldPlaytime);

		textFieldIMDB = new JTextField();
		textFieldIMDB.setColumns(10);
		textFieldIMDB.setBounds(146, 86, 86, 20);
		contentPanel.add(textFieldIMDB);

		textFieldFSK = new JTextField();
		textFieldFSK.setColumns(10);
		textFieldFSK.setBounds(338, 117, 86, 20);
		contentPanel.add(textFieldFSK);

		textFieldBasicCost = new JTextField();
		textFieldBasicCost.setColumns(10);
		textFieldBasicCost.setBounds(338, 11, 86, 20);
		contentPanel.add(textFieldBasicCost);

		textFieldNewCost = new JTextField();
		textFieldNewCost.setColumns(10);
		textFieldNewCost.setBounds(338, 36, 86, 20);
		contentPanel.add(textFieldNewCost);

		textFieldNewto = new JTextField();
		textFieldNewto.setColumns(10);
		textFieldNewto.setBounds(338, 61, 86, 20);
		contentPanel.add(textFieldNewto);

		comboBoxGenre.setBounds(338, 86, 86, 20);
		contentPanel.add(comboBoxGenre);
		try{
			for (String g : genre.values()) {
				comboBoxGenre.addItem(g);
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}

		JButton btnBeenden = new JButton("beenden");
		btnBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnBeenden.setBounds(335, 194, 89, 23);
		contentPanel.add(btnBeenden);

		txtrStory = new JTextArea();
		txtrStory.setWrapStyleWord(true);
		txtrStory.setLineWrap(true);
		txtrStory.setBounds(10, 139, 200, 70);
		contentPanel.add(txtrStory);
	}
	
	
}
