package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class NumberTest {

	@Test
	public void testZeroWorksCorrectly() throws CompilerException {
		Combinator zero = new Number(0), a = new DummyCombinator("A"),
				b = new DummyCombinator("B");
		
		Node node1 = new Node(
				NodeFieldFactory.create(zero),
				NodeFieldFactory.create(a));
		
		Node node2 = new Node(
				NodeFieldFactory.create(node1),
				NodeFieldFactory.create(b));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertTrue(zero.applyReduction(reg));
		
		Node result = reg.getNode();
		
		assertEquals(result, node2);
		assertEquals("I", result.getFunction().getCombinator().getName());
		assertEquals(b, result.getArgument().getCombinator());
		
		node1 = new Node(
				NodeFieldFactory.create(zero),
				NodeFieldFactory.create(a));
		
		reg.setNode(node1);
		
		assertFalse(zero.applyReduction(reg));
	}
	
	@Test
	public void testNonZeroorksCorrectly() throws CompilerException {
		Combinator two = new Number(2), a = new DummyCombinator("A"),
				b = new DummyCombinator("B");
		
		Node node1 = new Node(
				NodeFieldFactory.create(two),
				NodeFieldFactory.create(a));
		
		new Node(
				NodeFieldFactory.create(node1),
				NodeFieldFactory.create(b));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertTrue(two.applyReduction(reg));
		
		Node n = reg.getNode();
		
		assertEquals("I", n.getFunction().getCombinator().getName());
		
		for(int i=0; i < 2; i++) {
			assertEquals(a, n.getArgument().getCombinator());
			
			n = n.getNextNode();
			assertNotNull(n);
		}
		
		assertEquals(b, n.getArgument().getCombinator());
	}

}
