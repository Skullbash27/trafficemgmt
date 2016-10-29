import java.util.Map.Entry;

public class Schedule implements Runnable{
	
	private int greenTime = 5000;
	private int yellowTime = 2000;
	private int sleepTime = 100;
	
	private boolean isRunning = false;
	
	//Code added here
	TrafficPoint carQueue; 
	
	private Thread lightSchedule;
	char scheduleType = 'S';
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
		
		//Code added here
		int redTime = greenTime + yellowTime;
		
		
		if(scheduleType == 'D') {
			for(Entry<char[], TrafficPoint> entry : TrafficPoint.getEntrySet()) {
				tempPoint = entry.getValue();
				if(tempPoint.control[0] != 'E') { 		//not entrance or exit
					tempPoint.nextControl();
					if(tempPoint.control[0] == 'G' || tempPoint.control[1] == 'G')
						sleepTime = greenTime;
					else if(tempPoint.control[0] == 'Y' || tempPoint.control[1] == 'Y')
						sleepTime = yellowTime;
				}
			}
			
		} 
		//Code added here
		else if (scheduleType == 'S') {
			for(Entry<char[], TrafficPoint> entry : TrafficPoint.getEntrySet()) {
				tempPoint = entry.getValue();
				if(tempPoint.control[0] != 'E') {
					//tempPoint.nextControl();
					tempPoint.queueTimer += sleepTime;
					if(tempPoint.control[0] == 'Y' || tempPoint.control[1] == 'Y') {
						if(tempPoint.queueTimer >= yellowTime) {
							tempPoint.queueTimer = -1*sleepTime;
							//tempPoint.queueTimer = 0;
							tempPoint.nextControl();
						}
					} else if(tempPoint.queueTimer >= redTime) {
						tempPoint.nextControl();
						continue;
					} else if(tempPoint.control[0] == 'R') {
						if (carQueue.queuedCarsInStreet >= 5) {
							tempPoint.control[0] = 'G';
							//tempPoint.nextControl();
							continue;
						}
					} else if(tempPoint.control[1] == 'R') {
						if (carQueue.queuedCarsInAvenue >= 5) {
							tempPoint.control[1] = 'G';
							//tempPoint.nextControl();
							continue;
						}
						
					}
				}
			}
		//Code added here
			
		}	
		
		else {
			sleepTime = 100;
		}
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
		if(lightSchedule == null)
			lightSchedule = new Thread(this);
		isRunning = true;
		lightSchedule.start();
	}
	public void pause() {
		lightSchedule.interrupt();
		isRunning = false;
	}
}
