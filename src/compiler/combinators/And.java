package compiler.combinators;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Combinateur représentant le "et" logique
 * 
 * @author remot
 *
 */
public class And implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	/**
	 * and x y -> x y f
	 */
	@Override
	public boolean applyReduction(Registry registry) {
		
		Node node1 = registry.getNode();
		
		Node node2 = node1.getNextNode();
		
		if(node2 == null) {
			return false;
		}
		
		node1.setFunction(node1.getArgument());
		node1.setArgument(node2.getArgument());
		node2.setArgument(NodeFieldFactory.create(new False()));
		
		return true;
	}

	@Override
	public String getName() {
		return "and";
	}

}
