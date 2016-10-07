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
 * (e.g. Car_Speed 10). Default config values (if config file is missing
 * an entry to set the value) are defined in the variable declarations below.
 *
 * A function is also provided for printing the current configuration. */

package trafficemgmt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Configuration {
	// Simulation class:
	//	Exponential Car Insertion Rate
	//	Number of Cars
	private int Lambda = 10;
	private int Number_of_Cars = 1000;
	
	// Grid class:
	//	Number of Streets and Avenues
	//	Maximum and Minimum Block Side Length in c unit
	private int Number_of_Streets = 10;
	private int Number_of_Avenues = 10;
	private int Maximum_Block_Side = 500;
	private int Minimum_Block_Side = 800;
	
	// Road class:
	//	Number of Forward and Turning Lanes
	private int Number_of_Forward_Lanes = 2;
	private int Number_of_Turning_Lanes = 1;
	
	// TrafficLight class
	//	Maximum Red and Green time in seconds
	//	Maximum Red Time could be Maximum Green Time + Yellow Time
	//	Yellow Time in seconds
	//	Intersection light initial status (TBD)
	private int Max_Red_Time = 60;
	private int Max_Green_Time = 40;
	private int Yellow_Time = 5;
	
	// Car class:
	//	Maximum Car Speed in c/second unit
	//	Car Acceleration in c/second2 unit
	private int Car_Speed = 10;
	private int Car_Acceleration = 1;
	
	public Configuration(String configFile) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(configFile))));
		} catch (FileNotFoundException exception) {
			System.out.println("Configuration File cannot be found.\nDefault Configuration Initiated.");
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
		else if(name.equals("Number_of_Cars"))
			Number_of_Cars = value;
		else if(name.equals("Number_of_Streets"))
			Number_of_Streets = value;
		else if(name.equals("Number_of_Avenues"))
			Number_of_Avenues = value;
		else if(name.equals("Maximum_Block_Side"))
			Maximum_Block_Side = value;
		else if(name.equals("Minimum_Block_Side"))
			Minimum_Block_Side = value;
		else if(name.equals("Number_of_Forward_Lanes"))
			Number_of_Forward_Lanes = value;
		else if(name.equals("Number_of_Turning_Lanes"))
			Number_of_Turning_Lanes = value;
		else if(name.equals("Max_Red_Time"))
			Max_Red_Time = value;
		else if(name.equals("Max_Green_Time"))
			Max_Green_Time = value;
		else if(name.equals("Yellow_Time"))
			Yellow_Time = value;
		else if(name.equals("Car_Speed"))
			Car_Speed = value;
		else if(name.equals("Car_Acceleration"))
			Car_Acceleration = value;
		else 
			System.out.println("Config file parsing error on line " + line_number);
		return;
	}
	
	public void printConfig() {
		System.out.println("Lambda\t" + Lambda);
		System.out.println("Number of Cars\t" + Number_of_Cars);
		System.out.println("Number of Streets\t" + Number_of_Streets);
		System.out.println("Number of Avenues\t" + Number_of_Avenues);
		System.out.println("Maximum Block Side\t" + Maximum_Block_Side);
		System.out.println("Minimum Block Side\t" + Minimum_Block_Side);
		System.out.println("Number of Forward Lanes\t" + Number_of_Forward_Lanes);
		System.out.println("Number of Turning Lanes\t" + Number_of_Turning_Lanes);
		System.out.println("Maximum Red Time\t" + Max_Red_Time);
		System.out.println("Maximum Green Time\t" + Max_Green_Time);
		System.out.println("Yellow Time\t" + Yellow_Time);
		System.out.println("Car Speed\t" + Car_Speed);
		System.out.println("Car Acceleration\t" + Car_Acceleration);
	}
}