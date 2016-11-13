

public class TrafficLight implements Runnable{
	int i;
	    int signal;
	    int pause, redpause, greenpause;
	    Thread lighter;

	    TrafficLight(){
	       signal=1;
	       redpause=6000;
	       greenpause=6000;
	   }

	public void run() {
	              signal=1;
		while (true) {
		    if (signal==1){
	                      signal=0;
	                      pause=greenpause;
	                      System.out.println("Green light on");
	                  }
	                  else {
	                      signal=1;
	                      pause=redpause;
	                      System.out.println("Red light on");
	                  }
		    try {
			Thread.sleep(pause);
		    } catch (InterruptedException e) {
			break;
		    }
		    i++;
		}
	    }
	    public void start() {
		lighter = new Thread(this);
		lighter.start();
	    }
	    @SuppressWarnings("deprecation")
		public void stop() {
		lighter.stop();
	    }
	    
	    /*public static void main(String args[]){
	    	TrafficLight t=new TrafficLight();
	    	t.start();
	    	
	    	
	    }*/
	} 
	
