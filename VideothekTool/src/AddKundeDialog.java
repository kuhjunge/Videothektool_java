import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
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
import java.util.List;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import java.awt.GridLayout;

public class AddKundeDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private DBController db;
	private JTable table;
	private JScrollPane scrollPane;
	private JCheckBox chckbxEditierbar;

	/**
	 * Anzahl der Reihen im Table
	 */
	private int rows = 0;
	
	/**
	 * List<Kunde>
	 */
	private List<Kunde> kunden = null;
	
	/**
	 * Create the dialog.
	 */
	public AddKundeDialog(DBController db) {
		setTitle("Kunden");

		this.db = db;

		setBounds(100, 100, 600, 300);
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
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.SOUTH);
			panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			{
				chckbxEditierbar = new JCheckBox("editierbar?");
				chckbxEditierbar.addActionListener(new ActionListener() {
					//Ändern der Editierbarkeit des Tables
					public void actionPerformed(ActionEvent e) {
						if( chckbxEditierbar.isSelected() ){
							setTableEdit(rows,true);
							System.out.println("true");
						}
						else{
							setTableEdit(rows,false);
							System.out.println("false");
						}
					}
				});
				panel.add(chckbxEditierbar);
			}
			{
				JButton btnNewButton = new JButton("Neuer Kunde");
				panel.add(btnNewButton);
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
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		// Eigenschaften des Tables
		{
			// Lade Kundendaten aus DB
			this.kunden = db.getKunden();

			// Überschriften des Table
			if (this.kunden != null) {
				this.rows = kunden.size();
				setTableEdit(rows, false);					
			}

		}
	}

	/**
	 * Diese Methode setzt das TableModel mit einem neuen Row-Wert und editierbar
	 * @param value Anzahl der Rows
	 * @param editable Editierbar oder nicht
	 * @return
	 */
	private void setTableEdit(int value, boolean editable) {
		if(kunden == null){
			return;
		}
		TableModel model = null;
		if(editable){
			model = new DefaultTableModel(value, 5) {			
				private static final long serialVersionUID = -8558421582956901665L;
				
				public boolean isCellEditable(int row, int column) {
					return true;
				}
			};			
		}
		else{
			model = new DefaultTableModel(value, 5) {			
				private static final long serialVersionUID = -8558421582956901665L;
				
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};			
		}
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JTableHeader th = table.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("Name");
		tc.setPreferredWidth(200);
		tc = tcm.getColumn(1);
		tc.setHeaderValue("Vorname");
		tc.setPreferredWidth(200);
		tc = tcm.getColumn(2);
		tc.setHeaderValue("Geburtsdatum");
		tc.setPreferredWidth(100);
		tc = tcm.getColumn(3);
		tc.setHeaderValue("TelefonNr");
		tc.setPreferredWidth(100);
		tc = tcm.getColumn(4);
		tc.setHeaderValue("Email");
		tc.setPreferredWidth(200);

		// Laden der Kunden in den Table
		for (int i = 0; i < kunden.size(); i++) {
			table.setValueAt(kunden.get(i).getName(), i, 0);
			table.setValueAt(kunden.get(i).getVorname(), i, 1);
			table.setValueAt(kunden.get(i).getGeburtsDatum(), i, 2);
			table.setValueAt(kunden.get(i).getTelefonnummer(), i, 3);
			table.setValueAt(kunden.get(i).getEmail(), i, 4);
		}
	}

}
