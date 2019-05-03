package molecule;

public class Hydrogen extends Thread {

	private static int carbonCounter =0;
	private int id;
	private Methane sharedMethane;
	
	public Hydrogen(Methane methane_obj) {
		Hydrogen.carbonCounter+=1;
		id=carbonCounter;
		this.sharedMethane = methane_obj;
	}
	
	public void run() {
	    try {

	    	/* --------------------------------------------------------------
				Check to see if the current number of released carbonsQ's is 0
				and if it is, release one
			   -------------------------------------------------------------
	    	*/
	    	sharedMethane.mutex.acquire();//--------m
	    	if(sharedMethane.getCarbon()==0){
	    		sharedMethane.addCarbon();
	    		sharedMethane.carbonQ.release();
	    	}
	    	sharedMethane.mutex.release();//--------m
	    	
	    	/* -----------------------------------------------------------------
				Wait for a the carbonQ semaphore to be released by a carbon thread.
				A max of 4 will be available at a time
			   -------------------------------------------------------------------
	    	*/
	    	sharedMethane.hydrogensQ.acquire();//-----------------------------h

	    	/* -------------------------------------------------
				Add 1 to the hydrogen count
			   -------------------------------------------------
	    	*/
	    	sharedMethane.mutex.acquire();//-----m
	    	sharedMethane.addHydrogen();
	    	sharedMethane.mutex.release();//-----m

	    	/* --------------------------------------------------------
				Wait at the barrier for 1  carbon and 3 other hydrogens
			   --------------------------------------------------------
	    	*/
	    	sharedMethane.barrier.b_wait();//----------------BARRIER----------------------

	    	/* -------------------------------------------------
				bond and then remove 1 from the Carbon counter
			   -------------------------------------------------
	    	*/
	    	sharedMethane.mutex.acquire();//-----m
	    	sharedMethane.bond("H"+ this.id);  //bond
	    	sharedMethane.removeCarbon(1);
	    	sharedMethane.mutex.release();//-----m

	    	/* ------------------------------------------------------
				Wait at barrier for all the rest in the group to bond
			   ------------------------------------------------------
	    	*/
	    	sharedMethane.barrier.b_wait();//----------------BARRIER----------------------

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
	    //System.out.println(" ");
	}

}
