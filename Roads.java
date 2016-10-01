
public class Roads {
	boolean checkStreet; //Set to true if the object is a street, and false for avenue
	
	int roadID; //The number for each street or avenue
	
	//These two are essentially used to show the start and end points of a road.
	int entrancePoint;
	int exitPoint;
	
	public int distance_to_next_intersection(){
		return 0;
	}
}
