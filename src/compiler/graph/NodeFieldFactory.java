package compiler.graph;

import compiler.combinators.Combinator;

/**
 * @brief NodeFieldFactory : crée des NodeFields correspondant à un combinateur ou un noeud
 * 
 * @author kazmiero
 *
 */

public class NodeFieldFactory {

	static NodeField create(Combinator combinator){
		return new CombinatorNodeField(combinator);
	}
	
	static NodeField create(Node node){
		return new NodeNodeField(node);
	}
}
