package tubes;

import tubes.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String mode = null;
        String filePath = null;

        if (args.length > 0) {
            String a0 = args[0].trim();
            if (a0.equals("1") || a0.equals("2") || a0.equals("3")) {
                mode = a0;
                if (a0.equals("1") && args.length >= 2) {
                    filePath = args[1].trim();
                }
            } else {
                mode = "1";
                filePath = a0;
            }
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.println("Select mode:");
            System.out.println(" 1 - input file");
            System.out.println(" 2 - hand input");
            System.out.println(" 3 - tests");
            mode = sc.nextLine().trim();
            if (mode.equals("1")) {
                System.out.print("input filename: ");
                filePath = sc.nextLine().trim();
            }
        }

        switch (mode) {
            case "1":
                if (filePath == null || filePath.isEmpty()) {
                    Scanner sc = new Scanner(System.in);
                    System.out.print("path file: ");
                    filePath = sc.nextLine().trim();
                }
                runFromFile(filePath);
                break;
            case "2":
                runInteractive();
                break;
            case "3":
                runSampleTests();
                break;
            default:
                System.err.println("Error mode: " + mode);
                break;
        }
    }

    private static void runFromFile(String path) {
        try {
            List<String> lines = Files.readAllLines(Path.of(path));
            State s = parseState(lines);
            runSolverAndPrint(s);
        } catch (IOException e) {
            System.err.println("Error file: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Error in file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error file: " + e.getMessage());
        }
    }

    private static void runInteractive() {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("input: n v");
            String first = sc.nextLine().trim();
            while (first.isEmpty()) first = sc.nextLine().trim();
            String[] parts = first.split("\\s+");
            if (parts.length < 2) {
                System.err.println("Error string.");
                return;
            }
            int n = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);

            List<String> lines = new ArrayList<>();
            lines.add(n + " " + v);
            System.out.println(
                    "Input" + n + "string: input tubes from bottom to top");
            System.out.println("Empty tubes - empty string");
            for (int i = 0; i < n; i++) {
                System.out.print((i + 1) + ": ");
                String line = sc.nextLine();
                if (line == null) line = "";
                lines.add(line);
            }

            State s = parseState(lines);
            runSolverAndPrint(s);
        } catch (Exception ex) {
            System.err.println("Error input: " + ex.getMessage());
        }
    }

    private static void runSampleTests() {
        System.out.println("Start tests");

        List<Tube> t1 = new ArrayList<>();
        t1.add(new Tube(4, Arrays.asList("A","A","A","A")));
        t1.add(new Tube(4));
        State s1 = new State(t1);
        runAndPrint(s1, "Test 1");

        List<Tube> t2 = new ArrayList<>();
        t2.add(new Tube(2, Arrays.asList("A","B")));
        t2.add(new Tube(2, Arrays.asList("B","A")));
        t2.add(new Tube(2));
        State s2 = new State(t2);
        runAndPrint(s2, "Test 2");

        List<Tube> t3 = new ArrayList<>();
        t3.add(new Tube(4, Arrays.asList("R","G","G","R")));
        t3.add(new Tube(4, Arrays.asList("G","R","R","G")));
        t3.add(new Tube(4, Arrays.asList("B","B","B","B")));
        t3.add(new Tube(4));
        State s3 = new State(t3);
        runAndPrint(s3, "Test 3");

        System.out.println("End test");
    }

    private static void runAndPrint(State s, String title) {
        System.out.println(title);
        System.out.println("Start state:");
        int idx = 1;
        for (Tube t : s.getTubes()) {
            System.out.printf("%2d: %s\n", idx++, t.toString());
        }
        Solver solver = new Solver(s);
        List<Move> sol = solver.solve();
        if (sol == null) {
            System.out.println("Solution found");
        } else {
            System.out.println("Found " + sol.size() + " steps:");
            for (Move m : sol) System.out.print(m + " ");
            System.out.println();
        }
    }

    private static void runSolverAndPrint(State s) {
        runAndPrint(s, "Start");
    }

    private static State parseState(List<String> lines) {
        Iterator<String> it = lines.iterator();
        while (it.hasNext()) {
            String l = it.next().trim();
            if (l.isEmpty()) continue;
            String[] parts = l.split("\\s+");
            int n = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);

            List<Tube> tubes = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                String line = "";
                if (it.hasNext()) line = it.next();
                if (line == null) line = "";
                line = line.trim();
                if (line.equals("-") || line.isEmpty()) {
                    tubes.add(new Tube(v));
                } else {
                    String[] tokens = line.split("\\s+");
                    List<String> arr = Arrays.asList(tokens);
                    if (arr.size() > v) {
                        throw new IllegalArgumentException(
                                "Too many items in tube " + (i + 1));
                    }
                    tubes.add(new Tube(v, arr));
                }
            }
            return new State(tubes);
        }
        throw new IllegalArgumentException("Input error");
    }
}
