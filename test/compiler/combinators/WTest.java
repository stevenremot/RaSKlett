package compiler.combinators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class WTest {
	
	@Test
	public void testWReducesCorrectly() {
		Combinator w = new W(), x = new DummyCombinator("X"), f = new DummyCombinator("F");
		
		Node node1 = new Node(NodeFieldFactory.create(w), NodeFieldFactory.create(f));
		new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(x));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(w.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals(f,n.getFunction().getCombinator());
		assertEquals(x, n.getArgument().getCombinator());
		assertEquals(x, n.getNextNode().getArgument().getCombinator());
		
		Node nextNode = n.getNextNode().getNextNode();
		
		assertNull(nextNode);
	}
	
	
	@Test
	public void testKKeepsNextNode() {
		Combinator w = new W(), x = new DummyCombinator("X"), y = new DummyCombinator("Y"), f = new DummyCombinator("F");
		
		Node node1 = new Node(NodeFieldFactory.create(w), NodeFieldFactory.create(f));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(x));
		Node node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(y));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(w.applyReduction(registry));
		
		Node n = registry.getNode().getNextNode();
		
		assertEquals(node3, n.getNextNode());
		
	}
	
	@Test
	public void testKStopsWith1Argument() {
		Combinator w = new W(), f = new DummyCombinator("F");
		
		Node node1 =  new Node(NodeFieldFactory.create(w), NodeFieldFactory.create(f));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertFalse(w.applyReduction(registry));

		
	}

}
