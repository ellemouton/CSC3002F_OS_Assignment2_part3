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

	    	/* -------------------------------------------------
				Wait for a the carbonQ semaphore to be released.
				A max of 1 will be available at a time
			   -------------------------------------------------
	    	*/
	    	sharedMethane.carbonQ.acquire();//-----------------------------------c
	    	System.out.println("---Group ready for bonding---");

	    	/* -------------------------------------------------
				Release 4 hydrogens for every carbon
			   -------------------------------------------------
	    	*/
	    	sharedMethane.hydrogensQ.release(4);

	    	/* -------------------------------------------------
				Wait at the barrier for 4  hydrogens
			   -------------------------------------------------
	    	*/
	    	sharedMethane.barrier.b_wait();//----------------BARRIER------------

	    	/* -------------------------------------------------
				bond and then remove 1 from the Carbon counter
			   -------------------------------------------------
	    	*/
	    	sharedMethane.mutex.acquire();//---------m
	    	sharedMethane.bond("C"+ this.id);  //bond
	    	sharedMethane.removeCarbon(1);
	    	sharedMethane.mutex.release();//----------m

	    	/* -------------------------------------------------
				Wait at barrier for 4 hydrogens to bond
			   -------------------------------------------------
	    	*/
	    	sharedMethane.barrier.b_wait();//----------------BARRIER-------------

	    	/* -------------------------------------------------
				add 1 to carbon counter and if the count is 0, 
				then release the carbonQ mutex for the next carbon
			   -------------------------------------------------
	    	*/
	    	sharedMethane.mutex.acquire();//---------m
	    	sharedMethane.addCarbon();
	    	if(sharedMethane.getCarbon()==0){
	    		sharedMethane.addCarbon();
	    		sharedMethane.carbonQ.release();
	    	}
	    	sharedMethane.mutex.release();//---------m
	    	

	    	
	    }
	    catch (InterruptedException ex) { /* not handling this  */}
	   // System.out.println(" ");
	}

}
