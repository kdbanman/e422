import java.util.TimerTask;

/*
 * Created on Aug 6, 2004
 * Modified on Oct 19, 2014
 * 
 * Watchdog timer
 
 */

/**
 * @author dick
 * @author kdbanman
 
 */
public class Watchdog extends TimerTask {

Thread watched;

	public Watchdog (Thread target){
                // Associate the timer with a thread to be supervised.
		watched = target;
	}
	
        @Override
	public void run() {
		
		watched.stop(); // we live dangerously, deprecation warning
		System.out.println("Target thread terminated.");
	}

}