
public class Client implements Comparable<Client>
{
    static private int nextID = 0;

    public final int ID = nextID++;

    public final Place place;

    public final int minutesUntilFlight;

    public Client(Place place, int minutesUntilFlight)
    {
        this.place = place;
        this.minutesUntilFlight = minutesUntilFlight;
    }

    @Override
    public int compareTo(Client o)
    {
        if (minutesUntilFlight < o.minutesUntilFlight)
        {
            return -1;
        }
        else if (minutesUntilFlight > o.minutesUntilFlight)
        {
            return 1;
        }
        else
        {
            if(ID < o.ID)
            {
                return -1;
            }
            else if(ID > o.ID)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
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

        Client client = (Client) o;

        return ID == client.ID;
    }

    @Override
    public int hashCode()
    {
        return ID;
    }
}
