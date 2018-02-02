import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Node<Type extends HeuristicType<Type>>
{
    //region Private members
    public int id;
    public Type payload;
    public double cost;
    public double fCost;
    public double heuristicValue = 0;
    public Map<Edge<Type>, Edge<Type>> adjacents = new HashMap<>();
    public final Coordinates coords;

    public void setPayload(Type payload)
    {
        this.payload = payload;
    }

    //endregion

    public Node(Type payload)
    {
        this(payload,0,0);
    }

    public Node(Type payload, int x, int y)
    {
        setPayload(payload);
        coords = new Coordinates(x, y);
    }

    public Coordinates getCoordinates()
    {
        return coords;
    }

    public void setID(int id)
    {
        this.id = id;
    }
    public int getID()
    {
        return id;
    }

    public double getHeuristicValue(Type other)
    {
        return payload.getHeuristic(other);
    }

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

    public void addAdjacent(Edge<Type> edge) throws Exception {
        Edge<Type> oldEdge = this.adjacents.get(edge);

        if((oldEdge != null) && (oldEdge.getCost() <= edge.getCost()))
        {
            throw new Exception("Better edge exists.");
        }

        this.adjacents.put(edge, edge);
    }

    public Collection<Edge<Type>> getAdjacents()
    {
        return adjacents.values();
    }

    public double getEdgeCost(Node<Type> destination) throws Exception {
        Collection<Edge<Type>> adjacents = this.getAdjacents();
        for (Edge<Type> edge : adjacents) {
            if(destination.getPayload() == edge.getDestination().getPayload())
                return edge.getCost();
        }
        throw new Exception("Unexistent Edge");
    }

    public Edge<Type> getEdge(Node<Type> destination) throws Exception {
        Collection<Edge<Type>> adjacents = this.getAdjacents();
        for (Edge<Type> edge : adjacents) {
            if(destination.getPayload() == edge.getDestination().getPayload())
                return edge;
        }
        throw new Exception("Unexistent Edge");
    }

    public void removeEdge(Edge<Type> edge)
    {
        this.adjacents.remove(edge);
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

        if(!(obj instanceof Node))
        {
            return false;
        }

        Node objNode = (Node) obj;

        return objNode.getPayload().equals(this.getPayload());
    }

    @Override
    public int hashCode()
    {
        return this.getPayload().hashCode();
    }
}