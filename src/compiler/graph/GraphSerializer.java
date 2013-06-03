package compiler.graph;

/**
 * @brief Convertit un graphe en chîne de caractères
 * @author remot
 *
 */
public abstract class GraphSerializer {
	
	/**
	 * @brief Méthode de sérialisation de graphe
	 */
	public static String serialize(Node graph) {
		String ret = "";
		
		// On a une expresssion a M1 M2 ..., on se place au a
		Node root = graph;
		while(root.getFunction().getNode() != null) {
			root = root.getFunction().getNode();
		}
		
		// On écrit la fonction
		ret += root.getFunction().getCombinator().getName();
		
		// Puis les arguments
		for(Node node = root; node != null; node = node.getNextNode()) {
			NodeField arg = node.getArgument();
			
			if(arg.getCombinator() != null) {
				ret += " " + arg.getCombinator().getName();
			}
			else {
				ret += " ( " + serialize(arg.getNode()) + " )";
			}
		}
		
		return ret;
	}

}
