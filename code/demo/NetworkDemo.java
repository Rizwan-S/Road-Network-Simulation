package demo;

import base.*;
import java.util.*;

public class NetworkDemo extends Network {

	private ArrayList<Hub> hubs = new ArrayList<Hub>();
	private ArrayList<Highway> highways = new ArrayList<Highway>();
	private ArrayList<Truck> trucks = new ArrayList<Truck>();

	public NetworkDemo(){
		super();
	}

	@Override
	public void add(Hub hub){		//Add methods
		hubs.add(hub);
	}

	@Override
	public void add(Highway hwy){
		highways.add(hwy);
	}

	@Override
	public void add(Truck truck){
		trucks.add(truck);
	}

	@Override
	public void start() {		//Start method on all hubs and trucks

		for(Hub hub: hubs) {
			hub.start();
		}
		for(Truck truck: trucks) {
			truck.start();
		}	
	}

	@Override
	public void redisplay(Display disp) {	//Redisplay method on all hubs, highways and trucks
		for(Hub hub: hubs) {
			hub.draw(disp);
		}
		for(Highway highway: highways) {
			highway.draw(disp);
		}
		for(Truck truck: trucks) {
			truck.draw(disp);
		}
	}

	@Override
	public Hub findNearestHubForLoc(Location loc) {		//Method to find nearest hub to a given location
		Hub nearest = hubs.get(0);
		for(Hub hub: hubs) {
			if(hub.getLoc().distSqrd(loc) < nearest.getLoc().distSqrd(loc)) {
				nearest = hub;
			}
		}
		return nearest;
	}
}