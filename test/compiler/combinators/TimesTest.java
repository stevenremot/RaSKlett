package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class TimesTest {

	@Test
	public void testWorksCorrectly() throws CompilerException {
		Combinator times = new Times(), a = new DummyCombinator("A"),
				b = new DummyCombinator("B");
		
		Node node1 = new Node(
				NodeFieldFactory.create(times),
				NodeFieldFactory.create(a));
		
		Node node2 = new Node(
				NodeFieldFactory.create(node1),
				NodeFieldFactory.create(b));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertTrue(times.applyReduction(reg));
		
		assertEquals(node1, reg.getNode());
		
		assertEquals(a, node1.getFunction().getCombinator());
		
		Node ysb = node1.getArgument().getNode();
		
		assertNotNull(ysb);
		
		assertEquals(b, ysb.getFunction().getCombinator());
		
		Node sb = ysb.getArgument().getNode();
		
		assertNotNull(sb);
		
		assertEquals("S", sb.getFunction().getCombinator().getName());
		assertEquals("B", sb.getArgument().getCombinator().getName());
		
		assertEquals(node2, node1.getNextNode());
		assertEquals("0", node2.getArgument().getCombinator().getName());
	}
	
	@Test
	public void testReturnsFalseWith1Argument() throws CompilerException {
		Combinator times = new Times(), a = new DummyCombinator("A");
		
		Node node1 = new Node(
				NodeFieldFactory.create(times),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertFalse(times.applyReduction(reg));
	}

}
