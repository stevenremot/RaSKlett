package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class AndTest {

	@Test
	public void testWorksCorrectly() {
		Combinator and = new And(), a = new DummyCombinator("A"),
				b = new DummyCombinator("B");
		
		Node node1 = new Node(
				NodeFieldFactory.create(and),
				NodeFieldFactory.create(a));
		
		Node node2 = new Node(
				NodeFieldFactory.create(node1),
				NodeFieldFactory.create(b));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertTrue(and.applyReduction(reg));
		
		assertEquals(node1, reg.getNode());
		
		assertEquals(a, node1.getFunction().getCombinator());
		assertEquals(b, node1.getArgument().getCombinator());
		assertEquals("false", node2.getArgument().getCombinator().getName());
	}
	
	@Test
	public void testStopsWith1Argument() {
		Combinator and = new And(), a = new DummyCombinator("A");
		
		Node node1 = new Node(
				NodeFieldFactory.create(and),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertFalse(and.applyReduction(reg));
	}

}
