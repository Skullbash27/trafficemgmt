
public class Street extends Roads{
	int[] street_distance;
	
	public Street(char[] ID, char direction, int entrance, int exit, int next, int previous){
		checkStreet = true;
		roadID[0] = 'S';
		
		if (ID.length == 4){ //Make sure ID is length 4
			//This should copy ID into the right places in roadID
			System.arraycopy(ID, 0, roadID, 1, ID.length);
		}
		
		//Streets are only going east or west
		if (direction == 'E' || direction == 'W'){
			roadID[7] = direction;
		}
		
		entrancePoint = entrance;
		exitPoint = exit;
		
		if (next > 0){
			distanceNext = next;
		}
		if (previous > 0){
			distancePrevious = previous;
		}
	}
}
