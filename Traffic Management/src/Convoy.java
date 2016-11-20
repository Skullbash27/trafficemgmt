
public class Convoy {
	
	protected Car[] listOfCars = new Car[10];
	protected int carsInConv = 0;
	
	public Convoy(Car firstInConv) {
		this.listOfCars[0] = firstInConv;
		carsInConv++;
	}
	
	public Convoy joinConvoy(Car inConv) {
		if(carsInConv >= listOfCars.length)
			return null;
		else {
			int i=0;
			do {
				i++;
			} while();
		}
	}

}
