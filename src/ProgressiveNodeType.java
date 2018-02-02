import java.util.Collection;

public interface ProgressiveNodeType<Type extends HeuristicType<Type>>
{
    Collection<Edge<Type>> generateChildrenStates();
}
