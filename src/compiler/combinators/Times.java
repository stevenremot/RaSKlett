package compiler.combinators;

/**
 * Combinateur représentant la mutliplication d'entiers
 * 
 * @author remot
 *
 */
public class Times extends NumberOperator {

	@Override
	public String getName() {
		return "*";
	}

	@Override
	protected int doOperation(int n1, int n2) {
		return n1 * n2;
	}
	
}
