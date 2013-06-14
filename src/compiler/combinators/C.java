package compiler.combinators;

import compiler.graph.Node;
import compiler.graph.NodeField;
import compiler.reducer.Registry;

public class C implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	/**
	 * @brief Applique le combinateur de permutation C au graphe
	 * Vérifie qu'on a au moins trois arguments F X Y et remplace le troisième noeud par (F Y) X
	 */
	@Override
	public boolean applyReduction(Registry registry) {
		Node node1 = registry.getNode();
		Node node2 = node1.getNextNode();
		if(node2 == null)
			return false;
		Node node3 = node2.getNextNode();
		if(node3 == null)
			return false;
		NodeField y = node3.getArgument();
		node3.setArgument(node2.getArgument());
		node2.setFunction(node1.getArgument());
		node2.setArgument(y);
		registry.setNode(node2);
		return true;
	}

	@Override
	public String getName() {
		return "C";
	}

}
