import java.util.Map;
import java.util.ArrayList;


public class Statistics {
	
	static Car tempCar;
	public static long carEntryTime;
	public static long carExitTime;
	public static long totalCarTime;
	public static double totalCarListTime; //Calculates Total time of all cars
	public static double totalCarListDistance; //Calculates Total distance pf all cars
	public static double averageTime;
	public static double averageDistance;
	public static double shortTime;
	public static double shortDistance;
	public static double longTime;
	public static double longDistance;
	public static int carsInGrid;
	
	public static ArrayList<Long> carTimeList = new ArrayList<Long>();
	public static ArrayList<Long> carDistanceList = new ArrayList<Long>();
	
	public static void carStatistics() {
		
		carsInGrid = Frame.carCount;
		
		for(Map.Entry<char[], Car> entry : Car.getEntrySet()) {
			tempCar = entry.getValue();
			carEntryTime = tempCar.entryTime;
			carExitTime = tempCar.exitTime;
			totalCarTime = carExitTime - carEntryTime;		
			carTimeList.add(totalCarTime);
			carDistanceList.add(tempCar.carDistance);
		}
		System.out.println("This is the time for each car: " +carTimeList);
		System.out.println("This is the distance travelled for each car: " +carDistanceList);
	}
	
	public static void carTimeSum() {
		int i;
		long milliCounter = 0;		
		for (i=0; i<carTimeList.size(); i++) {
			milliCounter += carTimeList.get(i);		
		}
		totalCarListTime = (milliCounter / 1000);
		System.out.println("Total Time of all cars: " +totalCarListTime +" seconds");
	}
	
	public static void carDistanceSum() {
		int i;
		
		for (i=0; i<carDistanceList.size(); i++) {
			totalCarListDistance += carDistanceList.get(i);
		}
		System.out.println("Total Distance travelled by all cars: " +totalCarListDistance);
	}
	
	public static void averageStats() {		
		averageTime = totalCarListTime/carTimeList.size();
		averageDistance = totalCarListDistance/carDistanceList.size();	
		System.out.println("Average time travelled by all cars in the grid: " +averageTime +" seconds");
		System.out.println("Average time distance travelled by all cars in the grid: " +averageDistance);
	}
	
	public static void smallestStats() {
		int i, j;
		long tempTimeNumber, tempDistanceNumber;
		
		shortTime = carTimeList.get(0);
		shortDistance = carDistanceList.get(0);
		
		for (i=0; i<carTimeList.size(); i++) {
			tempTimeNumber = carTimeList.get(i);
			if (tempTimeNumber < shortTime) shortTime = tempTimeNumber;
		}
		
		for (j=0; j<carDistanceList.size(); j++) {
			tempDistanceNumber = carDistanceList.get(j);
			if (tempDistanceNumber < shortDistance) shortDistance = tempDistanceNumber;
		}
		
		shortTime = shortTime / 1000;		
		System.out.println("Shortest time travelled by all cars in the grid: " +shortTime +" seconds");
		System.out.println("Shortest distance travelled by all cars in the grid: " +shortDistance);		
	}
	
	public static void longestStats() {	
		int i, j;
		long tempTimeNumber, tempDistanceNumber;
		
		longTime = carTimeList.get(0);
		longDistance = carDistanceList.get(0);
		
		for (i=0; i<carTimeList.size(); i++) {
			tempTimeNumber = carTimeList.get(i);
			if (tempTimeNumber > longTime) longTime = tempTimeNumber;
		}
		
		for (j=0; j<carDistanceList.size(); j++) {
			tempDistanceNumber = carDistanceList.get(j);
			if (tempDistanceNumber > longDistance) longDistance = tempDistanceNumber;
		}		
		longTime = longTime / 1000;
		System.out.println("Longest time travelled by all cars in the grid: " +longTime +" seconds");
		System.out.println("Longest distance travelled by all cars in the grid: " +longDistance);		
	}	
}
