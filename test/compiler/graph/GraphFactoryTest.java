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
	public void simpleGraphTest() throws EmptyStackException, BadParenthesisException, CombinatorNotFoundException{
		
		String[] example = {"S","S","K","I"};
		Node graph = GraphFactory.create(new ArrayList<String>(Arrays.asList(example)));
		
		String ret = GraphSerializer.serialize(graph);
		assertEquals(ret,"S S K I");
		assertEquals(null, graph.getNextNode());
	}
	
	@Test
	public void simpleButLongerGraphTest() throws EmptyStackException, BadParenthesisException, CombinatorNotFoundException{
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		String[] example = {"S","K","I","S","K","I"};
		Node graph = GraphFactory.create(new ArrayList<String>(Arrays.asList(example)));
			
		assertEquals(cmanager.get("I"),graph.getArgument().getCombinator());
		
		while(graph.getFunction().getCombinator() == null) {
			graph = graph.getFunction().getNode();
		}
		assertEquals(cmanager.get("K"), graph.getArgument().getCombinator());
		assertEquals(cmanager.get("S"), graph.getFunction().getCombinator());
	}
	
	@Test
	public void firstParenthesisTest() throws EmptyStackException, BadParenthesisException, CombinatorNotFoundException{
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		String[] example = {"S","K","(","S","K","K",")"};
		Node graph = GraphFactory.create(new ArrayList<String>(Arrays.asList(example)));
		
		graph.getArgument().getNode();
		assertEquals(null,graph.getArgument().getCombinator());
		assertEquals(cmanager.get("K"), graph.getArgument().getNode().getArgument().getCombinator());
		assertEquals(null,graph.getArgument().getNode().getFunction().getCombinator());
		
		String ret = GraphSerializer.serialize(graph);
		assertEquals("S K ( S K K )", ret);
		assertEquals(null, graph.getNextNode());
	}
	
	@Test
	public void doubleParenthesisTest() throws EmptyStackException, BadParenthesisException, CombinatorNotFoundException {
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		String[] example = {"S","(","S","(","S","K",")",")"};
		Node graph = GraphFactory.create(new ArrayList<String>(Arrays.asList(example)));
	
		assertEquals(cmanager.get("S"), graph.getArgument().getNode().getFunction().getCombinator());
		assertEquals(cmanager.get("S"), graph.getArgument().getNode().getArgument().getNode().getFunction().getCombinator());
		
		String ret = GraphSerializer.serialize(graph);
		assertEquals("S ( S ( S K ) )", ret);
	}
	
	@Test
	public void singleCombinatorTest() throws EmptyStackException, BadParenthesisException, CombinatorNotFoundException {
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		Stack<String> example = new Stack<String>();
		example.push("K");
		Node graph = GraphFactory.create(example);
		
		assertEquals(cmanager.get("I"), graph.getFunction().getCombinator());
		assertEquals(cmanager.get("K"), graph.getArgument().getCombinator());
	}

}
