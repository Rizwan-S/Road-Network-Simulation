package demo;

import base.*;

import java.lang.Math;

class TruckDemo extends Truck {

	private Highway onhighway;		

	private int roadspeed = 5;				//Speed on roads 

	private boolean onRoad = false;			//If the truck is between hub and end station

	private int time = 0;					//time is in milliseconds
	private boolean isOnHighWay = false;	//highway maxspeed is in pixels/second


	@Override
	protected void update(int deltaT) {
		if(time >= super.getStartTime()) {
			if(onRoad == true) {								//If the truck is between end hub and destination
				double radius = roadspeed * deltaT / 1000.0;
				radius = radius * radius;
				if(super.getDest().distSqrd(super.getLoc()) <= radius) {			//If the truck is close to destination, set it to destination
					super.setLoc(super.getDest());
					if(onhighway != null) {
						onhighway.remove(this);
					}		
				}
				else {																//Otherwise, keep moving the truck closer to destination
					if(onhighway != null) {
						onhighway.remove(this);
					}													

					double length = 1.0 * super.getLoc().distSqrd(super.getDest());
					length = Math.sqrt(length);
					double cos_temp = ((super.getDest().getX() - super.getLoc().getX()) * 1.0 / length);
					double sin_temp = ((super.getDest().getY() - super.getLoc().getY()) * 1.0 / length);
					double deltaX = cos_temp * roadspeed * deltaT / 1000.0;
					double deltaY = sin_temp * roadspeed * deltaT / 1000.0;
					int newX = super.getLoc().getX() + (int) Math.round(deltaX);
					int newY = super.getLoc().getY() + (int) Math.round(deltaY);
					super.setLoc(new Location(newX, newY));
				}

			}
			else if(isOnHighWay) { 											//If the truck is on highway 
				Hub end = onhighway.getEnd();
				double radius = onhighway.getMaxSpeed() * deltaT / 1000.00;
				radius = radius * radius;
				if(end.getLoc().distSqrd(super.getLoc()) <= radius) {				//If the truck is closer to next hub
					if(end.equals(Network.getNearestHub(super.getDest()))) {			//If the truck is near to destination hub start moving it to destination
						onhighway.remove(this);
						onRoad = true;
						super.setLoc(end.getLoc());	
					}
					else {											//If the truck is not near to destination hub, add it to next hub
						if(end.add(this)) {
							onhighway.remove(this);
							super.setLoc(end.getLoc());
						}
					}
				}
				else {													//Keep moving the truck closer to next hub
					double length = 1.0 * super.getLoc().distSqrd(end.getLoc());
					length = Math.sqrt(length);
					double cos_temp = ((end.getLoc().getX() - super.getLoc().getX()) * 1.0 / length);
					double sin_temp = ((end.getLoc().getY() - super.getLoc().getY()) * 1.0 / length);
					double deltaX = cos_temp * onhighway.getMaxSpeed() * deltaT / 1000.0;
					double deltaY = sin_temp * onhighway.getMaxSpeed() * deltaT / 1000.0;
					int newX = super.getLoc().getX() + (int) Math.round(deltaX);
					int newY = super.getLoc().getY() + (int) Math.round(deltaY);
					super.setLoc(new Location(newX, newY));				
				}
			}
			else {									//If the truck is between start station and first hub
				Hub startHub = Network.getNearestHub(super.getSource());
				double radius = roadspeed * deltaT / 1000.00;
				radius = radius * radius;
				if(startHub.getLoc().distSqrd(super.getLoc()) <= radius) { 			//IF the truck is closer to start hub, add it to hub
					if(startHub.equals(Network.getNearestHub(super.getDest()))) {			//If the truck is near to destination hub, start moving it to destination
						onRoad = true;
						super.setLoc(startHub.getLoc());	
					}							
					else {																	//If the truck is closer to hub with is not end hub, add it to hub
						super.setLoc(startHub.getLoc());
						startHub.add(this);
					}
				}
				else{																	//Otherwise, keep moving the truck closer to start hub
					double length = 1.0 * startHub.getLoc().distSqrd(super.getLoc());
					length = Math.sqrt(length);
					double cos_temp = ((startHub.getLoc().getX() - super.getLoc().getX()) * 1.0/ length);
					double sin_temp = ((startHub.getLoc().getY() - super.getLoc().getY()) * 1.0/ length);
					double deltaX = cos_temp * roadspeed * deltaT / 1000.0;
					double deltaY = sin_temp * roadspeed * deltaT / 1000.0;
					int newX = super.getLoc().getX() + (int) Math.round(deltaX);
					int newY = super.getLoc().getY() + (int) Math.round(deltaY);
					super.setLoc(new Location(newX, newY));
				}
			}
		}
		time += deltaT;
	}

	@Override
	public String getTruckName() {			//Override getname to get the name of truck
		return "Truck";
	}

	@Override
	public Hub getLastHub() {			//Return last hub
		if(onhighway != null) {
			return onhighway.getStart();
		}
		else {
			return null;
		}
		
	}

	@Override
	public void enter(Highway hwy) {
		if(hwy != null) {			//If the truck entered the highway
			isOnHighWay = true;
		}
		onhighway = hwy;
	}
}
