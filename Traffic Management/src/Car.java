import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

public class Car {
	private static int carCount = 0;
	private static HashMap <char[], Car> allCars = new HashMap<char[], Car>();
	
	private int[] xy = new int[]{-1, -1};	//x, y position in grid
	private TrafficPoint entrancePoint;
	private TrafficPoint exitPoint;
	private TrafficPoint turningPoint1 = null;			//TBD intersection ID only or whole object
	private TrafficPoint turningPoint2 = null;			//if whole object then function equals to be implemented
	private int[] dxy = new int[2];						//horizontal and vertical speed
	private Road road;
	private int lane = 1;
	private char dir;
	private char[] carID = new char[8];
	/*
	 * char[0]=entrance direction
	 * char[1]=exit direction
	 * char[2]=number of turns
	 * char[3:7]=unique ID
	 */
	boolean isQueued = false;
	private Car nextInLine = null;
	//true=car is attached to a queue, false=car running in grid
	
	private Car(int ID, TrafficPoint entrance, TrafficPoint exit) {
		this.carID = Arrays.copyOfRange(getCarIDFromInt(ID), 0, 8);
		this.carID[0] = entrance.getDirection()[0];
		this.carID[1] = exit.getDirection()[0];
		if(carID[0] == carID[1])		//for the time being
			carID[2] = '0';
		else
			carID[2] = '0';
		this.entrancePoint = entrance;
		this.exitPoint = exit;
	}
	
	public static void addCars(int numberOfCars, Grid grid) {
		Car tempCar;
		//TrafficPoint entrance, exit;
		char[] roadID;
		Road tempRoad;
		TrafficPoint entrance, exit;
		Object[] roadKeysRand = Road.getKeySet().toArray();
		int i=0;
		
		while(i<numberOfCars) {
			roadID = (char[]) roadKeysRand[new Random().nextInt(roadKeysRand.length)];
			tempRoad = Road.getRoad(roadID);
			entrance = tempRoad.getEntrancePoint();
			exit = tempRoad.getExitPoint();
			tempCar = new Car(carCount, entrance, exit);
			allCars.put(tempCar.carID, tempCar);
				entrance.queueCar(tempCar);
			carCount++;
			i++;
		}
	}
	
	public static Set<Map.Entry<char[] ,Car>> getEntrySet() {
		return allCars.entrySet();
	}
	
	public int[] getXY() {
		return xy;
	}
	public void setXY(int[] xy) {
		this.xy = Arrays.copyOfRange(xy, 0, 2);
	}
	public void moveXY(int[] dist) {
		this.xy[0] += dist[0];
		this.xy[1] += dist[1];
	}
	public char getDirection() {
		return dir;
	}
	public void setDirection(char direction) {
		this.dir = direction;
	}
	
	public boolean carQueued() {
		return isQueued;
	}
	public boolean queueCar(Car nextInLine) {
		if(isQueued)			//continue till last one and queue to it
			return this.nextInLine.queueCar(nextInLine);
		this.nextInLine = nextInLine;
		return true;
	}
	public Car Dequeue() {
		if(isQueued == false)
			return null;
		Car tempCar = this.nextInLine;
		this.nextInLine = tempCar.nextInLine;
		if(this.nextInLine == null)
			isQueued = false;
		return tempCar;
	}
	public Car nextInLine() {
		return this.nextInLine;
	}
	
	private static char[] getCarIDFromInt(int i) {
		char[] carID = new char[8];
		char[] carNumber = String.valueOf(i).toCharArray();
		int k=0;
		for(int j=0; j<(8-carNumber.length); j++) {	//zero pending to hundreds and tens position
			carID[j]='0';
			k++;
		}
		for(int j=k; j<8; j++)
			carID[j]=carNumber[j-k];
		return carID;
	}
}
