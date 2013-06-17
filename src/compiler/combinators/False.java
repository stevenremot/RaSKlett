package compiler.combinators;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Combinateur false
 * 
 * annule son argument, et passe au suivant
 * 
 * @author remot
 *
 */
public class False implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) {
		
		Node node1 = registry.getNode();
		
		Node node2 = node1.getNextNode();
		
		if(node2 == null) {
			return false;
		}
		
		node2.setFunction(NodeFieldFactory.create(new I()));
		
		registry.setNode(node2);
		
		return true;
	}

	@Override
	public String getName() {
		return "false";
	}

}
