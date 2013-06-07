package compiler;

/**
 * @brief Interface pour une classe recevant les résultats et les erreurs du compilateur
 * 
 * @author remot
 *
 */
public interface CompilerCallback {
	
	/**
	 * @brief appelée lorsque la réduction est terminée ou stoppée
	 * @param reducedGraph le graphe réduit
	 */
	public void onResult(String reducedGraph, boolean finished);
	
	/**
	 * @brief appelé lorsqu'une erreur est survenue
	 * 
	 * @param message le message d'erreur du compilateur
	 */
	public void onFailure(String message);
}
