package compiler.combinators;

/**
 * Factory pour les variables
 * @author kazmiero
 *
 */
public class VarFactory implements CombinatorFactory {

	@Override
	public Combinator get(String name) {
		
		if(name.length() > 1 && name.charAt(0) == '$')
			return new Var(name);
		else
			return null;	
	}

}
