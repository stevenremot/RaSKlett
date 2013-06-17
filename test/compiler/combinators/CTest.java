package compiler.combinators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class CTest {
	
	@Test
	public void testCReducesCorrectly() throws CompilerException {
		Combinator c = new C(), x = new DummyCombinator("X"), f = new DummyCombinator("F"),
			y = new DummyCombinator("Y");
		
		Node node1 = new Node(NodeFieldFactory.create(c), NodeFieldFactory.create(f));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(x));
		new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(y));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(c.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals(f, n.getFunction().getCombinator());
		assertEquals(y, n.getArgument().getCombinator());
		assertEquals(x, n.getNextNode().getArgument().getCombinator());
		
		Node nextNode = n.getNextNode().getNextNode();
		
		assertNull(nextNode);
	}
	
	@Test
	public void testCKeepsNextNode() throws CompilerException {
		Combinator c = new C(), x = new DummyCombinator("X"), f = new DummyCombinator("F"),
				z = new DummyCombinator("Z"), y = new DummyCombinator("Y");
		
		Node node1 = new Node(NodeFieldFactory.create(c), NodeFieldFactory.create(f)),
				node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(x)),
				node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(y)),
				node4 = new Node(NodeFieldFactory.create(node3), NodeFieldFactory.create(z));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(c.applyReduction(registry));
		
		Node n = registry.getNode().getNextNode().getNextNode();
		
		assertEquals(node4, n);
	}
	
	public void testCStopsWith1Argument() throws CompilerException {
		Combinator c = new C(), f = new DummyCombinator("F");
		
		Node node1 =  new Node(NodeFieldFactory.create(c), NodeFieldFactory.create(f));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertFalse(c.applyReduction(registry));

		
	}
	
	public void testCStopsWith2Arguments() throws CompilerException {
		Combinator c = new C(), f = new DummyCombinator("F"),
				x = new DummyCombinator("X");
		
		Node node1 =  new Node(NodeFieldFactory.create(c), NodeFieldFactory.create(f));
		new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(x));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertFalse(c.applyReduction(registry));

		
	}


}
