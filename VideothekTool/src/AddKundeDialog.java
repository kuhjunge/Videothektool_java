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

import javax.swing.JTable;
import javax.swing.JScrollPane;

public class AddKundeDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private DBController dbi;
	private JTable table;

	/**
	 * Create the dialog.
	 */
	public AddKundeDialog(DBController db) {
		setTitle("Kunden");

		this.dbi = db;

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
		//Eigenschaften des Tables
		{	
			//Überschriften
			TableModel model = new DefaultTableModel(0, 5);
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
			//Lade Kundendaten aus DB
		}
	}
	

}
