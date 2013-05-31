package compiler.reducer;

import compiler.combinators.Combinator;
import compiler.graph.Node;

/**
 * @brief Implémentation du coeur de la machine SK
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
	 * @brief Effectue une étape de la réduction
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
	 */
	public boolean step() {
		Combinator c;
		
		while((c = registry.getNode().getFunction().getCombinator()) == null) {
			registry.setNode(registry.getNode().getFunction().getNode());
		}
		
		return c.applyReduction(registry);
	}

}
