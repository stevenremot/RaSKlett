package compiler.graph;

import compiler.combinators.Combinator;

/**
 * @brief Interface pour un champ d'un noeud dans le graphe
 * Le champ peut contenir un combinateur ou un noeud
 * 
 * @author kazmiero
 *
 */

public interface NodeField {
	
	public Combinator getCombinator();
	public Node getNode();
	
}
