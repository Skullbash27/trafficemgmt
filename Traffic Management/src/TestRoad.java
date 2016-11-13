import java.util.HashMap;
import java.util.Random;

public class TestRoad {
	public static void initializeRoad(int numStreets, int numAvenues, int maxNumBlocks, int minNumBlocks) {
		  int i;
		  char[] test1 = {'0' , '0' , '0' , '0'};
		  char[] test2 = {'0' , '0' , '0' , '0'};
		  char altDirection;
		  int entranceValue;
		  int exitValue;
		  int nextValue = 0;
		  int previousValue = 0;
		  
		  Random randValue = new Random(); //Used to generate block values randomly
		  
		  // Create a hash map for the intersections
		  HashMap<Street, Avenue[]> hashIntersection = new HashMap<Street, Avenue[]>();
		  
		  //These values would be passed from the config file
		  int n = numStreets;
		  int m = numAvenues;
		  int maxBlock = maxNumBlocks;
		  int minBlock = minNumBlocks;
		  
		  //From the specifications, n = number of streets, and m = number of avenues
		  Street[] streetArray = new Street[n];
		  Avenue[] avenueArray = new Avenue[m];
		  
		  for (i=0; i<n; i++){
			  test1 = getDigits(i);
			  
			  if (i%2 == 0){ //Alternate between East and West for each street
				  altDirection = 'E';
			  } else {
				  altDirection = 'W';
			  }
			  
			  //Need to be changed according to what these values should actually be
			  entranceValue = i;
			  exitValue = i;
			  
			  //Generate the next and previous values for each street.
			  //For streets other than the first one, used the old nextValue as the street's previousValues
			  //to keep consistency.
			  if (i == 0){
				  previousValue = randValue.nextInt((maxBlock - minBlock) + 1) + minBlock;
				  nextValue = randValue.nextInt((maxBlock - minBlock) + 1) + minBlock;
			  } else {
				  previousValue = nextValue;
				  nextValue = randValue.nextInt((maxBlock - minBlock) + 1) + minBlock;
			  }
			  
			  streetArray[i] = new Street(test1, altDirection, entranceValue, exitValue, nextValue, previousValue);
		  }
		  
		  for (i=0; i<m; i++){
			  test2 = getDigits(i);
			  
			  if (i%2 == 0){ //Alternate between North and South for each avenue
				  altDirection = 'N';
			  } else {
				  altDirection = 'S';
			  }
			  
			  //Need to be changed according to what these values should actually be
			  entranceValue = i;
			  exitValue = i;
			  
			  //Generate the next and previous values for each street.
			  //For streets other than the first one, used the old nextValue as the street's previousValues
			  //to keep consistency.
			  if (i == 0){
				  previousValue = randValue.nextInt((maxBlock - minBlock) + 1) + minBlock;
				  nextValue = randValue.nextInt((maxBlock - minBlock) + 1) + minBlock;
			  } else {
				  previousValue = nextValue;
				  nextValue = randValue.nextInt((maxBlock - minBlock) + 1) + minBlock;
			  }
			  
			  avenueArray[i] = new Avenue(test2, altDirection, entranceValue, exitValue, nextValue, previousValue);
		  }
		  
		  //Combine streetArray and avenueArray into the hash map for each intersection(?)
		  //hashIntersection.put("k",streetArray[i],avenueArray[j]); //Doesn't work, intersections can't be named?
		  for (i=0; i<n; i++){
			  hashIntersection.put(streetArray[i], avenueArray);
		  } //Not sure how this would work
		  
		  //System.out.println(streetArray[0].roadID); //Test to see that the ID shows as expected
	   }
	
	public static char[] getDigits(int original){
		char[] digits = new char[4];
		int number;
		
		number = original;
		digits[3] = (char) (number % 10  + 48); //The least significant digit, add 48 to show correct character
		number /= 10; //Divide by 10 to be able to get the next set of digits
		  
		//Using modulo and division to get the digits to put into the ID array.
		if (original < 10){
			digits[2] = '0';
		} else{
			digits[2] = (char) (number % 10  + 48);
		} number /= 10;
		  
		if (original < 100){
			digits[1] = '0';
		} else{
			digits[1] = (char) (number % 10  + 48);
		} number /= 10;
		  
		if (original < 1000){ //The most significant digit
			digits[0] = '0';
		} else{
			digits[0] = (char) (number % 10  + 48);
		}
		
		return digits;
	}
}
