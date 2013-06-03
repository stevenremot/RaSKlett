package compiler.combinators;

import compiler.graph.Node;
import compiler.reducer.Registry;

/**
 * 
 * @author lagrange
 *
 */
public interface Combinator {
	
	public Node getGraph();
	/**
	 * 
	 * @param registry
	 * @return true if reduction is over, false otherwise
	 */
	public boolean applyReduction(Registry registry);

	String getName();

}
