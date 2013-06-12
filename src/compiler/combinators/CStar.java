package compiler.combinators;

import compiler.graph.Node;
import compiler.reducer.Registry;

public class CStar implements Combinator {

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
		
		nextNode.setFunction(nextNode.getArgument());
		nextNode.setArgument(currentNode.getArgument());
		registry.setNode(nextNode);
		return true;
	}

	@Override
	public String getName() {
		return "C*";
	}

}
