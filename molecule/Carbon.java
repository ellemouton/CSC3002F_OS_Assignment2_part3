package molecule;

public class Carbon extends Thread {
	
	private static int carbonCounter =0;
	private int id;
	private Methane sharedMethane;
	
	public Carbon(Methane methane_obj) {
		Carbon.carbonCounter+=1;
		id=carbonCounter;
		this.sharedMethane = methane_obj;
	}
	
	public void run() {
	    try {	 
	    	 // you will need to fix below

	    	sharedMethane.carbonQ.acquire();//-----------------------------------c
	    	System.out.println("---Group ready for bonding---");

	    	sharedMethane.hydrogensQ.release(4);
	    		
	    	sharedMethane.barrier.b_wait();//BARRIER

	    	sharedMethane.mutex.acquire();//---------m

	    	sharedMethane.bond("C"+ this.id);  //bond
	    	sharedMethane.removeCarbon(1);

	    	sharedMethane.mutex.release();//----------m

	   
	    	sharedMethane.barrier.b_wait();//BARRIER


	    	sharedMethane.mutex.acquire();//---------m

	    	sharedMethane.addCarbon();

	    	sharedMethane.mutex.release();//----------m
	    	

	    	sharedMethane.barrier.b_wait();//BARRIER

	    }
	    catch (InterruptedException ex) { /* not handling this  */}
	   // System.out.println(" ");
	}

}
