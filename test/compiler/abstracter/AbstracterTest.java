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

}
