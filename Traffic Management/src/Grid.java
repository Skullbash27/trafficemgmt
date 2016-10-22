import java.util.Map;
import java.util.Set;

public class Grid {
	
	public Grid(int NumberOfStreets, int NumberOfAvenues, 
			int MinBlockSide, int MaxBlockSide, 
			int CarLength, int CarWidth, int Clearance) {
		if(NumberOfStreets >= 999 || NumberOfAvenues >= 999) {
			System.out.println("Numbers are outside program capacity");
			return;
		}
		System.out.println("Creating the grid");
		System.out.println("Initializing roads, entrance and exit points");
		initRoads(NumberOfStreets, NumberOfAvenues, 
				MinBlockSide, MaxBlockSide, CarLength, CarWidth, Clearance);
		initControlPoints(Road.getEntrySet());
	}
	
	private void initRoads(int NumberOfStreets, int NumberOfAvenues, 
			int MinBlockSide, int MaxBlockSide, int CarLength, int CarWidth, 
			int Clearance) {
		boolean initialized = Road.addRoads(NumberOfStreets, 
				MinBlockSide*CarLength, MaxBlockSide*CarLength, 'S', 
				CarWidth, Clearance);
		if(!initialized)
			System.out.println("Streets not initialized properly");
		initialized = Road.addRoads(NumberOfAvenues, 
				MinBlockSide*CarLength, MaxBlockSide*CarLength, 'A', 
				CarWidth, Clearance);
		if(!initialized)
			System.out.println("Avenues not initialized properly");
	}
	
	private void initControlPoints(Set<Map.Entry<char[] ,Road>> set) {
		boolean initialized = TrafficPoint.addControlPoits(set);
		if(!initialized) {
			System.out.println("Entrance and Exit Points and intersections"
					+ " not initialized properly");
		}
	}
}