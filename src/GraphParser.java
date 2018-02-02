import java.util.*;

public class GraphParser<Type extends HeuristicType<Type>> {

    /* Function to return a more simple graph based on a bigger one, keeping only the nodes of interest presents on the
     * parameter list toVisit, and computes the path cost in the original graph to be simplified in the new one */
    private LinkedList<Type> getPayloads(Graph<Type> graph, Type start, Type end, Interface.Methods method)
    {
        switch(method)
        {
            case AST:
                return graph.aStarAlgorithm(start, end);
            case BFS:
                return graph.breadthFirst(start, end);
            case BID:
                return graph.bidirectionalSearch(start, end);
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

    public Graph<Type> parseGraph(Graph<Type> graph, LinkedList<Node<Type>> toVisit, Interface.Methods method) throws Exception {
        Graph<Type> parsedGraph = new Graph<>();

        for(int i = 0; i < toVisit.size(); i++) {
            Node<Type> n = toVisit.get(i);
            try{parsedGraph.getNode(n.getPayload());}
            catch(Exception e) {
                n.setID(i);
                parsedGraph.addNode(n);
            }
        }

        for(int i = 0; i < toVisit.size(); i++) {
            Node<Type> start = toVisit.get(i);

            for(int k = 0; k < toVisit.size(); k++)
            {
                Node<Type> destination = toVisit.get(k);
                if (start.getPayload() != destination.getPayload())
                {
                    LinkedList<Type> payloads = graph.aStarAlgorithm(start.getPayload(), destination.getPayload());
                    if(payloads != null) {
                        LinkedList<Node<Type>> result = graph.parseListPayloads(payloads);

                        int totalCost = 0;
                        for (int j = 0; j < result.size() - 1; j++) {
                            totalCost += graph.getNode(result.get(j).getPayload()).getEdgeCost(result.get(j + 1));
                        }

                        Edge<Type> edge = parsedGraph.addEdge(start.getPayload(), destination.getPayload(), totalCost);

                       result.remove(start);
                       result.remove(destination);
                       edge.setPath(result);
                    }
                }
            }
        }
        return parsedGraph;
    }

    public LinkedList<Node<Type>> computePath(Graph<Type> graph, Type end) throws Exception {
        Map<Node<Type>,Node<Type>> nodes = graph.getNodes();
        int numberOfNodes = nodes.size();
        Set<Node<Type>> keySet = nodes.keySet();
        LinkedList<Node<Type>> path = new LinkedList<>();
        Node<Type> endNode = null;

        double dist[][] = new double[numberOfNodes][numberOfNodes];

        for(Node<Type> key: keySet) {
            Node<Type> node = nodes.get(key);
            int node1ID = node.getID();

            if(node.getPayload() == end)
                endNode = node;

            Collection<Edge<Type>> adjacents = node.getAdjacents();
            for (Edge<Type> edge : adjacents) {
                Node<Type> destination = edge.getDestination();
                int node2ID = destination.getID();
                dist[node1ID][node2ID] = edge.getCost();
            }
        }

      /* for(int i = 0; i < numberOfNodes; i++)
        {
            for(int j = 0; j < numberOfNodes; j++)
            {
                System.out.print(dist[i][j] + " ");
            }
            System.out.println();
        }*/

        int[] visited = new int[numberOfNodes+ 1];

        Stack<Integer> stack = new Stack<>();
        stack.push(endNode.getID());
        visited[endNode.getID()] = 1;
        path.add(endNode);
        numberOfNodes--;

        int element, dst = 0, i;

        double min;
        boolean minFlag = false;

        while(!stack.isEmpty())
        {
            element = stack.peek();
            i = 1;
            min = Double.POSITIVE_INFINITY;

            while(i <= numberOfNodes){
                if(dist[element][i] >0 && visited[i] == 0){
                    if(min > dist[element][i]){
                        min =  dist[element][i];
                        dst = i;
                        minFlag = true;
                    }
                }
                i++;
            }
            if(minFlag)
            {
                visited[dst] = 1;
                stack.push(dst);

                Node<Type> startNode = null;
                Node<Type> destinationNode = null;

                for(Node<Type> key: keySet) {
                    Node<Type> node = nodes.get(key);
                    if (node.getID() == element) {
                        startNode = node;
                    }
                    if (node.getID() == dst) {
                        destinationNode = node;
                    }
                }

                LinkedList<Node<Type>> originalGraphPath = startNode.getEdge(destinationNode).getPath();
                for(int k = 0; k < originalGraphPath.size(); k++) {
                    path.add(originalGraphPath.get(k));
                }
                path.add(destinationNode);

                minFlag = false;
                continue;
            }
            stack.pop();
        }
        Node<Type> startNode = null;
        for(Node<Type> key: keySet) {
            Node<Type> node = nodes.get(key);
            if (node.getID() == dst) {
                startNode = node;
            }
        }

        LinkedList<Node<Type>> originalGraphPath = startNode.getEdge(endNode).getPath();
        for(int k = 0; k < originalGraphPath.size(); k++) {
            path.add(originalGraphPath.get(k));
        }
        path.add(endNode);
        return path;
    }

}
