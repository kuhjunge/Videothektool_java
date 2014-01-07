import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
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




public class WarenkorbDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private DBController db;
	private List<Integer> warenkorb = null;
	private JTable table;
	
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
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
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
		//Table erstellen
		updateTable(warenkorb.size());		

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
		int anzahlExemplare = 0;
		if( warenkorb.size() != 0){
			for(int i = 0; i < warenkorb.size(); i++){
				anzahlExemplare = db.getAnzahlDVDPraesent( warenkorb.get(i) , db.getAnzahlDVD(warenkorb.get(i) ) );
				for(int a = 0; a <= anzahlExemplare; a++){
					comboBox.addItem(a+"");
				}			//TODO Ändern, daß jede Row der Column eigene ComboBox hat	
			}
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
		
		
	}
	
}
