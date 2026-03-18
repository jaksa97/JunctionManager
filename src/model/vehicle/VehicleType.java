package model.vehicle;

import java.util.Random;

public enum VehicleType {
	CAR,
	BUS,
	TRUCK;
	
	private static final Random random = new Random();
	
	public static VehicleType getRandomType() {
		VehicleType[] types = values();
		return types[random.nextInt(types.length)];
	}
}
