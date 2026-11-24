package trafficsim; 

import java.util.List; 
import java.util.ArrayList;
import java.util.Collections; 

public class VehicleManager<T extends Vehicles> { 

    private final List<T> vehicleList = new ArrayList<>();
    public void add(T vehicle) { 
        vehicleList.add(vehicle); 
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(vehicleList); 
    }

    public int count() {
        return vehicleList.size();  
    }

    public void clear() {
        vehicleList.clear(); 
    }


    public void removeLast() { 
        if (!vehicleList.isEmpty()) {
            vehicleList.remove(vehicleList.size() - 1);
        }
    }
}
