import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

/**
 * 
 */

/**
 * @author Kuhjunge
 * 
 */
public class LoginScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3885239109790662200L;
	private JPanel contentPane;
	private JTextField textFieldNutzername; // Nutzername
	private JPasswordField textFieldPasswort; // pw
	private JTextField textFieldDB; // DB
	private JTextField textFieldDatenbankname; // DBName
	private JLabel lblPasswort;
	private JLabel lblMysqlDatenbankmysql;
	private JLabel lblDatenbankname;
	private JCheckBox chckbxLogindatenSpeichern; // Checkbox

	/*
	 * public static void openLoginScreen(String[] args) {
	 * EventQueue.invokeLater(new Runnable() { public void run() { try {
	 * LoginScreen frame = new LoginScreen(); frame.setVisible(true); } catch
	 * (Exception e) { e.printStackTrace(); } } }); }
	 */
	/**
	 * Create the frame.
	 */
	public LoginScreen() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 315, 223);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblBitteGebenSie = new JLabel(
				"Bitte geben sie ihre Zugangsdaten ein:");
		lblBitteGebenSie.setBounds(10, 11, 267, 14);
		contentPane.add(lblBitteGebenSie);

		textFieldNutzername = new JTextField();
		textFieldNutzername.setBounds(145, 34, 148, 20);
		contentPane.add(textFieldNutzername);
		textFieldNutzername.setColumns(10);

		textFieldPasswort = new JPasswordField();
		textFieldPasswort.setBounds(145, 61, 148, 20);
		contentPane.add(textFieldPasswort);
		textFieldPasswort.setColumns(10);

		textFieldDB = new JTextField();
		textFieldDB
				.setToolTipText("Bitte geben sie NICHT mysql:// vor der Datenbank URL an!");
		textFieldDB.setBounds(145, 92, 148, 20);
		contentPane.add(textFieldDB);
		textFieldDB.setColumns(10);

		textFieldDatenbankname = new JTextField();
		textFieldDatenbankname.setBounds(145, 123, 147, 20);
		contentPane.add(textFieldDatenbankname);
		textFieldDatenbankname.setColumns(10);

		JLabel lblNutzername = new JLabel("Nutzername");
		lblNutzername.setBounds(10, 36, 125, 14);
		contentPane.add(lblNutzername);

		lblPasswort = new JLabel("Passwort:");
		lblPasswort.setBounds(10, 64, 125, 14);
		contentPane.add(lblPasswort);

		lblMysqlDatenbankmysql = new JLabel("MySQL Datenbank");
		lblMysqlDatenbankmysql.setBounds(10, 95, 125, 14);
		contentPane.add(lblMysqlDatenbankmysql);

		lblDatenbankname = new JLabel("Datenbankname");
		lblDatenbankname.setBounds(10, 126, 125, 14);
		contentPane.add(lblDatenbankname);

		chckbxLogindatenSpeichern = new JCheckBox("Logindaten speichern");
		chckbxLogindatenSpeichern.setBounds(10, 154, 164, 23);
		contentPane.add(chckbxLogindatenSpeichern);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				char[] zeichen = textFieldPasswort.getPassword();
				String password = new String(zeichen);
				SaveLoader.setDbName(textFieldDatenbankname.getText());
				SaveLoader.setPassword(password);
				SaveLoader.setUrl(textFieldDB.getText());
				SaveLoader.setUsername(textFieldNutzername.getText());
				SaveLoader.setSaveuser(chckbxLogindatenSpeichern.isSelected());
				SaveLoader.saveToXML();

				// Test Datenbank
				try{
					DBController db;
					db = new DBController();
					db.connect();
					SaveLoader.saveToXML();
					dispose();
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(null, "Fehler beim Verbinden mit der Datenbank!\r\n(" + e.toString()+")","Fehler",  JOptionPane.OK_OPTION);
					
				}
			}
		});
		btnLogin.setBounds(200, 154, 89, 23);
		contentPane.add(btnLogin);
		SaveLoader.read();
		textFieldNutzername.setText(SaveLoader.getUsername());
		textFieldPasswort.setText(SaveLoader.getPassword());
		textFieldDB.setText(SaveLoader.getcleanUrl());
		textFieldDatenbankname.setText(SaveLoader.getDbName());
		chckbxLogindatenSpeichern.setSelected(SaveLoader.isSaveuser());
	}
}
