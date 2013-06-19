package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;

public class NumberOperatorTest {

	@Test
	public void testReturnsANumber() throws CompilerException {
		
		class Test extends NumberOperator {
			@Override
			public String getName() {
				return null;
			}

			@Override
			protected Combinator doOperation(int n1, int n2) {
				return null;
			}
			
			public void test() throws CompilerException {
				Number c = new Number(10);
				
				Combinator ret = ensureIsNumber(NodeFieldFactory.create(c));
				
				assertEquals(ret, c);
			}
		}
	(new Test()).test();
	}
	
	@Test
	public void testReducesExpression() throws CompilerException {
		class Test extends NumberOperator {
			@Override
			public String getName() {
				return null;
			}

			@Override
			protected Combinator doOperation(int n1, int n2) {
				return null;
			}
			
			public void test() throws CompilerException {
				Node node1 = new Node(
						NodeFieldFactory.create(new K()),
						NodeFieldFactory.create(new Number(5)));
				
				new Node(
						NodeFieldFactory.create(node1),
						NodeFieldFactory.create(new I()));
				
				Combinator ret = ensureIsNumber(NodeFieldFactory.create(node1));
				
				assertNotNull(ret);
				
				Node graph = ret.getGraph();
				
				assertNotNull(graph);
				
				assertEquals("I", graph.getFunction().getCombinator().getName());
				assertEquals("5", graph.getArgument().getCombinator().getName());
				assertNull(graph.getNextNode());
				
				ret = ensureIsNumber(NodeFieldFactory.create(graph));
				
				assertEquals("5", ret.getName());
			}
		}
	(new Test()).test();
	}
	
	@Test
	public void testReturnsNullOnNotNumber() throws CompilerException {
		class Test extends NumberOperator {
			public void test() throws CompilerException {
				Node node1 = new Node(
						NodeFieldFactory.create(new K()),
						NodeFieldFactory.create(new Number(5)));

				Combinator ret = ensureIsNumber(NodeFieldFactory.create(node1));

				assertNull(ret);

				ret = ensureIsNumber(NodeFieldFactory.create(new K()));

				assertNull(ret);
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			protected Combinator doOperation(int n1, int n2) {
				return null;
			}
		}
		
		(new Test()).test();
	}

}
