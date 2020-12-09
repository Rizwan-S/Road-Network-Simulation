package demo;

import base.Highway;
import base.Hub;
import base.Location;
import base.Truck;
import java.util.*;

import base.Network;

class HubDemo extends Hub {

	private ArrayList<Truck> trucks = new ArrayList<Truck>();

	public HubDemo(Location loc) {
		super(loc);
		
	}

	@Override
	public synchronized boolean add(Truck truck) {			//Method to add truck to the hub
		if(trucks.size() > super.getCapacity()) {
			return false;
		}
		else {
			trucks.add(truck);
			truck.setLoc(super.getLoc());
			return true;
		}
	}

	@Override
	public synchronized void remove(Truck truck) {			//Method to remove truck from hub
			trucks.remove(truck);	
	}

	@Override
	public Highway getNextHighway(Hub last, Hub dest) {			//Method to get next highway (BFS Algorithm)

		Highway next = null;
		int min = Integer.MAX_VALUE;
		ArrayList<Highway>neighbour = super.getHighways();

		for(Highway highway: neighbour) {				//
			
			HashMap<Hub, Integer> l = new HashMap<>();
			Queue<Hub> queue = new LinkedList<>();
			queue.add(highway.getEnd());
			l.put(highway.getEnd(), 0);
			while(queue.size() != 0) {
				Hub currHub = queue.poll();
				ArrayList <Highway> adjacent = currHub.getHighways();	
				for(Highway h: adjacent) {
				
					if(!l.containsKey(h.getEnd())) {
						queue.add(h.getEnd());
						l.put(h.getEnd(), l.get(currHub) + 1);
					}
				}
			}

			if(l.get(dest) < min) {
				min = l.get(dest);
				next = highway;
			}
		}
		return next;
	}

	@Override
	protected void processQ(int deltaT) {			//Method to handle the trucks as per the traffic on the highways
		for(int i = 0; i < trucks.size(); i++) {
			Truck truck = trucks.get(i);		
			Highway highway = this.getNextHighway(this, Network.getNearestHub(truck.getDest()));
			if(highway.add(truck)) {
				truck.enter(highway);
				this.remove(truck);
			}			
		}		
	}	
}
