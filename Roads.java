
public class Roads {
	boolean checkStreet; //Set to true if the object is a street, and false for avenue
	
	int roadID; //The number for each street or avenue
	
	//These two are essentially used to show the start and end points of a road.
	int entrancePoint;
	int exitPoint;
	
	boolean roadDirection; //True for WE or NS, false for EW or SN
	
	//Distances to the next and previous road from the current one.
	int distanceNext;
	int distancePrevious;
	
	public int getRoadID(){
		return roadID; //Return the road's ID number
	}
	
	public int roadNext(){
		return distanceNext; //Return the distance to the next street/avenue
	}
	
	public int roadPrevious(){
		return distancePrevious; //Return the distance to the previous street/avenue
	}
}
