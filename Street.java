
public class Street extends Roads{
	int[] street_distance;
	
	public Street(int ID, int entrance, int exit /*, int arraySize*/){
		checkStreet = true;
		roadID = ID;
		entrancePoint = entrance;
		exitPoint = exit;
		
		//An array of each of the segments of the street.
		street_distance = new int[7]; //Should probably be able to dynamically set this
	}
	
	public void setDistance(int location, int value){
		street_distance[location] = value ;
	}
	
	public int getDistance(int location, int value){
		return street_distance[location] = value ;
	}
}
