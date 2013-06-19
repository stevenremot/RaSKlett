package compiler.combinators;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Combinateur représentant l'opérateur logique "ou"
 * 
 * @author remot
 *
 */
public class Or implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}
	
	/**
	 * or x y -> x true y
	 */
	@Override
	public boolean applyReduction(Registry registry) {
		Node node1 = registry.getNode();
		
		Node node2 = node1.getNextNode();
		
		if(node2 == null) {
			return false;
		}
		
		node1.setFunction(node1.getArgument());
		node1.setArgument(NodeFieldFactory.create(new True()));
		
		return true;
	}

	@Override
	public String getName() {
		return "||";
	}

}
