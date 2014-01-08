import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.BoxLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.SpinnerNumberModel;


public class FilmBestandAendernDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5110370406318458045L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	
	/**
	 * die Variable wird true gesetzt, wenn sich Änderungen ergeben
	 */
	private boolean changed = false;
	/**
	 * DBController
	 */
	private DBController db;
	/**
	 * Ausgewählter Film
	 */
	private int idFilm = -1;
	private JSpinner spinner_1;
	private JSpinner spinner_2;
	private JLabel label_1;
	private JLabel label_2;

	/**
	 * Create the dialog.
	 */
	public FilmBestandAendernDialog(DBController db) {
		addComponentListener(new ComponentAdapter() {
			/**
			 * Aufruf des Dialogs
			 */
			@Override
			public void componentShown(ComponentEvent arg0) {	
				changed = false;
				updateUI();				
			}
			
		});
		this.db = db;
		setTitle("Anzahl Filialexemplare \u00E4ndern");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 500, 100);
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
			label_1 = new JLabel("als DVD ()  ");
			contentPanel.add(label_1);
		}
		{
			spinner_1 = new JSpinner();
			spinner_1.setModel(new SpinnerNumberModel(0, 0, 99, 1));
			contentPanel.add(spinner_1);
		}
		{
			label_2 = new JLabel("als BluRay ()  ");
			contentPanel.add(label_2);
		}
		{
			spinner_2 = new JSpinner();
			spinner_2.setModel(new SpinnerNumberModel(0, 0, 99, 1));
			contentPanel.add(spinner_2);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					/**
					 * Änderungen übernehmen
					 */
					public void actionPerformed(ActionEvent arg0) {						
						int value = abfrageDialog("Änderungen übernehmen?");
						if(value == JOptionPane.YES_OPTION){
							writeValues();
						}
						if(value != JOptionPane.CANCEL_OPTION){
							dispose();
						}
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
	
	private void updateUI(){
		//alte Werte zurücksetzen
		textField.setText("");
		label_1.setText("  als DVD (0)  ");
		label_2.setText("  als BluRay (0)  ");
		//Werte neu setzen
		 textField.setText( db.getFilm(idFilm).getTitel() );		
		 int dvd = db.getAnzahlDVD(idFilm);
		 int bluRay = db.getAnzahlBluRay(idFilm);
		 spinner_1.setValue( dvd );
		 spinner_2.setValue( bluRay );
		 label_1.setText("  als DVD ("+dvd+")  ");
		 label_2.setText(" als BluRay ("+bluRay+")  ");
	}
	
	private void writeValues(){
		int dvd = (int)spinner_1.getValue();
		int bluRay = (int)spinner_2.getValue();
		//TODO SicherheitsPopup-Abfrage "wirklich?"
		//JOptionPane.showMessageDialog
		try{
			db.writeAnzahlDVD(dvd, idFilm);
			db.writeAnzahlBluRay(bluRay, idFilm);
		}catch(Exception e){
			System.out.println(e.toString()+" Fehler beim Schreiben der neuen Anzahl DVD");
		}
		this.changed = true;
	}
	
	public boolean isChanged(){
		return changed;
	}
	
	private int abfrageDialog(String str){		
		return JOptionPane.showConfirmDialog(this, str);
	}
	
}
