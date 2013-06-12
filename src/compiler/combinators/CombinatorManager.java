package compiler.combinators;

import java.util.ArrayList;
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
	ArrayList<CombinatorFactory> factories;
	
	private CombinatorManager() {
		combinators = new HashMap<String,Combinator>();
		set("S", new S());
		set("K", new K());
		set("I", new I());
		
		factories = new ArrayList<CombinatorFactory>();
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
		Combinator comb = askDictionnary(name);
		
		if(comb == null) {
			comb = askFactories(name);
			
			// On met le combinateur en cache
			// La recherche suivante ira plus vite,
			// et surtout tous les combinateurs avec ce nom partageront la même
			// référence, ce qui est pratique pour tester l'égalité
			if(comb != null) {
				set(name, comb);
			}
		}
		
		return comb;
	}
	
	// Demande un combinateur au dictionnaire
	private Combinator askDictionnary(String name) {
		return combinators.get(name);
	}
	
	// Demande un combinateur aux factories
	private Combinator askFactories(String name) {
		Combinator comb = null;
		
		for(CombinatorFactory fac : factories) {
			comb = fac.get(name);
			
			if(comb != null) {
				break;
			}
		}
		
		return comb;
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
	
	/**
	 * @brief Ajoute une factory de combinateurs à interroger dans get.
	 * @param factory
	 */
	public void addFactory(CombinatorFactory factory) {
		factories.add(factory);
	}

}
