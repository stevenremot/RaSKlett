package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Retourne le deuxième élément d'une paire
 * @author steven
 *
 */
public class Tail implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
        Node node1 = registry.getNode();

        Node argNode = node1.getArgument().getNode().getLastNode();

        if(argNode == null) {
            throw new CompilerException("tail attend en argument une expression contenant un vec et ses valeurs");
        }

        Node kiNode = new Node(
            NodeFieldFactory.create(new K()),
            NodeFieldFactory.create(new I())
        );

        Node leadNode = new Node(
                NodeFieldFactory.create(argNode),
                NodeFieldFactory.create(kiNode)
        );

        if(node1.getNextNode() != null) {
            node1.getNextNode().setFunction(NodeFieldFactory.create(leadNode));
        }

        registry.setNode(leadNode);

        return true;
	}

	@Override
	public String getName() {
		return "tail";
	}

}
