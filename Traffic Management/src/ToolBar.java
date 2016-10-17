import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ToolBar extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton start;
	private JButton stop;
	private DisplayEvents events;
	public boolean isRunning = false;		 
	
	public ToolBar() {		
		start = new JButton("Start");
		stop = new JButton("Stop");		
		start.addActionListener(this);
		stop.addActionListener(this);		
		setLayout(new FlowLayout());		
		add(start);
		add(stop);
	}	

	public void setDisplayEvents(DisplayEvents events) {
		this.events = events;
	}
		
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton clicked = (JButton) event.getSource();
		if (clicked == start) {
			start.setEnabled(false);
			isRunning = true;
			if (events != null) {
				events.draw();		
				
			}				
		}		
		else if (isRunning == true) {
			if (events != null) {
			//	events.draw();
			}
			Statistics stats = new Statistics();
			stats.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			stats.setVisible(true);
		}		
	}
}
