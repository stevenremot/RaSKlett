package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Combinateur retournant le premier élément d'une paire
 * 
 * @author steven
 *
 */
public class Head implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
		Node node1 = registry.getNode();
		
		Node argNode = node1.getArgument().getNode().getLastNode();

		if(argNode == null) {
			throw new CompilerException("head attend en argument une expression contenant un vec et ses valeurs");
		}

		 Node kNode = new Node(
                 NodeFieldFactory.create(argNode),
                 NodeFieldFactory.create(new K())
         );

        if(node1.getNextNode() != null) {
            node1.getNextNode().setFunction(NodeFieldFactory.create(kNode));
        }

        registry.setNode(kNode);

		return true;
	}

	@Override
	public String getName() {
		return "head";
	}

}
