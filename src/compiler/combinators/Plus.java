package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeField;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Combinateur représentant l'addition d'entier
 * 
 * @author remot
 *
 */
public class Plus implements Combinator {
	
	@Override
	public Node getGraph() {
		return null;
	}

	/**
	 * + x y = x s y
	 */
	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
		Node node1 = registry.getNode();
		
		// Recherche de la première opérande
		NodeField arg1 = node1.getArgument();
		
		if(arg1.getNode() != null) {
			Node argNode = arg1.getNode().getRoot();
			
			Combinator c = argNode.getFunction().getCombinator();
			
			Registry r = new Registry();
			r.setNode(argNode);
			
			if(!c.applyReduction(r)) {
				throw new CompilerException("La première opérande de + doit être un nombre, non pas une expression incomplète");
			}
			
			node1.setArgument(NodeFieldFactory.create(r.getNode().getLastNode()));
		}
		
		Number n1;
		
		try {
			n1 = (Number) arg1.getCombinator();
		}
		catch(ClassCastException e) {
			throw new CompilerException("La première opérande de + doit être un nombre");
		}
		
		// Recherche de la seconde
		Node node2 = node1.getNextNode();
		
		if(node2 == null) {
			return false;
		}
		
		NodeField arg2 = node2.getArgument();
		
		if(arg2.getNode() != null) {
			Node argNode = arg2.getNode().getRoot();
			
			Combinator c = argNode.getFunction().getCombinator();
			
			Registry r = new Registry();
			r.setNode(argNode);
			
			if(!c.applyReduction(r)) {
				throw new CompilerException("La seconde opérande de + doit être un nombre, non pas une expression incomplète");
			}
			
			node2.setArgument(NodeFieldFactory.create(r.getNode().getLastNode()));
		}
		
		Number n2;
		
		try {
			n2 = (Number) arg1.getCombinator();
		}
		catch(ClassCastException e) {
			throw new CompilerException("La première opérande de + doit être un nombre");
		}
		
		// Nouveau combinateur
		node2.setFunction(NodeFieldFactory.create(new I()));
		node2.setArgument(NodeFieldFactory.create(new Number(n1.getValue() + n2.getValue())));
		
		registry.setNode(node2);
		
		return true;
	}

	@Override
	public String getName() {
		return "+";
	}

}
