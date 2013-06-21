package compiler.combinators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @brief Factory pour créer des LazyCall
 * 
 * @author remot
 *
 */
public class LazyCallFactory implements CombinatorFactory {
	private static Pattern lazyCallPattern;
	
	static {
		lazyCallPattern = Pattern.compile("^@([a-zA-Z_][a-zA-Z_0-9]*)$");
	}
	
	@Override
	public Combinator get(String name) {
		Matcher m = lazyCallPattern.matcher(name);
		
		if(m.find()) {
			return new LazyCall(m.group(1));
		}
		
		return null;
	}

}
