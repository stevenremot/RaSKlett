package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class OrTest {

	@Test
	public void testWorksCorrectly() throws CompilerException {
		Combinator or = new Or(), a = new DummyCombinator("A"),
				b = new DummyCombinator("B");
		
		Node node1 = new Node(
				NodeFieldFactory.create(or),
				NodeFieldFactory.create(a));
		
		Node node2 = new Node(
				NodeFieldFactory.create(node1),
				NodeFieldFactory.create(b));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertTrue(or.applyReduction(reg));
		
		assertEquals(node1, reg.getNode());
		
		assertEquals(a, node1.getFunction().getCombinator());
		assertEquals("true", node1.getArgument().getCombinator().getName());
		assertEquals(b, node2.getArgument().getCombinator());
	}

	@Test
	public void testStopsWith1Argument() throws CompilerException {
		Combinator or = new Or(), a = new DummyCombinator("A");
		
		Node node1 = new Node(
				NodeFieldFactory.create(or),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertFalse(or.applyReduction(reg));
	}
}
