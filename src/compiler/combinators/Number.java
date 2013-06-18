package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Combinateur représentant un nombre
 * 
 * @author remot
 *
 */
public class Number implements Combinator {
	private int value;
	
	public Number(int value) {
		this.value = value;
	}
	
	@Override
	public Node getGraph() {
		return null;
	}
	
	public int getValue() {
		return value;
	}

	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
		Node node1 = registry.getNode();
		
		if(value == 0) {
			Node node2 = node1.getNextNode();
			
			if(node2 == null) {
				return false;
			}
			
			node2.setFunction(NodeFieldFactory.create(new I()));
			registry.setNode(node2);
			
			return true;
		}
		
		Number preceding = new Number(value - 1);
		
		Node node0 = new Node(
				NodeFieldFactory.create(preceding),
				node1.getArgument());
		
		node1.setFunction(NodeFieldFactory.create(node0));
		
		registry.setNode(node0);
		
		return preceding.applyReduction(registry);
	}

	@Override
	public String getName() {
		return Integer.toString(value);
	}

}
