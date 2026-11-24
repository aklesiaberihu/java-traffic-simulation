package trafficsim;

public final class VehicleUtility {


    public void logVehicle(int id) {
        System.out.println("Vehicle ID: " + id);
    }

    public void logVehicle(String type) {
        System.out.println("Vehicle Type: " + type);
    }

    public void logVehicle(double speed) {
        System.out.println("Vehicle Speed: " + speed + " km/h");
    }

    public void logVehicle(String type, double speed) {
        System.out.println("Type: " + type + " | Speed: " + speed + " km/h");
    }

    public void logVehicle(Vehicles v) {
        logVehicle(v.getClass().getSimpleName(), v.getSpeed());
    }
} 