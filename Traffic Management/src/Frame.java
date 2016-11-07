import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Frame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected PaintGrid paintGrid;
	protected Schedule lights;
	protected ToolBar toolBar;
	protected final int frameWidth = 800;
	protected final int frameHeight = 600;
	protected Configuration config;
	protected Grid grid;
	public static int carCount = 15; //added by Austin
	
	public Frame() {
		super("Traffic Management System");
		config  = new Configuration("traffic.conf");
		grid  = new Grid(config.NumberOfStreets, config.NumberOfAvenues, 
				config.MinimumBlockSide, config.MaximumBlockSide, 
				config.CarLength, config.CarWidth, config.Clearance);
		paintGrid = new PaintGrid(config.CarLength, config.CarWidth, config.Clearance);
		lights = new Schedule('D', config.MaxGreenTime, config.YellowTime);
		carCount = config.NumberOfCars;
		toolBar = new ToolBar();
		
		toolBar.setDisplayEvents(new DisplayEvents(){
			public void draw() {
				paintGrid.start();
				lights.start();
				Car.addCars(carCount, grid);
				
			}			
		});
				
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(toolBar, BorderLayout.PAGE_START);
		add(paintGrid, BorderLayout.CENTER);
		//setSize(frameWidth, frameHeight);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
    		
        
	}	
}