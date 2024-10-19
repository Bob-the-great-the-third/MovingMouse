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
	public static final Dimension CUR_DIMENSION=Toolkit.getDefaultToolkit().getBestCursorSize(0,0);
	public static final int movingSpeed=2;	
	
	// The thread for the bot, allows the button to be functional while the bot opperates
	private Thread thread;
	
	private JButton button;
	
	// Whether the button is toggled or not
	private boolean isOn;

	private int heightDiff;
	private int widthDiff;
	
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
		this.widthDiff=Window.movingSpeed*(Math.random()>0.5 ? -1 : 1);
		this.heightDiff=Window.movingSpeed*(Math.random()>0.5 ? -1 : 1);
		
		// Change state boolean to new value
		this.isOn=!this.isOn;
		
		// Change button graphics to the ON "design"
		this.button.setText(this.isOn ? "ON" : "OFF");
		this.button.setBackground(this.isOn ? Color.green.darker() : Color.red.darker());
	}

	@Override
	public void run() {
		// Variables for the mouse movements
		
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
				
				if(point.y>=Window.SCREENDIMENSIONS.getHeight()-Window.CUR_DIMENSION.getHeight()/2 || point.y<=1)
					heightDiff*=-1;
				
				if(point.x>=Window.SCREENDIMENSIONS.getWidth()-Window.CUR_DIMENSION.getWidth()/2 || point.x<=1)
					widthDiff*=-1;

				// Gradually make our way to the destination
				rob.mouseMove(point.x+(widthDiff),point.y+ (heightDiff));
			}
			
			// We wait 100 to 1 000 milliseconds before proceding with the loop
			rob.delay(5);
		}
	}
	
	// Useless but start the code
		public static void main(String[] args) {	
			new Window();
		}
}