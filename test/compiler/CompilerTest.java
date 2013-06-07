package compiler;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

public class CompilerTest {

	@Test
	public void testReducesOneStepCorrectly() {
		StringReader input = new StringReader("S X Y Z");
		
		CompilerCallback callback = new CompilerCallback() {
			public void onResult(String result, boolean finished) {
				if(finished) {
					assertEquals("X Z ( Y Z )", result);
				}
			}
			
			public void onFailure(String message) {
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
			public void onResult(String result, boolean finished) {
				assertTrue(finished);
				assertEquals("K", result);
			}
			
			public void onFailure(String message) {
				fail();
			}
		};
		
		Compiler comp = new Compiler(input, callback);
		
		Thread t = comp.reduceAll();
		
		t.join();
	}
	
	@Test
	public void testInfiniteReductionCanStop() throws InterruptedException {
		StringReader input = new StringReader("S I I (S I I)");
		
		CompilerCallback callback = new CompilerCallback() {
			public void onResult(String result, boolean finished) {
				assertFalse(finished);
			}
			
			public void onFailure(String message) {
				fail();
			}
		};
		
		Compiler comp = new Compiler(input, callback);
		
		Thread t = comp.reduceAll();
		
		t.join();
		
		comp.stopReduction();
	}
	
	@Test
	public void testParserExceptionAreThrown() {
		StringReader input = new StringReader("S (");
		
		CompilerCallback callback = new CompilerCallback() {

			@Override
			public void onResult(String reducedGraph, boolean finished) {
				fail();
				
			}

			@Override
			public void onFailure(String message) {
			}		
		};
		
		Compiler comp = new Compiler(input, callback);
	}
	
	@Test
	public void testGraphExceptionThrown() {
		StringReader input = new StringReader("blabla");
		
		CompilerCallback callback = new CompilerCallback() {

			@Override
			public void onResult(String reducedGraph, boolean finished) {
				fail();
				
			}

			@Override
			public void onFailure(String message) {
			}		
		};
		
		Compiler comp = new Compiler(input, callback);
	}
}
