import java.util.*;

public class PathState implements ProgressiveNodeType<PathState>, HeuristicType<PathState>
{
    public final ArrayList<Vehicle> vehicles;
    public final ArrayList<Client> clients;
    public final Graph<Place> graph;
    public final Place droppingPlace;
    public final boolean start;
    public final int vehicleCreationCost;
    public final int veiculeCapacity;

    public static final PathState goalState = new PathState(new ArrayList<>(), new ArrayList<>(), null, null, false, 1, 1);

    public PathState(ArrayList<Client> clients, Graph<Place> graph, Place droppingPlace, int vehicleCreationCost, int vehicleCapacity)
    {
        this(new ArrayList<>(), clients, graph, droppingPlace, true, vehicleCreationCost, vehicleCapacity);
    }

    public PathState(ArrayList<Vehicle> vehicles, ArrayList<Client> clients, Graph<Place> graph, Place droppingPlace, boolean start, int vehicleCreationCost, int veiculeCapacity)
    {
        this.vehicles = vehicles;
        Collections.sort(this.vehicles);
        this.clients = clients;
        Collections.sort(this.clients);
        this.graph = graph;
        this.droppingPlace = droppingPlace;
        this.start = start;
        this.vehicleCreationCost = vehicleCreationCost;
        this.veiculeCapacity = veiculeCapacity;
    }

    public ArrayList<Edge<PathState>> generateChildrenStates()
    {
        ArrayList<Edge<PathState>> out = new ArrayList<>();

        if(clients.isEmpty())
        {
            boolean check = true;
            for (Vehicle veh : vehicles)
            {
                if (veh.location != droppingPlace)
                {
                    check = false;
                    break;
                }
            }

            if (check)
            {
                ArrayList<Vehicle> newVehicles = new ArrayList<>();
                out.add(new Edge<>(new Node<>(new PathState(newVehicles, clients, graph, droppingPlace, false, vehicleCreationCost, veiculeCapacity)), 0));
                return out;
            }
        }

        if(start && (vehicles.size() < clients.size()))
        {
            ArrayList<Vehicle> newVehicles = new ArrayList<>();

            for(Vehicle vehicle : vehicles)
            {
                newVehicles.add(new Vehicle(vehicle));
            }
            newVehicles.add(new Vehicle(droppingPlace, veiculeCapacity));
            out.add(new Edge<>(new ProgressiveNode<>(new PathState(newVehicles, clients, graph, droppingPlace, true, vehicleCreationCost, veiculeCapacity)), vehicleCreationCost));
        }

        ArrayList<Client> clientsInVehicles = new ArrayList<>();
        for(Vehicle vehicle : vehicles)
        {
            clientsInVehicles.addAll(vehicle.clients);
        }
        Set<Place> placesWithClients = new HashSet<>();
        for(Client client : clients)
        {
            if(!(clientsInVehicles.contains(client)))
            {
                placesWithClients.add(client.place);
            }
        }
        int[] nextCombination = new int[vehicles.size()];
        Arrays.fill(nextCombination, 0);

        Place[] places = new Place[placesWithClients.size()+1];
        int k = 0;
        for(Node<Place> node : graph.getNodes().values())
        {
            Place nodePlace = node.getPayload();
            if(nodePlace == droppingPlace || placesWithClients.contains(nodePlace))
            {
                places[k++] = nodePlace;
            }
        }

        boolean done;
        do
        {

            //per state

            boolean allStatic = true;
            boolean clientsChanged = false;
            double costSum = 0;
            ArrayList<Client> tempClientsInVehicles = new ArrayList<>(clientsInVehicles);
            ArrayList<Place> tempPlacesWithClients = new ArrayList<>(placesWithClients);
            ArrayList<Client> newClients = new ArrayList<>(clients);
            Collections.sort(newClients);
            ArrayList<Vehicle> newVehicles = new ArrayList<>(vehicles.size());
            outerr:
            for (int i = 0; i < nextCombination.length; i++)
            {
                Vehicle vehicle = vehicles.get(i);
                Place place = places[nextCombination[i]];
                if (place != vehicle.location)
                {
                    allStatic = false;
                    boolean found = false;
                    double cost = 0;
                    outer:
                    for (Node<Place> node : graph.getNodes().values())
                    {
                        if (node.getPayload() == vehicle.location)
                        {
                            for (Edge<Place> edge : node.getAdjacents())
                            {
                                if (edge.getDestination().getPayload() == place)
                                {
                                    cost = edge.getCost();
                                    found = true;
                                    break outer;
                                }
                            }
                        }
                    }
                    if(!found)
                    {
                       return null;
                    }
                    costSum += cost;
                    if (place == droppingPlace)
                    {
                        double newTimePassed = vehicle.timePassed + cost;
                        for(Client client : vehicle.clients)
                        {
                            if(newTimePassed >= client.minutesUntilFlight)
                            {
                                allStatic = true;
                                break outerr;
                            }
                        }
                        tempClientsInVehicles.removeAll(vehicle.clients);
                        newClients.removeAll(vehicle.clients);
                        clientsChanged = true;
                        Vehicle newVehicle = new Vehicle(vehicle, place, vehicle.capacity, newTimePassed, new HashSet<>());
                        newVehicles.add(i, newVehicle);
                    }
                    else if (tempPlacesWithClients.contains(place))
                    {
                        Vehicle newVehicle = new Vehicle(vehicle, place, vehicle.capacity, vehicle.timePassed + cost, new HashSet<>(vehicle.clients));
                        int j = 0;
                        for (; j < newClients.size(); j++)
                        {
                            if (newVehicle.clients.size() >= newVehicle.capacity)
                            {
                                break;
                            }
                            Client client = newClients.get(j);
                            if (client.place == newVehicle.location && !tempClientsInVehicles.contains(client))
                            {
                                newVehicle.clients.add(client);
                                tempClientsInVehicles.add(client);
                            }
                        }
                        if (j >= newClients.size())
                        {
                            tempPlacesWithClients.remove(newVehicle.location);
                        }
                        newVehicles.add(i, newVehicle);
                    }
                    else
                    {
                        allStatic = true;
                        break;
                    }
                }
                else
                {
                    newVehicles.add(i, vehicle);
                }
            }

            if (!allStatic)
            {
                boolean check = true;
                for(Vehicle vehicle : newVehicles)
                {
                    for(Client client : vehicle.clients)
                    {
                        if(vehicle.timePassed >= client.minutesUntilFlight)
                        {
                            check = false;
                        }
                    }
                }

                if(check)
                {
                    if (!clientsChanged)
                    {
                        newClients = clients;
                    }

                    out.add(new Edge<>(new ProgressiveNode<>(new PathState(newVehicles, newClients, graph, droppingPlace, false, vehicleCreationCost, veiculeCapacity)), costSum));
                }
            }

            done = true;
            for(int value : nextCombination)
            {
                if(value != (places.length-1))
                {
                    done = false;
                }
            }

            increaseArrayCounter(nextCombination, nextCombination.length - 1, places.length - 1);
        } while(!done);

        return out;

        //per state end
    }

