import java.util.Collection;

public class ProgressiveNode<Type extends ProgressiveNodeType<Type> & HeuristicType<Type>> extends Node<Type>
{
    public ProgressiveNode(Type payload) {
        super(payload);
    }

    @Override
    public Collection<Edge<Type>> getAdjacents()
    {
        return payload.generateChildrenStates();
    }
}
