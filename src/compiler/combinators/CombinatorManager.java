package compiler.combinators;

import java.util.HashMap;
import compiler.graph.Node;

/**
 * 
 * @author lagrange
 *
 */
public class CombinatorManager {
	
	CombinatorManager instance = null;
	HashMap<String, Combinator> combinators;
	
	private CombinatorManager() {
		
	}
	
	public CombinatorManager getInstance() {
		if(instance == null)
			instance = new CombinatorManager();
		return instance;
	}
	
	public Combinator getName(String name) {
		return combinators.get(name);
	}
	
	public void set(String name, Node node) {
		
	}

}
