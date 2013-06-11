package compiler.parser;
import static org.junit.Assert.*;

import org.junit.Test;

import compiler.parser.lexicalAnalyser;


public class lexicalAnalyserTest {

	@Test
	public void test() {
		lexicalAnalyser analyser = new lexicalAnalyser();
		analyser.readRowByRow();
	}

}
