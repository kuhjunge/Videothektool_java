import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author Rene Kremer
 * @version 1
 */
public class UnpaidInvoiceDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5766788663811198237L;
	private final JPanel contentPanel = new JPanel();
	private JTextArea textArea;
	/**
	 * Anzeigetext für das Textfeld
	 */
	private String value = "";

	/**
	 * Create the dialog.
	 * 
	 * @param b
	 * @param string
	 * @param videothekFrame
	 */
	public UnpaidInvoiceDialog(JFrame frame, String string, boolean b) {
		super(frame, string, b);
		addComponentListener(new ComponentAdapter() {
			/**
			 * Beim Anzeigen wird der Text gesetzt
			 * 
			 * @param arg0
			 */
			@Override
			public void componentShown(ComponentEvent arg0) {
				textArea.setText("");
				textArea.setText(value);
			}
		});
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			textArea = new JTextArea();
			textArea.setEditable(false);
			contentPanel.add(textArea, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					/**
					 * schließt den Dialog
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
					 * schließt den Dialog
					 */
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
	 * Setzt das Attribut Value
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
