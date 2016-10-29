import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TrafficPoint {
	
	private static HashMap<char[], TrafficPoint> trafficPoints = new HashMap<char[], TrafficPoint>();
	
	protected Road street = null;
	protected Road avenue = null;
	protected TrafficPoint nextStreet = null;
	protected TrafficPoint nextAvenue = null;
	protected boolean carInQueueStreet = false;
	protected boolean carInQueueAvenue = false;
	public int queuedCarsInStreet = 0;
	public int queuedCarsInAvenue = 0;
	public int queueTimer = 0;
	
	protected char[] pointID = new char[8];
	/*
	 * TrafficControl is a class to represent intersection points
	 * ID divided into two 4-char IDs
	 * first 4-chars [0:3] represents the street ID
	 * second 4-chars [4:7] represents the avenue ID
	 * first char of the road ID 1=entrance, 2=exit, 3=street, 4=avenue
	 */
	protected char[] roadDir = new char[2];
	//first char street direction, second char avenue direction
	//protected int[] xy = new int[2];
	protected int[][][] sectors = new int[3][3][2];
	protected boolean[][] flag = new boolean[3][3];
	protected char[] control = new char[2];
	/*
	 * first char street light status R=Red G=Green Y=Yellow
	 * second char avenue light status R=Red G=Green Y=Yellow
	 * control 'EN'=entrance 'EX'=exit points
	 */
	protected Car firstInLine = null;
	protected boolean carInQueue = false;
	protected Car firstInLineStreet = null;
	protected Car firstInLineAvenue = null;
	
	//queue of cars waiting
	
	public static boolean addControlPoits(Set<Map.Entry<char[] ,Road>> set) {
		if(set == null)
			return false;
		//declaring and initializing street and avenue entrance and exit points
		System.out.println("Initializing Entrance and Exit Points");
		Road tempRoad;
		TrafficPoint tempPoint1, tempPoint2;
		int difference = 0;
		for(Map.Entry<char[], Road> entry : set) {
			tempRoad = entry.getValue();
			if(tempRoad.roadType == 'S') {
				tempPoint1 = new TrafficPoint(tempRoad, null, new char[]{'E','N'});
				tempRoad.setEntrancePoints(tempPoint1);
				for(int i=0; i<3; i++) {
					for(int j=0; j<3; j++) {
						tempPoint1.sectors[i][j][0] = (tempRoad.roadDir == 'E')? 
								Road.xAccumulativePosition : 0;
						tempPoint1.sectors[i][j][1] = tempRoad.sectors[i];
						tempPoint1.flag[i][j] = false;
					}
				}
				trafficPoints.put(tempPoint1.pointID, tempPoint1);
				tempPoint1 = new TrafficPoint(tempRoad, null, new char[]{'E','X'});
				tempRoad.setExitPoints(tempPoint1);
				for(int i=0; i<3; i++) {
					for(int j=0; j<3; j++) {
						tempPoint1.sectors[i][j][0] = (tempRoad.roadDir == 'W')? 
								Road.xAccumulativePosition : 0;
						tempPoint1.sectors[i][j][1] = tempRoad.sectors[i];
						tempPoint1.flag[i][j] = false;
					}
				}
				trafficPoints.put(tempPoint1.pointID, tempPoint1);
			}
			else if(tempRoad.roadType == 'A') {
				tempPoint1 = new TrafficPoint(null, tempRoad, new char[]{'E','N'});
				tempRoad.setEntrancePoints(tempPoint1);
				for(int i=0; i<3; i++) {
					for(int j=0; j<3; j++) {
						tempPoint1.sectors[i][j][0] = tempRoad.sectors[j];
						tempPoint1.sectors[i][j][1] = (tempRoad.roadDir == 'N')?
								0 : Road.yAccumulativePosition;
						tempPoint1.flag[i][j] = false;
					}
				}
				trafficPoints.put(tempPoint1.pointID, tempPoint1);
				tempPoint1 = new TrafficPoint(null, tempRoad, new char[]{'E','X'});
				tempRoad.setExitPoints(tempPoint1);
				for(int i=0; i<3; i++) {
					for(int j=0; j<3; j++) {
						tempPoint1.sectors[i][j][0] = tempRoad.sectors[j];
						tempPoint1.sectors[i][j][1] = (tempRoad.roadDir == 'S')?
								0 : Road.yAccumulativePosition;
						tempPoint1.flag[i][j] = false;
					}
				}
				trafficPoints.put(tempPoint1.pointID, tempPoint1);
			}
		}
		System.out.println("Initializing Intersection points");
		for(Map.Entry<char[], Road> entry1 : set) {
			for(Map.Entry<char[], Road> entry2 : set) {
				if(entry1.getValue().roadType == 'S' && 
						entry2.getValue().roadType == 'A') {
					tempPoint1 = new TrafficPoint(entry1.getValue(), entry2.getValue(), 
							new char[]{'R','Y'});
					for(int i=0; i<3; i++) {
						for(int j=0; j<3; j++) {
							tempPoint1.sectors[i][j][0] = entry2.getValue().sectors[j];
							tempPoint1.sectors[i][j][1] = entry1.getValue().sectors[i];
							tempPoint1.flag[i][j] = false;
						}
					}
					trafficPoints.put(tempPoint1.pointID, tempPoint1);
				}
			}
		}
		//setting next street and avenue intersection points
		for(Map.Entry<char[], TrafficPoint> entry1 : trafficPoints.entrySet()) {
			for(Map.Entry<char[], TrafficPoint> entry2 : trafficPoints.entrySet()) {
				tempPoint1 = entry1.getValue();
				tempPoint2 = entry2.getValue();
				difference = tempPoint1.distance(tempPoint2);
				if(tempPoint1.sectors[1][1][0] == tempPoint2.sectors[1][1][0]) {	//points on same avenue
					if(tempPoint1.roadDir[1] == 'N') { 		//same x position = same direction
						if(difference < 0) {
							if(tempPoint1.nextAvenue == null)
								tempPoint1.nextAvenue = tempPoint2;
							else if(difference > tempPoint1.distance(tempPoint1.nextAvenue))
								tempPoint1.nextAvenue = tempPoint2;
						}
					} else if(tempPoint1.roadDir[1] == 'S') {
						if(difference > 0) {
							if(tempPoint1.nextAvenue == null)
								tempPoint1.nextAvenue = tempPoint2;
							else if(difference < tempPoint1.distance(tempPoint1.nextAvenue))
								tempPoint1.nextAvenue = tempPoint2;
						}
					}
				} else if(tempPoint1.sectors[1][1][1] == tempPoint2.sectors[1][1][1]) {	//points on same street
					if(tempPoint1.roadDir[0] == 'E') {
						if(difference > 0) {
							if(tempPoint1.nextStreet == null)
								tempPoint1.nextStreet = tempPoint2;
							else if(difference < tempPoint1.distance(tempPoint1.nextStreet))
								tempPoint1.nextStreet = tempPoint2;
						}
					} else if(tempPoint1.roadDir[0] == 'W') {
						if(difference < 0) {
							if(tempPoint1.nextStreet == null)
								tempPoint1.nextStreet = tempPoint2;
							else if(difference > tempPoint1.distance(tempPoint1.nextStreet))
								tempPoint1.nextStreet = tempPoint2;
						}
					}
				}
			}
		}
		/*for(Map.Entry<char[], TrafficPoint> entry : trafficPoints.entrySet()) {
			tempPoint1 = entry.getValue();
			System.out.print(tempPoint1.pointID);
			System.out.print("\t");
			System.out.print(tempPoint1.roadDir[0]+" "+tempPoint1.roadDir[1]+"\t");
			if(tempPoint1.nextAvenue != null)
				System.out.print(tempPoint1.nextAvenue.pointID);
			else
				System.out.print("null");
			System.out.print("\t");
			if(tempPoint1.nextStreet != null)
				System.out.println(tempPoint1.nextStreet.pointID);
			else
				System.out.println("null");
		}*/
		return true;
	}
	
	private TrafficPoint(Road street, Road avenue, char[] control) {
		char[] streetID = new char[]{'0','0','0','0'};
		char[] avenueID = new char[]{'0','0','0','0'};
		if(avenue == null) {						//initialize as street
			streetID = street.roadID;
		} else if(street == null) {					//initialize as avenue
			avenueID = avenue.roadID;
		} else {									//initialize for both
			streetID = street.roadID;
			avenueID = avenue.roadID;
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
			//this.xy[1] = street.accumulativePosition;
			//based on entrance, exit and direction position assigned
			/*if((street.roadDir == 'E' && control[1] == 'N') || 		//from east entrance
					(street.roadDir == 'W' && control[1] == 'X'))	//from west exit
				this.xy[0] = Road.xAccumulativePosition;
			else if((street.roadDir == 'E' && control[1] == 'X') || 	//from east exit
					(street.roadDir == 'W' && control[1] == 'N'))	//from west entrance
				this.xy[0] = 0;*/
			this.roadDir[0] = street.roadDir;
			this.roadDir[1] = this.roadDir[0];
		} else if(street == null) {		//initialize as avenue entrance or exit
			if(control[1] == 'N')
				this.pointID[4] = '1';
			else if(control[1] == 'X')
				this.pointID[4] = '2';
			//this.xy[0] = avenue.accumulativePosition;
			/*if((avenue.roadDir == 'N' && control[1] == 'N') || 
					(avenue.roadDir == 'S' && control[1] == 'X'))
				this.xy[1] = 0;
			else if((avenue.roadDir == 'S' && control[1] == 'N') || 
					(avenue.roadDir == 'N' && control[1] == 'X'))
				this.xy[1] = Road.yAccumulativePosition;*/
			this.roadDir[0] = avenue.roadDir;
			this.roadDir[1] = this.roadDir[0];
		} else {						//initialize as intersection
			streetID = street.roadID;
			avenueID = avenue.roadID;
			//this.xy[0] = avenue.accumulativePosition;
			//this.xy[1] = street.accumulativePosition;
			this.roadDir[0] = street.roadDir;
			this.roadDir[1] = avenue.roadDir;
		}
		this.street = street;
		this.avenue = avenue;
		this.control = Arrays.copyOfRange(control, 0, 2);
	}
	
	public static Set<Map.Entry<char[] ,TrafficPoint>> getEntrySet() {
		return trafficPoints.entrySet();
	}
	
	public boolean emptyQueue() {
		if(this.control[0] == 'E'){
			if(this.roadDir[0] == 'E' || this.roadDir[0] == 'W')
				return !this.carInQueueStreet;
			else if(this.roadDir[0]  == 'N' || this.roadDir[0] == 'S') {
				return !this.carInQueueAvenue;
			}
		} else {
				return !(this.carInQueueAvenue && this.carInQueueStreet);
		}
		return false;
	}
	public boolean queueCar(Car nextInLine) {
		/*
		if(carInQueue)			//continue till last one and queue to it
			return this.firstInLine.queueCar(nextInLine);
		this.firstInLine = nextInLine;
		carInQueue = true;
		return carInQueue;
		*/
		
		if(this.control[0] == 'E') {
			if(this.roadDir[0] == 'E' || this.roadDir[0] == 'W') {
				return this.queueCarInStreet(nextInLine);
			}
			else if(this.roadDir[0] == 'N' || this.roadDir[0] == 'S') {
				return this.queueCarInAvenue(nextInLine);
			}
			else if(nextInLine.dir == this.roadDir[0]) {
				return this.queueCarInStreet(nextInLine);
			}
			else if(nextInLine.dir == this.roadDir[1]) {
				return this.queueCarInAvenue(nextInLine);
			}
			else {
				System.out.println("Queueing Problem");
			}				
		}	
		return false;
	}
	
	private boolean queueCarInStreet(Car nextInLine) {
		if(this.carInQueueStreet) {
			if(this.firstInLineStreet.queueCar(nextInLine)) {
				queuedCarsInStreet++;
				return true;
			}
			else {
				return false;
			}
		}
		queuedCarsInStreet++;
		this.firstInLineStreet = nextInLine;
		this.carInQueueStreet = true;
		return this.carInQueueStreet;
	}
	
	private boolean queueCarInAvenue(Car nextInLine) {
		if(this.carInQueueAvenue){
			if(this.firstInLineAvenue.queueCar(nextInLine)) {
				queuedCarsInAvenue++;
				return true;
			} else {
				return false;
			}
		}
		queuedCarsInAvenue++;
		this.firstInLineAvenue = nextInLine;
		this.carInQueueAvenue = true;
		return this.carInQueueAvenue;		
	}
			
	public boolean Dequeue(Car tempCarPresent) {
		/*if(carInQueue == false)
			return null;
		Car tempCar = this.firstInLine;
		this.firstInLine = tempCar.nextInLine;
		if(this.firstInLine == null)
			carInQueue = false;
		return tempCar;*/
		Car tempCarPresentReference;
		if(this.roadDir[0] == tempCarPresent.dir) {
			tempCarPresentReference = this.firstInLineStreet;
		} else if (this.roadDir[1] == tempCarPresent.dir) {
			tempCarPresentReference = this.firstInLineAvenue;
		} else {
			return false;
		}
		boolean found = (tempCarPresent == tempCarPresentReference);
		if(found) {
			this.Dequeue();
			return found;
		}
		while(!found && tempCarPresentReference != null) {
			if(tempCarPresent == tempCarPresentReference.nextInLine) {
				found = true;
				tempCarPresentReference.Dequeue();
			} else {
				tempCarPresentReference = tempCarPresentReference.nextInLine;
			}
		}
		if(found) {
			if(this.roadDir[0] == tempCarPresent.dir) {
				this.queuedCarsInStreet--;
			} else if(this.roadDir[1] == tempCarPresent.dir) {
				this.queuedCarsInAvenue--;
			}
		}
		return found;
	}
	
	public Car Dequeue() {
        Car tempCar = null;
        /*System.out.print("D\t");
        System.out.print(this.pointID);
        System.out.print("\t");
        System.out.print(this.queuedCarsS+" "+this.queuedCarsA+"\t");*/
        if(this.control[0] == 'E') {    //entrance or exit point
                if(this.roadDir[0] == 'E' || this.roadDir[0] == 'W')            //direction same for entrance and exit
                        tempCar = this.dequeueStreet();
                else if(this.roadDir[0] == 'N' || this.roadDir[0] == 'S')
                        tempCar = this.dequeueAvenue();
        } else if(this.control[0] != 'R')
                tempCar = this.dequeueStreet();
        else if(this.control[1] != 'R')
                tempCar = this.dequeueAvenue();
        if(tempCar != null) {
                tempCar.isQueued = false;
                tempCar.nextInLine = null;
        }
        return tempCar;
	}
	
	 private Car dequeueStreet() {
         if(!this.carInQueueStreet)
                 return null;
         queuedCarsInStreet --;
         Car tempCar = this.firstInLineStreet;
         this.firstInLineStreet = tempCar.nextInLine;
         if(this.firstInLineStreet == null)
                 this.carInQueueStreet = false;
         tempCar.nextInLine = null;
         return tempCar;
	 }
	
	 private Car dequeueAvenue() {
         if(this.carInQueueAvenue == false)
                 return null;
         queuedCarsInAvenue --;
         Car tempCar = this.firstInLineAvenue;
         this.firstInLineAvenue = tempCar.nextInLine;
         if(this.firstInLineAvenue == null)
                 carInQueueAvenue = false;
         tempCar.nextInLine = null;
         return tempCar;
	 } 
	 
	public boolean nextControl() {
		if(this.control[0] == 'E')		//not street or avenue
			return false;
		else if(control[0] == 'R') {
			
			//Code added
			
			
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
	
	public int distance(TrafficPoint tempPoint) {
		if(this.sectors[1][1][0] == tempPoint.sectors[1][1][0])
			return this.sectors[1][1][1] - tempPoint.sectors[1][1][1];
		else if(this.sectors[1][1][1] == tempPoint.sectors[1][1][1])
			return this.sectors[1][1][0] - tempPoint.sectors[1][1][0];
		else
			return Integer.MAX_VALUE;
	}
	public int distance(Car tempCar) {
		if(tempCar.dir == 'N') {
			if(tempCar.xy[0] == this.sectors[0][0][0])
				return this.sectors[0][0][1] - tempCar.xy[1];
			else if(tempCar.xy[0] == this.sectors[0][1][0])
				return this.sectors[0][1][1] - tempCar.xy[1];
			else if(tempCar.xy[0] == this.sectors[0][2][0])
				return this.sectors[0][2][1] - tempCar.xy[1];
		} else if(tempCar.dir == 'S') {
			if(tempCar.xy[0] == this.sectors[2][0][0])
				return this.sectors[2][0][1] - tempCar.xy[1];
			else if(tempCar.xy[0] == this.sectors[2][1][0])
				return this.sectors[2][1][1] - tempCar.xy[1];
			else if(tempCar.xy[0] == this.sectors[2][2][0])
				return this.sectors[2][2][1] - tempCar.xy[1];
		} else if(tempCar.dir == 'E') {
			if(tempCar.xy[1] == this.sectors[0][2][1])
				return this.sectors[0][2][0] - tempCar.xy[0];
			else if(tempCar.xy[1] == this.sectors[1][2][1])
				return this.sectors[1][2][0] - tempCar.xy[0];
			else if(tempCar.xy[1] == this.sectors[2][2][1])
				return this.sectors[2][2][0] - tempCar.xy[0];
		} else if(tempCar.dir == 'W') {
			if(tempCar.xy[1] == this.sectors[0][0][1])
				return this.sectors[0][0][0] - tempCar.xy[0];
			else if(tempCar.xy[1] == this.sectors[1][0][1])
				return this.sectors[1][0][0] - tempCar.xy[0];
			else if(tempCar.xy[1] == this.sectors[2][0][1])
				return this.sectors[2][0][0] - tempCar.xy[0];
		}
		return Integer.MAX_VALUE;
	}
}
