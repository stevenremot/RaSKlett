package compiler.combinators;

public class Minus extends NumberOperator {

	@Override
	public String getName() {
		return "-";
	}

	@Override
	protected int doOperation(int n1, int n2) {
		return n1 - n2;
	}

}
