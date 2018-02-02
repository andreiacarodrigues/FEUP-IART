
public class ProgressiveGraph<Type extends ProgressiveNodeType<Type> & HeuristicType<Type>> extends Graph<Type>
{
    public void addNode(Type payload) throws Exception {
        Node<Type> node = new ProgressiveNode<>(payload);

        if(nodes.get(node) != null)
        {
            throw new Exception("Duplicate node.");
        }

        nodes.put(node, node);
    }
}
