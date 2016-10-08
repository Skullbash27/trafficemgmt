
public class Avenue extends Roads{
	int[] avenue_distance;

	public Avenue(char[] ID, char direction, int entrance, int exit, int next, int previous){
		checkStreet = false;
		roadID[0] = 'A';
		
		if (ID.length == 4){ //Make sure ID is length 4
			//This should copy ID into the right places in roadID
			System.arraycopy(ID, 0, roadID, 1, ID.length);
		}
		
		//Avenues are only going north or south
		if (direction == 'N' || direction == 'S'){
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
