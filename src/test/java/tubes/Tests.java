package tubes;

import tubes.model.State;
import tubes.model.Tube;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class Tests {
    @Test
    public void trivialAlreadySolved() {
        List<Tube> t = new ArrayList<>();
        t.add(new Tube(3, Arrays.asList("A","A","A")));
        t.add(new Tube(3));
        State s = new State(t);
        Solver solver = new Solver(s);
        assertNotNull(solver.solve());
    }

    @Test
    public void smallCase() {
        List<Tube> t = new ArrayList<>();
        t.add(new Tube(3, Arrays.asList("A","B")));
        t.add(new Tube(3, Arrays.asList("B","A")));
        t.add(new Tube(3));
        State s = new State(t);
        Solver solver = new Solver(s);
        assertNotNull(solver.solve());
    }
}

