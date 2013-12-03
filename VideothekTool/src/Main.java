import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * 
 */

/**
 * @author Kuhjunge
 * 
 */
public class Main extends JFrame {

	/**
	 * @param args
	 */
	/*
	 * public static void main(String[] args) { // TODO Auto-generated method
	 * stub LoginScreen.openLoginScreen(null);
	 * //VideothekFrame.openVideothek(null); }
	 */

	public static void main(String[] args) {

		// LoginScreen.openLoginScreen(null);
		LoginScreen wle = new LoginScreen();
		wle.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		wle.setVisible(true);
		wle.addWindowListener(new WindowListener() {
			public void windowClosed(WindowEvent arg0) {
				System.out.println("Window close event occur");
				VideothekFrame.openVideothek(null);
			}

			public void windowActivated(WindowEvent arg0) {
				System.out.println("Window Activated");
			}

			public void windowClosing(WindowEvent arg0) {
				System.out.println("Window Closing");
			}

			public void windowDeactivated(WindowEvent arg0) {
				System.out.println("Window Deactivated");
			}

			public void windowDeiconified(WindowEvent arg0) {
				System.out.println("Window Deiconified");
			}

			public void windowIconified(WindowEvent arg0) {
				System.out.println("Window Iconified");
			}

			public void windowOpened(WindowEvent arg0) {
				System.out.println("Window Opened");
			}
		});
	}
}
