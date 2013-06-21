package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * @brief Combinateur permettant l'appel de fonction de manière paresseuse
 * 
 * LazyCall reçoit le nom d'une fonction en argument, et insère son graphe
 * dans le graphe de l'expression au moment de la réduction.
 * 
 * C'est ce combinateur qui permet la récursivité
 * 
 * @author lagrange
 *
 */
public class LazyCall implements Combinator {

	private String func;
	public LazyCall(String func) {
		this.func = func;
	}
	
	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
		Combinator f = CombinatorManager.getInstance().get(func);
		
		// Si la fonction n'existe pas, on considère qu'on est
		// en train de faire une réduction pendant la définition de la fonction.
		// On stoppe donc la réduction
		if(f == null) {
			return false;
		}
		
		if(f.getGraph() == null) {
			throw new CompilerException("Appel récursif vers un combinateur natif impossible et incohérent");
		}
		
		Node node = registry.getNode();
		Node graph = f.getGraph();
		node.setFunction(NodeFieldFactory.create(graph.getLastNode()));
		
		registry.setNode(graph);
		
		return true;
	}

	@Override
	public String getName() {
		return func;
	}

}
