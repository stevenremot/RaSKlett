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
	
	
	/**
	 * @brief cherche des abstracters dans le graphe à partir de la fin de l'expression (associativité à gauche) et réalise les abstractions correspondantes
	 * @param expression
	 * @return graphe après abstraction
	 */
	public Node findAbstracter(Node expression){
		
		Node lastNode = expression.getLastNode();
		
		Node node = lastNode;
		
		while(node.getFunction().getNode() != null){
			
			if(node.getArgument().getNode() != null){
			
				Node abstractedArgument = findAbstracter(node.getArgument().getNode()); 
				node.setArgument(NodeFieldFactory.create(abstractedArgument));
				
			}
			
			else if(node.getArgument().getCombinator() instanceof Var && node.getFunction().getNode().getArgument().getCombinator() instanceof Lambda){
				
				Var var = (Var) node.getArgument().getCombinator();
				Lambda lambda = (Lambda) node.getFunction().getNode().getArgument().getCombinator();
				// on coupe la connection au reste du graphe
				node.getNextNode().setFunction(new NodeNodeField(null));
				Node abstractedSubGraph = abstraction(node.getNextNode(), lambda.getLevel(), var);
				
				node = node.getFunction().getNode();
				node.setArgument(abstractedSubGraph.getFunction());
				abstractedSubGraph.setFunction(NodeFieldFactory.create(node));
				node.setNextNode(abstractedSubGraph);
				
			}
			
			node = node.getFunction().getNode();
		}
		
		// racine
		if(node.getFunction().getCombinator() instanceof Lambda && node.getArgument().getCombinator() instanceof Var){
			
			Var var = (Var) node.getArgument().getCombinator();
			Lambda lambda = (Lambda) node.getFunction().getCombinator();
			// on coupe la connection au reste du graphe
			node.getNextNode().setFunction(new NodeNodeField(null));
			Node abstractedGraph = abstraction(node.getNextNode(), lambda.getLevel(), var);
			node = abstractedGraph;
		}
		
		return node.getLastNode();
		
	}
	
	public Node abstraction(Node expression, int level, Var var){
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		NodeField nfS = NodeFieldFactory.create(cmanager.get("S"));
		
		Node root = expression.getRoot();
		
		// réécriture du graphe de manière plus pratique
		if(root.getFunction().getCombinator() != null){
			Node newRoot = new Node(new NodeNodeField(null), NodeFieldFactory.create(root.getFunction().getCombinator()));
			root.setFunction(NodeFieldFactory.create(newRoot));
			newRoot.setNextNode(root);
			root = newRoot;
		}		
		
		Node lastNode = root.getLastNode();
		Node currentNode = lastNode;
		// un seul combinateur
		if(lastNode.equals(root)){
			
			if(root.getArgument().getCombinator().equals(var))
				return new Node(NodeFieldFactory.create(cmanager.get("I")),NodeFieldFactory.create(cmanager.get("I")));
			else
				return new Node(NodeFieldFactory.create(cmanager.get("K")),NodeFieldFactory.create(root.getArgument().getCombinator()));
			
		}
		
		
		//règle lambda+++x . F = K F
		if(level >= 3 && (lastNode.getArgument().getCombinator() == null || lastNode.getArgument().getCombinator() != null && !lastNode.getArgument().getCombinator().equals(var))){
			
			currentNode = searchVariable(lastNode,var);
			
			if(currentNode == null){
				root.setFunction(NodeFieldFactory.create(cmanager.get("K")));
				return root;
			}
			
			Node nextNode = currentNode.getNextNode();
			currentNode.setNextNode(null);
			Node newNode = new Node(NodeFieldFactory.create(abstraction(currentNode,level,var)),NodeFieldFactory.create(cmanager.get("K")));
			nextNode.setFunction(NodeFieldFactory.create(newNode));
			return newNode;
			
		}
		
		
		
		// règle lambda++ x . F x = F
		if(level >= 2 && lastNode.getArgument().getCombinator() != null && lastNode.getArgument().getCombinator().equals(var)){
			
			currentNode = searchVariable(lastNode.getFunction().getNode(),var);		
		
			if(currentNode == null){
				
				if(root.getNextNode().equals(lastNode))
					return new Node(NodeFieldFactory.create(cmanager.get("I")), NodeFieldFactory.create(root.getArgument().getCombinator()));
				
				root.getNextNode().setFunction(root.getArgument());
				lastNode.getFunction().getNode().setNextNode(null);
				return lastNode.getFunction().getNode();
			}
			
			currentNode = currentNode.getNextNode();
			
			if(!currentNode.equals(lastNode)){
				
				lastNode.getFunction().getNode().setNextNode(null);
				currentNode.getFunction().getNode().setNextNode(null);
				currentNode.setFunction(NodeFieldFactory.create(abstraction(currentNode.getFunction().getNode(),level,var)));
				return currentNode;
			}
				
		}
		
		
		// règle lambda+ x . P Q = S (lambda+ x . P) (lambda+ x . Q)
		if(lastNode.getFunction().getNode().equals(root)){
	
			lastNode.setArgument(abstractNodeField(lastNode.getArgument(),level,var));
			Node firstNode = new Node(nfS,abstractNodeField(root.getArgument(),level,var));
			lastNode.setFunction(NodeFieldFactory.create(firstNode));
			firstNode.setNextNode(lastNode);
			
			return firstNode;
			
		}
		
		else{
			
			lastNode.getFunction().getNode().setNextNode(null);
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
		else {//cas des parenthèses
			Node node = abstraction(nf.getNode(),level,var);
			if(node.getFunction().getCombinator() != null && node.getFunction().getCombinator().equals(cmanager.get("I")))
				return NodeFieldFactory.create(node.getArgument().getCombinator());
			else
				return NodeFieldFactory.create(node);
			
		}
		
	}
	
	
	/**
	 * @brief parcourt le graphe à l'envers jusqu'à ce que la variable soit trouvée.
	 * Utile pour lambda+ et lambda++
	 * @param start
	 * @param var
	 * @return premier Node contenant var. null si var n'a pas été trouvée
	 */
	public Node searchVariable(Node start, Var var){
		
		Node node = start;
	
		do{
			
			if(node.getArgument().getNode() != null)
				if(searchVariable(node.getArgument().getNode(),var) != null)
					return node;
			
			if(node.getArgument().getCombinator().equals(var))
				return node;
					
			node = node.getFunction().getNode();
		}
		while(node != null);
		// ici rien n'a été trouvé
		return null;
	}

}
