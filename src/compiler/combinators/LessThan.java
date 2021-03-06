package compiler.combinators;

/**
 * Représente <
 * @author steven
 *
 */
public class LessThan extends NumberOperator {

	@Override
	public String getName() {
		return "<";
	}

	@Override
	protected Combinator doOperation(int n1, int n2) {
		if(n1 < n2) {
			return new True();
		}
		return new False();
	}

}
