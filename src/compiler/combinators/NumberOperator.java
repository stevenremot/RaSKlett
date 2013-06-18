package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeField;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * @brief Classe abstraite pour un opérateur prenant deux entiers
 * 
 * Définit des méthodes pour s'assurer que les deux opérandes sont entières
 * 
 * @author remot
 *
 */
public abstract class NumberOperator implements Combinator {
	
	/**
	 * Les opérateurs doivent définir cette méthode pour appliquer l'opération
	 * @param n1
	 * @param n2
	 * @return
	 */
	protected abstract int doOperation(int n1, int n2);
	
	public Node getGraph() {
		return null;
	}
	
	public boolean applyReduction(Registry registry) throws CompilerException {
		Node node1 = registry.getNode();
		
		Node node2 = node1.getNextNode();
		
		if(node2 == null) {
			return false;
		}
		
		Combinator c = ensureIsNumber(node1.getArgument());
		
		if(c == null) {
			throw new CompilerException("La première opérande de + n'est pas un nombre");
		}
		
		if(c.getGraph() != null) {
			node1.setArgument(NodeFieldFactory.create(c.getGraph()));
			return true;
		}
		
		int n1 = ((Number) c).getValue();
		
		c = ensureIsNumber(node2.getArgument());
		
		if(c == null) {
			throw new CompilerException("La première opérande de + n'est pas un nombre");

		}
		
		if(c.getGraph() != null) {
			node2.setArgument(NodeFieldFactory.create(c.getGraph()));
			return true;
		}
		
		int n2 = ((Number) c).getValue();
		
		Number n = new Number(doOperation(n1, n2));
		
		node2.setFunction(NodeFieldFactory.create(new I()));
		node2.setArgument(NodeFieldFactory.create(n));
		
		registry.setNode(node2);
		
		return true;
	}
	
	/**
	 * Effectue le traitement sur une opérande, et retourne le combinateur correspondany
	 * 
	 * @param nf
	 * @return le Number correspondant, ou un Combinator anonyme contenant un graphe de l'expression qui a été réduite, ou null si il ne peut y avoir de nombre 
	 * @throws CompilerException 
	 */
	protected Combinator ensureIsNumber(NodeField nf) throws CompilerException {
		if(nf.getCombinator() != null) {
			try {
				Number n = (Number) nf.getCombinator();
				return n;
			}
			catch(ClassCastException e) {
				return null;
			}
		}
		else {
			Registry reg = new Registry();
			reg.setNode(nf.getNode());
			
			
			if(reg.getNode().getFunction().getCombinator().applyReduction(reg)) {
				final Node expr = reg.getNode();
				
				return new Combinator() {
					@Override
					public Node getGraph() {
						return expr;
					}

					@Override
					public boolean applyReduction(Registry registry)
							throws CompilerException {
						return false;
					}

					@Override
					public String getName() {
						return null;
					}
					
				};
			}
			else {
				Node expr = reg.getNode();
				
				if(expr.getFunction().getCombinator().getName().equals("I")) {
					Combinator c = expr.getArgument().getCombinator();
					
					if(c == null) {
						return null;
					}
					
					try {
						Number n = (Number) c;
						return n;
					}
					catch(ClassCastException e) {
						return null;
					}
				}
			}
		}
		
		return null;
	}
}
