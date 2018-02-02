import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Vehicle implements Comparable<Vehicle>
{
    private static int nextID = 0;
    public final int ID;
    public final Place location;
    public final int capacity;
    public final double timePassed;
    public final Set<Client> clients;

    public Vehicle(Place location, int capacity)
    {
        this.ID = nextID++;
        this.location = location;
        this.capacity = capacity;
        this.timePassed = 0;
        this.clients = new HashSet<>();
    }

    public Vehicle(Vehicle oldVehicle, Place location, int capacity, double timePassed, Set<Client> clients)
    {
        this.ID = oldVehicle.ID;
        this.location = location;
        this.capacity = capacity;
        this.timePassed = timePassed;
        this.clients = clients;
    }

    public Vehicle(Vehicle vehicle)
    {
        this.ID = vehicle.ID;
        this.location = vehicle.location;
        this.capacity = vehicle.capacity;
        this.timePassed = vehicle.timePassed;
        this.clients = new HashSet<>(vehicle.clients);
    }



    @Override
    public int hashCode()
    {
        int result;
        long temp;
        result = location.hashCode();
        result = 31 * result + capacity;
        temp = Double.doubleToLongBits(timePassed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + clients.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Vehicle vehicle = (Vehicle) o;

        if (capacity != vehicle.capacity)
        {
            return false;
        }
        if (Double.compare(vehicle.timePassed, timePassed) != 0)
        {
            return false;
        }
        if (!location.equals(vehicle.location))
        {
            return false;
        }
        return clients.equals(vehicle.clients);
    }

    @Override
    public int compareTo(Vehicle o)
    {
        if(this.hashCode() < o.hashCode())
        {
            return -1;
        }
        else if(this.hashCode() > o.hashCode())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
