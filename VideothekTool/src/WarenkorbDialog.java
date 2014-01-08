import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.TableView.TableCell;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;




public class WarenkorbDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private DBController db;
	private List<Integer> warenkorb = null;
	private int idKunde = -1;
	private JTable table;
	private JTextField textField;
	
	/**
	 * Create the dialog.
	 */
	public WarenkorbDialog(DBController db) {
		addComponentListener(new ComponentAdapter() {
			/**
			 * 
			 */
			@Override
			public void componentShown(ComponentEvent arg0) {
				//erstelle Table
				updateTable(warenkorb.size());
			}
		});
		//Initialisierung der Variablen
		this.db = db;	
		warenkorb = new ArrayList<Integer>();
		//---
		//Setzen des Datums
		Date date = new Date();
		// Festlegung des Formats:
		SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy" );
		this.setTitle("Warenkorb"+ "  -  "+ df.format( date ));
		
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 600, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				table = new JTable();
				table.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent arg0) {
						if(table.getSelectedRow() != -1){						
							berechnePreis(table.getSelectedRow());
							berechneGesamtPreis();
						}
					}
					
				});
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Buchen");
				okButton.addActionListener(new ActionListener() {
					/**
					 * Buchen 
					 */
					public void actionPerformed(ActionEvent arg0) {						
						dispose();
					}
				});
				buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
				{
					JPanel panel = new JPanel();
					FlowLayout flowLayout = (FlowLayout) panel.getLayout();
					flowLayout.setAlignment(FlowLayout.LEFT);
					buttonPane.add(panel);
					{
						JLabel label_1 = new JLabel("GesamtPreis:");
						panel.add(label_1);
					}
					{
						textField = new JTextField();
						textField.setEditable(false);
						panel.add(textField);
						textField.setColumns(10);
					}
					{
						JButton btnKeinKundeGewhlt = new JButton("kein Kunde gew\u00E4hlt");
						btnKeinKundeGewhlt.addActionListener(new ActionListener() {
							/**
							 * Auswahl des Kunden
							 */
							public void actionPerformed(ActionEvent arg0) {
								
							}
						});
						panel.add(btnKeinKundeGewhlt);
					}
				}
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

	}

	public void setWarenkorb( List<Integer> warenkorb){
		this.warenkorb = warenkorb;		
	}
	
	/**
	 * Diese Methode fügt dem table row-Reihen hinzu und setzt die Überschrift
	 * 
	 * @param row
	 *            Anzahl der Reihen, mit denen der Table erzeugt werden soll
	 * 
	 */
	private void updateTable(int row) {		
		TableModel model = new DefaultTableModel(row, 5) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8558421582956901665L;

			public boolean isCellEditable(int row, int column) {
				if(column != 0 && column != 4 ){
					return true;
				}
				else{
					return false;
				}
			}
						
		};		
		
		//Tableinhalt löschen
		table.setModel( new DefaultTableModel(0,0));
				
		//erstelle neuen Table
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JTableHeader th = table.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		
		//Überschriften
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("Filmtitel");
		tc.setPreferredWidth(240);
		//---
		tc = tcm.getColumn(1);
		tc.setHeaderValue("Medium");
		tc.setPreferredWidth(60);
		//---
		tc = tcm.getColumn(2);
		tc.setHeaderValue("Anzahl");
		tc.setPreferredWidth(60);
		//---
		tc = tcm.getColumn(3);
		tc.setHeaderValue("Zeitraum");
		tc.setPreferredWidth(60);
		//---
		tc = tcm.getColumn(4);
		tc.setHeaderValue("Preis");
		tc.setPreferredWidth(60);
				
		//Hinzufügen der FilmTitel		
		for(int i = 0; i < warenkorb.size(); i++){
			String titel = db.getFilm(warenkorb.get(i)).getTitel();			
			table.setValueAt(titel, i, 0);			
		}
	
		
		//Hinzufügen von Componenten		
		tc = tcm.getColumn(1);
		JComboBox<String> comboBox = new JComboBox<String>();	
		List<String> medium = db.getMediumArt();
		for(int i = 0; i < medium.size(); i++){
			comboBox.addItem(medium.get(i));			
		}		
		tc.setCellEditor(new DefaultCellEditor(comboBox));
		
		//--
		tc = tcm.getColumn(2);
		comboBox = new JComboBox<String>();		
		for(int a = 0; a <= 3; a++){
			comboBox.addItem(a+"");
		}		
		tc.setCellEditor(new DefaultCellEditor(comboBox));
		
		//--
		tc = tcm.getColumn(3);
		comboBox = new JComboBox<String>();
		//Datum + 14Tage
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		// print out the dates...
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		for(int i = 0; i < 14; i++){
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			date = calendar.getTime();
			comboBox.addItem(""+df.format(date) );
		}		
		comboBox.setMaximumRowCount(14);
		comboBox.setSize(60, 30);	
		tc.setCellEditor(new DefaultCellEditor(comboBox));
		
		//Hinzufügen einer Sortierfunktion		
		//table.setRowSorter(new TableRowSorter(model)); //Hinzufügen führt zu Fehlern
		
	}
	
	/**
	 * Diese Methode berechnet für die aktuelle Zeile,
	 * sofern alle Parameter(Medium, Anzahl, Datum) gesetzt sind
	 * den Preis
	 * @param row
	 */
	private void berechnePreis(int row){
		if(table.getValueAt(row, 1) != null && table.getValueAt(row, 2) != null && table.getValueAt(row, 3) != null){
			//Medienzuschlag
			double medium = db.getMediumZuschlag( (String) table.getValueAt(row, 1));

			//Anzahl und Überprüfung, ob soviele möglich sind
			int anzahl = Integer.valueOf( (String)table.getValueAt(row, 2) );
						
			if( (String) table.getValueAt(row, 1) == "DVD"){
				if( db.getAnzahlDVD(warenkorb.get(row)) < anzahl ){
					return;
				}
			}
			else{
				if( db.getAnzahlBluRay(warenkorb.get(row)) < anzahl ){
					return;
				}
			}
			
			//Tage
			Calendar heute = new GregorianCalendar();
			heute.setTime(new Date());
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			Calendar ausleiheBis = new GregorianCalendar();
			try {
				ausleiheBis.setTime( df.parse( (String) table.getValueAt(row, 3)) );
			} catch (ParseException e) {				
				System.out.println(e.toString());
			}	
							
			long diff = ausleiheBis.getTimeInMillis() - heute.getTimeInMillis();
			int tage = (int)(diff / (1000*60*60*24)+1) ;			
			//------------------
			Film film = db.getFilm(warenkorb.get(row));
			double value = film.getGrundPreis() * tage * anzahl + medium * tage * anzahl;
			if( heute.before(film.getNeu_Bis())){
				value += film.getNeuheiten_Zuschlag() * tage * anzahl;
			}
			
			table.setValueAt(value, row, 4);			
		}
	}

	/**
	 * Diese Methode berechnet den Preis aller gewählten Filme
	 */
	private void berechneGesamtPreis() {
		double preis = 0.0;
		for(int i = 0; i < table.getRowCount(); i++){			
			if(table.getValueAt(i, 4) != null){				
				preis += (Double)table.getValueAt(i, 4);
			}
		}
		textField.setText(String.valueOf(preis));		
	}
	
}
