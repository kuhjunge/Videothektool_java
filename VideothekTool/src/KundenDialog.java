import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

import java.awt.GridLayout;

import javax.swing.CellEditor;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class KundenDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private DBController db;
	private JTable table;
	private JScrollPane scrollPane;
	
	/**
	 * Zur Auswahl einer KundenID für den Warenkobr
	 */
	private int idSelectedKunde = -1;

	/**
	 * Create the dialog.
	 */
	public KundenDialog(DBController db) {
		addComponentListener(new ComponentAdapter() {
			/**
			 * Aufruf des Dialogs
			 */
			@Override
			public void componentShown(ComponentEvent arg0) {
				idSelectedKunde = -1;
				updateTable();
			}
		});
		setModal(true);
		setResizable(false);
		setTitle("Kunden");

		this.db = db;

		setBounds(100, 100, 700, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			scrollPane = new JScrollPane();
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
					/**
					 * mit ok beenden
					 */
					public void actionPerformed(ActionEvent e) {						
						setIdKunde();
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
					/**
					 * mit cancel beenden
					 */
					public void actionPerformed(ActionEvent e) {
						idSelectedKunde = -1;
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}		
		{			
			{
				JMenuBar menuBar = new JMenuBar();
				setJMenuBar(menuBar);
				{
					JMenu mnKunde = new JMenu("Kunde");
					menuBar.add(mnKunde);
					{
						{
							JSeparator separator = new JSeparator();
							mnKunde.add(separator);
						}
						{
							JMenuItem mntmNeuerKunde = new JMenuItem(
									"Neuer Kunde");
							mntmNeuerKunde.addActionListener(new ActionListener() {
								/**
								 * Aufruf neuer KundeDialog
								 */
								public void actionPerformed(ActionEvent arg0) {
									//TODO neuer Kunde Dialog
								}
							});
							{
								JMenuItem mntmKundendatenndern = new JMenuItem("Kundendaten \u00E4ndern");
								mntmKundendatenndern.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										//TODO KundeEditDialog(oder so)
									}
								});
								mnKunde.add(mntmKundendatenndern);
							}
							{
								JSeparator separator = new JSeparator();
								mnKunde.add(separator);
							}
							mnKunde.add(mntmNeuerKunde);
						}
						{
							JSeparator separator = new JSeparator();
							mnKunde.add(separator);
						}
					}
				}
			}			
		}

	}

	/**
	 * Diese Methode setzt das TableModel mit einem neuen Row-Wert und
	 * editierbar
	 * 
	 * @param value
	 *            Anzahl der Rows
	 * @param editable
	 *            Editierbar oder nicht
	 * @return
	 */
	private void updateTable() {
		List<Kunde> kunden = db.getKunden();
		
		TableModel model =  new DefaultTableModel(kunden.size(), 6) {
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
		tc.setHeaderValue("ID");
		tc.setPreferredWidth(60);
		tc = tcm.getColumn(1);
		tc.setHeaderValue("Name");
		tc.setPreferredWidth(200);
		tc = tcm.getColumn(2);
		tc.setHeaderValue("Vorname");
		tc.setPreferredWidth(200);
		tc = tcm.getColumn(3);
		tc.setHeaderValue("Geburtsdatum");
		tc.setPreferredWidth(100);
		tc = tcm.getColumn(4);
		tc.setHeaderValue("TelefonNr");
		tc.setPreferredWidth(100);
		tc = tcm.getColumn(5);
		tc.setHeaderValue("Email");
		tc.setPreferredWidth(200);

		// Laden der Kunden in den Table
		for (int i = 0; i < kunden.size(); i++) {
			table.setValueAt(kunden.get(i).getIdKunde(), i, 0);
			table.setValueAt(kunden.get(i).getName(), i, 1);
			table.setValueAt(kunden.get(i).getVorname(), i, 2);
			table.setValueAt(kunden.get(i).getGeburtsDatum(), i, 3);
			table.setValueAt(kunden.get(i).getTelefonnummer(), i, 4);
			table.setValueAt(kunden.get(i).getEmail(), i, 5);
		}

		// Hinzufügen einer Sortierfunktio
		table.setRowSorter(new TableRowSorter<TableModel>(model));
	}

	/**
	 * Diese Methode gibt die ID eines Selectierten Kunden zurück
	 * @return
	 */
	public int getIdKunde(){
		return this.idSelectedKunde;
	}
	
	/**
	 * Diese Methode setzt die KundenID aus der Auswahl des Tables heraus
	 */
	private void setIdKunde(){
		if(table.getSelectedRow() != -1){
			idSelectedKunde = db.getKunde( (int)table.getValueAt( table.getSelectedRow(), 0) ).getIdKunde();
		}
		else{
			idSelectedKunde = -1;
		}
	}
	

}
