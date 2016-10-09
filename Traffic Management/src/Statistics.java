import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Statistics extends JFrame {
	
	private JTextArea textArea;
	private final int statFrameWidth = 700;
	private final int statFrameHeight = 700;
	
	public Statistics()
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
		textArea.append("This is the where the Statistics will be written too");
	}	
}