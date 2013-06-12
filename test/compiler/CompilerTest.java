package compiler;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

public class CompilerTest {

	@Test
	public void testReducesOneStepCorrectly() {
		StringReader input = new StringReader("S S K I");
		
		CompilerCallback callback = new CompilerCallback() {
			public void onResult(String result, int line, int position, boolean finished) {
				if(finished) {
					assertEquals("S I ( K I )", result);
				}
			}
			
			public void onFailure(CompilerException e) {
				fail();
			}
		};
		
		Compiler comp = new Compiler(input, callback);
		
		assertTrue(comp.reduceStep());
		
		assertFalse(comp.reduceStep());
	}
	
	@Test
	public void testReducesAllCorrectly() throws InterruptedException {
		StringReader input = new StringReader("S K K K");
		
		CompilerCallback callback = new CompilerCallback() {
			public void onResult(String result, int line, int position, boolean finished) {
				assertTrue(finished);
				assertEquals("K", result);
			}
			
			public void onFailure(CompilerException e) {
				fail();
			}
		};
		
		Compiler comp = new Compiler(input, callback);
		
		Thread t = comp.reduceAll();
		
		t.join();
	}
	
	@Test
	public void testReducesInstructionCorrectly() throws InterruptedException {
		StringReader input = new StringReader("S K K K; K I;");
		
		CompilerCallback callback = new CompilerCallback() {
			public void onResult(String result, int line, int position, boolean finished) {
				assertFalse(finished);
				assertEquals("K", result);
			}
			
			public void onFailure(CompilerException e) {
				fail();
			}
		};
		
		Compiler comp = new Compiler(input, callback);
		
		Thread t = comp.reduceInstruction();
		
		t.join();
	}
	
	@Test
	public void testInfiniteReductionCanStopWithAll() throws InterruptedException {
		StringReader input = new StringReader("S I I (S I I)");
		
		CompilerCallback callback = new CompilerCallback() {
			public void onResult(String result, int line, int position, boolean finished) {
				assertFalse(finished);
			}
			
			public void onFailure(CompilerException e) {
				fail();
			}
		};
		
		Compiler comp = new Compiler(input, callback);
		
		Thread t = comp.reduceAll();
		
		comp.stopReduction();
		
		t.join();
	}
	
	@Test
	public void testInfiniteReductionCanStopWithInstruction() throws InterruptedException {
		StringReader input = new StringReader("S I I (S I I)");
		
		CompilerCallback callback = new CompilerCallback() {
			public void onResult(String result, int line, int position, boolean finished) {
				assertFalse(finished);
			}
			
			public void onFailure(CompilerException e) {
				fail();
			}
		};
		
		Compiler comp = new Compiler(input, callback);
		
		Thread t = comp.reduceInstruction();
		
		comp.stopReduction();
		
		t.join();
	}
	
	@Test
	public void testParserExceptionAreThrown() {
		StringReader input = new StringReader("S (");
		
		CompilerCallback callback = new CompilerCallback() {

			@Override
			public void onResult(String reducedGraph, int line, int position, boolean finished) {
				fail();
				
			}

			@Override
			public void onFailure(CompilerException e) {
			}		
		};
		
		new Compiler(input, callback);
	}
	
	@Test
	public void testGraphExceptionThrown() throws InterruptedException {
		StringReader input = new StringReader("blabla");
		
		CompilerCallback callback = new CompilerCallback() {

			@Override
			public void onResult(String reducedGraph, int line, int position, boolean finished) {
				fail();
				
			}

			@Override
			public void onFailure(CompilerException e) {
			}		
		};
		
		Compiler c = new Compiler(input, callback);
		Thread t = c.reduceInstruction();
		t.join();
	}
}
