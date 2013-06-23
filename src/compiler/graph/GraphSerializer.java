package compiler.graph;

/**
 * Convertit un graphe en chîne de caractères
 * @author remot
 *
 */
public abstract class GraphSerializer {
	
	/**
	 * Méthode de sérialisation de graphe
	 */
	public static String serialize(Node graph) {
		String ret = "";

		Node root = graph.getRoot();
		
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
