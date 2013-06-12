package compiler.abstracter;

import compiler.graph.*;

/**
 * @brief classe réalisant l'abstraction d'une expression
 * @author kazmiero
 *
 */

public class Abstracter {
	
	private Node abstractedGraph;
	
	public Abstracter(Node expression, int level){
		abstractedGraph = expression;
	}
	
	public Node getAbstractedGraph(){
		return abstractedGraph;
	}
	
	public void abstractGraph(){
		
		
		Node node = abstractedGraph;
		
		// parcours du graphe pour trouver la dernière expression à extraire
		while(node.getArgument().getCombinator() != null && (node.getArgument().getCombinator().isLambda() || node.getArgument().getCombinator().isVar()))
			node = node.getNextNode();
		
		NodeField func = NodeFieldFactory.create(node.getFunction().getNode().getArgument().getCombinator());
		NodeField arg = NodeFieldFactory.create(node.getArgument().getCombinator());
		
		
		
	}

}
