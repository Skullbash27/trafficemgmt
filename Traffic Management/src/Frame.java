import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Frame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PaintGrid paintGrid;
	private Schedule lights;
	private ToolBar toolBar;
	private final int frameWidth = 900;
	private final int frameHeight = 700;
	private Configuration config;
	private Grid grid;
	
	public Frame() {
		super("Traffic Management System");
		config  = new Configuration("traffic.conf");
		grid  = new Grid(config.getNumberOfStreets(), config.getNumberOfAvenues(), 
				config.getMinimumBlockSide(), config.getMaximumBlockSide());
		paintGrid = new PaintGrid();
		lights = new Schedule('D', config.getMaxGreenTime(), config.getYellowTime());
		toolBar = new ToolBar();
		
		toolBar.setDisplayEvents(new DisplayEvents(){
			public void draw() {
				paintGrid.start();
				lights.start();
				Car.addCars(7, grid);
				
			}			
		});
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(toolBar, BorderLayout.PAGE_START);
		add(paintGrid, BorderLayout.CENTER);
		setSize(frameWidth, frameHeight);
		setVisible(true);
	}	
}
