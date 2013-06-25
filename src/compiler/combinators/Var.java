package compiler.combinators;

import compiler.graph.Node;
import compiler.reducer.Registry;

/**
 * Classe de combinateurs représentant les variables à abstraire. Le nom d'une variable commence par le caractère '$'
 * @author kazmiero
 *
 */

public class Var implements Combinator {

	String name;
	
	public Var(String name){
		this.name = name;
	}
	
	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) {
		return false;
	}

	@Override
	public String getName() {
		return getVarName();
	}
	
	public String getVarName() {
		return name.substring(1);
	}

}
