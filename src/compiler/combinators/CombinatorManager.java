package compiler.combinators;

import java.util.HashMap;

/**
 * @brief Enregistre et rend disponible les combinateurs natifs et créés par l'utilisateur
 * 
 * @author lagrange
 *
 */
public class CombinatorManager {
	
	private static CombinatorManager instance = null;
	HashMap<String, Combinator> combinators;
	
	private CombinatorManager() {
		combinators = new HashMap<String,Combinator>();
		set("S", new S());
		set("K", new K());
		set("I", new I());
	}
	
	/**
	 * @return l'unique instance de CombinatorManager
	 */
	public static CombinatorManager getInstance() {
		if(instance == null)
			instance = new CombinatorManager();
		return instance;
	}
	
	/**
	 * @param name
	 * @return le combinateur de nom name
	 */
	public Combinator get(String name) {
		return combinators.get(name);
	}
	
	/**
	 * Enregistre le combinator combinator de nom name
	 * 
	 * @param name
	 * @param combinator
	 */
	public void set(String name, Combinator combinator) {
		combinators.put(name, combinator);
	}

}
