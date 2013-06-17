package compiler.combinators;

/**
 * Factory pour les nombres
 * 
 * @author remot
 *
 */
public class NumberFactory implements CombinatorFactory {

	@Override
	public Combinator get(String name) {
		int value;
		try {
			value = Integer.parseInt(name);
		}
		catch(NumberFormatException e) {
			return null;
		}
		
		if(value < 0) {
			return null;
		}
		
		return new Number(value);
	}

}
