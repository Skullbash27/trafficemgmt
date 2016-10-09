/* Copyright 2016 CEN6087: Cloud Computing Course Project */

/* Configuration.java is part of Traffic Simulation Package. */

/****************************************************************************/

/* Configuration loader
 * AHMAD QUTBUDDIN 2016-10
 * AHMED ALHAZMI 2016-10
 *
 * Functions below provide basic configuration file parsing. Config file
 * support includes skipping blank lines, comment lines (begin with a #).
 * Parsed lines consist of the variable name, a space or a tab, then the value
 * (e.g. CarSpeed 10). Default config values (if config file is missing
 * an entry to set the value) are defined in the variable declarations below.
 *
 * A function is also provided for printing the current configuration.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Configuration {
	
	// Simulation class:
	//	Exponential Car Insertion Rate
	//	Number of Cars
	private int Lambda = 100;
	private int NumberOfCars = 1000;
		
	// Grid class:
	//	Number of Streets and Avenues
	//	Maximum and Minimum Block Side Length in c unit
	private int NumberOfStreets = 10;
	private int NumberOfAvenues = 10;
	private int MaximumBlockSide = 500;
	private int MinimumBlockSide = 800;
		
	// Road class:
	//	Number of Forward and Turning Lanes
	private int NumberOfForwardLanes = 2;
	private int NumberOfTurningLanes = 1;
		
	// TrafficLight class
	//	Maximum Red and Green time in seconds
	//	Maximum Red Time could be Maximum Green Time + Yellow Time
	//	Yellow Time in seconds
	//	Intersection light initial status (TBD)
	private int MaxRedTime = 60;
	private int MaxGreenTime = 40;
	private int YellowTime = 5;
		
	// Car class:
	//	Maximum Car Speed in c/second unit
	//	Car Acceleration in c/second2 unit
	private int CarSpeed = 10;
	private int CarAcceleration = 1;
	
	public Configuration(String configFile) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(configFile))));
		} catch (FileNotFoundException exception) {
			System.out.println("Configuration File cannot be found.\nDefault Configuration Initiated.");
			return;
		}
		String line;
		StringTokenizer st;
		int line_number = 0;
		String name;
		int value;
		try {
			while ((line = reader.readLine()) != null) {
				if(line.length() > 0 && line.charAt(0) != '#') {
					st = new StringTokenizer(line);
					name = st.nextToken();
					value = Integer.parseInt(st.nextToken());
					loadEntry(name, value, line_number);
				}
				line_number++;
			}
			reader.close();
		} catch (IOException exception) {
			System.out.println("Problem reading Configuration File lines.");
			System.exit(0);
		}
	}
	
	private void loadEntry(String name, int value, int line_number) {
		if(name.equals("Lambda"))
			Lambda = value;
		else if(name.equals("NumberOfCars"))
			NumberOfCars = value;
		else if(name.equals("NumberOfStreets"))
			NumberOfStreets = value;
		else if(name.equals("NumberOfAvenues"))
			NumberOfAvenues = value;
		else if(name.equals("MaximumBlockSide"))
			MaximumBlockSide = value;
		else if(name.equals("MinimumBlockSide"))
			MinimumBlockSide = value;
		else if(name.equals("NumberOfForwardLanes"))
			NumberOfForwardLanes = value;
		else if(name.equals("NumberOfTurningLanes"))
			NumberOfTurningLanes = value;
		else if(name.equals("MaxRedTime"))
			MaxRedTime = value;
		else if(name.equals("MaxGreenTime"))
			MaxGreenTime = value;
		else if(name.equals("YellowTime"))
			YellowTime = value;
		else if(name.equals("CarSpeed"))
			CarSpeed = value;
		else if(name.equals("CarAcceleration"))
			CarAcceleration = value;
		else 
			System.out.println("Config file parsing error on line " + line_number);
		return;
	}
	
	public void printConfig() {
		System.out.println("Lambda\t\t\t" + Lambda);
		System.out.println("Number of Cars\t\t" + NumberOfCars);
		System.out.println("Number of Streets\t" + NumberOfStreets);
		System.out.println("Number of Avenues\t" + NumberOfAvenues);
		System.out.println("Maximum Block Side\t" + MaximumBlockSide);
		System.out.println("Minimum Block Side\t" + MinimumBlockSide);
		System.out.println("Number of Forward Lanes\t" + NumberOfForwardLanes);
		System.out.println("Number of Turning Lanes\t" + NumberOfTurningLanes);
		System.out.println("Maximum Red Time\t" + MaxRedTime);
		System.out.println("Maximum Green Time\t" + MaxGreenTime);
		System.out.println("Yellow Time\t\t" + YellowTime);
		System.out.println("Car Speed\t\t" + CarSpeed);
		System.out.println("Car Acceleration\t" + CarAcceleration);
	}
}