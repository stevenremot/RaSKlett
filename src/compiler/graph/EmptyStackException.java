package compiler.graph;

import compiler.CompilerException;

/**
 * @brief Exception pour GraphFactory. Appelée lorsque la pile est vide à l'initialisation.
 * @author kazmiero
 *
 */

public class EmptyStackException extends CompilerException {
	
	private static final long serialVersionUID = 1L;

	public EmptyStackException(){
		super("GraphFactory Stack is empty");
	}

}
