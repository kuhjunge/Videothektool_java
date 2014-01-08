import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class FilmBestandAendernDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	/**
	 * DBController
	 */
	private DBController db;
	/**
	 * Ausgewählter Film
	 */
	private int idFilm = -1;

	/**
	 * Create the dialog.
	 */
	public FilmBestandAendernDialog(DBController db) {
		this.db = db;
		setTitle("Anzahl Filialexemplare \u00E4ndern");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 368, 100);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			textField = new JTextField();
			textField.setToolTipText("Ausgew\u00E4hlter Film");
			textField.setEditable(false);
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		{
			JLabel lblAlsDvd = new JLabel("als DVD ");
			contentPanel.add(lblAlsDvd);
		}
		{
			JSpinner spinner = new JSpinner();
			contentPanel.add(spinner);
		}
		{
			JLabel lblAlsBluray = new JLabel("als BluRay ");
			contentPanel.add(lblAlsBluray);
		}
		{
			JSpinner spinner = new JSpinner();
			contentPanel.add(spinner);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					/**
					 * Ohne Änderungen zurück
					 */					
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public void setFilm(int idFilm){
		this.idFilm = idFilm;
	}
	
}
