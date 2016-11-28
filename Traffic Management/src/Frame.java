import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Frame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected PaintGrid paintGrid;
	protected Schedule lights;
	protected final int frameWidth = 800;
	protected final int frameHeight = 600;
	protected Configuration config;
	protected Grid grid;
	public static boolean isRunning = true;
	public static int systemTime = 0;
	public static char schedulingScheme = 'D';
	/*
	 * communicating configuration variables to classes through static variables
	 */
	protected static int carSpeed, carAcceleration, carLength, 
			carWidth, Clearance, fullDistance, Lambda, NumberOfCars;
	
	public Frame() {
		super("Traffic Management System");
		config  = new Configuration("traffic.conf");
		config.printConfig();
		carSpeed = config.CarSpeed;
		carAcceleration = config.CarAcceleration;
		carLength = config.CarLength;
		carWidth = config.CarWidth;
		Clearance = config.Clearance;
		NumberOfCars = config.NumberOfCars;
		Lambda = config.Lambda;
		Frame.schedulingScheme = config.ScheulingScheme;
		fullDistance = 0;
		int temp = 0;
		while(temp <= carSpeed) {
			temp += carAcceleration;
			fullDistance += temp;
		}
		fullDistance += Clearance;
		/*for(i=0; i<SpeedAndDist[0].length; i++) {
			System.out.println("Speed: "+SpeedAndDist[0][i]+" Distance: "+SpeedAndDist[1][i]);
		}
		System.out.println("Full distance = "+fullDistance);*/
		grid  = new Grid(config.NumberOfStreets, config.NumberOfAvenues, 
				config.MinimumBlockSide, config.MaximumBlockSide);
		/*for(Map.Entry<char[], Road> entry : Road.getEntrySet()) {
			System.out.println(entry.getValue().sectors[1]+"\t"+entry.getValue().roadDir);
		}*/
		paintGrid = new PaintGrid();
		if(config.ScheulingScheme == 'D')
			System.out.print("Dumb");
		else if(config.ScheulingScheme == 'S')
			System.out.print("Self Managed");
		else if(config.ScheulingScheme == 'C')
			System.out.print("Coordinated");
		else if(config.ScheulingScheme == 'V')
			System.out.print("Convoy");
		System.out.println(" Scheduling is in use");
		lights = new Schedule(config.ScheulingScheme, config.MaxGreenTime, config.YellowTime);
				
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(paintGrid, BorderLayout.CENTER);
		setSize(frameWidth, frameHeight);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		paintGrid.paint(paintGrid.getGraphics());
		while(isRunning) {
			paintGrid.relax();
			paintGrid.repaint();
			lights.workTime();
			lights.whatCars();
			systemTime += 100;
			if(Car.carCount != 0 && Car.sCarCount == Car.carCount && Car.mCarCount == 0) {
				isRunning = false;
			}
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		new StatWindow();
	}
	
}