package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class KTest {
	
	@Test
	public void testKReducesCorrectlyWithXAtomic() {
		Combinator k = new K(), x = new DummyCombinator("X"), y = new DummyCombinator("Y"),
				z = new DummyCombinator("Z");
		
		Node node1 = new Node(NodeFieldFactory.create(k), NodeFieldFactory.create(x));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		@SuppressWarnings("unused")
		Node node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(z));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(k.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals(z,n.getArgument().getCombinator());
		assertNotNull(n.getFunction().getNode());
		assertEquals(x, n.getFunction().getNode().getArgument().getCombinator());
		assertEquals("I", n.getFunction().getNode().getFunction().getCombinator().getName());
		
		Node nextNode = n.getNextNode();
		
		assertNull(nextNode);
	}
	
	@Test
	public void testKReducesCorrectlyWithXNonAtomic() {
		Combinator k = new K(), p = new DummyCombinator("P"),q = new DummyCombinator("Q"), y = new DummyCombinator("Y"),
				z = new DummyCombinator("Z");
		
		Node node0 = new Node(NodeFieldFactory.create(p), NodeFieldFactory.create(q));
		Node node1 = new Node(NodeFieldFactory.create(k), NodeFieldFactory.create(node0));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		@SuppressWarnings("unused")
		Node node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(z));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(k.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals(p,n.getFunction().getNode().getFunction().getCombinator());
		assertEquals(q,n.getFunction().getNode().getArgument().getCombinator());
		assertEquals(z,n.getArgument().getCombinator());
		
	}
	
	@Test
	public void testKKeepsNextNode() {
		Combinator k = new K(), x = new DummyCombinator("X"), y = new DummyCombinator("Y"), z = new DummyCombinator("Z");
		
		Node node1 = new Node(NodeFieldFactory.create(k), NodeFieldFactory.create(x));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		Node node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(z));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(k.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals(node3, n);
		
	}
	
	@Test
	public void testKStopsWith1Argument() {
		Combinator k = new K(), x = new DummyCombinator("X"), y = new DummyCombinator("Y");
		
		Node node1 =  new Node(NodeFieldFactory.create(k), NodeFieldFactory.create(x));
		@SuppressWarnings("unused")
		Node node2 =  new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertFalse(k.applyReduction(registry));

		
	}

}
