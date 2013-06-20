package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class LazyCall implements Combinator {

	private String func;
	public LazyCall(String func) {
		this.func = func;
	}
	
	@Override
	public Node getGraph() {
		return null;
	}

	@Override
	public boolean applyReduction(Registry registry) throws CompilerException {
		// à finir et tester
		Combinator f = CombinatorManager.getInstance().get(func);
		Node node = registry.getNode().getLastNode();
		Node graph = f.getGraph();
		node.setFunction(NodeFieldFactory.create(graph));
		
		return false;
	}

	@Override
	public String getName() {
		return func;
	}

}
