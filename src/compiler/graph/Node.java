package compiler.graph;

public class Node {
	
	private NodeField function, argument;
	private Node nextNode;
	
	public Node(NodeField function, NodeField argument){
		
	}
	
	public NodeField getFunction(){
		return function;
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

}
