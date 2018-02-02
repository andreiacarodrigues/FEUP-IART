import jdk.nashorn.internal.runtime.Undefined;

import java.util.*;

public class Graph<Type extends HeuristicType<Type>>
{
    //region Private members

    //region Edge class
    /*public class Edge
    {
        private LinkedList<Node> path = new LinkedList<>();
        private Node destination;
        private int cost;

        public Edge(Node destination, int cost) {
            this.setDestination(destination);
            this.setCost(cost);
        }

        public Node getDestination()
        {
            return destination;
        }

        public int getCost()
        {
            return cost;
        }

        private void setDestination(Node destination)
        {
            this.destination = destination;
        }

        private void setCost(int cost)
        {
            this.cost = cost;
        }

        public LinkedList<Node> getPath()
        {
            return path;
        }

        public void setPath(LinkedList<Node> path) {
           this.path = path;
        }
    }
    //endregion

    //region Node class
    /*public class Node
    {
        //region Private members
        private int id;
        private Type payload;
        private double cost;
        private double fCost;
        private double heuristicValue;
        private Map<Edge, Edge> adjacents = new HashMap<>();

        private Coordinates coordinates;

        private void setPayload(Type payload)
        {
            this.payload = payload;
        }

        private void setCoordinates(Coordinates c){ coordinates = c; }

        //endregion

       public Node(Type payload, int x, int y) {
           setPayload(payload);
           setCoordinates(new Coordinates(x,y));
       }

        public void setID(int id)
        {
            this.id = id;
        }
        public int getID()
        {
            return id;
        }

        public Node(Type payload) {
            setPayload(payload);
        }

       public double getHeuristicValue() {
           return heuristicValue;
       }

        public void setHeuristicValue(Node goal) {
            this.heuristicValue = this.coordinates.getDistance(goal.getCoordinates());

        }

        public Coordinates getCoordinates(){ return coordinates; }

       public double getCost() {
           return cost;
       }

       public void setCost(double cost) {
           this.cost = cost;
       }

        public double getFCost() {
            return fCost;
        }

        public void setFCost(double fCost) {
            this.fCost = fCost;
        }

       public void addAdjacent(Edge edge) throws Exception {
           Edge oldEdge = this.adjacents.get(edge);

           if((oldEdge != null) && (oldEdge.getCost() <= edge.getCost()))
           {
               throw new Exception("Better edge exists.");
           }

           this.adjacents.put(edge, edge);
       }

       public Collection<Edge> getAdjacents()
       {
           return adjacents.values();
       }

        public int getEdgeCost(Node destination) throws Exception {
            Collection<Edge> adjacents = this.getAdjacents();
            for (Edge edge : adjacents) {
               if(destination.getPayload() == edge.getDestination().getPayload())
                   return edge.getCost();
            }
            throw new Exception("Unexistent Edge");
        }

        public Edge getEdge(Node destination) throws Exception {
            Collection<Edge> adjacents = this.getAdjacents();
            for (Edge edge : adjacents) {
                if(destination.getPayload() == edge.getDestination().getPayload())
                    return edge;
            }
            throw new Exception("Unexistent Edge");
        }

       public Type getPayload()
       {
           return payload;
       }

       @Override
       public boolean equals(Object obj) {
           if(this == obj)
           {
               return true;
           }

           if(!(obj instanceof Graph<?>.Node))
           {
               return false;
           }

           Graph<?>.Node objNode = (Graph<?>.Node) obj;

           return objNode.getPayload().equals(this.getPayload());
       }

       @Override
       public int hashCode()
       {
           return this.getPayload().hashCode();
       }
   }*/
   //endregion

   protected Map<Node<Type>, Node<Type>> nodes = new HashMap<>();
   //endregion

    //region Graph Functions
    public Graph() {}

    public void addNode(Type payload) throws Exception {
       Node<Type> node = new Node<>(payload);

       if(nodes.get(node) != null)
       {
           throw new Exception("Duplicate node.");
       }

       nodes.put(node, node);
   }

    public void addNode(Type payload, int x, int y) throws Exception {
        Node<Type> node = new Node<>(payload, x, y);

        if(nodes.get(node) != null)
        {
            throw new Exception("Duplicate node.");
        }

        nodes.put(node, node);
    }

   public void addNode(Node<Type> node) throws Exception {
        if(nodes.get(node) != null)
        {
            throw new Exception("Duplicate node.");
        }

        nodes.put(node, node);
    }

