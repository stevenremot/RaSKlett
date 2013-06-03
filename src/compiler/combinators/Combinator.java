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
	 * @return true si le combinateur a pu appliquer sa réduction, false sinon (la réduction est probablement finie)
	 */
	public boolean applyReduction(Registry registry);

	String getName();

}
