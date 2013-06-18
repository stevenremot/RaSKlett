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

public class BTest {
	
	@Test
	public void testBReducesCorrectly() throws CompilerException {
		Combinator b = new B(), x = new DummyCombinator("X"), f = new DummyCombinator("F"),
			g = new DummyCombinator("G");
		
		Node node1 = new Node(NodeFieldFactory.create(b), NodeFieldFactory.create(f));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(g));
		new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(x));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(b.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals(f, n.getFunction().getCombinator());
		Node node = n.getArgument().getNode();
		assertEquals(g, node.getFunction().getCombinator());
		assertEquals(x, node.getArgument().getCombinator());
		
		Node nextNode = n.getNextNode();
		
		assertNull(nextNode);
	}
	
	@Test
	public void testBKeepsNextNode() throws CompilerException {
		Combinator b = new B(), x = new DummyCombinator("X"), f = new DummyCombinator("F"),
				g = new DummyCombinator("G"), y = new DummyCombinator("Y");
		
		Node node1 = new Node(NodeFieldFactory.create(b), NodeFieldFactory.create(f)),
				node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(g)),
				node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(x)),
				node4 = new Node(NodeFieldFactory.create(node3), NodeFieldFactory.create(y));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(b.applyReduction(registry));
		
		Node n = registry.getNode().getNextNode();
		
		assertEquals(node4, n);
	}
	
	public void testBStopsWith1Argument() throws CompilerException {
		Combinator b = new B(), f = new DummyCombinator("F");
		
		Node node1 =  new Node(NodeFieldFactory.create(b), NodeFieldFactory.create(f));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertFalse(b.applyReduction(registry));

		
	}
	
	public void testBStopsWith2Arguments() throws CompilerException {
		Combinator b = new B(), f = new DummyCombinator("F"),
				g = new DummyCombinator("G");
		
		Node node1 =  new Node(NodeFieldFactory.create(b), NodeFieldFactory.create(f));
		new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(g));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertFalse(b.applyReduction(registry));

		
	}

}
