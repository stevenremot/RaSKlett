package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Combinateur servant à indiquer une ligne à éxécuter en pas-à-pas
 * L'implémentation n'a pas pu être effectuée, mais pour laisser éventullement la porte ouverte,
 * le combinateur et là, et se remplace silencieusement par I.
 *
 * @author remot
 */
public class Debug implements Combinator {

    @Override
    public Node getGraph() {
        return null;
    }

    @Override
    public boolean applyReduction(Registry registry) throws CompilerException {
        registry.getNode().setFunction(NodeFieldFactory.create(new I()));
        return true;
    }

    @Override
    public String getName() {
        return "debug";
    }
}
