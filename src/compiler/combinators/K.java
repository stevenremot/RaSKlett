package compiler.combinators;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;


public class K implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	/**
	 * Applique le combinateur K au graphe.
	 * 
	 * On se place sur le noeud pointé par le registre qui contient K comme fonction.
	 * On s'assure d'avoir au moins 2 arguments X et Y pour pouvoir réduire KXY en X
	 * Si X est atomique, on remplace le deuxième noued par IX.
	 * Sinon, si X = (PQ), on le remplace par PQ
	 */
	@Override
	public boolean applyReduction(Registry registry) {
		Node currentNode = registry.getNode(),
				nextNode = currentNode.getNextNode();
		if(nextNode == null)
			return false;
		
		// Si X atomique
		if(currentNode.getArgument().getCombinator() != null) {
			nextNode.setFunction(NodeFieldFactory.create(new I()));
			nextNode.setArgument(currentNode.getArgument());
		}
		else {
			nextNode.setFunction(currentNode.getArgument().getNode().getFunction());
			nextNode.setArgument(currentNode.getArgument().getNode().getArgument());
		}
		registry.setNode(nextNode);
		return true;
	}

	@Override
	public String getName() {
		return "K";
	}

}
