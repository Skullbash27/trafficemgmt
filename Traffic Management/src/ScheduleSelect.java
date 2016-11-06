import javax.swing.JFrame;

public class ScheduleSelect extends JFrame implements ActionListener {
	private JButton select;
	private JRadioButton dumbSchedule;
	private JRadioButton selfSchedule;
	private JRadioButton coordSchedule;
	private JRadioButton convoySchedule;
	private ButtonGroup scheduleList;
	
	public char sendOption = 'D';
	private final int statFrameWidth = 300;
	private final int statFrameHeight = 300; //Need to adjust these
	
	public ScheduleSelect() {
		super("Select A Schedule");
		setSize(statFrameWidth, statFrameHeight);	
		setLayout(new BorderLayout());
		
		select = new JButton("OK");
		dumbSchedule = new JRadioButton();
		dumbSchedule.setText("Dumb Schedule");
		dumbSchedule.setSelected(true);
        
		selfSchedule = new JRadioButton();
		selfSchedule.setText("Self-Managing Schedule");
		
		coordSchedule = new JRadioButton();
		coordSchedule.setText("Coordinated Schedule");
		
		convoySchedule = new JRadioButton();
		convoySchedule.setText("Convoy Schedule");
		
		scheduleList = new ButtonGroup();
		
		scheduleList.add(dumbSchedule);
		scheduleList.add(selfSchedule);
		scheduleList.add(coordSchedule);
		scheduleList.add(convoySchedule);
		
		add(scheduleList, BorderLayout.CENTER);
		add(select, BorderLayout.CENTER);
		
		dumbSchedule.addActionListener(this);
		selfSchedule.addActionListener(this);
		coordSchedule.addActionListener(this);
		convoySchedule.addActionListener(this);
		select.addActionListener(this);
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton clicked = (JButton) event.getSource();
		if (clicked == select) {
			if (dumbSchedule.isSelected()){
				sendOption = 'D';
			} else if (selfSchedule.isSelected()){
				sendOption = 'S';
			} else if (coordSchedule.isSelected()){
				sendOption = 'C';
			} else if (convoySchedule.isSelected()){
				sendOption = 'V';
			} else {
				sendOption = 'D'; // Default option just in case
			}
			
			// Set the schedule option in Frame
			//schOption = sendOption;
			
			// Then close the window
			setVisible(false);
		}	
	}
}
