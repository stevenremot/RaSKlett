package compiler.combinators;

/**
 * @brief Interface de classes permettant de créer des combinateurs
 * 
 * Permet l'utilisation de noms dynamiques
 * 
 * @author remot
 *
 */
public interface CombinatorFactory {
	/**
	 * Crée un combinateur correspondant au nom name si possible
	 * @param name
	 * @return le combinateur si possibilité de le créer, null sinon
	 */
	public Combinator get(String name);
}
