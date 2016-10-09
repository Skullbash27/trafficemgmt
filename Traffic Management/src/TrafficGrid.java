/* This class will be responsible for drawing the grid 
 * Layouting the streets, avenues, entry points
 * and traffic signals
 */

import java.awt.*;

public class TrafficGrid extends Canvas implements Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int ypos[]= new int [5];
	char ydir[] = new char[5];
	int carwidth=6, carlength=9;
	int xpos[]= new int[5];
	char xdir[] = new char[5];
	
	public TrafficGrid() {
		xpos[0] = 0;		//entrance and exit points
		ypos[0] = 0;		//left and top edges of grid
		xdir[0] = 0;		//neglected direction at edges of grid
		ydir[0] = 0;
		for (int i=1; i<5; i++) {
			//light[i]= new ChangeLight();
			//carpermin[i]= new CalFlow();
			xpos[i]= xpos[i-1] + (int) (100+100*Math.random());	//middle line for vertical road
			ypos[i]= ypos[i-1] + (int) (100+100*Math.random());	//middle line for horizontal road
			xdir[i] = (i%2) == 0 ? 'E' : 'W';		//determining directions based on iterator i
			ydir[i] = (i%2) == 0 ? 'N' : 'S';		//source of traffic directions assumption
			//brgflag[i]=0;					//intersection not occupied
		}
	}
	
	Image offscreen;
	Dimension offscreensize;
	Graphics offgraphics;
	
	public synchronized void paint(Graphics g) {
		Dimension d = getSize();
		if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) {
				//for window resizing
				offscreen = createImage(d.width, d.height);
				offscreensize = d;
				offgraphics = offscreen.getGraphics();
		}
		offgraphics.setColor(Color.black);
		offgraphics.fillRect(0, 0, d.width, d.height);
		paintRoad(offgraphics);
		paintLights(offgraphics);
		g.drawImage(offscreen, 0, 0, null);
	}
	
	public void paintRoad(Graphics g){
		Dimension d = getSize();
		//drawing streets and avenues
		g.setColor(Color.gray);
		for(int i = 1; i<4; i++) {
			g.fillRect(xpos[i]-3*carwidth, 0, 6*carwidth, d.height);
			g.fillRect(0, ypos[i]-3*carwidth, d.width, 6*carwidth);
		}
		//drawing lanes
		g.setColor(Color.yellow);
		for(int i = 1; i<4; i++) {
			g.drawLine(xpos[i]-carwidth, 0, xpos[i]-carwidth, d.height);
			g.drawLine(xpos[i]+carwidth, 0, xpos[i]+carwidth, d.height);
			g.drawLine(0, ypos[i]-carwidth, d.width, ypos[i]-carwidth);
			g.drawLine(0, ypos[i]+carwidth, d.width, ypos[i]+carwidth);
		}
	}
	
	public void paintLights(Graphics g) {
		g.setColor(Color.WHITE);
		for(int i = 1; i<4; i++) {
			if(xdir[i] == 'E')
				g.fillRect(xpos[i]-3*carwidth, 0, 6*carwidth, carwidth);
			if(ydir[i] == 'N')
				g.fillRect(0, ypos[i]-3*carwidth, carwidth, 6*carwidth);
		}
		//draw traffic lights at intersections as green initial
		g.setColor(Color.GREEN);
		for(int i = 1; i<4; i++) {
			for(int j = 1; j<4; j++) {
				if(ydir[i] == 'N') {
					g.fillRect(xpos[j]-4*carwidth, ypos[i]-3*carwidth, carwidth, 6*carwidth);
				} else if(ydir[i] == 'S') {
					g.fillRect(xpos[j]+3*carwidth, ypos[i]-3*carwidth, carwidth, 6*carwidth);
				}
				if(xdir[i] == 'E') {
					g.fillRect(xpos[i]-3*carwidth, ypos[j]-4*carwidth, 6*carwidth, carwidth);
				} else if(xdir[i] == 'W') {
					g.fillRect(xpos[i]-3*carwidth, ypos[j]+3*carwidth, 6*carwidth, carwidth);
				}
			}
		}
	}
	
	public void run() {
		while (true) {
			repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}