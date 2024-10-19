import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;



/** Simple class that opens a frame on the top left of the screen. The whole frame is a button. When switched on a bot
 * moves the mouse around every second or so.
 * 
 */
public class Window extends JFrame implements Runnable {
	// Apparently mandatory with a JComponent
	private static final long serialVersionUID = 1L;
	
	// Constant used to choose the destination of the mouse
	public static final Dimension SCREENDIMENSIONS=Toolkit.getDefaultToolkit().getScreenSize();	
	
	// The thread for the bot, allows the button to be functional while the bot opperates
	private Thread thread;
	
	private JButton button;
	
	// Whether the button is toggled or not
	private boolean isOn;
	
	/** Constructor of the window
	 * 
	 * Constructor to the code of the window. It sets up the JFrame frame configuration, the thread and values.
	 */
	public Window () {
		super("AFK");
		
		this.setUndecorated(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Button SetUp with default OFF position
		this.button=new JButton("OFF");
		this.isOn=false;
		this.button.setPreferredSize(new Dimension(100,100));
		
		Font f=new Font("Arial", Font.BOLD, 20);
		this.button.setForeground(Color.white);
		this.button.setBackground(Color.red.darker());
		this.button.setFont(f);
		
		// End of the page configuration
		this.add(this.button);
		this.button.addActionListener(new Listener()); // Listener is a private class witch implements ActionListener
													   // And turns out to be a little Overkill
		this.pack();
		this.setVisible(true); 
		
		// Thread setUp
		this.thread=new Thread(this);
		this.thread.start();
	}

	// Self explanatory
	private class Listener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			Window.this.changeState(); // Calls changeState() when button is pressed
		}
		
	}
	
	/** Changes the state of the button and application
	 * 
	 * Changes the state of the button and application when the button is pressed.
	 */
	public void changeState() {
		// Change state boolean to new value
		this.isOn=!this.isOn;
		
		// Change button graphics to the ON "design"
		this.button.setText(this.isOn ? "ON" : "OFF");
		this.button.setBackground(this.isOn ? Color.green.darker() : Color.red.darker());
	}

	@Override
	public void run() {
		// Variables for the mouse movements
		int heightDiff,widthDiff;
		
		// Position of the mouse at the start
		Point point;
		
		// Class used to emulate the mouse actions
		Robot rob=null;
		try {
			rob=new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		// While the app is running
		while(this.thread!= null) {
			
			// If the functionality is toggled
			if(isOn) {
				
				// We take the current mouse location
				point=MouseInfo.getPointerInfo().getLocation();
				
				// Take a random destination in the screen
				int destX=(int)(Math.random()*SCREENDIMENSIONS.width);
				int destY=(int)(Math.random()*SCREENDIMENSIONS.height);
				
				// Calculate the size of the 100 steps to the destination
				widthDiff=(destX-point.x)/100;
				heightDiff=(destY-point.y)/100;
				
				// Gradually make our way to the destination
				for(int i=0;i<100 && isOn;i++) { // The loop stops if the bot is disabled
					rob.mouseMove(point.x+(i*widthDiff),point.y+ (heightDiff*i));
					rob.delay(1000/60);
				}
			}
			
			// We wait 100 to 1 000 milliseconds before proceding with the loop
			rob.delay(((int)(Math.random()*10000))+1000);
		}
	}
	
	// Useless but start the code
		public static void main(String[] args) {	
			new Window();
		}
}