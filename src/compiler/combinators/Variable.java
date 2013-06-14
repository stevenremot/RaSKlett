package compiler.combinators;

import compiler.graph.Node;
import compiler.reducer.Registry;

/**
 * Combinateur représentant une variable dans une abstraction
 * 
 * @author remot
 *
 */
public class Variable implements Combinator {
	private String name;
	
	/**
	 * 
	 * @param name le nom de la variable
	 */
	public Variable(String name) {
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
