package compiler.combinators;

import compiler.graph.Node;
import compiler.reducer.Registry;

public class W implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	/**
	 * Applique le combinateur permutateur W au graphe.
	 * On vérifie qu'on a au moins 2 arguments F et X.
	 * Ensuite on remplace le deuxième noeud par (F X) X
	 */
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
