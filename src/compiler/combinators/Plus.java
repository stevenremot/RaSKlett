package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Combinateur représentant l'addition d'entier
 * 
 * @author remot
 *
 */
public class Plus implements Combinator {
	
	@Override
	public Node getGraph() {
		return null;
	}

	/**
	 * + x y = x (S B) y
	 */
	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
		Node node1 = registry.getNode();
		
		node1.setFunction(node1.getArgument());
		
		Node sb = new Node(
				NodeFieldFactory.create(new S()),
				NodeFieldFactory.create(new B()));
		
		node1.setArgument(NodeFieldFactory.create(sb));
		return true;
	}

	@Override
	public String getName() {
		return "+";
	}

}
