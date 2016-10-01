
public class Avenue extends Roads{
	int[] avenue_distance;

	public Avenue(int ID, int entrance, int exit){
		checkStreet = false;
		roadID = ID;
		entrancePoint = entrance;
		exitPoint = exit;
		
		//An array of each of the segments of the avenue.
		avenue_distance = new int[7]; //Should probably be able to dynamically set this
	}
	
	public void setDistance(int location, int value){
		avenue_distance[location] = value ;
	}
	
	public int getDistance(int location, int value){
		return avenue_distance[location] = value ;
	}
}
