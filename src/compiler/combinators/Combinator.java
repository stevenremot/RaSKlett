package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.reducer.Registry;

/**
 * Interface permettant de définir les combinateurs du programme
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
	public boolean applyReduction(Registry registry) throws CompilerException;

	String getName();

}
