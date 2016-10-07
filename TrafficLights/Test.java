

public class Test extends TrafficLight implements Runnable{
	/*int i;
	    int signal;
	    int pause, redpause, greenpause;
	    Thread lighter;
*/
	    Test(){
	       /*signal=1;
	       redpause=6000;
	       greenpause=6000;*/
	   }

	public void run() {
		TrafficLight tl= new TrafficLight();
		tl.start();
		int s= tl.signal;  
		while (true) {
		    if (s==1){
	                      s=tl.signal;
	                    //  pause=greenpause;
	                      System.out.println("car is moving as light is green i.e., signal is"+s);
	                  }
	                  else {
	                      s=tl.signal;
	                    //  pause=redpause;
	                      System.out.println("car stopped as light is red i.e., signal is"+s);
	                  }
		    try {
			//Thread.sleep(6000);
		//System.out.println("car is moving");
		    } catch (Exception e) {
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
	    
	    public static void main(String args[]){
	    	Test t=new Test();
	    	t.start();
	    	
	    	
	    }
	} 
	
