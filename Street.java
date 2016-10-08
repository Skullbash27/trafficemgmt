
public class Street extends Roads{
	int[] street_distance;
	
	public Street(int ID, int entrance, int exit, boolean direction, int next, int previous){
		checkStreet = true;
		roadID = ID;
		entrancePoint = entrance;
		exitPoint = exit;
		roadDirection = direction;
		
		//Check to make sure 'next' and 'previous' are actual values
		//If a street is on the edge, then there is no previous/next street,
		//so the corresponding value would be 0 in those cases.
		if (next > 0){
			distanceNext = next;
		}
		if (previous > 0){
			distancePrevious = previous;
		}
	}
}
