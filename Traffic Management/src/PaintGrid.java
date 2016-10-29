/* This class will be responsible for drawing the grid 
 * Layouting the streets, avenues, entry points
 * and traffic signals
 */

import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import javax.swing.ImageIcon;

public class PaintGrid extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private int CarLength = 6, CarWidth=3, Clearance = 2;
	private Thread gridPaint;
	private boolean isRunning = false;
	
	private Image northImg, southImg, eastImg, westImg;
	
	public PaintGrid(int CarLength, int CarWidth, int Clearance) {
		this.CarLength = CarLength;
		this.CarWidth = CarWidth;
		this.Clearance = Clearance;
		loadImages();
	}
	
	Image offscreen;
	Graphics offgraphics;
	
	 private synchronized void relax() {
		 TrafficPoint tempPoint;
		 Car tempCar = null;
		 int speed = 0;
		 boolean wait = false;
		 for(Map.Entry<char[], TrafficPoint> entry : TrafficPoint.getEntrySet()) {
			 if(entry.getValue().control[1] != 'N') continue;                //not entrance point
		     	tempPoint = entry.getValue();
		                         //check if entrance point && road in front clear
		        if(!tempPoint.emptyQueue()) {
		                                 //see if a car is blocking entrance
		        	for(Map.Entry<char[], Car> entry2 : Car.getEntrySet()) {
		        		if(entry2.getValue().phase != 'M') continue;
		                	wait = Math.abs(tempPoint.distance(entry2.getValue())) < 
		                                                         (CarLength+Clearance);
		                                         //replace 2 with car clearance pixels from configuration
		                	if(wait) break;
		                                 }
		                    if(!wait) {
		                    	tempCar = tempPoint.Dequeue();
		                        tempCar.enterGrid();
		                        if(tempCar.lane == 'M')
		                        	tempCar.xy = Arrays.copyOfRange(tempPoint.sectors[1][1], 0, 2);
		                            else if((tempPoint.roadDir[0] == 'E' && tempCar.lane == 'R') || 
		                                                         (tempPoint.roadDir[0] == 'W' && tempCar.lane == 'L'))
		                            	tempCar.xy = Arrays.copyOfRange(tempPoint.sectors[0][0], 0, 2);
		                            else if((tempPoint.roadDir[0] == 'E' && tempCar.lane == 'L') || 
		                                                         (tempPoint.roadDir[0] == 'W' && tempCar.lane == 'R'))
		                            	tempCar.xy = Arrays.copyOfRange(tempPoint.sectors[2][0], 0, 2);
		                            else if((tempPoint.roadDir[0] == 'N' && tempCar.lane == 'R') || 
		                                                         (tempPoint.roadDir[0] == 'S' && tempCar.lane == 'L'))
		                            	tempCar.xy = Arrays.copyOfRange(tempPoint.sectors[0][0], 0, 2);
		                            else if((tempPoint.roadDir[0] == 'N' && tempCar.lane == 'L') ||
		                                                         (tempPoint.roadDir[0] == 'S' && tempCar.lane == 'R'))
		                            	tempCar.xy = Arrays.copyOfRange(tempPoint.sectors[0][2], 0, 2);
		                        		tempCar.setDirection(tempPoint.roadDir[0]);
		                                         //direction same for entrance and exit points
		                                tempCar.nextPoint = (tempPoint.nextStreet == null)? 
		                                                         tempPoint.nextAvenue : tempPoint.nextStreet;
		                                         //tempCar.phase = 'M'; fixed in enterGrid()
		                    		}
		                 		}
		                 	wait = false;
		                 }
		 			for(Map.Entry<char[], Car> entry : Car.getEntrySet()) {
		 				if(entry.getValue().phase != 'M') continue;             //if car not moving
		 					tempCar = entry.getValue();
		                    for(Map.Entry<char[], Car> entry1 : Car.getEntrySet()) {
		                    	if(entry1.getValue().phase != 'M') continue;    //if car not moving
		                        if(tempCar.road != entry1.getValue().road) continue;
		                                 //replace constant with clearance
		                        if(tempCar.dir == 'N' || tempCar.dir == 'W') {                  //positive directions
		                        	if(tempCar.distance(entry1.getValue()) < 0)
		                        		wait = (Math.abs(tempCar.distance(entry1.getValue())) < 
		                                                                 2*CarLength);
		                        	} else if(tempCar.dir == 'S' || tempCar.dir == 'E') {   //negative directions
		                        			if(tempCar.distance(entry1.getValue()) > 0)
		                                                 wait = (Math.abs(tempCar.distance(entry1.getValue())) < 
		                                                                 2*CarLength);
		                        			}
		                        			if(wait) {
		                                         tempCar.nextPoint.queueCar(tempCar);
		                                         break;
		                        			}
		                    		}
		                         if(!wait) {
		                                 tempCar.nextPoint.Dequeue(tempCar);
		                                 speed = 5;              //(int)((CarLength+Clearance)/2);
		                                 if(tempCar.dir == 'N')
		                                         tempCar.moveXY(new int[]{0, speed});
		                                 else if(tempCar.dir == 'S')
		                                         tempCar.moveXY(new int[]{0, -1*speed});
		                                 else if(tempCar.dir == 'E')
		                                         tempCar.moveXY(new int[]{-1*speed, 0});
		                                 else if(tempCar.dir == 'W')
		                                         tempCar.moveXY(new int[]{speed, 0});
		                         }
		                         wait = false;
		                 }
		                 repaint();
		                 wait = false;
		                 for(Map.Entry<char[], Car> entry : Car.getEntrySet()) {
		                         wait = entry.getValue().phase != 'S';
		                         if(wait) break;
		                 }
		    if (!wait && Car.carCount != 0) {
		             this.stop();
		             System.out.println("\nExecution stopped");
		             return;
		    } 
	 }
	
	
	public synchronized void paint(Graphics g) {
		Dimension d = new Dimension(Road.xAccumulativePosition, 
				Road.yAccumulativePosition);
		if (offscreen == null) {
				//for window resizing
				offscreen = createImage(d.width, d.height);
				offgraphics = offscreen.getGraphics();
		}
		offgraphics.setColor(Color.black);
		offgraphics.fillRect(0, 0, d.width, d.height);
		paintRoad(offgraphics);
		paintLights(offgraphics);
		paintCars(offgraphics);
		g.drawImage(offscreen, 0, 0, null);
	}
	
	private void paintRoad(Graphics g){
		Dimension d = getSize();
		Road tempRoad;
		int roadCoord = 1+(int)(1.5*(CarWidth+Clearance));
		int roadWidth = 2+3*(CarWidth+Clearance);
		for(Map.Entry<char[], Road> entry : Road.getEntrySet()) {
			tempRoad = entry.getValue();
			if(tempRoad.roadType == 'S') {
				//Drawing streets
				g.setColor(Color.gray);
				g.fillRect(0, tempRoad.sectors[1] - roadCoord, d.width, roadWidth);
				g.setColor(Color.WHITE);
				if(tempRoad.roadDir == 'E')
					g.drawImage(eastImg, Road.xAccumulativePosition-30, 
							tempRoad.sectors[1] -8, null);
				else if(tempRoad.roadDir == 'W')
					g.drawImage(westImg, 0, tempRoad.sectors[1]-8, null);
			} else if(tempRoad.roadType == 'A') {
				//Drawing avenues
				g.setColor(Color.gray);
				g.fillRect(tempRoad.sectors[1] - roadCoord, 0, roadWidth, d.height);
				g.setColor(Color.WHITE);
				if(tempRoad.roadDir == 'N')
					g.drawImage(northImg, tempRoad.sectors[1]-8, 
							0, null);
				else if(tempRoad.roadDir == 'S')
					g.drawImage(southImg, tempRoad.sectors[1]-8, 
							Road.yAccumulativePosition-30, null);
			}
		}
		int yellowLine = 1 + (int)(0.5*(CarWidth+Clearance));
		for(Map.Entry<char[], Road> entry : Road.getEntrySet()) {
			tempRoad = entry.getValue();
			if(tempRoad.roadType == 'S') {
				//Drawing streets
				g.setColor(Color.yellow);
				g.drawLine(0, tempRoad.sectors[1]-yellowLine, d.width, 
						tempRoad.sectors[1]-yellowLine);
				g.drawLine(0, tempRoad.sectors[1]+yellowLine, d.width, 
						tempRoad.sectors[1]+yellowLine);
			} else if(tempRoad.roadType == 'A') {
				//Drawing avenues
				g.setColor(Color.yellow);
				g.drawLine(tempRoad.sectors[1]-yellowLine, 0, 
						tempRoad.sectors[1]-yellowLine, d.height);
				g.drawLine(tempRoad.sectors[1]+yellowLine, 0, 
						tempRoad.sectors[1]+yellowLine, d.height);
			}
		}
	}
	
	private void paintLights(Graphics g) {
		//draw traffic lights at intersections as green initial
		TrafficPoint tempPoint;
		for(Map.Entry<char[], TrafficPoint> entry : TrafficPoint.getEntrySet()) {
			tempPoint = entry.getValue();
			if(tempPoint.control[0] != 'E') {		//not entrance nor exit
				switch(tempPoint.control[1]) {
					case 'R':	g.setColor(Color.red);
								break;
					case 'G':	g.setColor(Color.green);
								break;
					case 'Y':	g.setColor(Color.yellow);
								break;
				}
				if(tempPoint.roadDir[1] == 'N') {
					g.fillRect(tempPoint.sectors[1][1][0]-3*CarWidth, 
							tempPoint.sectors[1][1][1]-4*CarWidth, 6*CarWidth, CarWidth);
				} else if(tempPoint.roadDir[1] == 'S') {
					g.fillRect(tempPoint.sectors[1][1][0]-3*CarWidth, 
							tempPoint.sectors[1][1][1]+3*CarWidth, 6*CarWidth, CarWidth);
				}
				switch(tempPoint.control[0]) {
					case 'R':	g.setColor(Color.red);
								break;
					case 'G':	g.setColor(Color.green);
								break;
					case 'Y':	g.setColor(Color.yellow);
								break;
				}
				if(tempPoint.roadDir[0] == 'E') {
					g.fillRect(tempPoint.sectors[1][1][0]+3*CarWidth, 
							tempPoint.sectors[1][1][1]-3*CarWidth, CarWidth, 6*CarWidth);
				} else if(tempPoint.roadDir[0] == 'W') {
					g.fillRect(tempPoint.sectors[1][1][0]-4*CarWidth, 
							tempPoint.sectors[1][1][1]-3*CarWidth, CarWidth, 6*CarWidth);
				}
			}
		}
	}
	
	private void paintCars(Graphics g) {
		g.setColor(Color.cyan);
		Car tempCar;
		int HLength = 1+(int)(CarLength/2);
		int HWidth = 1+(int)(0.5*(CarWidth-Clearance));
		for(Map.Entry<char[], Car> entry : Car.getEntrySet()) {
			if(entry.getValue().phase != 'M') continue;
			tempCar = entry.getValue();
			if(!tempCar.xy.equals(new int[]{-1, -1})) {
				if(tempCar.dir == 'E' || tempCar.dir == 'W')
					g.fillRect(tempCar.xy[0]-HLength, tempCar.xy[1]-HWidth, CarLength, CarWidth);
				else if(tempCar.dir == 'N' || tempCar.dir == 'S')
					g.fillRect(tempCar.xy[0]-HWidth, tempCar.xy[1]-HLength, CarWidth, CarLength);
					
			}
		}
	}
	
	private void loadImages() {
		northImg = new ImageIcon("images/north.png").getImage();
        southImg = new ImageIcon("images/south.png").getImage();
        eastImg = new ImageIcon("images/east.png").getImage();
        westImg = new ImageIcon("images/west.png").getImage();
	}
	
	@Override
	public void run() {
		while (isRunning) {
			relax();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	public void start() {
		gridPaint = new Thread(this);
		isRunning = true;
		gridPaint.start();
	}
	
	public void stop() {
		gridPaint.interrupt();
		isRunning = false;
	}
}
