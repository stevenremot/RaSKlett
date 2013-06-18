package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Combinateur représentant la mutliplication d'entiers
 * 
 * @author remot
 *
 */
public class Times implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	/**
	 * * x y -> x (y (s b)) 0
	 */
	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
		Node node1 = registry.getNode();
		Node node2 = node1.getNextNode();
		
		if(node2 == null) {
			return false;
		}
		
		node1.setFunction(node1.getArgument());
		
		Node sb = new Node(
				NodeFieldFactory.create(new S()),
				NodeFieldFactory.create(new B()));
		
		Node ysb = new Node(
				node2.getArgument(),
				NodeFieldFactory.create(sb));
		
		node1.setArgument(NodeFieldFactory.create(ysb));
		
		node2.setArgument(NodeFieldFactory.create(new Number(0)));
		
		return true;
	}

	@Override
	public String getName() {
		return "*";
	}
}
