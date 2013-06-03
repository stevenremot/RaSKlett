package compiler.graph;

import compiler.combinators.Combinator;

/**
 * @brief Champ de noeud contenant une référence vers un autre noeud
 * 
 * @author kazmiero
 *
 */

public class NodeNodeField implements NodeField {

	private Node node;
	
	public NodeNodeField(Node node){
		this.node = node;
	}
	
	public Combinator getCombinator(){
		return null;
	}
	
	public Node getNode(){
		return node;
	}
	
	
	
}
