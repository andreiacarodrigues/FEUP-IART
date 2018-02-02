/**
 * Created by andre on 31/03/2017.
 */
public class MutableBoolean
{
    boolean value;

    public MutableBoolean(boolean bool)
    {
        value = bool;
    }

    public void setValue(boolean bool)
    {
        value = bool;
    }

    public boolean getValue()
    {
        return value;
    }
}
