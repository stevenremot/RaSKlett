package compiler.graph;

/**
 * @brief Champ de noeud contenant un combinateur
 * 
 * @author kazmiero
 *
 */

public class CombinatorNodeField implements NodeField {
	
	private Combinator combinator;
	
	public Combinator getCombinator(){
		return combinator;
	}
	
	public Node getNode(){
		return null;
	}

}
