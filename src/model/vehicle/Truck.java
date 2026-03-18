package model.vehicle;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import model.TrafficLight;

public class Truck extends Vehicle {

	public Truck(int x, int y, int entrance, TrafficLight light, List<Vehicle> allVehicles) {
		super(x, y, entrance, light, allVehicles);
		
		Random random = new Random();
        this.speed = random.nextInt(3) + 2;
        this.color = Color.BLUE;
        this.vehicleSize = 50;
	}

}
