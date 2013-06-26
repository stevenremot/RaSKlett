package compiler.graph;

import compiler.CompilerException;

/**
 * Exception pour GraphFactory. Appelée lorsqu'il y a des parenthèses non désirables.
 * @author kazmiero
 *
 */

public class BadParenthesisException extends CompilerException {

	private static final long serialVersionUID = 1L;

	public BadParenthesisException(){
		super("Parenthèse non désirée");
	}
}
