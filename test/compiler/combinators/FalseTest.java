package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class FalseTest {

	@Test
	public void testWorksCorrectly() {
		Combinator f = new False(), a = new DummyCombinator("A"),
				b = new DummyCombinator("B");
		
		Node node1 = new Node(
				NodeFieldFactory.create(f),
				NodeFieldFactory.create(a));
		
		new Node(
				NodeFieldFactory.create(node1),
				NodeFieldFactory.create(b));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertTrue(f.applyReduction(reg));
		
		Node result = reg.getNode();
		
		assertEquals("I", result.getFunction().getCombinator().getName());
		assertEquals(b, result.getArgument().getCombinator());
	}
	
	@Test
	public void testStopsWith1Argument() {
		Combinator f = new False(), a = new DummyCombinator("A");
		
		Node node1 = new Node(
				NodeFieldFactory.create(f),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertFalse(f.applyReduction(reg));
	}

}
