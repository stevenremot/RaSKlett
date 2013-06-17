package compiler.combinators;

import compiler.graph.Node;
import compiler.reducer.Registry;
import compiler.abstracter.Abstracter;

/**
 * Combinateur réalisant l'abstraction de l'expression
 * 
 * @author remot
 *
 */
public class Lambda implements Combinator {
	private int level;
	
	/**
	 * @param level Niveau de l'abstraction, de 1 à 4
	 */
	public Lambda(int level) {
		this.level = level;
	}

	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) {
		Abstracter a = new Abstracter(registry.getNode(), level);
		
		registry.setNode(a.getAbstractedGraph());
		
		return true;
	}

	@Override
	public String getName() {
		String s = "lambda";
		
		for(int i=0; i < level; i++) {
			s += "+";
		}
		
		return s;
	}
	
	public int getLevel() {
		return level;
	}

}
