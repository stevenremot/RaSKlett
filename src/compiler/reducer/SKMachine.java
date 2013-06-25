package compiler.reducer;

import compiler.CompilerException;
import compiler.graph.Node;

/**
 * Implémentation du coeur de la machine SK
 * 
 * Déclenche la réduction d'un graphe en laissant les combinateurs
 * se charger de modifier le graphe.
 * 
 * @author remot
 *
 */
public class SKMachine {
	private Registry registry;
	
	public SKMachine(Node graph) {
		registry = new Registry();
		setGraph(graph);
	}
	
	public void setGraph(Node graph) {
		registry.setNode(graph);
	}
	
	/**
	 * @return le graphe sur lequel on a appliqué les réductions
	 */
	public Node getReducedGraph() {
		return registry.getNode();
	}
	
	/**
	 * Effectue une étape de la réduction
	 * 
	 * Le graphe, avant step(), représente une expression a M1 M2 ...
	 * On s'assure que e registre pointe sur le combinateur a.
	 * 
	 * a est un combinateur natif, il possède une méthode applyReduction(), on l'appel
	 * en lui donnant le registre.
	 * 
	 * Si a return true, il a pu appliquer la réduction..
	 * 
	 * Si a retourne false, la réduction est probablement terminée.
	 * 
	 * @return true si la réduction s'est appliquée, false sinon
	 * @throws CompilerException 
	 */
	public boolean step() throws CompilerException {
		Node n = registry.getNode();
		
		return n.getFunction().getCombinator().applyReduction(registry);
	}

}
