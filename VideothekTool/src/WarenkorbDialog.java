import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private JButton button_1;
	private JPopupMenu popupMenu;
	/**
	 * Oberfenster, das diesen Dialog aufruft,
	 * wird genutzt um den Table aus Oberfenster zu aktualisieren,
	 * wenn hier Änderungen kommen
	 */
	private VideothekFrame topFrame;
	
	/**
	 * Create the dialog.
	 */
	public WarenkorbDialog(DBController db, VideothekFrame topFrame) {
		addComponentListener(new ComponentAdapter() {
			/**
			 * 
			 */
			@Override
			public void componentShown(ComponentEvent arg0) {
				//erstelle Table
				updateTable(warenkorb.size());				
				textField.setText("");
			}
		});
		//Initialisierung der Variablen
		this.db = db;	
		warenkorb = new ArrayList<Integer>();
		this.topFrame = topFrame;
		//---
		//Setzen des Datums
		Date date = new Date();
		// Festlegung des Formats:
		SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy" );
		this.setTitle("Warenkorb"+ "  -  "+ df.format( date ));
		
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 800, 300);
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
						buchen();
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
						button_1 = new JButton("kein Kunde gew\u00E4hlt");
						button_1.addActionListener(new ActionListener() {
							/**
							 * Auswahl des Kunden
							 */
							public void actionPerformed(ActionEvent arg0) {
								auswahlKunde();
							}
						});
						panel.add(button_1);
					}
				}
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("zur\u00FCck zur Auswahl");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
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
				if (currentRow >= 0 && currentRow <= table.getRowCount()) {
					table.setRowSelectionInterval(currentRow, currentRow);					
				}
			}
		});
		
		//füge popupMenu hinzu
		addPopupMenu();		

	}
	
	
	/**
	 * Diese Methode fügt dem table row-Reihen hinzu und setzt die Überschrift
	 * 
	 * @param row
	 *            Anzahl der Reihen, mit denen der Table erzeugt werden soll
	 * 
	 */
	private void updateTable(int row) {		
		TableModel model = new DefaultTableModel(row, 4) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8558421582956901665L;

			public boolean isCellEditable(int row, int column) {
				if(column == 2){
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
		tc.setHeaderValue("Zeitraum");
		tc.setPreferredWidth(60);
		//---
		tc = tcm.getColumn(3);
		tc.setHeaderValue("Preis");
		tc.setPreferredWidth(60);
				
		//Hinzufügen der FilmTitel, Medium		
		for(int i = 0; i < warenkorb.size(); i++){
			FilmExemplar exemplar = db.getExemplar(warenkorb.get(i));
			
			System.out.println(exemplar.getFilm_Ref());
			String titel = db.getFilm( exemplar.getFilm_Ref() ).getTitel() + " - ID: "+exemplar.getIdExemplar();			
			table.setValueAt(titel, i, 0);
			String medium = db.getMedium( exemplar.getMedium_Ref() ).getNameMedium();
			table.setValueAt(medium, i, 1);
		}
	
		
		//Hinzufügen von Componenten
		tc = tcm.getColumn(2);
		JComboBox<String> comboBox = new JComboBox<String>();
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
		if(warenkorb.isEmpty()){
			return;
		}
		if(table.getValueAt(row, 1) != null && table.getValueAt(row, 2) != null){			
			FilmExemplar exemplar = db.getExemplar(warenkorb.get(row));
			Film film = db.getFilm( exemplar.getFilm_Ref() );
			Medium medium = db.getMedium(exemplar.getMedium_Ref());
			
			//Medienzuschlag			
			double mediumZuschlag = medium.getMedienAufschlag();			
			
			//Tage
			Calendar heute = new GregorianCalendar();
			heute.setTime(new Date());
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			Calendar ausleiheBis = new GregorianCalendar();
			try {
				ausleiheBis.setTime( df.parse( (String) table.getValueAt(row, 2)) );
			} catch (ParseException e) {				
				System.out.println(e.toString());
			}	
							
			long diff = ausleiheBis.getTimeInMillis() - heute.getTimeInMillis();
			int tage = (int)(diff / (1000*60*60*24)+1) ;			

			//------------------Berechnung---------------			
			double value = film.getGrundPreis() * tage + mediumZuschlag * tage;
			if( heute.before(film.getNeu_Bis())){
				value += film.getNeuheiten_Zuschlag() * tage;
			}
			
			table.setValueAt(value, row, 3);			
		}
	}

	/**
	 * Diese Methode berechnet den Preis aller gewählten Filme
	 */
	private void berechneGesamtPreis() {
		if(warenkorb.isEmpty()){
			return;
		}
		double preis = 0.0;
		for(int i = 0; i < table.getRowCount(); i++){			
			if(table.getValueAt(i, 3) != null){				
				preis += (Double)table.getValueAt(i, 3);
			}
		}
		textField.setText(String.valueOf(preis));		
	}
	
	/**
	 * Diese Methode öffnet den KundenDialog und man kann dort einen Kunden auswählen
	 */
	private void auswahlKunde(){
		KundenDialog dialog = new KundenDialog(db);
		dialog.setModal(true);
		dialog.setLocationRelativeTo(getParent());
		dialog.setVisible(true);
		this.idKunde = dialog.getIdKunde();
		if(idKunde != -1){
			Kunde kunde = db.getKunde(idKunde);
			button_1.setText( kunde.getName()+", "+kunde.getVorname());
		}
		else{
			button_1.setText("kein Kunde ausgewählt");
		}
	}
	
	/**
	 * diese Methode öffnet einen FrageDialog
	 * @param str
	 * @return
	 */
	private int abfrageDialog(String str){		
		return JOptionPane.showConfirmDialog(this, str);
	}
	
	/**
	 * Diese Methode fügt eine FilmExemplarId dem Warenkorb hinzu
	 * @param idFilm
	 */
	public void addWarenkorbItem(int idExemplar){
		if(idExemplar >= 0){
			warenkorb.add(idExemplar);
		}
	}
	
	/**
	 * Diese Methode überprüft, ob ein Element schon im Warenkorb ist
	 * @param idExemplar
	 * @return
	 */
	public boolean isInWarenkorb(int idExemplar){
		for(int i = 0; i < warenkorb.size(); i++){
			if(warenkorb.get(i) == idExemplar){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Füge ein PopupMenu für den Table hinzu
	 */
	private void addPopupMenu(){
		this.popupMenu = new JPopupMenu();
		JMenuItem entry = new JMenuItem("aus Warenkorb entfernen");
		entry.addActionListener(new ActionListener() {
			/**
			 * Ausgewähltes Exemplar zu Warenkorb hinzufügen
			 */
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() != -1){
					String str = (String)table.getValueAt(table.getSelectedRow(), 0);
					int value = abfrageDialog("Ausgewähltes Exemplar:\n "+str+"\naus Warenkorb entfernen?");
					if(value == JOptionPane.YES_OPTION){
						warenkorb.remove(table.getSelectedRow());
						updateTable(warenkorb.size());
						textField.setText("");
					}
				}
				
			}
		});
		
		this.popupMenu.add(entry);
		table.setComponentPopupMenu(popupMenu);
	}
	
	/**
	 * Diese Methode verbucht den Warenkorb
	 *  
	 */
	private void verbuchen() {
		if(warenkorb.size() != 0){
			for(int i = 0; i < warenkorb.size(); i++){
				double betrag = Double.parseDouble( textField.getText() );
				//schreibe Rechnung				
				int idRechnung = db.writeRechnung(idKunde, betrag);		
				System.out.println(idKunde+ " mit Betrag ="+betrag +" und idRechnung "+idRechnung);
				
				//Zeitumrechnung von Date zu java.sql.Date
				String str = (String) table.getValueAt(i, 2);
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");				
				
				Date date = new Date();
				try {
					date = df.parse(str);
				} catch (ParseException e) {					
					e.printStackTrace();
				}
				DateFormat df_sql = new SimpleDateFormat("yyyy-MM-dd");				
				String str2 = df_sql.format(date);
				java.sql.Date date_sql = java.sql.Date.valueOf(str2);			
				
				//schreibe die einzelnen Posten der Rechnung
				db.writeVerliehen((int)warenkorb.get(i), date_sql, idRechnung);
			}
		}
	}
	
	/**
	 * Diese Methode verbucht den Warenkorb
	 */
	private void buchen(){
		String str = textField.getText();
		if( str.isEmpty() || Double.parseDouble(str) == 0 || idKunde == -1){
			return;
		}						
		int value = abfrageDialog("Wollen Sie diesen Warenkorb \nmit "+textField.getText()+" € verbuchen");
		if(value == JOptionPane.YES_OPTION){
			verbuchen();							
			warenkorb.clear();
			button_1.setText("kein Kunde ausgewählt");
			idKunde = -1;
			topFrame.setTableValues();
			dispose();
		}
		else{
			if(value == JOptionPane.CANCEL_OPTION){
				warenkorb.clear();
				button_1.setText("kein Kunde ausgewählt");
				idKunde = -1;
				dispose();
			}
		}						
	}
	
}