    public Node<Type> getNode(Type payload) throws Exception {
        Node<Type> node = new Node<>(payload);

        if(nodes.get(node) == null)
        {
            throw new Exception("Unexistent node.");
        }
        return nodes.get(node);
    }

    public  Map<Node<Type>, Node<Type>> getNodes(){
        return nodes;
    }

   public Edge<Type> addEdge(Type from, Type to, double cost) throws Exception {
       Node<Type> fromNode = new Node<>(from);
       fromNode = nodes.get(fromNode);
       if(fromNode == null)
       {
           throw new Exception("From doesn't exist.");
       }

       Node<Type> toNode = new Node<>(to);
       toNode = nodes.get(toNode);
       if(toNode == null)
       {
           throw new Exception("To doesn't exist.");
       }

       Edge<Type> edge = new Edge<>(toNode, cost);
       fromNode.addAdjacent(edge);

       return edge;
   }

   public boolean removeNode(Type payload)
   {
       Node<Type> node = new Node<>(payload);
       node = nodes.get(node);
       if(node == null)
       {
           return false;
       }

       for(Node<Type> other : nodes.values())
       {
           for(Edge<Type> edge : other.getAdjacents())
           {
               if(edge.getDestination() == node)
               {
                   other.removeEdge(edge);
               }
           }
       }

       return true;
   }

   public void removeEdge(Type start, Type end)
   {
       Node<Type> node = new Node<>(start);
       for(Edge<Type> edge : node.getAdjacents())
       {
           if(edge.getDestination().getPayload().equals(end))
           {
               node.removeEdge(edge);
           }
       }
   }

    public void print(){
        Set<Node<Type>> keySet = nodes.keySet();
        for(Node<Type> key: keySet) {
            System.out.println("Node Payload: " + key.getPayload());

            Node<Type> node = nodes.get(key);
            System.out.println("ID: " + node.getID());
            Collection<Edge<Type>> adjacents = node.getAdjacents();
            for (Edge edge : adjacents){
                System.out.println("EdgeDest + EdgeCost: " + edge.getDestination().getPayload() + " " + edge.getCost());
            }
            System.out.println("--------------------------------------");
        }
    }

    //endregion

    //region Graph Algorithms

    /* Depth First Search */
    public LinkedList<Type> depthFirst(Type start, Type end) {
        LinkedList<Type> result = new LinkedList<>();
        Set<Node<Type>> visited = new HashSet<>();
        Stack<Node<Type>> toVisit = new Stack<>();
        Node<Type> goal = new Node<>(end);
        toVisit.add(nodes.get(new Node<>(start)));

        while(!toVisit.empty())
        {
            Node<Type> current = toVisit.pop();
            if(current == null)
            {
                result.removeLast();
                continue;
            }
            result.addLast(current.getPayload());
            if(current.equals(goal))
            {
                break;
            }

            if(!visited.contains(current))
            {
                visited.add(current);
                Collection<Edge<Type>> adjacents = current.getAdjacents();
                toVisit.push(null);
                for (Edge<Type> edge : adjacents)
                {
                    if(!visited.contains(edge.getDestination())) {
                        toVisit.push(edge.getDestination());
                    }
                }
            }
        }

        return result;
    }

    /* Breadth First Search */
    public LinkedList<Type> breadthFirst(Type start, Type end) {
        LinkedList<Type> result = new LinkedList<>();

        Set<Node<Type>> visited = new HashSet<>();
        Queue<Node<Type>> toVisit = new LinkedList<>();
        // Node, Parent
        Map<Node<Type>, Node<Type>> path = new HashMap<>();

        Node<Type> goal = new Node<>(end);

        toVisit.add(nodes.get(new Node<>(start)));
        visited.add(nodes.get(new Node<>(start)));
        path.put(nodes.get(new Node<>(start)), null);

        while(toVisit.size()!=0)
        {
            Node<Type> current = toVisit.poll();

            if(current.equals(goal))
                break;

            for(Edge<Type> edge : current.getAdjacents())
            {
               Node<Type> n = edge.getDestination();
               if(!visited.contains(n))
               {
                   path.put(n, current);
                   visited.add(n);
                   toVisit.add(n);
               }
            }
        }
        computePath(path, result, goal);
        return result;
    }

