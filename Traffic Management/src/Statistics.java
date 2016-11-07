import java.util.Map;


public class Statistics {
	
	static Car tempCar;
	public static long carTimeDifference = 0;
	public static long totalCarTime = 0;		//Calculates Total time of all cars
	//public static double totalCarListTime;
	public static double totalCarDistance = 0;	//Calculates Total distance pf all cars
	public static double averageTime;
	public static double averageDistance;
	public static double shortTime = Double.POSITIVE_INFINITY;
	public static double shortDistance = Double.POSITIVE_INFINITY;
	public static double longTime = 0;
	public static double longDistance = 0;
	public static int carsInGrid;
	
	//public static ArrayList<Long> carTimeList = new ArrayList<Long>();
	//public static ArrayList<Long> carDistanceList = new ArrayList<Long>();
	
	public static void carStatistics() {
		carsInGrid = Frame.carCount;
		for(Map.Entry<char[], Car> entry : Car.getEntrySet()) {
			tempCar = entry.getValue();
			carTimeDifference = tempCar.exitTime - tempCar.entryTime;
			totalCarTime += carTimeDifference;
			totalCarDistance += tempCar.carDistance;
			shortTime = Math.min(shortTime, carTimeDifference);
			shortDistance = Math.min(shortDistance, tempCar.carDistance);
			longTime = Math.max(longTime, carTimeDifference);
			longDistance = Math.max(longDistance, tempCar.carDistance);
			//carTimeList.add(totalCarTime);
			//carDistanceList.add(tempCar.carDistance);
		}
		//System.out.println("This is the time for each car: " +carTimeList);
		//System.out.println("This is the distance travelled for each car: " +carDistanceList);
	}
	
	public static void carTimeSum() {
		totalCarTime = (totalCarTime / 1000);
		System.out.println("Total Time of all cars: " +totalCarTime +" seconds");
	}
	
	public static void carDistanceSum() {
		System.out.println("Total Distance travelled by all cars: " +totalCarDistance);
	}
	
	public static void averageStats() {
		averageTime = totalCarTime/carsInGrid;
		averageDistance = totalCarDistance/carsInGrid;	
		System.out.println("Average time travelled by all cars in the grid: " 
				+ averageTime +" seconds");
		System.out.println("Average time distance travelled by all cars in the grid: " 
				+ averageDistance);
	}
	
	public static void smallestStats() {
		shortTime = shortTime / 1000;		
		System.out.println("Shortest time travelled by all cars in the grid: " 
				+ shortTime +" seconds");
		System.out.println("Shortest distance travelled by all cars in the grid: " 
				+ shortDistance);		
	}
	
	public static void longestStats() {	
		longTime = longTime / 1000;
		System.out.println("Longest time travelled by all cars in the grid: " 
				+ longTime +" seconds");
		System.out.println("Longest distance travelled by all cars in the grid: " 
				+ longDistance);		
	}
}
