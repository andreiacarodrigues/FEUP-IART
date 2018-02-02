import java.util.LinkedList;

public class Edge<Type extends HeuristicType<Type>>
{
    public LinkedList<Node<Type>> path = new LinkedList<>();
    public Node<Type> destination;
    public double cost;

    public Edge(Node<Type> destination, double cost) {
        this.setDestination(destination);
        this.setCost(cost);
    }

    public Node<Type> getDestination()
    {
        return destination;
    }

    public double getCost()
    {
        return cost;
    }

    public void setDestination(Node<Type> destination)
    {
        this.destination = destination;
    }

    public void setCost(double cost)
    {
        this.cost = cost;
    }

    public LinkedList<Node<Type>> getPath()
    {
        return path;
    }

    public void setPath(LinkedList<Node<Type>> path) {
        this.path = path;
    }
}