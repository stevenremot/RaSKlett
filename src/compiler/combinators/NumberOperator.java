package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeField;
import compiler.reducer.Registry;

/**
 * @brief Classe abstraite pour un opérateur prenant deux entiers
 * 
 * Définit des méthodes pour s'assurer que les deux opérandes sont entières
 * 
 * @author remot
 *
 */
public final class NumberOperator {
	
	/**
	 * Effectue le traitement sur une opérande, et retourne le combinateur correspondany
	 * 
	 * @param nf
	 * @return le Number correspondant, ou un Combinator anonyme contenant un graphe de l'expression qui a été réduite, ou null si il ne peut y avoir de nombre 
	 * @throws CompilerException 
	 */
	public static Combinator ensureIsNumber(NodeField nf) throws CompilerException {
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
