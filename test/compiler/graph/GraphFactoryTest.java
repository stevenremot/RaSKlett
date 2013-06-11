package compiler.graph;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import org.junit.Test;

import compiler.combinators.CombinatorManager;

public class GraphFactoryTest {
	
	@Test
	public void parserTest() {
		
		String[] example = {"(","(","I","K",")",")"};
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(example));
		GraphFactory.parseParenthesis(list);
		
		assertEquals("I",list.get(0));
		assertEquals(2,list.size());
	}
	
	@Test
	public void parserTest2() {
		
		String[] example = {"S","K","(","(","S","K",")","K",")"};
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(example));
		GraphFactory.parseParenthesis(list);
		
		assertEquals("(",list.get(2));
		assertEquals(")",list.get(6));
		assertEquals(7,list.size());
	}
	
	@Test
	public void parserTest3() {
		
		String[] example = {"(","S","K","(","S",")","K",")"};
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(example));
		GraphFactory.parseParenthesis(list);
		
		assertEquals("S",list.get(2));
		assertEquals("K",list.get(3));
		assertEquals(4,list.size());
	}
	
	@Test
	public void simpleGraphTest() throws EmptyStackException, BadParenthesisException{
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		String[] example = {"S","K","I"};
		Node graph = GraphFactory.create(example);
		
		/*
		Stack<String> example = new Stack<String>();
		example.push("I");
		example.push("K");
		example.push("S"); // stack à remplir dans l'ordre inverse
		Node graph = GraphFactory.create(example,null);
		*/
		
		
		assertEquals(cmanager.get("I"),graph.getArgument().getCombinator());
		Node firstNode = graph.getFunction().getNode();
		
		assertEquals(cmanager.get("K"), firstNode.getArgument().getCombinator());
		assertEquals(cmanager.get("S"), firstNode.getFunction().getCombinator());
	}
	
	@Test
	public void simpleButLongerGraphTest() throws EmptyStackException, BadParenthesisException{
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		String[] example = {"S","K","I","S","K","I"};
		Node graph = GraphFactory.create(example);
		
		/*
		Stack<String> example = new Stack<String>();
		example.push("I");
		example.push("I");
		example.push("K");
		example.push("S");
		Node graph = GraphFactory.create(example,null);
		*/
		
		assertEquals(cmanager.get("I"),graph.getArgument().getCombinator());
		
		while(graph.getFunction().getCombinator() == null) {
			graph = graph.getFunction().getNode();
		}
		assertEquals(cmanager.get("K"), graph.getArgument().getCombinator());
		assertEquals(cmanager.get("S"), graph.getFunction().getCombinator());
	}
	
	@Test
	public void firstParenthesisTest() throws EmptyStackException, BadParenthesisException{
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		String[] example = {"S","K","(","S","K","K",")"};
		Node graph = GraphFactory.create(example);
		
		/*
		Stack<String> example = new Stack<String>();
		example.push(")");
		example.push("K");
		example.push("K");
		example.push("S");
		example.push("(");
		example.push("K");
		example.push("S");
		
		Node graph = GraphFactory.create(example,null);
		*/
		
		graph.getArgument().getNode();
		assertEquals(null,graph.getArgument().getCombinator());
		assertEquals(cmanager.get("K"), graph.getArgument().getNode().getArgument().getCombinator());
		assertEquals(null,graph.getArgument().getNode().getFunction().getCombinator());
	}
	
	@Test
	public void doubleParenthesisTest() throws EmptyStackException, BadParenthesisException {
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		String[] example = {"S","(","S","(","S","K",")",")"};
		Node graph = GraphFactory.create(example);
		// S K ( S ( S K ) )
		/*
		Stack<String> example = new Stack<String>();
		example.push(")");
		example.push(")");
		example.push("K");
		example.push("S");
		example.push("(");
		example.push("S");
		example.push("(");
		example.push("S");
		Node graph = GraphFactory.create(example,null);
		*/
		assertEquals(cmanager.get("S"), graph.getArgument().getNode().getFunction().getCombinator());
		assertEquals(cmanager.get("S"), graph.getArgument().getNode().getArgument().getNode().getFunction().getCombinator());
	}
	
	@Test
	public void singleCombinatorTest() throws EmptyStackException, BadParenthesisException {
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		Stack<String> example = new Stack<String>();
		example.push("K");
		Node graph = GraphFactory.create(example,null);
		
		assertEquals(cmanager.get("I"), graph.getFunction().getCombinator());
		assertEquals(cmanager.get("K"), graph.getArgument().getCombinator());
	}

}
