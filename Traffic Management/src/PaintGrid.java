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
	int carwidth=3, carlength=6;
	Thread gridPaint;
	boolean isRunning = false;
	
	private Image northImg, southImg, eastImg, westImg;
	
	public PaintGrid() {
		loadImages();
	}
	
	Image offscreen;
	Graphics offgraphics;
	
	private synchronized void relax() {
		TrafficPoint tempPoint;
		Car tempCar = null;
		int speed = 0;
		for(Map.Entry<char[], TrafficPoint> entry : TrafficPoint.getEntrySet()) {
			tempPoint = entry.getValue();
			//check if entrance point && road in front clear
			if(!tempPoint.emptyQueue())
				tempCar = tempPoint.Dequeue();
			if(tempCar != null) {				//make sure queue not empty
				tempCar.setXY(Arrays.copyOfRange(tempPoint.getXY(), 0, 2));
				tempCar.setDirection(tempPoint.getDirection()[0]);
				//direction same for entrance and exit points
			}
			tempCar = null;
		}
		for(Map.Entry<char[], Car> entry : Car.getEntrySet()) {
			tempCar = entry.getValue();
			speed = 1;
			if(!tempCar.getXY().equals(new int[]{-1, -1})) {
				if(tempCar.getDirection() == 'N')
					tempCar.moveXY(new int[]{0, speed});
				else if(tempCar.getDirection() == 'S')
					tempCar.moveXY(new int[]{0, -1*speed});
				else if(tempCar.getDirection() == 'E')
					tempCar.moveXY(new int[]{-1*speed, 0});
				else if(tempCar.getDirection() == 'W')
					tempCar.moveXY(new int[]{speed, 0});
			}
		}
		repaint();
	}
	
	
	public synchronized void paint(Graphics g) {
		Dimension d = new Dimension(Road.getXAccPos()*carlength, 
				Road.getYAccPos()*carlength);
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
	
	public void paintRoad(Graphics g){
		Dimension d = getSize();
		Road tempRoad;
		for(Map.Entry<char[], Road> entry : Road.getEntrySet()) {
			tempRoad = entry.getValue();
			if(tempRoad.getType() == 'S') {
				//Drawing streets
				g.setColor(Color.gray);
				g.fillRect(0, carlength*tempRoad.getAccPos()-3*carwidth, 
						d.width, 6*carwidth);
				g.setColor(Color.WHITE);
				if(tempRoad.getRoadDirection() == 'E')
					g.drawImage(eastImg, Road.getXAccPos()*carlength-30, 
							tempRoad.getAccPos()*carlength-8, null);
				else if(tempRoad.getRoadDirection() == 'W')
					g.drawImage(westImg, 0, tempRoad.getAccPos()*carlength-8, null);
			} else if(tempRoad.getType() == 'A') {
				//Drawing avenues
				g.setColor(Color.gray);
				g.fillRect(carlength*tempRoad.getAccPos()-3*carwidth, 0, 
						6*carwidth, d.height);
				g.setColor(Color.WHITE);
				if(tempRoad.getRoadDirection() == 'N')
					g.drawImage(northImg, carlength*tempRoad.getAccPos()-8, 
							0, null);
				else if(tempRoad.getRoadDirection() == 'S')
					g.drawImage(southImg, carlength*tempRoad.getAccPos()-8, 
							Road.getYAccPos()*carlength-30, null);
			}
		}
		for(Map.Entry<char[], Road> entry : Road.getEntrySet()) {
			tempRoad = entry.getValue();
			if(tempRoad.getType() == 'S') {
				//Drawing streets
				g.setColor(Color.yellow);
				g.drawLine(0, carlength*tempRoad.getAccPos()-carwidth, d.width, 
						carlength*tempRoad.getAccPos()-carwidth);
				g.drawLine(0, carlength*tempRoad.getAccPos()+carwidth, d.width, 
						carlength*tempRoad.getAccPos()+carwidth);
			} else if(tempRoad.getType() == 'A') {
				//Drawing avenues
				g.setColor(Color.yellow);
				g.drawLine(carlength*tempRoad.getAccPos()-carwidth, 0, 
						carlength*tempRoad.getAccPos()-carwidth, d.height);
				g.drawLine(carlength*tempRoad.getAccPos()+carwidth, 0, 
						carlength*tempRoad.getAccPos()+carwidth, d.height);
			}
		}
	}
	
	public void paintLights(Graphics g) {
		//draw traffic lights at intersections as green initial
		TrafficPoint tempPoint;
		for(Map.Entry<char[], TrafficPoint> entry : TrafficPoint.getEntrySet()) {
			tempPoint = entry.getValue();
			if(tempPoint.getControl()[0] != 'E') {
				switch(tempPoint.getControl()[1]) {
					case 'R':	g.setColor(Color.red);
								break;
					case 'G':	g.setColor(Color.green);
								break;
					case 'Y':	g.setColor(Color.yellow);
								break;
				}
				if(tempPoint.getDirection()[1] == 'N') {
					g.fillRect(carlength*tempPoint.getXY()[0]-3*carwidth, 
							carlength*tempPoint.getXY()[1]-4*carwidth, 6*carwidth, carwidth);
				} else if(tempPoint.getDirection()[1] == 'S') {
					g.fillRect(carlength*tempPoint.getXY()[0]-3*carwidth, 
							carlength*tempPoint.getXY()[1]+3*carwidth, 6*carwidth, carwidth);
				}
				switch(tempPoint.getControl()[0]) {
					case 'R':	g.setColor(Color.red);
								break;
					case 'G':	g.setColor(Color.green);
								break;
					case 'Y':	g.setColor(Color.yellow);
								break;
				}
				if(tempPoint.getDirection()[0] == 'E') {
					g.fillRect(carlength*tempPoint.getXY()[0]-4*carwidth, 
							carlength*tempPoint.getXY()[1]-3*carwidth, carwidth, 6*carwidth);
				} else if(tempPoint.getDirection()[0] == 'W') {
					g.fillRect(carlength*tempPoint.getXY()[0]+3*carwidth, 
							carlength*tempPoint.getXY()[1]-3*carwidth, carwidth, 6*carwidth);
				}
			}
		}
	}
	
	private void paintCars(Graphics g) {
		g.setColor(Color.cyan);
		Car tempCar;
		for(Map.Entry<char[], Car> entry : Car.getEntrySet()) {
			tempCar = entry.getValue();
			if(!tempCar.getXY().equals(new int[]{-1, -1})) {
				g.fillRect(tempCar.getXY()[0]*carlength-4, 
						tempCar.getXY()[1]*carlength-4, 8, 8);
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
				Thread.sleep(500);
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