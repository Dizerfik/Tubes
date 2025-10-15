package tubes.model;

public class Move {
    public final int from;
    public final int to;

    public Move(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "(" + (from + 1) + ", " + (to + 1) + ")";
    }
}
