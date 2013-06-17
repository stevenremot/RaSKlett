package compiler.combinators;

import java.util.ArrayList;
import java.util.HashMap;

import compiler.config.ConfigManager;

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
		factories = new ArrayList<CombinatorFactory>();
		
		ConfigManager conf = ConfigManager.getInstance();
		
		addCombinator(new S());
		addCombinator(new K());
		addCombinator(new I());
		
		addCombinator(new Definition());
		
		addFactory(new LambdaFactory());
		
		if(conf.isEnabled(ConfigManager.BASIC_COMBINATORS)) {
			addCombinator(new B());
			addCombinator(new C());
			addCombinator(new CStar());
			addCombinator(new W());
		}
		
		if(conf.isEnabled(ConfigManager.BOOLEANS)) {
			addCombinator(new True());
			addCombinator(new False());
			addCombinator(new And());
			addCombinator(new Or());
			addCombinator(new Not());
		}
		
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
	 * Remet à zéro l'instance du combinatorManager
	 */
	public static void reset() {
		instance = null;
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
				addCombinator(name, comb);
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
	 * Enregistre le combinator combinator avec le nom name
	 * 
	 * @param name
	 * @param combinator
	 */
	public void addCombinator(String name, Combinator combinator) {
		combinators.put(name, combinator);
	}
	
	/**
	 * Enregistre le combinator combinator
	 * 
	 * @param name
	 * @param combinator
	 */
	public void addCombinator(Combinator combinator) {
		addCombinator(combinator.getName(), combinator);
	}
	
	/**
	 * @brief Ajoute une factory de combinateurs à interroger dans get.
	 * @param factory
	 */
	public void addFactory(CombinatorFactory factory) {
		factories.add(factory);
	}

}
