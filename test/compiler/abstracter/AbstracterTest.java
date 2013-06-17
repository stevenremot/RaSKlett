package compiler.abstracter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import compiler.graph.NodeFieldFactory;
import compiler.graph.GraphSerializer;
import compiler.graph.Node;
import compiler.combinators.Combinator;
import compiler.combinators.DummyCombinator;
import compiler.combinators.Var;

public class AbstracterTest {
	
	@Test
	public void firstAbstractionTest(){
		
		
		Var var = new Var("$x");
		Combinator S = new DummyCombinator("S"); 
		Combinator K = new DummyCombinator("K");
		
		// lambda+ x . S x K
		Node root = new Node(NodeFieldFactory.create(S),NodeFieldFactory.create(var));
		Node second = new Node(NodeFieldFactory.create(root),NodeFieldFactory.create(K));
		root.setNextNode(second);
		
		Abstracter ab = new Abstracter(root,1);
		Node result = ab.abstraction(root,1,var);
		String ret = GraphSerializer.serialize(result);
		
		assertEquals(ret,"S ( S ( K S ) I ) ( K K )");
	}
	
	@Test
	public void firstAbstractionParenthesisTest(){
		
		
		Var var = new Var("$x");
		Combinator S = new DummyCombinator("S"); 
		Combinator K = new DummyCombinator("K");
		
		// lambda+ x . S (x K)
		Node child = new Node(NodeFieldFactory.create(var),NodeFieldFactory.create(K));
		Node root = new Node(NodeFieldFactory.create(S),NodeFieldFactory.create(child));
		
		Abstracter ab = new Abstracter(root,1);
		Node result = ab.abstraction(root,1,var);
		String ret = GraphSerializer.serialize(result);
		
		assertEquals(ret,"S ( K S ) ( S I ( K K ) )");
	}
	
	@Test
	public void firstAbstractionParenthesisTest2(){
		
		
		Var var = new Var("$x");
		Combinator S = new DummyCombinator("S"); 
		Combinator K = new DummyCombinator("K");
		
		// lambda+ x . S (S x K)
		Node childRoot = new Node(NodeFieldFactory.create(S),NodeFieldFactory.create(var));
		Node child = new Node(NodeFieldFactory.create(childRoot),NodeFieldFactory.create(K));
		childRoot.setNextNode(child);
		Node root = new Node(NodeFieldFactory.create(S),NodeFieldFactory.create(child));
		
		Abstracter ab = new Abstracter(root,1);
		Node result = ab.abstraction(root,1,var);
		String ret = GraphSerializer.serialize(result);
		
		assertEquals(ret,"S ( K S ) ( S ( S ( K S ) I ) ( K K ) )");
	}


}