    /* Iterative Deepening Depth First Search */
    public LinkedList<Type> iterativeDeepeningDepthFirst(Type start, Type end) {
        LinkedList<Type> result = new LinkedList<>();
        Map<Node<Type>, Node<Type>> path;
        Node<Type> goal = new Node<>(end);
        Node<Type> startNode = nodes.get(new Node<>(start));
        int maxDepth = Integer.MAX_VALUE;
        MutableBoolean found = new MutableBoolean(false);

        for(int i = 0; i < maxDepth; i++)
        {
            path = DLS(startNode, end, i, found);

            if(path != null) {
                path.put(nodes.get(new Node<>(start)), null);
                computePath(path, result, goal);
            }

            if(found.getValue())
                break;
        }
        return result;
    }

    /* Bidirectional Search */
    public LinkedList<Type> bidirectionalSearch(Type start, Type end) {
        LinkedList<Type> result = new LinkedList<>();
        Map<Node<Type>, Node<Type>> path = new HashMap<>();
        boolean found = false;

        Queue<Node<Type>> queueA = new LinkedList<>();
        Queue<Node<Type>> queueB = new LinkedList<>();
        Set<Node<Type>> visitedA = new HashSet<>();
        Set<Node<Type>> visitedB = new HashSet<>();

        Node<Type> root = nodes.get(new Node<>(start));
        Node<Type> goal = new Node<>(end);

        visitedA.add(root);
        visitedB.add(goal);

        queueA.add(root);
        queueB.add(goal);

        while(!queueA.isEmpty() && !queueB.isEmpty()) {
            if(!queueA.isEmpty()) {
                Node<Type> a = queueA.remove();
                if(a.equals(goal) || visitedB.contains(a)) {
                    found = true;
                }
                Collection<Edge<Type>> adjacents = a.getAdjacents();
                for (Edge<Type> edge : adjacents) {
                    Node<Type> dest = edge.getDestination();
                    path.put(dest, a);
                   //------
                    if(visitedB.contains(dest)) {
                        found = true;
                    }
                    else
                    {
                        visitedA.add(dest);
                        queueA.add(dest);
                    }
                }
                if(found) {
                    path.put(nodes.get(new Node<>(start)), null);
                    computePath(path, result, goal);
                    break;
                }
            }

            if(!queueB.isEmpty()) {
                Node<Type> b = queueB.remove();
                if(b.equals(root) || visitedA.contains(b)) {
                    found = true;
                }
                LinkedList<Node<Type>> parents = findParents(b);
                for (Node<Type> node : parents) {
                    path.put(b, node);

                    if(visitedA.contains(node)) {
                        found = true;
                    }
                    else
                    {
                        visitedB.add(node);
                        queueB.add(node);
                    }
                }
                if(found) {
                    path.put(nodes.get(new Node<>(start)), null);
                    computePath(path, result, goal);
                    break;
                }
            }

        }

        return result;
    }

    /* Greedy Algorithm */
    public LinkedList<Type> greedyAlgorithm(Type start, Type end){
        PriorityQueue<Node<Type>> queue = new PriorityQueue<>(new NodeHeuristicComparator(end));
        Map<Node<Type>, Node<Type>> path = new HashMap<>(); // Node, Parent
        LinkedList<Type> result = new LinkedList<>();

        queue.add(nodes.get(new Node<>(start)));

        while(!queue.isEmpty()) {
            Node<Type> current = queue.poll();

            if (current.getPayload().equals(end)) {
                computePath(path, result, new Node<>(end));
                return result;
            }

            Collection<Edge<Type>> adjacents = current.getAdjacents();
            for (Edge<Type> edge : adjacents)
            {
                Node<Type> destination = edge.getDestination();

                queue.add(destination);
                path.put(destination, current);
            }
        }

        return null;

    }

    /* Uniform Cost Algorithm */
    public LinkedList<Type> uniformCostAlgorithm(Type start, Type end){

        PriorityQueue<Node<Type>> queue = new PriorityQueue<>(new NodeCostComparator());
        Map<Node<Type>, Node<Type>> path = new HashMap<>(); // Node, Parent
        Map<Node<Type>, Node<Type>> relate = new HashMap<>(); // Template, Real
        LinkedList<Type> result = new LinkedList<>();

        Node<Type> node = nodes.get(new Node<>(start));
        node.setCost(0);
        queue.add(node);

        while(!queue.isEmpty()) {
            Node<Type> current = queue.poll();

            if (current.getPayload().equals(end)) {
                computePath(path, result, new Node<>(end));
                return result;
            }

            Collection<Edge<Type>> adjacents = current.getAdjacents();
            for (Edge<Type> edge : adjacents)
            {
                Node<Type> destination = edge.getDestination();
                Node<Type> real = relate.get(destination);
                if(real == null)
                {
                    destination.setCost(current.getCost() + edge.getCost());
                    relate.put(destination, destination);
                    path.put(destination, current);
                    queue.add(destination);
                }
                else
                {
                    if (current.getCost() + edge.getCost() < real.getCost())
                    {
                        queue.remove(real);
                        real.setCost(current.getCost() + edge.getCost());
                        path.put(real, current);
                        queue.add(real);
                    }
                }
            }
        }

        return null;
    }

