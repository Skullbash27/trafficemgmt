import java.util.Arrays;

public class Roads {
	boolean checkStreet; //Set to true if the object is a street, and false for avenue
	
	char[] roadID = new char[6]; //The number for each street or avenue
	
	//These two are essentially used to show the start and end points of a road.
	int entrancePoint;
	int exitPoint;
	
	//Distances to the next and previous road from the current one.
	int distanceNext;
	int distancePrevious;
	
	public char getType(){
		return roadID[0]; //Return the road's type, S for Street and A for Avenue
	}
	
	public char[] getRoadID(){
		char[] onlyID = Arrays.copyOfRange(roadID, 1, 5); //Gets the ID values 1 to 4
		return onlyID; //Return the road's ID
	}
	
	public char getRoadDirection(){
		return roadID[5]; //Returns the direction the street is going in: N, E, W, or S
	}
	
	public int roadNext(){
		return distanceNext; //Return the distance to the next street/avenue
	}
	
	public int roadPrevious(){
		return distancePrevious; //Return the distance to the previous street/avenue
	}
}
