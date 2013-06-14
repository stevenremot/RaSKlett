package compiler.combinators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe créant les Variables
 * 
 * @author remot
 *
 */
public class VariableFactory implements CombinatorFactory {
	private static Pattern varPattern;
	
	static {
		varPattern = Pattern.compile("^\\$([a-zA-Z_][a-zA-Z0-9_]*)$");
	}

	@Override
	public Combinator get(String name) {
		Matcher mat = varPattern.matcher(name);
		
		if(mat.find()) {
			return new Variable(mat.group(1));
		}
		
		return null;
	}

}
