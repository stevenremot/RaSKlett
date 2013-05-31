package compiler.reducer;

import compiler.graph.Node;

/**
 * @brief Registre de la machine SK
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
	
	public Node setNode(Node node) {
		return node;
	}

}
