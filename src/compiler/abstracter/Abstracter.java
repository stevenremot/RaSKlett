package compiler.abstracter;

import compiler.graph.*;
import compiler.combinators.CombinatorManager;
import compiler.combinators.Lambda;
import compiler.combinators.Var;

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
	// lambda+	
		
		Node node = abstractedGraph;
		
		// parcours du graphe pour trouver la dernière expression à extraire
		while(node.getArgument().getCombinator() != null && (node.getArgument().getCombinator() instanceof Lambda || node.getArgument().getCombinator() instanceof Var))
			node = node.getNextNode();
		
		
		if(node.getFunction().getCombinator() == null){
			NodeField func = NodeFieldFactory.create(node.getFunction().getNode().getArgument().getCombinator());
			NodeField arg = NodeFieldFactory.create(node.getArgument().getCombinator());
		} 	
		
	}
	
	public Node abstraction(Node expression, int level, Var var){
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		NodeField nfS = NodeFieldFactory.create(cmanager.get("S"));
		
		Node root = expression.getRoot();
		
		// réécriture du graphe de manière plus pratique
		if(root.getFunction().getCombinator() != null){
			Node newRoot = new Node(null, NodeFieldFactory.create(root.getFunction().getCombinator()));
			root.setFunction(NodeFieldFactory.create(root));
			newRoot.setNextNode(root);
		}
			
		
		
		Node lastNode = expression.getLastNode();
		
		// cas particuliers
		if(lastNode.equals(expression)){
			
			// pas possible
			return null;
			
		}
		
		else if(lastNode.getFunction().getNode().equals(expression)){
			
			Node firstNode = new Node(nfS,abstractNodeField(expression.getArgument(),level,var));
			lastNode.setArgument(abstractNodeField(lastNode.getArgument(),level,var));
			lastNode.setFunction(NodeFieldFactory.create(firstNode));
			firstNode.setNextNode(lastNode);
			
			return firstNode;
			
		}
		
		else{
			
			Node firstNode = new Node(nfS,abstractNodeField(lastNode.getFunction(),level,var));
			lastNode.setArgument(abstractNodeField(lastNode.getArgument(),level,var));
			lastNode.setFunction(NodeFieldFactory.create(firstNode));
			firstNode.setNextNode(lastNode);
			
			return firstNode;
		}
		
		
	}
	
	private NodeField abstractNodeField(NodeField nf, int level, Var var){

		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		if(nf.getCombinator() != null){
			if(nf.getCombinator().equals(var))
				return NodeFieldFactory.create(cmanager.get("I"));
			else
				return NodeFieldFactory.create(new Node(NodeFieldFactory.create(cmanager.get("K")),NodeFieldFactory.create(nf.getCombinator())));
		}
		else //cas des parenthèses
			return NodeFieldFactory.create(abstraction(nf.getNode(),level,var));
		
	}

}
