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
	    	 // you will need to fix below

	    	sharedMethane.mutex.acquire();//--------m
	    	if(sharedMethane.getCarbon()==0){
	    		sharedMethane.addCarbon();
	    		sharedMethane.carbonQ.release();
	    	}
	    	sharedMethane.mutex.release();//--------m
	    	

	    	sharedMethane.hydrogensQ.acquire();//-----------------------------h

	    	sharedMethane.mutex.acquire();//-----m
	    	sharedMethane.addHydrogen();
	    	sharedMethane.mutex.release();//-----m


	    	sharedMethane.barrier.b_wait();//BARRIER-----------------------------------------


	    	sharedMethane.mutex.acquire();//-----m
	    	sharedMethane.bond("H"+ this.id);  //bond
	    	sharedMethane.removeCarbon(1);
	    	sharedMethane.mutex.release();//-----m


	    	sharedMethane.barrier.b_wait();//BARRIER-----------------------------------------

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
