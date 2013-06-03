package compiler.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.combinators.Combinator;
import compiler.combinators.DummyCombinator;

public class GraphSerializerTest {

	@Test
	public void testParsesSimpleGraphCorrectly() {
		Combinator a = new DummyCombinator("A"),
				b = new DummyCombinator("B"),
				c = new DummyCombinator("C");
		
		Node node1 = new Node(NodeFieldFactory.create(a), NodeFieldFactory.create(b));
		new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(c));
		
		String ret = GraphSerializer.serialize(node1);
		
		assertEquals("A B C", ret);
	}
	
	@Test
	public void testCanStartFromEnd() {
		Combinator a = new DummyCombinator("A"),
				b = new DummyCombinator("B"),
				c = new DummyCombinator("C");
		
		Node node1 = new Node(NodeFieldFactory.create(a), NodeFieldFactory.create(b));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(c));
		
		String ret = GraphSerializer.serialize(node2);
		
		assertEquals("A B C", ret);
	}
	
	@Test
	public void testParsesComplexGraphCorrectly() {
		Combinator a = new DummyCombinator("A"),
				b = new DummyCombinator("B"),
				c = new DummyCombinator("C"),
				d = new DummyCombinator("D");
		
		Node argNode = new Node(NodeFieldFactory.create(b), NodeFieldFactory.create(c));
		Node node1 = new Node(NodeFieldFactory.create(a), NodeFieldFactory.create(argNode));
		new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(d));
		
		String ret = GraphSerializer.serialize(node1);
		
		assertEquals("A ( B C ) D", ret);
	}
}
