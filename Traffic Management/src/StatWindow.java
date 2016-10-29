import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class StatWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private final int statFrameWidth = 700;
	private final int statFrameHeight = 700;
	Statistics stats;
	
	public StatWindow()
	{
		super("Statistics Window");
		setSize(statFrameWidth, statFrameHeight);	
		setLayout(new BorderLayout());
		textArea = new JTextArea();
		add(textArea, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		resultDisplayed();
	}
	
	public void resultDisplayed() {		
		textArea.append("Final statistics:\n");
		Statistics.carStatistics();
		Statistics.carTimeSum();
		Statistics.carDistanceSum();
		Statistics.averageStats();
		Statistics.smallestStats();
		Statistics.longestStats();
		textArea.append("\n");
		textArea.append("Cars running in grid: " +Statistics.carsInGrid +" cars\n");
		textArea.append("Total Time of all cars in grid: " +Statistics.totalCarTime
				+" seconds\n");
		textArea.append("Total Distance travelled by all cars in grid: " 
				+ Statistics.totalCarDistance +"\n");
		textArea.append("Average time by all cars in grid: " 
				+ Statistics.averageTime +" seconds\n");
		textArea.append("Average distance travelled by all cars in the grid: " 
				+ Statistics.averageDistance +"\n");
		textArea.append("Shortest time travelled by a car in the grid: " 
				+ Statistics.shortTime +" seconds\n");
		textArea.append("Shortest distance travelled by a car in the grid: " 
				+ Statistics.shortDistance +"\n");
		textArea.append("Longest time travelled by a car in the grid: " 
				+ Statistics.longTime +" seconds" +"\n");
		textArea.append("Longest distance travelled by a car in the grid: " 
				+ Statistics.longDistance);
	}
}