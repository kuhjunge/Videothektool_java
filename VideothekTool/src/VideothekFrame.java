import java.awt.BorderLayout;
import java.awt.EventQueue;

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
	private JPanel panel;
	private JPanel panel_2;
	private JPanel panel_1;
	private JButton btnFilmeSuchen;
	private JTextField textField;
	private JComboBox<String> comboBox;
	private Component horizontalGlue_1;
	private JScrollPane scrollPane;
	private JTable table;
	private UnpaidInvoiceDialog unpaidInvoiceDialog;
	private AddMovieDialog addMovieDialog;
	/**
	 * Die FilmDatenbank
	 */
	private DBController db;
	private JMenuBar menuBar;
	private JMenu mnDatei;
	private JMenu mnEinstellungen;
	private JMenuItem mntmNewMenuItem;
	private JMenu mnKunden;
	private JMenuItem mntmNewMenuItem_2;
	private JMenuItem mntmNewMenuItem_3;
	private JMenu mnBeenden;
	private JMenu mnWarenkorb;
	private JMenuItem mntmNewMenuItem_4;
	private JMenuItem mntmNewMenuItem_5;
	private JMenuItem menuItem_1;
	private JCheckBoxMenuItem chckbxmntmFilialleitung;
	private JMenuItem mntmNewMenuItem_6;
	private JMenuItem mntmNewMenuItem_7;
	private JMenuItem mntmBeenden;
	private JMenuItem mntmAlteRechnungen;
	private JPopupMenu popupMenu;
	private JMenuItem mntmFilmbestandndern_1;
	private JMenuItem mntmFilmdetailsndern;
	private JMenuItem mntmInWarenkorb;

	/**
	 * Launch the application.
	 */
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
	}

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
		this.db = new DBController();
		try {
			db.connect();
		} catch (SQLException e) {
			db.close();
			System.exit(0);
		}
		this.unpaidInvoiceDialog = new UnpaidInvoiceDialog(this,
				"Offene Rechnungen", true);
		this.addMovieDialog = new AddMovieDialog(this, db);

		setTitle("Videothek Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 349);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnBeenden = new JMenu("Datei");
		menuBar.add(mnBeenden);
		
		mntmNewMenuItem_6 = new JMenuItem("Ausloggen");
		mnBeenden.add(mntmNewMenuItem_6);
		
		mntmNewMenuItem_7 = new JMenuItem("");
		mntmNewMenuItem_7.setEnabled(false);
		mnBeenden.add(mntmNewMenuItem_7);
		
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
		mnBeenden.add(mntmBeenden);
		
		mnWarenkorb = new JMenu("Warenkorb");
		menuBar.add(mnWarenkorb);
		
		mntmNewMenuItem_4 = new JMenuItem("Anzeigen");
		mnWarenkorb.add(mntmNewMenuItem_4);
		
		menuItem_1 = new JMenuItem("");
		menuItem_1.setEnabled(false);
		mnWarenkorb.add(menuItem_1);
		
		mntmNewMenuItem_5 = new JMenuItem("Bezahlen");
		mnWarenkorb.add(mntmNewMenuItem_5);
		
		mnDatei = new JMenu("Filme");
		menuBar.add(mnDatei);
		
		mntmNewMenuItem = new JMenuItem("Neuen Film hinzuf\u00FCgen");
		mnDatei.add(mntmNewMenuItem);
		
		mnKunden = new JMenu("Kunden");
		menuBar.add(mnKunden);
		
		mntmNewMenuItem_2 = new JMenuItem("Kunden hinzuf\u00FCgen");
		mnKunden.add(mntmNewMenuItem_2);
		
		mntmNewMenuItem_3 = new JMenuItem("Kundendaten \u00E4ndern");
		mnKunden.add(mntmNewMenuItem_3);
		
		mntmAlteRechnungen = new JMenuItem("alte Rechnungen");
		mnKunden.add(mntmAlteRechnungen);
		
		mnEinstellungen = new JMenu("Einstellungen");
		menuBar.add(mnEinstellungen);
		
		chckbxmntmFilialleitung = new JCheckBoxMenuItem("Filialleitung");
		mnEinstellungen.add(chckbxmntmFilialleitung);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 2, 0, 0));

		panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			/**
			 * Es wird der FilmInfoDialog für einen ausgewählten Film aufgerufen
			 */
			@Override
			public void mouseClicked(MouseEvent arg0) {
				addMovieDialog.setFilm(db.getFilm((String) table.getValueAt(
						table.getSelectedRow(), 0)));
				addMovieDialog.setVisible(true);
			}
		});
		updateTable(0);
		
		popupMenu = new JPopupMenu();
		addPopup(scrollPane, popupMenu);
		
		mntmInWarenkorb = new JMenuItem("in Warenkorb");
		popupMenu.add(mntmInWarenkorb);
		
		mntmFilmbestandndern_1 = new JMenuItem("Filmbestand \u00E4ndern");
		popupMenu.add(mntmFilmbestandndern_1);
		
		mntmFilmdetailsndern = new JMenuItem("Filmdetails \u00E4ndern");
		popupMenu.add(mntmFilmdetailsndern);

		scrollPane.setViewportView(table);

		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));

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
		/*
		 * contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray( new
		 * Component[] { panel_1, panel, btnFilmeSuchen, btnNewButton,
		 * btnNewButton_1, btnNewButton_2, horizontalGlue, panel_2,
		 * horizontalGlue_1, comboBox, textField }));
		 * setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] {
		 * btnFilmeSuchen, btnNewButton, btnNewButton_1, btnNewButton_2,
		 * panel_2, horizontalGlue, textField, horizontalGlue_1, comboBox,
		 * panel_1, panel, contentPane }));
		 */

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

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
