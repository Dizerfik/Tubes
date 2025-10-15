package tubes;

import tubes.model.*;

import java.util.*;

public class Solver {
    private final State initial;
    private final long visitedLimit = 20_000_000L;

    private static class ParentInfo {
        final String parentKey;
        final Move move;
        ParentInfo(String parentKey, Move move) {
            this.parentKey = parentKey;
            this.move = move;
        }
    }

    public Solver(State initial) {
        this.initial = initial;
    }

    public List<Move> solve() {
        String startKey = initial.serialize();

        Deque<State> stack = new ArrayDeque<>();
        Deque<String> keyStack = new ArrayDeque<>();

        Set<String> visited = new HashSet<>();
        Map<String, ParentInfo> parent = new HashMap<>();

        stack.push(initial.cloneState());
        keyStack.push(startKey);
        visited.add(startKey);
        parent.put(startKey, null);

        long iterations = 0;

        while (!stack.isEmpty()) {
            State cur = stack.pop();
            String curKey = keyStack.pop();

            iterations++;
            if ((iterations & 0x1FFFF) == 0) {
                System.out.println(
                        "iter=" + iterations + " visited=" + visited.size());
            }

            if (cur.isGoal()) {
                List<Move> path = new ArrayList<>();
                String k = curKey;
                while (parent.get(k) != null) {
                    ParentInfo pi = parent.get(k);
                    path.add(pi.move);
                    k = pi.parentKey;
                }
                Collections.reverse(path);
                System.out.println(
                        "Iterations: " + iterations + ", visited=" +
                                visited.size());
                return path;
            }

            ParentInfo curParent = parent.get(curKey);
            Move parentMove = (curParent == null) ? null : curParent.move;

            List<Move> moves = generateMoves(cur);

            for (Move m : moves) {
                if (parentMove != null && m.from == parentMove.to &&
                        m.to == parentMove.from) {
                    continue;
                }

                State next = applyMove(cur, m);
                String nextKey = next.serialize();

                if (nextKey.equals(curKey)) {
                    continue;
                }
                if (visited.contains(nextKey)) {
                    continue;
                }

                visited.add(nextKey);
                parent.put(nextKey, new ParentInfo(curKey, m));
                stack.push(next);
                keyStack.push(nextKey);

                if (visited.size() > visitedLimit) {
                    System.err.println(
                            "Visited limit exceeded (" + visitedLimit +
                                    "). Aborting.");
                    return null;
                }
            }
        }
        return null;
    }

    private List<Move> generateMoves(State state) {
        List<Move> res = new ArrayList<>();
        List<Tube> tubes = state.getTubes();
        int n = tubes.size();
        for (int i = 0; i < n; i++) {
            Tube a = tubes.get(i);
            if (a.isEmpty()) {
                continue;
            }
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                Tube b = tubes.get(j);
                if (b.isFull()) {
                    continue;
                }
                String top = a.top();
                if (!b.isEmpty() && !b.top().equals(top)) {
                    continue;
                }
                int canTake = a.topCount();
                int space = b.capacity() - b.size();
                int moveAmt = Math.min(canTake, space);
                if (moveAmt <= 0) {
                    continue;
                }
                res.add(new Move(i, j));
            }
        }
        return res;
    }

    private State applyMove(State state, Move m) {
        State next = state.cloneState();
        Tube a = next.getTubes().get(m.from);
        Tube b = next.getTubes().get(m.to);

        int canTake = a.topCount();
        int space = b.capacity() - b.size();
        int moveAmt = Math.min(canTake, space);
        if (moveAmt <= 0) {
            return next;
        }

        List<String> group = a.popMany(moveAmt);
        b.pushMany(group);
        return next;
    }
}
