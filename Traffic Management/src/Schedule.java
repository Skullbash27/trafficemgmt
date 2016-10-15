import java.util.Map.Entry;

public class Schedule implements Runnable{
	
	private int greenTime = 5000;
	private int yellowTime = 2000;
	private int sleepTime = 100;
	
	private boolean isRunning = false;
	
	private Thread lightSchedule;
	char scheduleType = 'D';
	/*
	 * D=dumb scheduling
	 * S=self scheduling
	 * C=coordinated scheduling
	 * V=convoy scheduling
	 */
	
	public Schedule(char scheduleType, int greenTime, int yellowTime) {
		this.scheduleType = scheduleType;
		this.greenTime = greenTime;
		this.yellowTime = yellowTime;
	}
	
	private synchronized void workTime() {
		TrafficPoint tempPoint = null;
		if(scheduleType == 'D') {
			for(Entry<char[], TrafficPoint> entry : TrafficPoint.getEntrySet()) {
				tempPoint = entry.getValue();
				if(tempPoint.getControl()[0] != 'E') { 		//not entrance or exit
					tempPoint.nextControl();
					if(tempPoint.getControl()[0] == 'G' || tempPoint.getControl()[1] == 'G')
						sleepTime = greenTime;
					else if(tempPoint.getControl()[0] == 'Y' || tempPoint.getControl()[1] == 'Y')
						sleepTime = yellowTime;
				}
			}
			
		} else
			sleepTime = 100;
	}
	
	@Override
	public void run() {
		while (isRunning) {
			workTime();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	public void start() {
		lightSchedule = new Thread(this);
		isRunning = true;
		lightSchedule.start();
	}
	public void pause() {
		lightSchedule.interrupt();
		isRunning = false;
	}
}
