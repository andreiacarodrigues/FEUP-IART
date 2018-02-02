public class Place implements HeuristicType<Place>
{
    static private int nextID = 0;

    public final int ID = nextID++;

    public Place(int x, int y)
    {
        this.coordinates = new Coordinates(x, y);
    }

    public final Coordinates coordinates;

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }

        if(!(obj instanceof Place))
        {
            return false;
        }

        Place other = (Place) obj;

        return (other.ID == this.ID);
    }

    @Override
    public double getHeuristic(Place other)
    {
        return this.coordinates.getDistance(other.coordinates);
    }

    @Override
    public int hashCode()
    {
        return ID;
    }
}
