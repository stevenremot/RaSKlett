package compiler.combinators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class TTest {

	@Test
	public void testTReducesCorrectly() {
		Combinator t = new T(), x = new DummyCombinator("X"), y = new DummyCombinator("Y");
		
		Node node1 = new Node(NodeFieldFactory.create(t), NodeFieldFactory.create(x));
		new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(t.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals("I",n.getFunction().getCombinator().getName());
		assertEquals(x, n.getArgument().getCombinator());
		
		Node nextNode = n.getNextNode();
		
		assertNull(nextNode);
	}
	
	
	@Test
	public void testTKeepsNextNode() {
		Combinator t = new T(), x = new DummyCombinator("X"), y = new DummyCombinator("Y"), f = new DummyCombinator("F");
		
		Node node1 = new Node(NodeFieldFactory.create(t), NodeFieldFactory.create(f));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(x));
		Node node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(y));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(t.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals(node3, n.getNextNode());
		
	}
	
	@Test
	public void testTStopsWith1Argument() {
		Combinator t = new T(), x = new DummyCombinator("x");
		
		Node node1 =  new Node(NodeFieldFactory.create(t), NodeFieldFactory.create(x));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertFalse(t.applyReduction(registry));

		
	}
}
