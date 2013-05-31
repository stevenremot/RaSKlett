package compiler.combinators;

import compiler.graph;
/**
 * 
 * @author lagrange
 *
 */
public interface Combinator {
	
	Node getGraph();
	/**
	 * 
	 * @param registry
	 * @return true if reduction is over, false otherwise
	 */
	boolean applyReduction(Registry registry);

	String getName();

}
