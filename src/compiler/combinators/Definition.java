package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeField;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;
import compiler.reducer.SKMachine;

/**
 * Combinateur pour la définition de nouveaux combinateurs
 * @author remot
 *
 */
public class Definition implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
		
		Node node1 = registry.getNode();
		
		NodeField nameField = node1.getArgument();
		
		if(nameField.getCombinator() == null) {
			throw new CompilerException("Impossible de trouver le nom du combinateur pendant la définition");
		}
		
		Var name;
		
		try {
			name = (Var) nameField.getCombinator();
		}
		catch(ClassCastException e) {
			throw new CompilerException("Impossible de trouver le nom du combinateur pendant la définition");
		}
		
		final String combinatorName = name.getVarName();
		
		Node expr = node1.getNextNode();
		
		if(expr == null) {
			throw new CompilerException("Impossible de trouver d'expression pour définir un nouveau combinateur");
		}
		
		expr.setFunction(NodeFieldFactory.create(new I()));
		
		SKMachine sk = new SKMachine(expr);
		while(sk.step());
		
		final Node graph = sk.getReducedGraph();
		
		CombinatorManager.getInstance().addCombinator(new Combinator() {
			@Override
			public String getName() {
				return combinatorName;
			}

			@Override
			public Node getGraph() {
				return graph.copy();
			}

			@Override
			public boolean applyReduction(Registry registry)
					throws CompilerException {
				return false;
			}
		});
		
		// On a pu finir, mais le définition a "mangé tout le reste de l'instruction.
		return false;
	}

	@Override
	public String getName() {
		return ":=";
	}

}
