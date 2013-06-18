package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeField;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * @brief Retourne le deuxième élément d'une paire
 * @author steven
 *
 */
public class Tail implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
		Node node1 = registry.getNode();
		
		Node argNode = node1.getArgument().getNode();
		
		if(argNode == null) {
			throw new CompilerException("head attend en argument une expression contenant un vec et ses valeurs");
		}
		
		argNode = argNode.getRoot();
		
		Combinator c = argNode.getFunction().getCombinator();
		
		if(!(c instanceof Vector)) {
			throw new CompilerException("head ne peut être appelé que sur un vecteur");
		}
		
		Node headNode = argNode.getNextNode();
		
		if(headNode == null) {
			throw new CompilerException("Vecteur mal formé à un seul élément donné à tail");
		}
		
		NodeField head = headNode.getArgument();
		
		Node node2 = node1.getNextNode();
		
		if(node2 == null) {
			node1.setFunction(NodeFieldFactory.create(new I()));
			node1.setArgument(head);
		}
		else {
			node2.setFunction(head);
			registry.setNode(node2);
		}
		
		return true;
	}

	@Override
	public String getName() {
		return "tail";
	}

}