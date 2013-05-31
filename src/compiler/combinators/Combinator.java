package compiler.combinators;

import compiler.graph;

public interface Combinator {
	
	Graph getGraph();
	
	boolean applyReduction(Registry registry);
	
	String getName();

}
