package compiler.graph;

import compiler.combinators.Combinator;

/**
 * @brief Champ de noeud contenant un combinateur
 * 
 * @author kazmiero
 *
 */

public class CombinatorNodeField implements NodeField {
	
	private Combinator combinator;
	
	public CombinatorNodeField(Combinator combinator){
		this.combinator = combinator;
	}
	
	public Combinator getCombinator(){
		return combinator;
	}
	
	public Node getNode(){
		return null;
	}

}
