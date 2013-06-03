package compiler.combinators;

import compiler.graph.Node;
import compiler.reducer.Registry;

public class K implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	/**
	 * On se place sur le noeud pointé par le registre qui contient K comme fonction.
	 * 
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
			nextNode.setFonction(NodeFieldFactory.create(new I));
			nexNode.setArgument(NodeFieldFactory.create(currentNode.getArgument().getCombinator()));
		}
		else {
			
		}
		
		return true;
	}

	@Override
	public String getName() {
		return "K";
	}

}
