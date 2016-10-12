import java.util.Arrays;

public class TrafficControl implements Runnable {
	
	private static int greenTime = 5000;
	private static int yellowTime = 2000;
	private int sleepTime;
	
	private char[] pointID = new char[8];
	/*
	 * TrafficControl is a class to represent intersection points
	 * ID divided into two 4-char IDs
	 * first 4-chars [0:3] represents the street ID
	 * second 4-chars [4:7] represents the avenue ID
	 * first char of the road ID 1=entrance, 2=exit, 3=street, 4=avenue
	 */
	private char[] roadDir = new char[2];
	
	private int[] xy = new int[2];
	
	private char[] control = new char[2];
	/*
	 * first char street light status R=Red G=Gree Y=Yellow
	 * second char avenue light status R=Red G=Gree Y=Yellow
	 * control 'EN'=entrance 'EX'=exit points
	 */
	
	private Thread lightCycle;
	
	public TrafficControl(char[] streetID, char[] avenueID, 
			char[] roadDir, char[] control, int[] xy) {
		if(streetID.length != 4 || avenueID.length != 4 || xy.length != 2) {
			return;
		}
		for(int i=0; i<pointID.length; i++){		//copying road IDs as traffic light ID
			if(i<4)
				pointID[i]=streetID[i];
			else
				pointID[i]=avenueID[i-4];
		}
		this.roadDir = Arrays.copyOfRange(roadDir, 0, 2);
		this.xy = Arrays.copyOfRange(xy, 0, 2);
		this.control = Arrays.copyOfRange(control, 0, 2);
		if(control[0] != 'E')
			this.start();
	}
	
	public char[] getPointID() {
		return pointID;
	}
	public char getStreetDir() {
		return roadDir[0];
	}
	public char getAvenueDir() {
		return roadDir[1];
	}
	public int getX() {
		return xy[0];
	}
	public int getY() {
		return xy[1];
	}
	public char getStreetControl() {
		return control[0];
	}
	public char getAvenueControl() {
		return control[1];
	}
	
	public boolean setStreetControl(char control) {
		if(pointID[0] != '3' || pointID[0] != '4')		//not street or avenue
			return false;
		else if((control == 'G' || control == 'Y') && this.control[1] != 'R')
			//other light must be red to change this to green or yellow
			return false;
		else
			this.control[0] = control;
		return true;
	}
	
	public boolean setAvenueControl(char control) {
		if(pointID[4] != '3' || pointID[4] != '4')		//not street or avenue
			return false;
		else if((control == 'G' || control == 'Y') && this.control[0] != 'R')
			//other light must be red to change this to green or yellow
			return false;
		else
			this.control[1] = control;
		return true;
	}

	@Override
	public void run() {
		while (true) {
			if (control[0] == 'R'){
				if(control[1] == 'G') {
					control[1] = 'Y';
					sleepTime = yellowTime;
				} else if(control[1] == 'Y') {
					control[1] = 'R';
					control[0] = 'G';
					sleepTime = greenTime;
				} else if(control[1] == 'R') {
					control[0] = 'G';
					sleepTime = greenTime;
				}
			} else if(control[0] == 'G') {
				if(control[1] == 'G') {
					control[1] = 'R';
					sleepTime = greenTime;
				} else if(control[1] == 'Y') {
					control[1] = 'R';
					sleepTime = yellowTime;
				} else if(control[1] == 'R') {
					control[0] = 'Y';
					sleepTime = yellowTime;
				}
			} else if(control[0] == 'Y') {
				if(control[1] == 'G') {
					control[1] = 'R';
					sleepTime = yellowTime;
				} else if(control[1] == 'Y') {
					control[1] = 'R';
					sleepTime = yellowTime;
				} else if(control[1] == 'R') {
					control[0] = 'R';
					control[1] = 'G';
					sleepTime = greenTime;
				}
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	public void start() {
		lightCycle = new Thread(this);
		lightCycle.start();
	}
	
	public void stop() {
		lightCycle.interrupt();
	}
}
