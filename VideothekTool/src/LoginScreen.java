import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 */

/**
 * @author Kuhjunge
 *
 */
public class LoginScreen extends JFrame {

	private JPanel contentPane;
	SaveLoader sl = new SaveLoader(); // das SaveLoader Objekt
	private JTextField textFieldNutzername; // Nutzername
	private JPasswordField textFieldPasswort; // pw
	private JTextField textFieldDB; // DB
	private JTextField textFieldDatenbankname; // DBName
	private JLabel lblPasswort;
	private JLabel lblMysqlDatenbankmysql;
	private JLabel lblDatenbankname;
	private JCheckBox chckbxLogindatenSpeichern; // Checkbox

	public static void openLoginScreen(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen frame = new LoginScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
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
		
		JLabel lblBitteGebenSie = new JLabel("Bitte geben sie ihre Zugangsdaten ein:");
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
		textFieldDB.setToolTipText("Bitte geben sie NICHT mysql:// vor der Datenbank URL an!");
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
				sl.setDbName(textFieldDatenbankname.getText());
				sl.setPassword(password);
				sl.setUrl(textFieldDB.getText());
				sl.setUsername(textFieldNutzername.getText());
				sl.setSaveuser(chckbxLogindatenSpeichern.isSelected());
				sl.saveToXML();
				dispose();
			}
		});
		btnLogin.setBounds(200, 154, 89, 23);
		contentPane.add(btnLogin);
		sl.read();
		textFieldNutzername.setText(sl.getUsername());
		textFieldPasswort.setText(sl.getPassword());
		textFieldDB.setText(sl.getUrl());
		textFieldDatenbankname.setText(sl.getDbName());
		chckbxLogindatenSpeichern.setSelected(sl.isSaveuser());
	}
}
