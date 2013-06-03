package compiler.combinators;

import java.util.HashMap;
import compiler.graph.Node;

/**
 * 
 * @author lagrange
 *
 */
public class CombinatorManager {
	
	private static CombinatorManager instance = null;
	HashMap<String, Combinator> combinators;
	
	private CombinatorManager() {
		combinators = new HashMap<String,Combinator>();
	}
	
	public static CombinatorManager getInstance() {
		if(instance == null)
			instance = new CombinatorManager();
		return instance;
	}
	
	public Combinator get(String name) {
		return combinators.get(name);
	}
	
	public void set(String name, Node node) {
		
	}

}
