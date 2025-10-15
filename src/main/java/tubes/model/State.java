package tubes.model;

import java.util.*;

public class State implements Cloneable {
    private final List<Tube> tubes;

    public State(List<Tube> tubes) {
        this.tubes = tubes;
    }

    public List<Tube> getTubes() {
        return tubes;
    }

    public State cloneState() {
        List<Tube> copy = new ArrayList<>();
        for (Tube t : tubes) {
            copy.add(t.clone());
        }
        return new State(copy);
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Tube t : tubes) {
            if (!first) {
                sb.append("|");
            }
            sb.append(t.serialize());
            first = false;
        }
        return sb.toString();
    }

    public boolean isGoal() {
        for (Tube t : tubes) {
            if (t.isEmpty()) {
                continue;
            }
            if (t.size() != t.capacity()) {
                return false;
            }
            if (!t.allSameColor()) {
                return false;
            }
        }
        return true;
    }
}
