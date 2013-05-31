package compiler.graph;

import compiler.combinators.Combinator;

public interface NodeField {
	
	public Combinator getCombinator();
	public Node getNode();

}
