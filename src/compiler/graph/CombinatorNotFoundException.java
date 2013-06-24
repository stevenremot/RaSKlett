package compiler.graph;

import compiler.CompilerException;

public class CombinatorNotFoundException extends CompilerException {

	private static final long serialVersionUID = 1L;

	public CombinatorNotFoundException(String name){
		super("Combinateur " + name + " inconnu");
	}
}
