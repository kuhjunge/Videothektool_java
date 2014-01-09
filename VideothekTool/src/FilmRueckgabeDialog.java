import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;

public class FilmRueckgabeDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5969535907682557571L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DBController db;
	private VideothekFrame topFrame;
	
	/**
	 * Wenn Änderungen an DB vorgenommen wurden
	 */
	private boolean changed = false;

	/**
	 * Create the dialog.
	 */
	public FilmRueckgabeDialog(DBController db, VideothekFrame topFrame) {
		addComponentListener(new ComponentAdapter() {
			/**
			 * Beim Anzeigen des Fensters
			 */
			@Override
			public void componentShown(ComponentEvent arg0) {
				changed = false;
				updateTable();
				setTableValues();
			}
			@Override
			public void componentHidden(ComponentEvent arg0) {
				if(changed){
					updateTopFrame();
				}
			}
		});
		this.db = db;
		this.topFrame = topFrame;
		setTitle("FilmR\u00FCckgabe");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 800, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
				{
					JPanel panel = new JPanel();
					buttonPane.add(panel);
					panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
					{
						JButton btnAusgewhltenFilmZurckbuchen = new JButton("ausgew\u00E4hlten Film zur\u00FCckbuchen");
						btnAusgewhltenFilmZurckbuchen.addActionListener(new ActionListener() {
							/**
							 * Zurückbuchen
							 */
							public void actionPerformed(ActionEvent e) {
								zurueckBuchen();								
							}
						});
						panel.add(btnAusgewhltenFilmZurckbuchen);
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

	/**
	 * Diese Methode erzeugt den Table
	 */
	private void updateTable() {
		List<FilmExemplar> exemplare = db.getExemplar(true);

		// Überschreiben des TableModels
		TableModel model = new DefaultTableModel(exemplare.size(), 7) {
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

		JTableHeader th = table.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("Filmtitel");
		tc.setPreferredWidth(240);
		// ---
		tcm = th.getColumnModel();
		tc = tcm.getColumn(1);
		tc.setHeaderValue("ID");
		tc.setPreferredWidth(80);
		// ---
		tcm = th.getColumnModel();
		tc = tcm.getColumn(2);
		tc.setHeaderValue("Medium");
		tc.setPreferredWidth(80);
		// ---
		tcm = th.getColumnModel();
		tc = tcm.getColumn(3);
		tc.setHeaderValue("von Kunde");
		tc.setPreferredWidth(200);
		// ---
		tcm = th.getColumnModel();
		tc = tcm.getColumn(4);
		tc.setHeaderValue("Ausgeliehen");
		tc.setPreferredWidth(80);
		// ---
		tcm = th.getColumnModel();
		tc = tcm.getColumn(5);
		tc.setHeaderValue("Leihfrist bis");
		tc.setPreferredWidth(80);
		// ---
		tcm = th.getColumnModel();
		tc = tcm.getColumn(6);
		tc.setHeaderValue("überfällig");
		tc.setPreferredWidth(60);

		// Hinzufügen einer Sortierfunktion
		table.setRowSorter(new TableRowSorter<TableModel>(model));
	}

	/**
	 * Setzen der Values für Table
	 */
	private void setTableValues() {
		List<FilmExemplar> exemplare = db.getExemplar(true);
		List<Verliehen> verliehen = db.getVerliehen();

		for (int i = 0; i < exemplare.size(); i++) {
			Film film = db.getFilm(exemplare.get(i).getFilm_Ref());
			String str = film.getTitel();					
			table.setValueAt(str, i, 0);
			str = String.valueOf( exemplare.get(i).getIdExemplar() );
			table.setValueAt(str, i, 1);
			str = db.getMedium(exemplare.get(i).getMedium_Ref())
					.getNameMedium();
			table.setValueAt(str, i, 2);
		}
		for (int i = 0; i < verliehen.size(); i++) {
			Kunde kunde = db.getKunde(db.getKundeRechnung(verliehen.get(i)
					.getRechnung_Ref()));
			String str = kunde.getName() + ", " + kunde.getVorname();
			table.setValueAt(str, i, 3);
			str = verliehen.get(i).getAusleihDatum().toString();
			table.setValueAt(str, i, 4);
			str = verliehen.get(i).getLeihFrist().toString();
			table.setValueAt(str, i, 5);
			// Berechne Überfälligkeit

			//Berechnung java.sql.Date
			java.util.Date date = new java.util.Date();			
			DateFormat df_sql = new SimpleDateFormat("yyyy-MM-dd");				
			String str2 = df_sql.format(date);
			java.sql.Date date_sql = java.sql.Date.valueOf(str2);
			
			Calendar cal = new GregorianCalendar();
			cal.setTime(date_sql);
			
			Calendar cal2 = new GregorianCalendar();
			cal2.setTime(verliehen.get(i).getLeihFrist());
			
			if( cal.after( cal2 ) ){				
				table.setValueAt("ja", i, 6);
			}
			

		}
	}
	
	/**
	 * Ausgewähltes Exemplar zurückbuchen
	 */
	private void zurueckBuchen(){
		if(table.getSelectedRow() != -1){
			String str = (String)table.getValueAt(table.getSelectedRow(), 1);
			
			int value = abfrageDialog("Ausgewählten Film: \n"+(String)table.getValueAt(table.getSelectedRow(), 0)
					+" - ID: "+str+"\nwirklich zurückbuchen?");
			if(value == JOptionPane.YES_OPTION){
			
				int idExemplar = Integer.parseInt(str);			
				int idVerliehen = db.getVerliehen(idExemplar).getIdVerliehen();			
			
				//Berechnung java.sql.Date
				java.util.Date date = new java.util.Date();			
				DateFormat df_sql = new SimpleDateFormat("yyyy-MM-dd");				
				String str2 = df_sql.format(date);
				java.sql.Date date_sql = java.sql.Date.valueOf(str2);
				
				//update der DB-Werte
				db.updateExemplar(idExemplar);
				db.updateVerliehen(idVerliehen, date_sql);
						
				//aktualisieren des Tables
				updateTable();
				setTableValues();		
				
				changed = true;
				
			}
			
		}
	}

	/**
	 * diese Methode öffnet einen FrageDialog
	 * 
	 * @param str
	 * @return
	 */
	private int abfrageDialog(String str) {
		return JOptionPane.showConfirmDialog(this, str, "Frage", JOptionPane.YES_NO_OPTION);
	}
	
	/**
	 * Im TopFrame werden die Werte des Tables upgedatet
	 */
	private void updateTopFrame(){		
		topFrame.setTableValues();		
	}
	
}
