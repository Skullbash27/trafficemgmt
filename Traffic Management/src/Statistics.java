import java.util.Map.Entry;


public class Statistics {
	
	public static long carTimeDifference = 0;
	public static long carQueueTime = 0;
	public static long totalCarTime = 0;		//Calculates Total time of all cars
	//public static double totalCarListTime;
	public static double totalCarDistance = 0;	//Calculates Total distance pf all cars
	public static double averageTime;
	public static double averageQueueTime;
	public static double averageDistance;
	public static double shortTime = Double.POSITIVE_INFINITY;
	public static double shortDistance = Double.POSITIVE_INFINITY;
	public static double longTime = 0;
	public static double longDistance = 0;
	public static int carsInGrid;
	
	//public static ArrayList<Long> carTimeList = new ArrayList<Long>();
	//public static ArrayList<Long> carDistanceList = new ArrayList<Long>();
	
	public static void carStatistics() {
		Car tempCar;
		carsInGrid = Car.carCount;
		
		for(Entry<char[], Car> entry1 : Car.getEntrySet()) {
			tempCar = entry1.getValue();
			/*if(tempCar.exitTime == 0) {
				tempCar.exitTime = System.currentTimeMillis();
				System.out.print("ERROR: Zero Exit Time\t");
				tempCar.printCar();
			}*/
			carQueueTime += (tempCar.entryTime - tempCar.queueTime);
			totalCarTime += (tempCar.exitTime - tempCar.entryTime);
			totalCarDistance += tempCar.carDistance;
			shortTime = Math.min(shortTime, carTimeDifference);
			shortDistance = Math.min(shortDistance, tempCar.carDistance);
			longTime = Math.max(longTime, carTimeDifference);
			longDistance = Math.max(longDistance, tempCar.carDistance);
			//carTimeList.add(totalCarTime);
			//carDistanceList.add(tempCar.carDistance);
		}
		//System.out.println("This is the time for each car: " +carTimeList);
		//System.out.println("This is the distance traveled for each car: " +carDistanceList);
	}
	
	public static void carTimeSum() {
		totalCarTime = totalCarTime / 1000;
		System.out.println("Total moving time of all cars in system:\t" +totalCarTime +"\tseconds");
		carQueueTime = carQueueTime / 1000;
		System.out.println("Total queue time of all cars:\t" +carQueueTime +"\tseconds");
	}
	
	public static void carDistanceSum() {
		System.out.println("Total Distance travelled by all cars:\t" +totalCarDistance);
	}
	
	public static void averageStats() {
		averageTime = totalCarTime/carsInGrid;
		averageQueueTime = carQueueTime/carsInGrid;
		averageDistance = totalCarDistance/carsInGrid;
		System.out.println("Average time travelled by all cars in the grid:\t" 
				+ averageTime +"\tseconds");
		System.out.println("Average queue time for all cars before entring the grid:\t"
				+averageQueueTime+"\tseconds");
		System.out.println("Average time distance travelled by all cars in the grid:\t" 
				+ averageDistance);
	}
	
	public static void smallestStats() {
		shortTime = shortTime / 1000;		
		System.out.println("Shortest time travelled by all cars in the grid:\t" 
				+ shortTime +"\tseconds");
		System.out.println("Shortest distance travelled by all cars in the grid:\t" 
				+ shortDistance);		
	}
	
	public static void longestStats() {
		longTime = longTime / 1000;
		System.out.println("Longest time travelled by all cars in the grid:\t" 
				+ longTime +"\tseconds");
		System.out.println("Longest distance travelled by all cars in the grid:\t" 
				+ longDistance);		
	}
}
