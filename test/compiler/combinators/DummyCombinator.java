package compiler.combinators;

import compiler.graph.Node;
import compiler.reducer.Registry;

/**
 * Classe simulant un combinateur pour les tests
 */
public class DummyCombinator implements Combinator {
	private String name;
	
	public DummyCombinator(String name) {
		this.name = name;
	}
	
	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) {
		return false;
	}

	@Override
	public String getName() {
		return name;
	}
}
