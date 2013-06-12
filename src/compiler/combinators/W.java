package compiler.combinators;

import compiler.graph.Node;
import compiler.reducer.Registry;

public class W implements Combinator {

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
		
		currentNode.setFunction(currentNode.getArgument());
		currentNode.setArgument(nextNode.getArgument());
		registry.setNode(currentNode);
		return true;
	}

	@Override
	public String getName() {
		return "W";
	}

}
