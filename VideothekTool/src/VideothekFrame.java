import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import java.awt.Component;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JSeparator;

/**
 * @author Simon Krause
 * @version 1.1
 */
public class VideothekFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 627881990149564418L;
	private JPanel contentPane;
	private JPanel panel_2;
	private JPanel panel_1;
	private JButton btnFilmeSuchen;
	private JTextField textField;
	private JComboBox<String> comboBox;
	private Component horizontalGlue_1;
	private JScrollPane scrollPane;
	private JTable table;
	/**
	 * AddMovieDialog
	 */
	private AddMovieDialog addMovieDialog;
	/**
	 * AddKundeDialog
	 */
	private AddKundeDialog addKundeDialog;
	/**
	 * Die FilmDatenbank
	 */
	private DBController db;
	
	private JMenuBar menuBar;
	private JMenu mnFilme;
	private JMenu mnEinstellungen;
	private JMenuItem mntmNewMenuItem;
	private JMenu mnKunden;
	private JMenuItem mntmNewMenuItem_2;
	private JMenu mnDatei;
	private JMenu mnWarenkorb;
	private JMenuItem mntmNewMenuItem_4;
	private JMenuItem mntmNewMenuItem_5;
	private JCheckBoxMenuItem chckbxmntmFilialleitung;
	private JMenuItem mntmNewMenuItem_6;
	private JMenuItem mntmBeenden;
	private JPopupMenu popupMenu;
	private JMenuItem mntmFilmbestandndern_1;
	private JMenuItem mntmFilmdetailsndern;
	private JMenuItem mntmInWarenkorb;
	private JMenuItem mntmReservieren;
	private JSeparator separator;
	private JSeparator separator_1;

	/**
	 * Launch the application.
	 */
	/*
	public static void openVideothek(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VideothekFrame frame = new VideothekFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame. Es wird eine neue Verbindung zur Datenbank aufgebaut
	 */
	public VideothekFrame() {		
		addWindowListener(new WindowAdapter() {
			/**
			 * Die DB wird beim Schließen wieder geschlossen
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				db.close();
			}
		});
		
		//neuer DBController
		this.db = new DBController();
		
		//Versuche zu verbinden
		try {
			db.connect();
		} catch (SQLException e) {
			db.close();
			System.exit(0);
		}		
		
		//Initialisierung der Dialoge
		this.addMovieDialog = new AddMovieDialog(this, db);
		this.addMovieDialog.setModal(true);
		
		this.addKundeDialog = new AddKundeDialog(db);
		this.addKundeDialog.setModal(true);
		

		setTitle("Videothek Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 349);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
		mntmNewMenuItem_6 = new JMenuItem("Ausloggen");
		mnDatei.add(mntmNewMenuItem_6);
		
		mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(new ActionListener() {
			/**
			 * Anwendung beenden und schließen der DB-Connection
			 */
			public void actionPerformed(ActionEvent arg0) {
				db.close();
				System.exit(0);
			}
		});
		
		separator_1 = new JSeparator();
		mnDatei.add(separator_1);
		mnDatei.add(mntmBeenden);
		
		mnWarenkorb = new JMenu("Warenkorb");
		menuBar.add(mnWarenkorb);
		
		mntmNewMenuItem_4 = new JMenuItem("Anzeigen");
		mnWarenkorb.add(mntmNewMenuItem_4);
		
		separator = new JSeparator();
		mnWarenkorb.add(separator);
		
		mntmNewMenuItem_5 = new JMenuItem("Bezahlen");
		mnWarenkorb.add(mntmNewMenuItem_5);
		
		mnFilme = new JMenu("Filme");
		mnFilme.setEnabled(false);
		menuBar.add(mnFilme);
		
		mntmNewMenuItem = new JMenuItem("Neuen Film hinzuf\u00FCgen");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			/**
			 * Neuen Film hinzufügen
			 */
			public void actionPerformed(ActionEvent e) {
				addMovieDialog.setLocationRelativeTo(getParent());
				addMovieDialog.setVisible(true);
			}
		});
		mnFilme.add(mntmNewMenuItem);
		
		mnKunden = new JMenu("Kunden");
		menuBar.add(mnKunden);
		
		mntmNewMenuItem_2 = new JMenuItem("Kunde hinzuf\u00FCgen/\u00E4ndern");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			/**
			 * Aufruf des addKundeDialogs
			 */
			public void actionPerformed(ActionEvent e) {
				addKundeDialog.setLocationRelativeTo(getParent());
				addKundeDialog.setVisible(true);
			}
		});
		mnKunden.add(mntmNewMenuItem_2);
		
		mnEinstellungen = new JMenu("Einstellungen");
		menuBar.add(mnEinstellungen);
		
		chckbxmntmFilialleitung = new JCheckBoxMenuItem("Filialleitung");
		chckbxmntmFilialleitung.addActionListener(new ActionListener() {
			/**
			 * Änderung des Editierstatus zu FilialLeitung oder Mitarbeiter
			 * geänderte Schreib- und Leserechte
			 */
			public void actionPerformed(ActionEvent arg0) {
				if(chckbxmntmFilialleitung.isSelected() ) {
					mnFilme.setEnabled(true);
					mntmFilmbestandndern_1.setEnabled(true);
					mntmFilmdetailsndern.setEnabled(true);
				}
				else{
					mnFilme.setEnabled(false);
					mntmFilmbestandndern_1.setEnabled(false);
					mntmFilmdetailsndern.setEnabled(false);
				}
			}
		});
		mnEinstellungen.add(chckbxmntmFilialleitung);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			/**
			 * Aufruf des popupMenu
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupMenu.setVisible(true);
				}
			}
			/**
			 * Markiert die Zeile bei click einer Maustaste
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				// selects the row at which point the mouse is clicked
				Point point = e.getPoint();
				int currentRow = table.rowAtPoint(point);
				if(currentRow >= 0 && currentRow <= table.getRowCount()){
					table.setRowSelectionInterval(currentRow, currentRow);					
				}
			}
		});
		updateTable(0);
		
		popupMenu = new JPopupMenu();
		table.setComponentPopupMenu(popupMenu);
		
		mntmInWarenkorb = new JMenuItem("in Warenkorb");
		popupMenu.add(mntmInWarenkorb);
		
		mntmReservieren = new JMenuItem("Reservieren");
		popupMenu.add(mntmReservieren);
		
		mntmFilmbestandndern_1 = new JMenuItem("Filmbestand \u00E4ndern");
		mntmFilmbestandndern_1.setEnabled(false);
		popupMenu.add(mntmFilmbestandndern_1);
		
		mntmFilmdetailsndern = new JMenuItem("Filmdetails \u00E4ndern");
		mntmFilmdetailsndern.addActionListener(new ActionListener() {
			/**
			 * Ändern der FilmDetails
			 */
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() >= 0){
					addMovieDialog.setFilm(db.getFilm((String) table.getValueAt(
						table.getSelectedRow(), 0)));
					addMovieDialog.setLocationRelativeTo(getParent());
					addMovieDialog.setVisible(true);
				}
			}
		});
		mntmFilmdetailsndern.setEnabled(false);
		popupMenu.add(mntmFilmdetailsndern);

		scrollPane.setViewportView(table);

		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));

		//Überprüfung, ob Filialleiter-Rechte vergeben sind
		if(db.checkright() == 2){
			chckbxmntmFilialleitung.setEnabled(true);
		}
		else{
			chckbxmntmFilialleitung.setEnabled(false);
		}
		
		btnFilmeSuchen = new JButton("Film/e suchen");
		btnFilmeSuchen.setToolTipText("Film suchen");
		btnFilmeSuchen.addActionListener(new ActionListener() {
			/**
			 * Einschränkung der Filmliste nach folgenden Kriterien: - Auswahl
			 * nach FSK - Auswahl mit Hilfe eines Suchstrings
			 */
			public void actionPerformed(ActionEvent e) {
				// Auswahl der Filme
				String fsk = "0";
				switch (comboBox.getSelectedItem().toString()) {
				case ("FSK  0 Jahre"):
					fsk = "0";
					break;
				case ("FSK  6 Jahre"):
					fsk = "6";
					break;
				case ("FSK 12 Jahre"):
					fsk = "12";
					break;
				case ("FSK 16 Jahre"):
					fsk = "16";
					break;
				case ("FSK 18 Jahre"):
					fsk = "18";
				}

				// List<Film> filme = db.getFilme();
				List<Film> filme = db.getFilme(textField.getText(), fsk);
				updateTable(filme.size());

				for (int i = 0; i < filme.size(); i++) {
					table.setValueAt(filme.get(i).getTitel(), i, 0);
					table.setValueAt(db.getAnzahlDVD(filme.get(i).getTitel()),
							i, 1);
					table.setValueAt(
							db.getAnzahlBluRay(filme.get(i).getTitel()), i, 2);
				}

			}
		});
		panel_1.add(btnFilmeSuchen);

		horizontalGlue_1 = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue_1);

		textField = new JTextField();
		textField.setToolTipText("Suchtext eingeben");
		textField.setColumns(10);
		panel_1.add(textField);

		comboBox = new JComboBox<String>();
		comboBox.setToolTipText("Auswahl nach FSK");
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"FSK  0 Jahre", "FSK  6 Jahre", "FSK 12 Jahre", "FSK 16 Jahre",
				"FSK 18 Jahre" }));
		panel_1.add(comboBox);	

	}

	/**
	 * Diese Methode fügt dem table row-Reihen hinzu und setzt die Überschrift
	 * 
	 * @param row
	 *            Anzahl der Reihen, mit denen der Table erzeugt werden soll
	 * 
	 */
	private void updateTable(int row) {
		TableModel model = new DefaultTableModel(row, 3) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8558421582956901665L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		List<String> header = db.getMediumArt();

		JTableHeader th = table.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("Filmtitel");
		tc.setPreferredWidth(240);

		for (int i = 0; i < header.size(); i++) {
			tc = tcm.getColumn(i + 1);
			tc.setHeaderValue("als " + header.get(i));
			tc.setPreferredWidth(40);
		}
	}

	
}
