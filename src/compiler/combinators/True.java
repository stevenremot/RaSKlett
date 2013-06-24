package compiler.combinators;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * 
 * @author Juliette
 *
 * @brief Combinateur True
 * 
 * Prend le premier argument, ignore le suivant
 */
public class True implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) {
		Node currentNode = registry.getNode(),
				nextNode = currentNode.getNextNode();
		if(nextNode == null)
			return false;
		
		nextNode.setFunction(NodeFieldFactory.create(new I()));
		nextNode.setArgument(currentNode.getArgument());
		registry.setNode(nextNode);
		return true;
	}

	@Override
	public String getName() {
		return "true";
	}

}
