import java.util.Map.Entry;

public class Schedule {
	
	private int greenTime = 5000;
	private int yellowTime = 2000;
	private int sleepTime = 100;
	private double x = 0;
	private double xFact = x;
	
	//private boolean isRunning = false;
	
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
		sleepTime = 100;
		System.out.println("Time\tWiting\tIn\tMoving\tTotal Out");
	}
	
	public void workTime() {
		TrafficPoint tempPoint = null;
		if(scheduleType == 'D') {
			for(Entry<char[], TrafficPoint> entry : TrafficPoint.getEntrySet()) {
				if(entry.getValue().control[0] == 'E') continue;
				tempPoint = entry.getValue();
				tempPoint.cycleTime += sleepTime;
				if((tempPoint.control[0] == 'Y' || tempPoint.control[1] == 'Y') &&
						tempPoint.cycleTime > (yellowTime+sleepTime)) {
					tempPoint.nextControl();
					tempPoint.cycleTime = 0;
				} else if((tempPoint.control[0] == 'G' || tempPoint.control[1] == 'G') && 
						tempPoint.cycleTime > (greenTime+sleepTime)) {
					tempPoint.nextControl();
					tempPoint.cycleTime = 0;
				}
			}
		} else if(scheduleType == 'S') {
			for(Entry<char[], TrafficPoint> entry : TrafficPoint.getEntrySet()) {
				if(entry.getValue().control[0] == 'E') continue;
				tempPoint = entry.getValue();
				tempPoint.cycleTime += sleepTime;
				if((tempPoint.control[0] == 'Y' || tempPoint.control[1] == 'Y') &&
						tempPoint.cycleTime > (yellowTime+sleepTime)) {
					tempPoint.nextControl();
					tempPoint.cycleTime = 0;
					/*if(scheduleType == 'C') { keep separate
						if(tempPoint.control[0] == 'G') {
							tempPoint.nextStreet.expectedCars[0] = tempPoint.comingCars[0];
							tempPoint.nextStreet
						}
					}*/
				} else if((tempPoint.control[0] == 'G' || tempPoint.control[1] == 'G') &&
						tempPoint.cycleTime > (greenTime+sleepTime)) {
					tempPoint.nextControl();
					tempPoint.cycleTime = 0;
				} else if((tempPoint.control[0] != 'Y' && tempPoint.control[1] != 'Y') &&
						tempPoint.cycleTime > (yellowTime+sleepTime)) {
					if(tempPoint.control[0] == 'R' && 
							(tempPoint.comingCars[0] > tempPoint.comingCars[1])) {
						tempPoint.nextControl();
						tempPoint.cycleTime = 0;
					} else if(tempPoint.control[1] == 'R' &&
							(tempPoint.comingCars[1] > tempPoint.comingCars[0])) {
						tempPoint.nextControl();
						tempPoint.cycleTime = 0;
					}
				}
			}
		} else if(scheduleType == 'C') {
			for(Entry<char[], TrafficPoint> entry : TrafficPoint.getEntrySet()) {
				if(entry.getValue().control[0] == 'E') continue;
				tempPoint = entry.getValue();
				tempPoint.cycleTime += sleepTime;
				if((tempPoint.control[0] == 'Y' || tempPoint.control[1] == 'Y') &&
						tempPoint.cycleTime > (yellowTime+sleepTime)) {
					tempPoint.nextControl();
					tempPoint.cycleTime = 0;
				} else if((tempPoint.control[0] == 'G' || tempPoint.control[1] == 'G') && 
						tempPoint.cycleTime > (greenTime+sleepTime)) {
					tempPoint.nextControl();
					tempPoint.cycleTime = 0;
				} else if((tempPoint.control[0] != 'Y' && tempPoint.control[1] != 'Y') && 
						tempPoint.cycleTime > (yellowTime+sleepTime)) {
					if(tempPoint.control[0] == 'R' && 
							((tempPoint.expectedCars[0]+tempPoint.comingCars[0]) > 
							(tempPoint.expectedCars[1]+tempPoint.comingCars[1]))) {
						tempPoint.nextControl();
						tempPoint.cycleTime = 0;
					} else if(tempPoint.control[1] == 'R' && 
							((tempPoint.expectedCars[1]+tempPoint.comingCars[1]) > 
							(tempPoint.expectedCars[0]+tempPoint.comingCars[0]))) {
						tempPoint.nextControl();
						tempPoint.cycleTime = 0;
					}
				}
			}
		}
	}
	
	public void whatCars() {
		if(x > 0) {
			this.x++;
			this.xFact = this.x * this.xFact;
		} else {
			x = 1; 
			xFact = x;
		}
		int temp = (Frame.systemTime == 0)? 0 : (int) ((Frame.NumberOfCars*Math.exp(-1*Frame.Lambda)*(Frame.Lambda*this.x))/(this.xFact));
		if(temp > 0) Car.addCars(temp);
		System.out.println(Frame.systemTime+"\t"+(Car.carCount-Car.mCarCount-Car.sCarCount)
				+"\t"+temp+"\t"+Car.mCarCount+"\t"+Car.sCarCount);
	}
	
	/*@Override
	public void run() {
		while (isRunning) {
			workTime();
			whatCars();
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
	}*/
}
