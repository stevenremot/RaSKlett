package compiler;

/**
 * Interface pour une classe recevant les résultats et les erreurs du compilateur
 * 
 * @author remot
 *
 */
public interface CompilerCallback {
	
	/**
	 * Appelée lorsque la réduction est terminée ou stoppée
	 * @param reducedGraph le graphe réduit
	 * @param line la ligne de la réduction
	 * @param position la position de l'instruction sur la ligne
	 * @param finished true si la réduction est finie, false sinon
	 */
	public void onResult(String reducedGraph, int line, int position, boolean finished);
	
	/**
	 * Appelé lorsqu'une erreur est survenue
	 */
	public void onFailure(CompilerException e);
}
