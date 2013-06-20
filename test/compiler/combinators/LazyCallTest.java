package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class LazyCallTest {

	@Test
	public void testDoesNothingWhenNoFunction() throws CompilerException {
		Combinator lazy = new LazyCall("f"), a = new DummyCombinator("A");
		
		Node n = new Node(
				NodeFieldFactory.create(lazy),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(n);
		
		assertFalse(lazy.applyReduction(reg));
	}
	
	@Test
	public void testWorksWhenFunctionExists() throws CompilerException {
		Combinator lazy = new LazyCall("f"), a = new DummyCombinator("A");
		
		final Node f = new Node(
				NodeFieldFactory.create(new S()),
				NodeFieldFactory.create(new K()));
		
		CombinatorManager.getInstance().addCombinator(new Combinator() {

			@Override
			public Node getGraph() {
				return f.copy();
			}

			@Override
			public boolean applyReduction(Registry registry)
					throws CompilerException {
				return false;
			}

			@Override
			public String getName() {
				return "f";
			}
		});
		
		Node n = new Node(
				NodeFieldFactory.create(lazy),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(n);
		
		assertTrue(lazy.applyReduction(reg));
		
		Node res = reg.getNode();
		
		assertEquals("S", res.getFunction().getCombinator().getName());
		assertEquals("K", res.getArgument().getCombinator().getName());
		
		Node res2 = res.getNextNode();
		
		assertNotNull(res2);
		
		assertEquals("A", res2.getArgument().getCombinator().getName());
	}
}
