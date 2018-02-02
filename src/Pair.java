
public class Pair<T>
{
    public final T left;
    public final T right;

    public Pair(T left, T right)
    {
        this.left = left;
        this.right = right;
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

        Pair<?> pair = (Pair<?>) o;

        if (!left.equals(pair.left))
        {
            return false;
        }
        return right.equals(pair.right);
    }

    @Override
    public int hashCode()
    {
        int result = left.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }
}
