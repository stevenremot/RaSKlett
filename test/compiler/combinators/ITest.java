package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class ITest {
	
	@Test
	public void testIReducesCorrectlyWithXAtomic() {
		Combinator i = new I(), x = new DummyCombinator("X"), y = new DummyCombinator("Y");
		
		Node node1 = new Node(NodeFieldFactory.create(i), NodeFieldFactory.create(x));
		@SuppressWarnings("unused")
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(i.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals(x, n.getFunction().getCombinator());
		assertEquals(y, n.getArgument().getCombinator());
		
		Node nextNode = n.getNextNode();
		
		assertNull(nextNode);
	}
	
	@Test
	public void testIReducesCorrectlyWithXNonAtomic() {
		Combinator i = new I(), y = new DummyCombinator("Y"),
				p = new DummyCombinator("P"), q = new DummyCombinator("Q");
		
		Node x = new Node(NodeFieldFactory.create(p), NodeFieldFactory.create(q));
		
		Node node1 = new Node(NodeFieldFactory.create(i), NodeFieldFactory.create(x));
		@SuppressWarnings("unused")
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(i.applyReduction(registry));
		
		Node n = registry.getNode();

		assertNotNull(n.getFunction().getNode());
		assertEquals(p, n.getFunction().getNode().getFunction().getCombinator());
		assertEquals(q, n.getFunction().getNode().getArgument().getCombinator());
		assertEquals(y, n.getArgument().getCombinator());
		
		Node nextNode = n.getNextNode();
		
		assertNull(nextNode);
	}
	
	@Test
	public void testIKeepsNextNode() {
		Combinator i = new I(), x = new DummyCombinator("X"), y = new DummyCombinator("Y"),
				z = new DummyCombinator("Z");
		
		Node node1 = new Node(NodeFieldFactory.create(i), NodeFieldFactory.create(x)),
				node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y)),
				node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(z));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(i.applyReduction(registry));
		
		Node n = registry.getNode().getNextNode();
		
		assertEquals(node3, n);
	}
	
	@Test
	public void testIStopsWith1Argument() {
		Combinator i = new I(), x = new DummyCombinator("X");
		
		Node node = new Node(NodeFieldFactory.create(i), NodeFieldFactory.create(x));
		
		Registry registry = new Registry();
		registry.setNode(node);
		
		assertFalse(i.applyReduction(registry));
	}

}