    public static void increaseArrayCounter(int[] arr, int index, int maxVal)
    {
        if(index < 0)
        {
            return;
        }

        int val = ++arr[index];

        if(val > maxVal)
        {
            arr[index] = 0;
            increaseArrayCounter(arr, index-1, maxVal);
        }
    }

    public static LinkedList<LinkedList<Place>> decodeStringOfStates(LinkedList<PathState> pathStates)
    {
        LinkedList<LinkedList<Place>> vehicles = new LinkedList<>();
        Map<Integer, LinkedList<Place>> IDtoPath = new HashMap<>();

        int vehicleAmount = 0;
        for(PathState pathState : pathStates)
        {
            int stateVehiclesAmount = pathState.vehicles.size();
            if(stateVehiclesAmount > vehicleAmount)
            {
                vehicleAmount++;
                LinkedList<Place> newVehicle = new LinkedList<>();
                newVehicle.add(pathState.droppingPlace);
                vehicles.add(newVehicle);
                for(Vehicle vehicle : pathState.vehicles)
                {
                    if(!IDtoPath.containsKey(vehicle.ID))
                    {
                        IDtoPath.put(vehicle.ID, vehicles.getLast());
                    }
                }
            }
            else if(stateVehiclesAmount == vehicleAmount)
            {
                for(int i = 0; i < stateVehiclesAmount; i++)
                {
                    Vehicle vehicle = pathState.vehicles.get(i);
                    IDtoPath.get(vehicle.ID).add(vehicle.location);
                }
            }
        }

        return vehicles;
    }

    @Override
    public double getHeuristic(PathState other)
    {
        //ignore other, assume always the same

        double sum = 0;

        ArrayList<Client> clientsInVehicles = new ArrayList<>();
        for(Vehicle vehicle : vehicles)
        {
            clientsInVehicles.addAll(vehicle.clients);
        }
        Set<Place> placesWithClients = new HashSet<>();
        for(Client client : clients)
        {
            if(!(clientsInVehicles.contains(client)))
            {
                placesWithClients.add(client.place);
            }
        }

        Map<Place, Double> map = new HashMap<>();

        for(Node<Place> node : graph.getNodes().values())
        {
            Place place = node.getPayload();
            if(place != droppingPlace)
            {
                for(Edge<Place> edge : node.getAdjacents())
                {
                    if(edge.getDestination().getPayload() == droppingPlace)
                    {
                        map.put(place, edge.getCost());
                    }
                }
            }
        }

        for(Place place : placesWithClients)
        {
            sum += map.get(place);
        }
        for(Vehicle vehicle : vehicles)
        {
            if(vehicle.clients.size() > 0)
            {
                sum += map.get(vehicle.location);
            }
        }

        return sum;
    }

    @Override
    public int hashCode()
    {
        int result = vehicles.hashCode();
        result = 31 * result + clients.hashCode();
        result = 31 * result + (start ? 1 : 0);
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

        PathState pathState = (PathState) o;

        if (start != pathState.start)
        {
            return false;
        }
        if (!vehicles.equals(pathState.vehicles))
        {
            return false;
        }
        return clients.equals(pathState.clients);
    }
}
