package compiler.abstracter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import compiler.graph.NodeFieldFactory;
import compiler.graph.GraphSerializer;
import compiler.graph.Node;
import compiler.graph.NodeNodeField;
import compiler.combinators.Combinator;
import compiler.combinators.DummyCombinator;
import compiler.combinators.Lambda;
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
		
		// lambda+ x . x et lambda+ x . K
		root = new Node(new NodeNodeField(null), NodeFieldFactory.create(K));
		result = ab.abstraction(root,1,var);
		ret = GraphSerializer.serialize(result);
		assertEquals(ret,"K K");
		
		root = new Node(new NodeNodeField(null), NodeFieldFactory.create(var));
		result = ab.abstraction(root,1,var);
		ret = GraphSerializer.serialize(result);
		assertEquals(ret,"I I");
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
	
	@Test
	public void findAbstracterTest(){
		
		Lambda lambdaPlus = new Lambda(1);
		Var var = new Var("$x");
		Combinator S = new DummyCombinator("S"); 
		Combinator K = new DummyCombinator("K");
		
		// lambda+ x . S (x K)
		Node root = new Node(NodeFieldFactory.create(lambdaPlus), NodeFieldFactory.create(var));
		Node second = new Node(NodeFieldFactory.create(root), NodeFieldFactory.create(S));
		root.setNextNode(second);
		Node child = new Node(NodeFieldFactory.create(var),NodeFieldFactory.create(K));
		Node third = new Node(NodeFieldFactory.create(second), NodeFieldFactory.create(child));
		second.setNextNode(third);
		
		Abstracter ab = new Abstracter(root,1);
		Node result = ab.findAbstracter(root);
		String ret = GraphSerializer.serialize(result);
		
		assertEquals(ret,"S ( K S ) ( S I ( K K ) )");
		
	}

	@Test
	public void twoAbstractersTest(){
	
		Lambda lambdaPlus = new Lambda(1);
		Var var = new Var("$x");
		Var var2 = new Var("$y");
		
		Node root = new Node(NodeFieldFactory.create(lambdaPlus), NodeFieldFactory.create(var));
		Node second = new Node(NodeFieldFactory.create(root), NodeFieldFactory.create(lambdaPlus));
		root.setNextNode(second);
		Node third = new Node(NodeFieldFactory.create(second), NodeFieldFactory.create(var2));
		second.setNextNode(third);
		Node fourth = new Node(NodeFieldFactory.create(third), NodeFieldFactory.create(var));
		third.setNextNode(fourth);
		
		Abstracter ab = new Abstracter(root,1);
		Node result = ab.findAbstracter(root);
		String ret = GraphSerializer.serialize(result);
		
		assertEquals(ret,"S ( K K ) I");
	}
}
