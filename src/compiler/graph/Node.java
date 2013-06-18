package compiler.graph;

/**@bief Noeud du graphe de réduction
 * 
 * Comprend deux champs fonction et argument, ainsi qu'une référence vers le noeud suivant dans le graphe
 * 
 * @author kazmiero
 *
 */

public class Node {
	
	private NodeField function, argument;
	private Node nextNode = null;
	
	public Node(NodeField function, NodeField argument){
		setFunction(function);
		setArgument(argument);
	}
	
	public Node(NodeField function){
		setFunction(function);
		setArgument(null);
	}
	
	public void setFunction(NodeField function) {
		this.function = function;
		if(function.getNode() != null)
			function.getNode().setNextNode(this);
	}
	
	public NodeField getFunction(){
		return function;
	}
	
	public void setArgument(NodeField argument) {
		this.argument = argument;
	}
	
	public NodeField getArgument(){
		return argument;
	}
	
	public Node getNextNode(){
		return nextNode;
	}
	
	public void setNextNode(Node node){
		nextNode = node;
	}
	
	public Node getLastNode(){
		Node node = this;
		while(node.getNextNode() != null)
			node = node.getNextNode();
		return node;
	}
	
	public Node getRoot(){
		Node node = this;
		while(node.getFunction().getNode() != null)
			node = node.getFunction().getNode();
		return node;
	}

}
