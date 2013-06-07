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
	 * @brief Applique le combinateur K au graphe.
	 * 
	 * On se place sur le noeud pointé par le registre qui contient K comme fonction.
	 * On s'assure d'avoir au moins 2 arguments X et Y pour pouvoir réduire KXY en X
	 * Si X est atomique, on remplace le deuxième noued par IX.
	 * Sinon, si X = (PQ), on le remplace par PQ
	 */
	@Override
	public boolean applyReduction(Registry registry) {
		// TODO Auto-generated method stub
		Node currentNode = registry.getNode(),
				nextNode = currentNode.getNextNode();
		if(nextNode == null)
			return false;
		Node thirdNode = nextNode.getNextNode();
		if(thirdNode == null)
			return false;
		// Si X atomique
		if(currentNode.getArgument().getCombinator() != null) {
			nextNode.setFunction(NodeFieldFactory.create(new I()));
			nextNode.setArgument(NodeFieldFactory.create(currentNode.getArgument().getCombinator()));
		}
		else {
			//thirdNode.setFunction(currentNode.getArgument().getNode().getFunction());
			//thirdNode.setArgument(currentNode.getArgument().getNode().getArgument());
			nextNode.setFunction(currentNode.getArgument().getNode().getFunction());
			nextNode.setArgument(currentNode.getArgument().getNode().getArgument());
		}
		registry.setNode(thirdNode);
		return true;
	}

	@Override
	public String getName() {
		return "K";
	}

}
