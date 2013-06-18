package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class VectorTest {

	@Test
	public void testWorksCorrectly() throws CompilerException {
		Combinator vec = new Vector(), a = new DummyCombinator("A"), b = new DummyCombinator("B"),
				c = new DummyCombinator("C");
		
		Node node1 = new Node(
				NodeFieldFactory.create(vec),
				NodeFieldFactory.create(a));
		
		Node node2 = new Node(
				NodeFieldFactory.create(node1),
				NodeFieldFactory.create(b));
		
		Node node3 = new Node(
				NodeFieldFactory.create(node2),
				NodeFieldFactory.create(c));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertTrue(vec.applyReduction(reg));
		
		Node res = reg.getNode();
		
		assertEquals(res, node2);
		assertEquals(res.getNextNode(), node3);
		
		assertEquals(c, node2.getFunction().getCombinator());
		assertEquals(a, node2.getArgument().getCombinator());
		assertEquals(b, node3.getArgument().getCombinator());
	}
	
	@Test
	public void testStopsWith2Arguments() throws CompilerException {
		Combinator vec = new Vector(), a = new DummyCombinator("A"), b = new DummyCombinator("B");
		
		Node node1 = new Node(
				NodeFieldFactory.create(vec),
				NodeFieldFactory.create(a));
		
		new Node(
				NodeFieldFactory.create(node1),
				NodeFieldFactory.create(b));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertFalse(vec.applyReduction(reg));
	}

	@Test
	public void testStopsWith1Argument() throws CompilerException {
		Combinator vec = new Vector(), a = new DummyCombinator("A");
		
		Node node1 = new Node(
				NodeFieldFactory.create(vec),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertFalse(vec.applyReduction(reg));
	}
}
