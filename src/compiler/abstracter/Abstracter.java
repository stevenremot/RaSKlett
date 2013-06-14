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
		
		// on suppose que expression.getFunction() = null (technique pour éviter de remonter trop loin)
		Node lastNode = expression.getLastNode();
		
		// cas particuliers
		if(lastNode.equals(expression)){
	
		}
		
		else if(lastNode.getFunction().getNode().equals(expression)){
			
		}
		
		else{
			
		}
		
		
		return null;
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
