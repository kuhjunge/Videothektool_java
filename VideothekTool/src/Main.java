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
	 * 
	 */
	private static final long serialVersionUID = 4648172894076113183L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// LoginScreen.openLoginScreen(null);
		LoginScreen wle = new LoginScreen();
		wle.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		wle.setLocationRelativeTo(null);//positioniert in Bildschirmmitte
		wle.setVisible(true);
		wle.addWindowListener(new WindowListener() {
			public void windowClosed(WindowEvent arg0) {
				System.out.println("Window close event occur");
				
				VideothekFrame video = new VideothekFrame();
				video.setLocationRelativeTo(null);
				video.setVisible(true);
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
