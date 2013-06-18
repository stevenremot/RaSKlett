package compiler.combinators;

/**
 * Combinateur représentant l'addition d'entier
 * 
 * @author remot
 *
 */
public class Plus extends NumberOperator {
	@Override
	public String getName() {
		return "+";
	}

	@Override
	protected int doOperation(int n1, int n2) {
		return n1 + n2;
	}
}
