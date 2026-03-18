package model.vehicle;

import java.util.List;
import model.TrafficLight;

public class VehicleFactory {
    public static Vehicle createVehicle(VehicleType type, int x, int y, int entrance, TrafficLight light, List<Vehicle> allVehicles) {
        return switch (type) {
            case VehicleType.CAR -> new Car(x, y, entrance, light, allVehicles);
            case VehicleType.BUS -> new Bus(x, y, entrance, light, allVehicles);
            case VehicleType.TRUCK -> new Truck(x, y, entrance, light, allVehicles);
            default -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
        };
    }
}
