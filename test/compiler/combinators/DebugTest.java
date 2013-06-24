package compiler.combinators;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

import org.junit.Test;
import static org.junit.Assert.*;

public class DebugTest {

    @Test
    public void testDebugReplacesWithI() throws CompilerException {
        Combinator debug = new Debug(), a = new DummyCombinator("A");

        Node n = new Node(
                NodeFieldFactory.create(debug),
                NodeFieldFactory.create(a)
        );

        Registry reg = new Registry();
        reg.setNode(n);

        assertTrue(debug.applyReduction(reg));

        assertEquals(n, reg.getNode());
        assertEquals("I", n.getFunction().getCombinator().getName());
        assertEquals(a, n.getArgument().getCombinator());

    }
}
