package compiler.config;

import java.util.HashMap;

/**
 * Classe centralisant la configuration du compilateur
 * 
 * Par défaut, tout est désactivé, et le niveau d'abstraction est à 4
 * 
 * @author remot
 *
 */
public class ConfigManager {

	private HashMap<String, Boolean> features;
	private int abstractionLevel = 4;
	
	private static ConfigManager instance = null;
	
	public static final String
		BASIC_COMBINATORS = "basic-combinators",
		BOOLEANS = "booleans",
		NUMBERS = "numbers",
		LISTS = "lists";
	
	private ConfigManager() {
		features = new HashMap<String, Boolean>();
	}
	
	public static ConfigManager getInstance() {
		if(instance == null) {
			instance = new ConfigManager();
		}
		return instance;
	}
	
	/**
	 * Activer / Désactiver une fonctionnalité
	 * 
	 * @param feature Foncionnalité à modifier
	 * @param toggled true si on l'active, false sinon
	 */
	public void toggle(String feature, boolean toggled) {
		features.put(feature, toggled);
	}
	
	/**
	 * Pour vérifier si la fonctionnalité est activée
	 * Si une fonctionnalité n'a pas encore été spécifiée est toggle,
	 * elle est considérée comme désactivée
	 * 
	 * @param feature fonctionnalité à détecter
	 * @return true si elle est activée, false sinon
	 */
	public boolean isEnabled(String feature) {
		return features.containsKey(feature) && features.get(feature);
	}
	
	public void setDefaultAbstractionLevel(int level) {
		abstractionLevel = level;
	}
	
	public int getDefaultAbstractionLevel() {
		return abstractionLevel;
	}
}
