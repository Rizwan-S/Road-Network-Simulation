package demo;

import base.Highway;
import base.Truck;
import java.util.*;

class HighwayDemo extends Highway {

	private ArrayList<Truck> trucks = new ArrayList<Truck>();

	@Override
	public synchronized boolean hasCapacity() {						//Check if the highway has capacity or not
		if(trucks.size() < super.getCapacity()) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public synchronized boolean add(Truck truck) {		//Add method
		if(this.hasCapacity()) {
			trucks.add(truck);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public synchronized void remove(Truck truck) {		//Remove truck from highway
		Iterator<Truck> it = trucks.iterator();
		while(it.hasNext()) {
			Truck t = it.next();
   		 	if(t.equals(truck)) {
        		it.remove();
    		}
		}
		// trucks.remove(truck);
	}
}
