package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Combinateur représentant la négation binaire
 * @author remot
 *
 */
public class Not implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	/**
	 * not x => x f t
	 */
	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
		Node node1 = registry.getNode();
		Node node0 = new Node(
				node1.getArgument(),
				NodeFieldFactory.create(new False()));
		
		node1.setFunction(NodeFieldFactory.create(node0));
		node1.setArgument(NodeFieldFactory.create(new True()));
		
		registry.setNode(node0);
		
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
