package compiler.reducer;

import compiler.graph.Node;

/**
 * Registre de la machine SK
 * 
 * Contient une référence vers le noeud sur lequel s'applique la réduction
 * 
 * @author remot
 *
 */
public class Registry {
	private Node node = null;
	
	public Node getNode() {
		return node;
	}
	
	public void setNode(Node node) {
		this.node = node.getRoot();
	}

}
