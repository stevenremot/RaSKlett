package compiler.combinators;

import compiler.graph.Node;
import compiler.reducer.Registry;

public class I implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	/**
	 * @brief Applique le combinateur I au graphe.
	 */
	@Override
	public boolean applyReduction(Registry registry) {
		Node currentNode = registry.getNode(),
				nextNode = currentNode.getNextNode();
		if(nextNode == null)
			return false;
		nextNode.setFunction(currentNode.getArgument());
		registry.setNode(nextNode);
		return true;
	}

	@Override
	public String getName() {
		return "I";
	}

}
