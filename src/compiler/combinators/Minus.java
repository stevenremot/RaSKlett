package compiler.combinators;

public class Minus extends NumberOperator {

	@Override
	public String getName() {
		return "-";
	}

	@Override
	protected Combinator doOperation(int n1, int n2) {
		return new Number(n1 - n2);
	}

}
