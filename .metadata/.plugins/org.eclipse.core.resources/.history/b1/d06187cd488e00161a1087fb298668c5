import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Frame extends JFrame {
	
	private Canvas canvas;
	private ToolBar toolBar;
	private final int frameWidth = 1800;
	private final int frameHeight = 1500;
	
	public Frame() {
		super("Traffic Management System");		
		canvas = new Canvas();
		toolBar = new ToolBar();
		
		toolBar.setDisplayEvents(new DisplayEvents(){
			public void draw(String text) {
				canvas.appendText(text);			
			}			
		});
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(toolBar, BorderLayout.PAGE_START);
		add(canvas, BorderLayout.CENTER);
		setSize(frameWidth, frameHeight);
		setVisible(true);
	}	
}
