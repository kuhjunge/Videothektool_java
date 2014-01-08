import java.awt.BorderLayout;
import java.awt.Color;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
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
	 * WarenkorbDialog
	 */
	private WarenkorbDialog warenkorbDialog; 
	/**
	 * FilmBestandAendernDialog
	 */
	private FilmBestandAendernDialog bestandDialog;
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
	private JCheckBoxMenuItem chckbxmntmFilialleitung;
	private JMenuItem mntmNewMenuItem_6;
	private JMenuItem mntmBeenden;
	private JPopupMenu popupMenu;
	private JMenuItem mntmFilmbestandndern_1;
	private JMenuItem mntmFilmdetailsndern;
	private JMenuItem mntmInWarenkorb;
	private JMenuItem mntmReservieren;
	private JSeparator separator_1;

	/**
	 * der Warenkorb: eine Liste mit Referenzen auf die ausgewählten Filme
	 */
	private List<Integer> warenkorb;
	private JMenuItem mntmWarenkorb;
	private JMenuItem mntmAusgewhltenFilmLschen;
	private JSeparator separator;
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
		
		this.warenkorbDialog = new WarenkorbDialog(db);
		this.warenkorbDialog.setModal(true);		
		
		this.bestandDialog = new FilmBestandAendernDialog(db);
		this.bestandDialog.setModal(true);

		//Variableninitialisierung
		this.warenkorb = new LinkedList<Integer>();
		//----
		setTitle("Videothek Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 699, 360);
		
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
		
		mntmWarenkorb = new JMenuItem("Warenkorb");
		mntmWarenkorb.addActionListener(new ActionListener() {
			/**
			 * WarenkorbDialog wird aufgerufen
			 */
			public void actionPerformed(ActionEvent e) {
				warenkorbDialog.setLocationRelativeTo(getParent());
				warenkorbDialog.setWarenkorb(warenkorb);
				warenkorbDialog.setVisible(true);
			}
		});
		mnWarenkorb.add(mntmWarenkorb);
		
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
		
		separator = new JSeparator();
		mnFilme.add(separator);
		
		mntmAusgewhltenFilmLschen = new JMenuItem("ausgew\u00E4hlten Film l\u00F6schen");
		mntmAusgewhltenFilmLschen.addActionListener(new ActionListener() {
			/**
			 * Ausgewählter Film wird gelöscht, wenn alle Exemplare nicht verliehen sind
			 */
			public void actionPerformed(ActionEvent arg0) {							
				if(table.getSelectedRow() != -1){
					Film film = db.getFilm( (String) table.getValueAt( table.getSelectedRow(), 0) );		

					int value = abfrageDialog("Wollen Sie den Film: \n"
												+ film.getTitel() +"\n"
												+ "und alle Exemplare wirklich löschen?");
					if(value == JOptionPane.YES_OPTION){					
						int idFilm = film.getIdFilm();					
						db.deleteFilm(idFilm);
						setTableValues();
					}
				}
			}
		});
		mnFilme.add(mntmAusgewhltenFilmLschen);
		
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
		
		mntmFilmbestandndern_1.addActionListener(new ActionListener(){
			/**
			 * Aufruf des FilmBestandAendernDialogs
			 */
			public void actionPerformed(ActionEvent e) {
				int value = db.getFilm( (String)table.getValueAt( table.getSelectedRow() , 0) ).getIdFilm();				
				bestandDialog.setFilm( value );
				bestandDialog.setLocationRelativeTo(getParent());
				bestandDialog.setVisible(true);
				if(bestandDialog.isChanged()){
					setTableValues();
				}
			}
		});
		
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
		mntmInWarenkorb.addActionListener(new ActionListener() {
			/**
			 * Ausgewählten Film zu Warenkorb hinzufügen
			 */
			public void actionPerformed(ActionEvent e){
				int row = db.getFilm( (String)table.getValueAt(table.getSelectedRow(), 0) ).getIdFilm();
				//Überprüfen, ob schon in Warenkorb vorhanden
				for(Integer value : warenkorb){
					if(row == value){
						return;
					}
				}
				Film film = db.getFilm((String) table.getValueAt(table.getSelectedRow(), 0) );
				warenkorb.add( film.getIdFilm() );
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
				setTableValues();
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
		//Setzen des Datums
		Date date = new Date();
		// Festlegung des Formats:
		SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy" );
		this.setTitle("Videothek Manager"+ "  -  "+ df.format( date ));

	}

	/**
	 * Diese Methode fügt dem table row-Reihen hinzu und setzt die Überschrift
	 * 
	 * @param row
	 *            Anzahl der Reihen, mit denen der Table erzeugt werden soll
	 * 
	 */
	private void updateTable(int row) {
		//Überschreiben des TableModels
		TableModel model = new DefaultTableModel(row, 5) {
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

		//Setzen der Überschriften - in Filiale vorhandene Exemplare
		for (int i = 0; i < header.size(); i++) {
			tc = tcm.getColumn(i + 1);
			tc.setHeaderValue(header.get(i) + " in Filiale");
			tc.setPreferredWidth(80);
		}
		
		//Setzen der Überschriften - insgesamte Exemplare
		for (int i = 0; i < header.size(); i++) {
			tc = tcm.getColumn(i + 3);
			tc.setHeaderValue(header.get(i));
			tc.setPreferredWidth(40);
		}
		
		//Hinzufügen einer Sortierfunktion		
		table.setRowSorter(new TableRowSorter<TableModel>(model));
	}
	
	/**
	 *Diese Methode lädt aus der DB in den Table Filme anhand der Einschränkungen
	 *von Combobox und TextField 
	 */
	private void setTableValues(){		
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
					
		
		List<Film> filme = db.getFilme(textField.getText(), fsk);				
		updateTable(filme.size());

		for (int i = 0; i < filme.size(); i++) {					
			table.setValueAt(filme.get(i).getTitel(), i, 0);					
			table.setValueAt(db.getAnzahlDVD( filme.get(i).getIdFilm() ), i, 3);					
			table.setValueAt(db.getAnzahlBluRay( filme.get(i).getIdFilm() ), i, 4);
			table.setValueAt(db.getAnzahlDVDPraesent( filme.get(i).getIdFilm() ), i, 1);
			table.setValueAt(db.getAnzahlBluRayPraesent( filme.get(i).getIdFilm() ), i, 2);
		}
			
		
	}

	private int abfrageDialog(String str){		
		return JOptionPane.showConfirmDialog(this, str);
	}
	
}