    /* A* Algorithm */
    public LinkedList<Type> aStarAlgorithm(Type start, Type end){
        LinkedList<Node<Type>> closedSet = new LinkedList<>();
        PriorityQueue<Node<Type>> openSet = new PriorityQueue<>(new NodeCostHeuristicComparator());
        Map<Node<Type>, Node<Type>> path = new HashMap<>(); // Node, Parent
        Map<Node<Type>, Node<Type>> relate = new HashMap<>(); // Template, Real
        LinkedList<Type> result = new LinkedList<>();

        Node<Type> startNode = nodes.get(new Node<>(start));
        startNode.setCost(0);
        startNode.setFCost(startNode.getHeuristicValue(end));
        openSet.add(startNode);

        while(!openSet.isEmpty()) {
            Node<Type> current = openSet.poll();

            if (current.getPayload().equals(end)) {
                computePath(path, result, new Node<>(end));
                return result;
            }

            openSet.remove(current);
            closedSet.add(current);

            Collection<Edge<Type>> adjacents = current.getAdjacents();
            for (Edge<Type> edge : adjacents)
            {
                Node<Type> destination = edge.getDestination();

                if(closedSet.contains(destination))
                    continue;

                boolean added = false;
                if(!openSet.contains(destination))
                {
                    added = true;
                    destination.setCost(current.getCost() + edge.getCost());
                    destination.setFCost(destination.getCost() + destination.getHeuristicValue(end));
                    openSet.add(destination);
                    relate.put(destination, destination);
                }
                else
                {
                    Node<Type> real = relate.get(destination);
                    if (current.getCost() + edge.getCost() < real.getCost())
                        continue;
                }

                path.put(destination, current);

                if(!added)
                {
                    Node<Type> real = relate.get(destination);
                    openSet.remove(real);
                    real.setCost(current.getCost() + edge.getCost());
                    real.setFCost(real.getCost() + real.getHeuristicValue(end));
                    openSet.add(real);
                }
            }
        }
        return null;
    }

    /* A* Iterative Deepening Depth First Search */
    public LinkedList<Type> aStarIterativeDeepeningDepthFirstAlgorithm(Type start, Type end){
        LinkedList<Type> result = new LinkedList<>();
        Map<Node<Type>, Node<Type>> path = new HashMap<>();
        Node<Type> goal = new Node<>(end);
        Node<Type> current;

        current = nodes.get(new Node<>(start));

       if(current == null) // didn't find the start node in the list of nodes of the graph
           return null;

       double bound = current.getHeuristicValue(end);
       current.setCost(0);

       while(true)
       {
           double t = aStarIterativeSearch(current, bound, goal, path);
           if(t == -1)
           {
               // FOUND
               computePath(path, result, goal);
               return result;
           }
           if(t == -2)
           {
               // NOT FOUND
               return null;
           }
           bound = t;
       }
    }

    //region Auxiliary Algorithm Functions

    /* Auxiliary Function that Computes Path According to (node, parent) Map */
    private void computePath(Map<Node<Type>,Node<Type>> path, LinkedList<Type> result, Node<Type> goal){
        ArrayList<Node<Type>> keys = new ArrayList<>(path.keySet());
        ArrayList<Node<Type>> values = new ArrayList<>(path.values());
        int indexOfChild = keys.indexOf(goal);
        if(indexOfChild == -1) {
            result = null;
            return;
        }
        Node<Type> parent = values.get(indexOfChild);


        result.add(goal.getPayload());

        while(indexOfChild != -1 && parent != null)
        {
            result.addFirst(parent.getPayload());
            indexOfChild = keys.indexOf(parent);
            if(indexOfChild == -1)
            {
                continue;
            }
            parent = values.get(indexOfChild);
        }
    }

