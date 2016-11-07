import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class StatWindow extends JFrame {
	
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
		setLocationRelativeTo(null);
		setVisible(true);
		resultDisplayed();
	}
		
	public void resultDisplayed() {		
		textArea.append("Final statistics:\n");
		stats.carStatistics();
		stats.carTimeSum();
		stats.carDistanceSum();
		stats.averageStats();
		stats.smallestStats();
		stats.longestStats();
		textArea.append("\n");
		textArea.append("Cars running in grid: " +stats.carsInGrid +" cars\n");
		textArea.append("Total Time of all cars in grid: " +stats.totalCarListTime +" seconds\n");
		textArea.append("Total Distance travelled by all cars in grid: " +stats.totalCarListDistance +"\n");
		textArea.append("Average time by all cars in grid: " +stats.averageTime +" seconds\n");
		textArea.append("Average distance travelled by all cars in the grid: " +stats.averageDistance +"\n");
		textArea.append("Shortest time travelled by a car in the grid: " +stats.shortTime +" seconds\n");
		textArea.append("Shortest distance travelled by a car in the grid: " +stats.shortDistance +"\n");
		textArea.append("Longest time travelled by a car in the grid: " +stats.longTime +" seconds" +"\n");
		textArea.append("Longest distance travelled by a car in the grid: " +stats.longDistance);
	}	
}