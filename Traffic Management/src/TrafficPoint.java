import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TrafficPoint {
	
	private static HashMap<char[], TrafficPoint> trafficPoints = new HashMap<char[], TrafficPoint>();
	
	private Road street;
	private Road avenue;
	
	private char[] pointID = new char[8];
	/*
	 * TrafficControl is a class to represent intersection points
	 * ID divided into two 4-char IDs
	 * first 4-chars [0:3] represents the street ID
	 * second 4-chars [4:7] represents the avenue ID
	 * first char of the road ID 1=entrance, 2=exit, 3=street, 4=avenue
	 */
	private char[] roadDir = new char[2];
	//first char street direction, second char avenue direction
	private int[] xy = new int[2];
	private char[] control = new char[2];
	/*
	 * first char street light status R=Red G=Green Y=Yellow
	 * second char avenue light status R=Red G=Green Y=Yellow
	 * control 'EN'=entrance 'EX'=exit points
	 */
	private Car firstInLine = null;
	private boolean carInQueue = false;
	//queue of cars waiting
	
	public static boolean addControlPoits(Set<Map.Entry<char[] ,Road>> set) {
		if(set == null)
			return false;
		//declaring and initializing street and avenue entrance and exit points
		System.out.println("Initializing Entrance and Exit Points");
		Road tempRoad;
		TrafficPoint tempPoint;
		for(Map.Entry<char[], Road> entry : set) {
			tempRoad = entry.getValue();
			if(tempRoad.getType() == 'S') {
				tempPoint = new TrafficPoint(tempRoad, null, new char[]{'E','N'});
				tempRoad.setEntrancePoints(tempPoint);
				trafficPoints.put(tempPoint.pointID, tempPoint);
				tempPoint = new TrafficPoint(tempRoad, null, new char[]{'E','X'});
				tempRoad.setExitPoints(tempPoint);
				trafficPoints.put(tempPoint.pointID, tempPoint);
			}
			else if(tempRoad.getType() == 'A') {
				tempPoint = new TrafficPoint(null, tempRoad, new char[]{'E','N'});
				tempRoad.setEntrancePoints(tempPoint);
				trafficPoints.put(tempPoint.pointID, tempPoint);
				tempPoint = new TrafficPoint(null, tempRoad, new char[]{'E','X'});
				tempRoad.setExitPoints(tempPoint);
				trafficPoints.put(tempPoint.pointID, tempPoint);
			}
		}
		System.out.println("Initializing Intersection points");
		for(Map.Entry<char[], Road> entry1 : set) {
			for(Map.Entry<char[], Road> entry2 : set) {
				if(entry1.getValue().getType() == 'S' && 
						entry2.getValue().getType() == 'A') {
					tempPoint = new TrafficPoint(entry1.getValue(), entry2.getValue(), 
						new char[]{'R','Y'});
					trafficPoints.put(tempPoint.getPointID(), tempPoint);
				}
			}
		}
		return true;
	}
	
	private TrafficPoint(Road street, Road avenue, char[] control) {
		char[] streetID = new char[]{'0','0','0','0'};
		char[] avenueID = new char[]{'0','0','0','0'};
		if(avenue == null) {						//initialize as street
			streetID = street.getRoadID();
		} else if(street == null) {					//initialize as avenue
			avenueID = avenue.getRoadID();
		} else {									//initialize for both
			streetID = street.getRoadID();
			avenueID = avenue.getRoadID();
		}
		for(int i=0; i<pointID.length; i++){		//copying road IDs as traffic light ID
			if(i<4)
				this.pointID[i]=streetID[i];
			else
				this.pointID[i]=avenueID[i-4];
		}
		if(avenue == null) {			//initialize as street entrance or exit
			if(control[1] == 'N')
				this.pointID[0] = '1';
			else if(control[1] == 'X')
				this.pointID[0] = '2';
			this.xy[1] = street.getAccPos();
			//based on entrance, exit and direction position assigned
			if((street.getRoadDirection() == 'E' && control[1] == 'N') || 		//from east entrance
					(street.getRoadDirection() == 'W' && control[1] == 'X'))	//from west exit
				this.xy[0] = Road.getXAccPos();
			else if((street.getRoadDirection() == 'E' && control[1] == 'X') || 	//from east exit
					(street.getRoadDirection() == 'W' && control[1] == 'N'))	//from west entrance
				this.xy[0] = 0;
			this.roadDir[0] = street.getRoadDirection();
			this.roadDir[1] = this.roadDir[0];
		} else if(street == null) {		//initialize as avenue entrance or exit
			if(control[1] == 'N')
				this.pointID[4] = '1';
			else if(control[1] == 'X')
				this.pointID[4] = '2';
			this.xy[0] = avenue.getAccPos();
			if((avenue.getRoadDirection() == 'N' && control[1] == 'N') || 
					(avenue.getRoadDirection() == 'S' && control[1] == 'X'))
				this.xy[1] = 0;
			else if((avenue.getRoadDirection() == 'S' && control[1] == 'N') || 
					(avenue.getRoadDirection() == 'N' && control[1] == 'X'))
				this.xy[1] = Road.getYAccPos();
			this.roadDir[0] = avenue.getRoadDirection();
			this.roadDir[1] = this.roadDir[0];
		} else {						//initialize as intersection
			streetID = street.getRoadID();
			avenueID = avenue.getRoadID();
			this.xy[0] = avenue.getAccPos();
			this.xy[1] = street.getAccPos();
			this.roadDir[0] = street.getRoadDirection();
			this.roadDir[1] = avenue.getRoadDirection();
		}
		this.street = street;
		this.avenue = avenue;
		this.control = Arrays.copyOfRange(control, 0, 2);
	}
	
	public static Set<Map.Entry<char[] ,TrafficPoint>> getEntrySet() {
		return trafficPoints.entrySet();
	}
	
	public Road getStreet() {
		return street;
	}
	public Road getAvenue() {
		return avenue;
	}
	
	public boolean emptyQueue() {
		return !carInQueue;
	}
	public boolean queueCar(Car nextInLine) {
		if(carInQueue)			//continue till last one and queue to it
			return this.firstInLine.queueCar(nextInLine);
		this.firstInLine = nextInLine;
		carInQueue = true;
		return carInQueue;
	}
	public Car Dequeue() {
		if(carInQueue == false)
			return null;
		Car tempCar = this.firstInLine;
		this.firstInLine = tempCar.nextInLine();
		if(this.firstInLine == null)
			carInQueue = false;
		return tempCar;
	}
	public Car nextInLine() {
		return this.firstInLine;
	}
	
	public char[] getPointID() {
		return pointID;
	}
	public char[] getDirection() {
		return roadDir;
	}
	public int[] getXY() {
		return xy;
	}
	public char[] getControl() {
		return control;
	}
	
	public boolean nextControl() {
		if(pointID[0] != '3' || pointID[4] != '4')		//not street or avenue
			return false;
		else if(control[0] == 'R') {
			if(control[1] == 'R')
				control[0] = 'G';
			else if(control[1] == 'G')
				control[1] = 'Y';
			else if(control[1] == 'Y')
				control = new char[]{'G','R'};
		} else if(control[0] == 'G') {
			control[0] = 'Y';
		} else if(control[0] == 'Y') {
			control = new char[]{'R','G'};
		} else
			control = new char[]{'R','R'};
		return true;
	}
}
