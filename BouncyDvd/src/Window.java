import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.imageio.ImageIO;


/** Simple class that opens a frame on the top left of the screen. The whole frame is a button. When switched on a bot
 * moves the mouse around every second or so.
 * 
 */
public class Window extends JFrame implements Runnable {
	// Apparently mandatory with a JComponent
	private static final long serialVersionUID = 1L;
	
	// Constant used to choose the destination of the mouse
	public static final Dimension SCREENDIMENSIONS=Toolkit.getDefaultToolkit().getScreenSize();
	public static final int movingSpeed=3;	
	public static final int DvdHeight=100;
	public static final int DvdWidth=200;

	public static Image dvd;
	
	static{
		try {
			Window.dvd=ImageIO.read(new File("DvdLogo.png")).getScaledInstance(Window.DvdWidth, Window.DvdHeight, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

	// The thread for the bot, allows the button to be functional while the bot opperates
	private Thread thread;
	
	private JButton button;
	
	// Whether the button is toggled or not
	private boolean isOn;

	private JFrame dvdFrame;

	private int y;
	private int x;

	private int heightSpeed;
	private int widthSpeed;

	
	/** Constructor of the window
	 * 
	 * Constructor to the code of the window. It sets up the JFrame frame configuration, the thread and values.
	 */
	public Window () {
		super("AFK");
		
		this.setUndecorated(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setAutoRequestFocus(true); 

		// Button SetUp with default OFF position
		this.button=new JButton("OFF");
		this.isOn=false;
		this.button.setPreferredSize(new Dimension(100,100));
		
		Font f=new Font("Arial", Font.BOLD, 20);
		this.button.setForeground(Color.white);
		this.button.setBackground(Color.red.darker());
		this.button.setFont(f);

		this.dvdFrame=new JFrame();
		this.dvdFrame.setUndecorated(true);
		this.dvdFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.dvdFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		JPanel jp=new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(Window.dvd, Window.this.x, Window.this.y,Window.DvdWidth,Window.DvdHeight, Window.this);
			}
		};

		this.dvdFrame.setBackground(new Color(0,0,0,0));

		this.dvdFrame.add(jp);


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

		this.dvdFrame.setVisible(this.isOn);
		this.toFront();
	}

	@Override
	public void run() {
		// Variables for the mouse movements
		this.y=(int)Window.SCREENDIMENSIONS.getHeight()/2 - Window.DvdHeight/2;
		this.x=(int)Window.SCREENDIMENSIONS.getWidth()/2 - Window.DvdWidth/2;
		
		this.heightSpeed=Window.movingSpeed*(Math.random()>0.5 ? 1 : -1);
		this.widthSpeed=Window.movingSpeed*(Math.random()>0.5 ? 1 : -1);
		
		// Class used to emulate the mouse actions
		Robot rob=null;
		try {
			rob=new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		boolean up=true;

		// While the app is running
		while(this.thread!= null) {
			
			// If the functionality is toggled
			if(isOn) {
				
				// We take the current mouse location
				
				if(this.y>=Window.SCREENDIMENSIONS.getHeight()-Window.DvdHeight || this.y<=1)
					this.heightSpeed*=-1;
				
				if(this.x>=Window.SCREENDIMENSIONS.getWidth()-Window.DvdWidth || this.x<=1)
					this.widthSpeed*=-1;

				this.x+=this.widthSpeed;
				this.y+=this.heightSpeed;

				this.dvdFrame.repaint();

				// Gradually make our way to the destination
				// rob.mouseMove((int)Window.SCREENDIMENSIONS.getWidth(), up ? 100 : 200);
				// up= !up;
			}	
			
			// We wait 10 milliseconds before proceding with the loop
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// Useless but starts the code
	public static void main(String[] args) {	
		new Window();
	}
}