    /* Iterative Deepening Depth First Search Auxiliar Function */
    private Map<Node<Type>, Node<Type>> DLS(Node<Type> start, Type end, int depth, MutableBoolean found) {
        Map<Node<Type>, Node<Type>> path = new HashMap<>();
        Node<Type> goal = new Node<>(end);

        if(start.equals(goal)) {
            found.setValue(true);
            return path;
        }
        if(depth == 0) {
            return null;
        }
        if(depth > 0)
        {
            Collection<Edge<Type>> adjacents = start.getAdjacents();
            for (Edge<Type> edge : adjacents)
            {
                Node<Type> dest = edge.getDestination();
                path.put(dest, start);

                if(dest.equals(goal)) {
                    found.setValue(true);
                    return path;
                }

                Map<Node<Type>, Node<Type>> res = DLS(dest, end, depth - 1, found);
                if((res != null) && (found.getValue())) {
                    Set<Node<Type>> keySet = res.keySet();
                    for(Node<Type> key: keySet){
                        Node<Type> parent = res.get(key);
                        path.put(key, parent);
                    }
                    return path;
                }
            }
        }
        else
        {
            System.out.println("Error, negative length");
        }
        return null;
    }

    /* Bidirectional Search Auxiliar Function - to get the node parents of a given node */
    private LinkedList<Node<Type>> findParents(Node<Type> goal){
        LinkedList<Node<Type>> result = new LinkedList<>();
        Set<Node<Type>> keySet = nodes.keySet();
        for (Node<Type> key : keySet) {
            Node<Type> current = nodes.get(key);
            Collection<Edge<Type>> adjacents = current.getAdjacents();
            for (Edge<Type> edge : adjacents)
            {
                if(edge.getDestination().equals(goal)) {
                    result.add(current);
                }
            }

        }
        return result;
    }

    /* A* Iterative Deepening Depth First Search Auxiliary Function */
    private double aStarIterativeSearch(Node<Type> current, double bound, Node<Type> goal,  Map<Node<Type>, Node<Type>> path) {
        double fCost = current.getCost() + current.getHeuristicValue(goal.payload);
        if(fCost > bound)
            return fCost;
        if(current.getPayload().equals(goal.getPayload()))
            return -1;

        double min = Double.POSITIVE_INFINITY;

        Collection<Edge<Type>> adjacents = current.getAdjacents();
        for (Edge<Type> edge : adjacents)
        {
            Node<Type> destination = edge.getDestination();
            destination.setCost(current.getCost() + edge.getCost());

            path.put(destination, current);

            double t = aStarIterativeSearch(destination, bound, goal, path);

            if(t == -1)
            {
                // FOUND
                return -1;
            }
            if(t < min)
            {
                // NOT FOUND AND ITS A BETTER PATH
                min = t;
            }
        }
        return min;
    }

    //endregion

    //region Node Comparators

    /* Comparator for the Uniform Cost Algorithm */
    class NodeCostComparator implements Comparator<Node<Type>> {
        @Override
        public int compare(Node<Type> n1, Node<Type> n2) {
            if(n1.cost < n2.cost)
                return -1;
            if(n1.cost > n2.cost)
                return 1;
            return 0;
        }
    }

    /* Comparator for the Greedy Algorithm */
    class NodeHeuristicComparator implements Comparator<Node<Type>> {

        private Type goal;

        public NodeHeuristicComparator(Type goal)
        {
            this.goal = goal;
        }

        @Override
        public int compare(Node<Type> n1, Node<Type> n2) {
            if (n1.getHeuristicValue(goal) < n2.getHeuristicValue(goal))
                return -1;
            if (n1.getHeuristicValue(goal) > n2.getHeuristicValue(goal))
                return 1;
            return 0;
        }
    }

    /* Comparator for the A* Algorithm */
    class NodeCostHeuristicComparator implements Comparator<Node<Type>> {
        @Override
        public int compare(Node<Type> n1, Node<Type> n2) {
            if (n1.getFCost() < n2.getFCost())
                return -1;
            if (n1.getFCost() > n2.getFCost())
                return 1;
            return 0;
        }
    }

    //endregion

    //endregion

    /* Auxiliar function so that a graph can give back a list of nodes with the original characteristics (except adjacents info)
     * based on a list with their payloads */
    public LinkedList<Node<Type>> parseListPayloads(LinkedList<Type> payloads) {
        LinkedList<Node<Type>> result = new LinkedList<>();

        for(int i = 0; i < payloads.size(); i++)
        {
            Node<Type> n = new Node<>(payloads.get(i));
            n.setCost(nodes.get(n).getCost());
            n.setFCost(nodes.get(n).getFCost());
            result.add(n);
        }
        return result;
    }
}



