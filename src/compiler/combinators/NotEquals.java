package compiler.combinators;

/**
 * Représente l'inégalité entre entiers
 * @author steven
 *
 */
public class NotEquals extends NumberOperator {

	@Override
	public String getName() {
		return "!=";
	}

	@Override
	protected Combinator doOperation(int n1, int n2) {
		if(n1 == n2) {
			return new False();
		}
		return new True();
	}

}
