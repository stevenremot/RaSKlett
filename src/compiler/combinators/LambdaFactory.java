package compiler.combinators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compiler.config.ConfigManager;

/**
 * Factory pour créer les combinateurs d'abstraction
 * 
 * reconnaît "lambda(+{0,4})"
 * 
 * @author remot
 *
 */
public class LambdaFactory implements CombinatorFactory {
	private static Pattern lambdaPattern;
	
	static {
		lambdaPattern = Pattern.compile("^lambda(\\+{0,4})$");
	}

	@Override
	public Combinator get(String name) {
		Matcher mat = lambdaPattern.matcher(name);
		
		if(mat.find()) {
			int level = ConfigManager.getInstance().getDefaultAbstractionLevel();
			if(mat.groupCount() > 0) {
				level = mat.group(1).length();
			}
			
			return new Lambda(level);
		}
		
		return null;
	}

}
