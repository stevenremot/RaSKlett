package compiler.combinators;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class B implements Combinator{

	@Override
	public Node getGraph() {
		return null;
	} 

	@Override
	public boolean applyReduction(Registry registry) {
		Node node1 = registry.getNode();
		Node node2 = node1.getNextNode();
		if(node2 == null)
			return false;
		Node node3 = node2.getNextNode();
		if(node3 == null)
			return false;
		node3.setFunction(node1.getArgument());
		Node node = new Node(node2.getArgument(),node3.getArgument());
		node3.setArgument(NodeFieldFactory.create(node));
		registry.setNode(node3);
		return true;
	}

	@Override
	public String getName() {
		return "B";
	}

}
