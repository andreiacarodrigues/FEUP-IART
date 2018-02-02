import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Interface
{
    static int nextID = 0;
    static GraphViewer gv = null;
    static Graph<Place> graph = new Graph<>();
    static Map<String, Place> identifierToPlace = new HashMap<>();
    static Map<Place, String> placeToIdentifier = new HashMap<>();
    static Map<String, Integer> identifierToNode = new HashMap<>();
    static Map<String, Integer> identifierToEdge = new HashMap<>();
    static Map<String, Pair<String>> edgeIdentifierToNodes = new HashMap<>();
    static String airportIdentifier = null;
    static ArrayList<String> hotelsIdentifiers = new ArrayList<>();
    static ArrayList<Client> clients = new ArrayList<>();
    static Methods minimalPathMethod = Methods.AST;
    static Methods solutionFindingMethod = Methods.AST;
    public enum Methods
    {
        DFS,
        BFS,
        IDS,
        BID,
        GRD,
        UNI,
        AST,
        IDA
    }

    public static void main(String[] args)
    {
        init();

        try
        {
            mainMenu();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //region MainMenu
    public static int mainMenuOptions()
    {
        String str = "";
        str += "1 - Add Node\n";
        str += "2 - Remove Node\n";
        str += "3 - Add Edge\n";
        str += "4 - Remove Edge\n";
        str += "5 - Set Airport\n";
        str += "6 - Add Hotel\n";
        str += "7 - Remove Hotel\n";
        str += "8 - Add Client\n";
        str += "9 - Remove Client\n";
        str += "10- Status\n";
        str += "11- Compute Path\n";
        str += "12- Clear Colors\n";
        str += "13- Save to file\n";
        str += "14- Load from file\n";
        str += "15- Options\n";
        str += "16- Reset graph\n";
        str += "0 - Exit\n";
        str += "Choose your option: ";

        return promptIntInput(str);
    }

    public static void mainMenu() throws IOException
    {
        while(true)
        {
            int input = mainMenuOptions();
            switch(input)
            {
                case 1:
                    addNode();
                    break;
                case 2:
                    removeNode();
                    break;
                case 3:
                    addEdge();
                    break;
                case 4:
                    removeEdge();
                    break;
                case 5:
                    setAirport();
                    break;
                case 6:
                    addHotel();
                    break;
                case 7:
                    removeHotel();
                    break;
                case 8:
                    addClient();
                    break;
                case 9:
                    removeClient();
                    break;
                case 10:
                    status();
                    break;
                case 11:
                    computePath();
                    break;
                case 12:
                    clearColors();
                    break;
                case 13:
                    saveToFile();
                    break;
                case 14:
                    loadFromFile();
                    break;
                case 15:
                    options();
                    break;
                case 16:
                    resetGraph();
                    break;
                case 0:
                    return;
                default:
                    break;
            }
        }
    }
    //endregion

    public static void addNode() throws IOException
    {
        String identifier = null;
        int x;
        int y;

        while(identifier == null)
        {
            identifier = promptIdentifierInput("Type the desired node identifier: ");
            if(identifier.equals("null"))
            {
                return;
            }
            if(identifierToNode.containsKey(identifier))
            {
                System.out.println("Identifier already exists.");
                identifier = null;
            }
        }

        while(true)
        {
            x = promptIntInput("Type the desired node x coordinate: ");
            if(x >= 0)
            {
                break;
            }
        }

        while(true)
        {
            y = promptIntInput("Type the desired node y coordinate: ");
            if(y >= 0)
            {
                break;
            }
        }

        addNodeFunc(identifier, x, y);
        gv.rearrange();
        clearColors();
    }

    public static void addNodeFunc(String identifier, int x, int y) throws IOException
    {
        int id = nextID++;
        Place place = new Place(x, y);
        identifierToPlace.put(identifier, place);
        placeToIdentifier.put(place, identifier);
        identifierToNode.put(identifier, id);
        try
        {
            graph.addNode(place, x, y);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        gv.addNode(id, x, y);
        gv.setVertexLabel(id, identifier);
    }

    public static void removeNode() throws IOException
    {
        String identifier = null;

        while(identifier == null)
        {
            identifier = promptIdentifierInput("Type the desired node identifier: ");
            if(identifier.equals("null"))
            {
                return;
            }
            if(!identifierToNode.containsKey(identifier))
            {
                System.out.println("Identifier doesn't exist.");
                identifier = null;
            }
        }

        removeNodeFunc(identifier);
        gv.rearrange();
    }

    public static void removeNodeFunc(String identifier) throws IOException
    {
        if(airportIdentifier.equals(identifier))
        {
            airportIdentifier = null;
        }
        else if(hotelsIdentifiers.contains(identifier))
        {
            hotelsIdentifiers.remove(identifier);
        }

        Place nodePlace = identifierToPlace.get(identifier);
        placeToIdentifier.remove(nodePlace);
        identifierToPlace.remove(identifier);
        int id = identifierToNode.remove(identifier);
        graph.removeNode(nodePlace);

        for(Map.Entry<String, Pair<String>> entry : edgeIdentifierToNodes.entrySet())
        {
            if(identifier.equals(entry.getValue().left) || identifier.equals(entry.getValue().right))
            {
                int edgeid = identifierToEdge.remove(entry.getKey());
                gv.removeEdge(edgeid);
                edgeIdentifierToNodes.remove(entry.getKey());
            }
        }

        gv.removeNode(id);
    }

    public static void addEdge() throws IOException
    {
        String identifier = null;
        String identifier1 = null;
        String identifier2 = null;
        Double cost = null;

        while(identifier == null)
        {
            identifier = promptIdentifierInput("Type the desired edge identifier: ");
            if(identifier.equals("null"))
            {
                return;
            }
            if(identifierToEdge.containsKey(identifier))
            {
                System.out.println("Identifier already exists.");
                identifier = null;
            }
        }

        boolean valid = false;

        while(!valid)
        {
            while (identifier1 == null)
            {
                identifier1 = promptIdentifierInput("Type the first node identifier: ");
                if(identifier.equals("null"))
                {
                    return;
                }
                if (!identifierToNode.containsKey(identifier1))
                {
                    System.out.println("Node doesn't exist.");
                    identifier1 = null;
                }
            }

            while (identifier2 == null)
            {
                identifier2 = promptIdentifierInput("Type the second node identifier: ");
                if(identifier.equals("null"))
                {
                    return;
                }
                if (!identifierToNode.containsKey(identifier2))
                {
                    System.out.println("Node doesn't exist.");
                    identifier2 = null;
                }
            }

            if(identifier1.equals(identifier2))
            {
                System.out.println("Node identifiers can't be the same.");
            }
            else
            {
                valid = true;
            }
        }

        while(cost == null)
        {
            cost = promptDoubleInput("Type the edge's cost: ");
        }

        addEdgeFunc(identifier, identifier1, identifier2, cost);
        gv.rearrange();
    }

    public static void addEdgeFunc(String identifier, String identifier1, String identifier2, double cost) throws IOException
    {
        int id = nextID++;
        identifierToEdge.put(identifier, id);
        edgeIdentifierToNodes.put(identifier, new Pair<>(identifier1, identifier2));
        Place place1 = identifierToPlace.get(identifier1);
        Place place2 = identifierToPlace.get(identifier2);
        int id1 = identifierToNode.get(identifier1);
        int id2 = identifierToNode.get(identifier2);
        try
        {
            graph.addEdge(place1, place2, cost);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        gv.addEdge(id, id1, id2, 1);
        gv.setEdgeLabel(id, identifier + "," + cost);
    }

    public static void removeEdge() throws IOException
    {
        String identifier = null;

        while(identifier == null)
        {
            identifier = promptIdentifierInput("Type the desired edge identifier: ");
            if(identifier.equals("null"))
            {
                return;
            }
            if(!identifierToEdge.containsKey(identifier))
            {
                System.out.println("Identifier doesn't exist.");
                identifier = null;
            }
        }

        removeEdgeFunc(identifier);
        gv.rearrange();
    }

    public static void removeEdgeFunc(String identifier) throws IOException
    {
        Pair<String> pair = edgeIdentifierToNodes.remove(identifier);
        Place place1 = identifierToPlace.get(pair.left);
        Place place2 = identifierToPlace.get(pair.right);
        graph.removeEdge(place1, place2);

        int id = identifierToEdge.remove(identifier);
        gv.removeEdge(id);
    }

    public static void setAirport() throws IOException
    {
        String identifier = null;

        while(identifier == null)
        {
            identifier = promptIdentifierInput("Type the desired airport identifier: ");
            if(identifier.equals("null"))
            {
                return;
            }
            if(!identifierToNode.containsKey(identifier))
            {
                System.out.println("Identifier doesn't exist.");
                identifier = null;
            }
            if(hotelsIdentifiers.contains(identifier))
            {
                System.out.println("A hotel can't be an airport.");
                identifier = null;
            }
        }

        setAirportFunc(identifier);
        clearColors();
    }

    public static void setAirportFunc(String identifier)
    {
        airportIdentifier = identifier;
    }

    public static void addHotel() throws IOException
    {
        String identifier = null;

        while (identifier == null)
        {
            identifier = promptIdentifierInput("Type the desired hotel identifier: ");
            if (identifier.equals("null"))
            {
                return;
            }
            if (!identifierToNode.containsKey(identifier))
            {
                System.out.println("Identifier doesn't exist.");
                identifier = null;
            }
            if(hotelsIdentifiers.contains(identifier))
            {
                System.out.println("Identifier already a hotel.");
                identifier = null;
            }
            if(airportIdentifier.equals(identifier))
            {
                System.out.println("The airport can't be a hotel.");
            }
        }

        addHotelFunc(identifier);
        clearColors();
    }

    public static void addHotelFunc(String identifier)
    {
        hotelsIdentifiers.add(identifier);
    }

    public static void removeHotel() throws IOException
    {
        String identifier = null;

        while (identifier == null)
        {
            identifier = promptIdentifierInput("Type the desired hotel identifier: ");
            if (identifier.equals("null"))
            {
                return;
            }
        }

        removeHotelFunc(identifier);
        clearColors();
    }

    public static void removeHotelFunc(String identifier)
    {
        hotelsIdentifiers.remove(identifier);
    }

    public static void addClient()
    {
        String identifier = null;
        int minutesUntilFlight;

        while (identifier == null)
        {
            identifier = promptIdentifierInput("Type the desired hotel identifier: ");
            if (identifier.equals("null"))
            {
                return;
            }
            if (!identifierToNode.containsKey(identifier))
            {
                System.out.println("Identifier doesn't exist.");
                identifier = null;
            }
            if(!hotelsIdentifiers.contains(identifier))
            {
                System.out.println("Identifier is not a hotel.");
                identifier = null;
            }
        }

        while(true)
        {
            minutesUntilFlight = promptIntInput("Type the time in which the client must be at the airport: ");
            if(minutesUntilFlight >= 0)
            {
                break;
            }
        }

        addClientFunc(identifier, minutesUntilFlight);
    }

    public static void addClientFunc(String identifier, int minutesUntilFlight)
    {
        Place place = identifierToPlace.get(identifier);
        clients.add(new Client(place, minutesUntilFlight));
    }

    public static void removeClient()
    {
        int id;

        while(true)
        {
            id = promptIntInput("Type the id of the client: ");
            if(id >= 0)
            {
                break;
            }
        }

        removeClientFunc(id);
    }

    public static void removeClientFunc(int id)
    {
        clients.removeIf(client -> client.ID == id);
    }

    public static void status()
    {
        System.out.println("Airport: " + ((airportIdentifier != null) ? airportIdentifier : "None assigned"));
        System.out.println("Hotels:");
        for(String identifier : hotelsIdentifiers)
        {
            System.out.println("\t" + identifier);
            for(Client client : clients)
            {
                if(client.place == identifierToPlace.get(identifier))
                {
                    System.out.println("\t\t " + client.ID + " " + client.minutesUntilFlight);
                }
            }
        }
    }

    public static void computePath() throws IOException
    {
        LinkedList<Place> relevantPlaces = new LinkedList<>();

        for(String hotelIdentifier : hotelsIdentifiers)
        {
            relevantPlaces.add(identifierToPlace.get(hotelIdentifier));
        }
        Place airport = identifierToPlace.get(airportIdentifier);
        relevantPlaces.add(airport);

        LinkedList<Node<Place>> relevantNodes = graph.parseListPayloads(relevantPlaces);

        Graph<Place> minimalDistancesGraph = null;

        try
        {
            minimalDistancesGraph = new GraphParser<Place>().parseGraph(graph, relevantNodes, solutionFindingMethod);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(minimalDistancesGraph == null)
        {
            //TODO
        }

        PathState initialPathState = new PathState(clients, minimalDistancesGraph, airport, 10, 10);

        Graph<PathState> pathFindingGraph = new ProgressiveGraph<>();
        try
        {
            pathFindingGraph.addNode(initialPathState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        LinkedList<PathState> statePath = getPathStatesByMethod(pathFindingGraph, initialPathState, solutionFindingMethod);
        if(statePath == null)
            return;
        LinkedList<LinkedList<Place>> places = PathState.decodeStringOfStates(statePath);
        LinkedList<LinkedList<Place>> truncatedPlaces = new LinkedList<>();
        LinkedList<LinkedList<Place>> expandedPlaces = new LinkedList<>();
        for(LinkedList<Place> list : places)
        {
            LinkedList<Place> truncatedList = new LinkedList<>();
            truncatedList.add(airport);
            Place previous = airport;
            for(int i = 1; i < list.size(); i++)
            {
                Place place = list.get(i);
                if(place != previous)
                {
                    truncatedList.add(place);
                    previous = place;
                }
            }
            truncatedPlaces.add(truncatedList);
        }

        for(LinkedList<Place> list : truncatedPlaces)
        {
            LinkedList<Place> expandedList = new LinkedList<>();
            expandedList.add(airport);
            Place previous = airport;
            for(int i = 1; i < list.size(); i++)
            {
                Place place = list.get(i);
                Node<Place> node = minimalDistancesGraph.getNodes().get(new Node<>(previous));
                for(Edge<Place> edge : node.getAdjacents())
                {
                    if(edge.getDestination().getPayload() == place)
                    {
                        for(Node<Place> pathElement : edge.getPath())
                        {
                            expandedList.add(pathElement.getPayload());
                        }
                    }
                }
                expandedList.add(place);
                previous = place;
            }
            expandedPlaces.add(expandedList);
        }

        for(LinkedList<Place> truncatedPath : truncatedPlaces)
        {
            boolean first = true;
            for(Place place : truncatedPath)
            {
                for(Map.Entry<String, Place> entry : identifierToPlace.entrySet())
                {
                    if(entry.getValue() == place)
                    {
                        if(!first)
                        {
                            System.out.print(" => ");
                        }
                        System.out.print(entry.getKey());
                        first = false;
                    }
                }
            }
            System.out.println();
        }

        clearColors();
        for(LinkedList<Place> expandedPath : expandedPlaces)
        {
            boolean first = true;
            for(Place place : expandedPath)
            {
                for(Map.Entry<String, Place> entry : identifierToPlace.entrySet())
                {
                    if(entry.getValue() == place)
                    {
                        if(!first)
                        {
                            System.out.print(" => ");
                        }
                        System.out.print(entry.getKey());
                        first = false;
                    }
                }
            }
            System.out.println();
        }

        int i = 0;
        boolean atLeastOne = false;
        Map<String, Boolean> switches = new HashMap<>();
        for(String identifier : identifierToNode.keySet())
        {
            switches.put(identifier, false);
        }
        do
        {
            atLeastOne = false;
            for(LinkedList<Place> path : expandedPlaces)
            {
                if(i < path.size())
                {
                    atLeastOne = true;
                    String nodeIdentifier = placeToIdentifier.get(path.get(i));
                    int nodeID = identifierToNode.get(nodeIdentifier);
                    boolean nodeSwitch = switches.get(nodeIdentifier);
                    try
                    {
                        if (nodeSwitch = !nodeSwitch)
                        {
                            gv.setVertexColor(nodeID, Utilities.WHITE);
                        }
                        else
                        {
                            gv.setVertexColor(nodeID, Utilities.BLACK);
                        }
                        gv.rearrange();
                        Thread.sleep(250);
                    }
                    catch(IOException | InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    switches.put(nodeIdentifier, nodeSwitch);
                }
            }
            i++;
        } while(atLeastOne);
    }

    public static void saveToFile()
    {
        String identifier = null;

        while (identifier == null)
        {
            identifier = promptIdentifierInput("Type the desired filename: ");
            if (identifier.equals("null"))
            {
                return;
            }
        }

        Map<Place, String> placeToIdentifier = new HashMap<>();
        for(Map.Entry<String,Place> entry : identifierToPlace.entrySet())
        {
            placeToIdentifier.put(entry.getValue(), entry.getKey());
        }

        try(PrintWriter writer = new PrintWriter(identifier))
        {
            writer.print("N\n");
            for(Node<Place> node : graph.getNodes().values())
            {
                String nodeIdentifier = placeToIdentifier.get(node.getPayload());
                Coordinates nodeCoordinates = node.getCoordinates();
                writer.print(nodeIdentifier + " " + nodeCoordinates.getXValue() + " " + nodeCoordinates.getYValue());
                if(nodeIdentifier.equals(airportIdentifier))
                {
                    writer.print(" A");
                }
                else if(hotelsIdentifiers.contains(nodeIdentifier))
                {
                    writer.print(" H");
                }
                writer.print('\n');
            }
            writer.print("E\n");
            for(Node<Place> node : graph.getNodes().values())
            {
                String nodeIdentifier = placeToIdentifier.get(node.getPayload());
                for(Edge<Place> edge : node.getAdjacents())
                {
                    String destinationIdentifier = placeToIdentifier.get(edge.getDestination().getPayload());
                    Pair<String> pair = new Pair<>(nodeIdentifier, destinationIdentifier);
                    for(Map.Entry<String, Pair<String>> entry : edgeIdentifierToNodes.entrySet())
                    {
                        if(pair.equals(entry.getValue()))
                        {
                            String edgeIdentifier = entry.getKey();
                            writer.print(edgeIdentifier + " " + nodeIdentifier + " " + destinationIdentifier + " " + edge.getCost() + '\n');
                        }
                    }
                }
            }
            writer.print("C\n");
            for(Client client : clients)
            {
                String clientIdentifier = placeToIdentifier.get(client.place);
                writer.print(clientIdentifier + " " + client.minutesUntilFlight + '\n');
            }
            writer.print("X\n");
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static void loadFromFile() throws IOException
    {
        String identifier = null;

        while (identifier == null)
        {
            identifier = promptIdentifierInput("Type the desired filename: ");
            if (identifier.equals("null"))
            {
                return;
            }
        }

        resetGraph();

        try(Scanner scanner = new Scanner(new File(identifier)))
        {
            String input;
            input = scanner.nextLine();
            if(input.equals("N"))
            {
                while(!(input = scanner.nextLine()).equals("E"))
                {
                    Scanner stringScanner = new Scanner(input);
                    String nodeIdentifier;
                    int x;
                    int y;

                    nodeIdentifier = stringScanner.next();
                    x = stringScanner.nextInt();
                    y = stringScanner.nextInt();

                    addNodeFunc(nodeIdentifier, x, y);
                    if(stringScanner.hasNext())
                    {
                        input = stringScanner.next();
                        if(input.equals("A"))
                        {
                            setAirportFunc(nodeIdentifier);
                        }
                        else if(input.equals("H"))
                        {
                            addHotelFunc(nodeIdentifier);
                        }
                    }
                }
                while(!(input = scanner.nextLine()).equals("C"))
                {
                    Scanner stringScanner = new Scanner(input);
                    stringScanner.useLocale(Locale.ENGLISH);
                    String edgeIdentifier;
                    String node1Identifier;
                    String node2Identifier;
                    double cost;

                    edgeIdentifier = stringScanner.next();
                    node1Identifier = stringScanner.next();
                    node2Identifier = stringScanner.next();
                    cost = stringScanner.nextDouble();

                    addEdgeFunc(edgeIdentifier, node1Identifier, node2Identifier, cost);
                }
                while(!(input = scanner.nextLine()).equals("X"))
                {
                    Scanner stringScanner = new Scanner(input);
                    String hotelIdentifier;
                    int minutesUntilFlight;

                    hotelIdentifier = stringScanner.next();
                    minutesUntilFlight = stringScanner.nextInt();

                    addClientFunc(hotelIdentifier, minutesUntilFlight);
                }
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        gv.rearrange();
        clearColors();
    }

    public static int optionsOptions()
    {
        String str = "";
        str += "1- Set Pathfinding algorithm\n";
        str += "2- Set solution-finding algorithm\n";
        str += "0- Back\n";
        str += "Choose your option: ";

        return promptIntInput(str);
    }

    public static void options()
    {
        int input = optionsOptions();
        switch(input)
        {
            case 1:
                setPathfinding();
                break;
            case 2:
                setSolutionFinding();
                break;
            case 0:
                return;
            default:
                return;
        }
    }

    public static int setPathfindingOptions()
    {
        String str = "";
        str += "1- Depth-first\n";
        str += "2- Iterative deepening depth-first\n";
        str += "3- Breadth-first\n";
        str += "4- Greedy best-first\n";
        str += "5- Uniform cost best-first (Djisktra's Algorithm)\n";
        str += "6- A*\n";
        str += "7- Iterative deepening A*\n";
        str += "8- Bidirectional (using breadth-first)\n";
        str += "0- Back\n";
        str += "Choose your option: ";

        return promptIntInput(str);
    }

    public static void setPathfinding()
    {
        Methods method;
        int input = setPathfindingOptions();
        switch(input)
        {
            case 1:
                method = Methods.DFS;
                break;
            case 2:
                method = Methods.IDS;
                break;
            case 3:
                method = Methods.BFS;
                break;
            case 4:
                method = Methods.GRD;
                break;
            case 5:
                method = Methods.UNI;
                break;
            case 6:
                method = Methods.AST;
                break;
            case 7:
                method = Methods.IDA;
                break;
            case 8:
                method = Methods.BID;
                break;
            case 0:
                return;
            default:
                return;
        }

        minimalPathMethod = method;
    }

    public static int setSolutionFindingOptions()
    {
        String str = "";
        str += "1- Depth-first\n";
        str += "2- Iterative deepening depth-first\n";
        str += "3- Breadth-first\n";
        str += "4- Greedy best-first\n";
        str += "5- Uniform cost best-first (Djisktra's Algorithm)\n";
        str += "6- A*\n";
        str += "7- Iterative deepening A*\n";
        str += "0- Back\n";
        str += "Choose your option: ";

        return promptIntInput(str);
    }

    public static void setSolutionFinding()
    {
        Methods method;
        int input = setPathfindingOptions();
        switch(input)
        {
            case 1:
                method = Methods.DFS;
                break;
            case 2:
                method = Methods.IDS;
                break;
            case 3:
                method = Methods.BFS;
                break;
            case 4:
                method = Methods.GRD;
                break;
            case 5:
                method = Methods.UNI;
                break;
            case 6:
                method = Methods.AST;
                break;
            case 7:
                method = Methods.IDA;
                break;
            case 0:
                return;
            default:
                return;
        }

        minimalPathMethod = method;
    }

    public static void resetGraph() throws IOException
    {
        for(Integer edgeIdentifier : identifierToEdge.values())
        {
            gv.removeEdge(edgeIdentifier);
        }

        for(Integer nodeIdentifier : identifierToNode.values())
        {
            gv.removeNode(nodeIdentifier);
        }

        graph = new Graph<>();
        identifierToPlace = new HashMap<>();
        placeToIdentifier = new HashMap<>();
        identifierToNode = new HashMap<>();
        identifierToEdge = new HashMap<>();
        edgeIdentifierToNodes = new HashMap<>();
        airportIdentifier = null;
        hotelsIdentifiers = new ArrayList<>();
        clients = new ArrayList<>();

        gv.rearrange();
    }

    //region Utils
    public static void init()
    {
        try
        {
            gv = new GraphViewer(30, 20, false);
            gv.createWindow(600, 600);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void clearColors() throws IOException
    {
        for(Map.Entry<String, Integer> entry : identifierToNode.entrySet())
        {
            String identifier = entry.getKey();
            int id = entry.getValue();
            if(identifier.equals(airportIdentifier))
            {
                gv.setVertexColor(id, Utilities.BLUE);
            }
            else if(hotelsIdentifiers.contains(identifier))
            {
                gv.setVertexColor(id, Utilities.RED);
            }
            else
            {
                gv.setVertexColor(id, Utilities.YELLOW);
            }
        }
        gv.rearrange();
    }

    public static LinkedList<PathState> getPathStatesByMethod(Graph<PathState> graph, PathState start, Methods method)
    {
        PathState end = PathState.goalState;
        switch(method)
        {
            case AST:
                return graph.aStarAlgorithm(start, end);
            case BFS:
                return graph.breadthFirst(start, end);
            case DFS:
                return graph.depthFirst(start, end);
            case GRD:
                return graph.greedyAlgorithm(start, end);
            case IDA:
                return graph.aStarIterativeDeepeningDepthFirstAlgorithm(start, end);
            case IDS:
                return graph.iterativeDeepeningDepthFirst(start, end);
            case UNI:
                return graph.uniformCostAlgorithm(start, end);
            default:
                return null;
        }
    }

    public static class InputOut<T>
    {
        public final T value;
        public final boolean success;

        public InputOut(T value, boolean success)
        {
            this.value = value;
            this.success = success;
        }
    }

    //region DoublePrompt
    public static Double promptDoubleInput(String text)
    {
        while (true)
        {
            System.out.print(text);
            InputOut<Double> input = getDouble();
            if (input.success)
            {
                return input.value;
            }
            System.out.println("Please enter a number.");
        }
    }

    public static InputOut<Double> getDouble()
    {
        Scanner scanner = new Scanner(System.in);

        if (scanner.hasNextDouble())
        {
            Double input = scanner.nextDouble();
            scanner.nextLine();
            return new InputOut<>(input, true);
        }
        scanner.nextLine();
        return new InputOut<>(null, false);
    }
    //endregion

    //region IdentifierPrompt
    public static String promptIdentifierInput(String text)
    {
        while (true)
        {
            System.out.print(text);
            InputOut<String> input = getIdentifer();
            if (input.success)
            {
                return input.value;
            }
            System.out.println("Please enter letters to form an identifier.");
        }
    }

    public static InputOut<String> getIdentifer()
    {
        Scanner scanner = new Scanner(System.in);

        Pattern pattern = Pattern.compile("^\\w+$");

        if (scanner.hasNext(pattern))
        {
            String input = scanner.next(pattern);
            scanner.nextLine();
            return new InputOut<>(input, true);
        }
        scanner.nextLine();
        return new InputOut<>(null, false);
    }
    //endregion

    //region PromptInt
    public static int promptIntInput(String text)
    {
        while (true)
        {
            System.out.print(text);
            int input = getInt();
            if (input >= 0)
            {
                return input;
            }
            System.out.println("Please enter a number.");
        }
    }

    public static int getInt()
    {
        Scanner scanner = new Scanner(System.in);

        if (scanner.hasNextInt())
        {
            return scanner.nextInt();
        }
        scanner.nextLine();
        return -1;
    }
    //endregion
    //endregion
}
