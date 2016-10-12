import java.util.Arrays;

public class Road {
	private char[] roadID = new char[4]; 	//The number for each street or avenue
	/*
	 * first char of the road ID 1=entrance, 2=exit, 3=street, 4=avenue
	 */
	private char roadType;
	private char roadDir;		//direction of source of traffic
	
	private int accumulativePosition; 
	
	//These two are essentially used to show the start and end points of a road.
	private TrafficControl entrancePoint;
	private TrafficControl exitPoint;
	
	//Distances to the next road from the current one.
	/*private Road NextRoad;
	private Road PreviousRoad;*/
	
	public Road(char[] roadID, char roadDir, int accPos) {
		this.roadID = Arrays.copyOfRange(roadID, 0, 4);
		this.roadDir = roadDir;
		this.accumulativePosition = accPos;
	}
	
	public boolean setRoadType(char roadType) {
		if(this.roadType != 0)
			return false;
		else
			this.roadType = roadType;
		return true;
	}
	
	public boolean setEntrancePoints(TrafficControl entrancePoint) {
		if(this.entrancePoint != null)
			return false;
		else {
			this.entrancePoint = entrancePoint;
		}
		return true;
	}
	public boolean setExitPoints(TrafficControl exitPoint) {
		if(this.exitPoint != null)
			return false;
		else {
			this.exitPoint = exitPoint;
		}
		return true;
	}
	
	public char getType(){
		return roadType; //Return the road's type, S for Street and A for Avenue
	}
	public char[] getRoadID(){
		return roadID; //Return the road's ID
	}
	public char getRoadDirection(){
		return roadDir;		//Returns the direction the street is going in: N, E, W, or S
	}
	public int getAccPos() {
		return accumulativePosition;
	}
	/*public Road getNextRoad(){
		return NextRoad; //Return next street/avenue object
	}
	public Road getPreviousRoad(){
		return PreviousRoad; //Return previous street/avenue object
	}*/
	
	public TrafficControl getEntrancePoint() {
		return entrancePoint; //Return the distance to the previous street/avenue
	}
	public TrafficControl getExitPoint() {
		return exitPoint; //Return the distance to the previous street/avenue
	}
}