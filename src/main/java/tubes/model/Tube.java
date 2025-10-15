package tubes.model;

import java.util.*;

public class Tube implements Cloneable {
    private final int capacity;
    private final ArrayDeque<String> stack;

    public Tube(int capacity) {
        this.capacity = capacity;
        this.stack = new ArrayDeque<>();
    }

    public Tube(int capacity, List<String> contentsBottomToTop) {
        this.capacity = capacity;
        this.stack = new ArrayDeque<>(contentsBottomToTop);
    }

    public int size() {
        return stack.size();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public boolean isFull() {
        return stack.size() == capacity;
    }

    public int capacity() {
        return capacity;
    }

    public String top() {
        return stack.peekLast();
    }

    public int topCount() {
        if (stack.isEmpty()) {
            return 0;
        }
        String c = stack.peekLast();
        int cnt = 0;
        Iterator<String> it = stack.descendingIterator();
        while (it.hasNext()) {
            if (it.next().equals(c)) {
                cnt++;
            }
            else break;
        }
        return cnt;
    }

    public boolean allSameColor() {
        if (stack.isEmpty()) {
            return true;
        }
        String c = stack.peekLast();
        for (String s : stack) {
            if (!s.equals(c)) {
                return false;
            }
        }
        return true;
    }

    public void push(String color) {
        if (stack.size() >= capacity) {
            throw new IllegalStateException("Tube full");
        }
        stack.addLast(color);
    }

    public void pushMany(List<String> itemsBottomToTop) {
        for (String s : itemsBottomToTop) {
            push(s);
        }
    }

    public List<String> popMany(int k) {
        List<String> tmp = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            tmp.add(stack.removeLast());
        }
        Collections.reverse(tmp);
        return tmp;
    }

    public String serialize() {
        if (stack.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : stack) {
            if (!first) {
                sb.append(',');
            }
            sb.append(s);
            first = false;
        }
        return sb.toString();
    }

    @Override
    public Tube clone() {
        return new Tube(capacity, new ArrayList<>(stack));
    }

    @Override
    public String toString() {
        return "[" + serialize() + "]";
    }
}
