import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Car {
	protected static int carCount = 0;
	private static HashMap <char[], Car> allCars = new HashMap<char[], Car>();
	
	protected int[] xy = new int[]{-1, -1};	//x, y position in grid
	protected TrafficPoint entrancePoint;
	protected TrafficPoint exitPoint;
	protected TrafficPoint turningPoint1 = null;			//TBD intersection ID only or whole object
	protected TrafficPoint turningPoint2 = null;			//if whole object then function equals to be implemented
	protected TrafficPoint nextPoint = null;
	protected int[] dxy = new int[2];						//horizontal and vertical speed
	protected Road road;
	protected char lane = 'M';
	/*
	 * M=middle, L=left, R=right
	 */
	protected char dir;
	protected char[] carID = new char[8];
	/*
	 * char[0]=entrance direction
	 * char[1]=exit direction
	 * char[2]=number of turns
	 * char[3:7]=unique ID
	 */
	//Code added here
	public long entryTime;
	public long exitTime;
	public long carDistance;
	//String tempStringExit;
	//Code added here
	
	protected boolean isQueued = false;
	//protected boolean inAQueue = false;
	protected Car nextInLine = null;
	//true=car is attached to a queue, false=car running in grid
	protected char phase = 'Q';
	/*
	 * Q=queue or before, M=moving, S=statistics or after
	 */
	private Car(int ID, TrafficPoint entrance, TrafficPoint exit) {
		this.carID = Arrays.copyOfRange(getCarIDFromInt(ID), 0, 8);
		this.carID[0] = entrance.roadDir[0];	//Direction same for entrance
		this.carID[1] = exit.roadDir[0];		//Direction same for exit
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
		int rand = 0;
		while(i<numberOfCars) {
			roadID = (char[]) roadKeysRand[new Random().nextInt(roadKeysRand.length)];
			tempRoad = Road.getRoad(roadID);
			//if(tempRoad.roadDir != 'E') continue;
			entrance = tempRoad.entrancePoint;
			exit = tempRoad.exitPoint;
			tempCar = new Car(carCount+1, entrance, exit);
			tempCar.road = (entrance.street == null)? entrance.avenue : entrance.street;
			rand = (int)(Math.random()*3);
			if(rand == 0) tempCar.lane = 'L';
			else if(rand == 1) tempCar.lane = 'M';
			else if(rand == 2) tempCar.lane = 'R';
			allCars.put(tempCar.carID, tempCar);
			entrance.queueCar(tempCar);
			carCount++;
			i++;
		}
		System.out.println(numberOfCars + " Cars Added to the Grid");
	}
	
	public static Set<Map.Entry<char[] ,Car>> getEntrySet() {
		return allCars.entrySet();
	}
	
	//Code added here
	public boolean enterGrid() {
		if (phase != 'Q') return false;
		this.phase = 'M';
		this.entryTime = System.currentTimeMillis();
		return true;
	}
	//Code added here
	
	public void moveXY(int[] dist) {
		
		if(this.nextPoint.control[1] == 'X') {			//next point is exit point
			this.xy[0] += dist[0];
			this.xy[1] += dist[1];
			this.carDistance += (dist[0] == 0)? Math.abs(dist[1]):Math.abs(dist[0]);
			if(this.xy[0] < 0 || this.xy[0] > Road.xAccumulativePosition ||
					this.xy[1] < 0 || this.xy[1] > Road.yAccumulativePosition) {
				this.phase = 'S';
				exitTime = System.currentTimeMillis();
				//this.nextPoint.queueCar(this);
			}
			return;
		}

		boolean[] decide = this.nextPoint.intersectionLogic(this, dist);
		if(decide[0]) {
			this.xy[0] += dist[0];
			this.xy[1] += dist[1];
			this.nextPoint.Dequeue(this);
			this.carDistance += (dist[0] == 0)? Math.abs(dist[1]):Math.abs(dist[0]);
			
			if(decide[1]) {
				this.nextPoint.Dequeue(this);
				if(this.dir == 'N' || this.dir == 'S') {
					this.nextPoint = this.nextPoint.nextAvenue;
				} else if(this.dir == 'E' || this.dir == 'W') {
					this.nextPoint = this.nextPoint.nextStreet;
				}
				
			}
		} else {
			this.nextPoint.queueCar(this);
		}
	}
	
	public void setDirection(char direction) {
		this.dir = direction;
	}
	
	public boolean queueCar(Car nextCar) {
		//if(nextCar.inAQueue) return false;
		if(this == nextCar) {
			//System.out.println("Same car queuing itself");
			return false;
			//System.exit(0);
		}
		if(this.isQueued)			//continue till last one and queue to it
			return this.nextInLine.queueCar(nextCar);
		else {
			this.nextInLine = nextCar;
			//nextCar.inAQueue = true;
			isQueued = true;
			return isQueued;
		}
	}
	public Car Dequeue() {
		if(isQueued == false)
			return null;
		Car tempCar = this.nextInLine;
		this.nextInLine = tempCar.nextInLine;
		if(this.nextInLine == null)
			isQueued = false;
		tempCar.isQueued = false;
		//tempCar.inAQueue = false;
		tempCar.nextInLine = null;
		return tempCar;
	}
	
	public int distance(Car tempCar) {
		if((this.dir == tempCar.dir) && (this.dir == 'N' || this.dir == 'S') && 
				(this.xy[0] == tempCar.xy[0]))
			return this.xy[1] - tempCar.xy[1];
		else if((this.dir == tempCar.dir) && (this.dir == 'E' || this.dir == 'W') && 
				(this.xy[1] == tempCar.xy[1]))
			return this.xy[0] - tempCar.xy[0];
		else
			return Integer.MAX_VALUE;
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
