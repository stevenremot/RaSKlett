package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.reducer.Registry;

/**
 * @brief Combinateur permettant de créer un vecteur
 * 
 * Équivalent de pi x, y, z := z x y
 * 
 * @author steven
 *
 */
public class Vector implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
		Node node1 = registry.getNode();
		
		Node node2 = node1.getNextNode();
		
		if(node2 == null) {
			return false;
		}
		
		Node node3 = node2.getNextNode();
		
		if(node3 == null) {
			return false;
		}
		
		node2.setFunction(node3.getArgument());
		node3.setArgument(node2.getArgument());
		node2.setArgument(node1.getArgument());
		
		registry.setNode(node2);
		
		return true;
	}

	@Override
	public String getName() {
		return "vec";
	}

}
