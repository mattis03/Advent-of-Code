import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Part1 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Graph g = new Graph(ip);
        System.out.println(g.count());
    }

    private static class Graph {
        private final HashMap<String, HashSet<String>> EDGES = new HashMap<>();

        public Graph(InputParser ip) {
            // Lägg till alla noder
            for (String line : ip.lines()) {
                String[] nodes = line.split("-");

                if (!EDGES.containsKey(nodes[0]))
                    EDGES.put(nodes[0], new HashSet<>());
                if (!EDGES.containsKey(nodes[1]))
                    EDGES.put(nodes[1], new HashSet<>());
            }
            // Lägg till kanter
            for (String line : ip.lines()) {
                String[] nodes = line.split("-");
                EDGES.get(nodes[0]).add(nodes[1]);
                EDGES.get(nodes[1]).add(nodes[0]);
            }
        }

        public int count() {
            // Skapa alla möjliga tripletter av noder
            ArrayList<ArrayList<String>> combinations = new ArrayList<>();
            Object[] nodes = EDGES.keySet().toArray();

            for (int i = 0; i < EDGES.size() - 2; i++) {
                for (int j = i + 1; j < EDGES.size() - 1; j++) {
                    for (int k = j + 1; k < EDGES.size(); k++) {
                        ArrayList<String> combination = new ArrayList<>();
                        combination.add((String)nodes[i]);
                        combination.add((String)nodes[j]);
                        combination.add((String)nodes[k]);
                        combinations.add(combination);
                    }
                }
            }
            // Kolla om alla noder i tripletten är anslutna till varandra
            int count = 0;
            for (ArrayList<String> combination : combinations) {

                String n1 = combination.get(0);
                String n2 = combination.get(1);
                String n3 = combination.get(2);
                HashSet<String> n1Edges = EDGES.get(n1);
                HashSet<String> n2Edges = EDGES.get(n2);
                HashSet<String> n3Edges = EDGES.get(n3);

                if (n1Edges.contains(n2) && n1Edges.contains(n3) && 
                    n2Edges.contains(n1) && n2Edges.contains(n3) &&
                    n3Edges.contains(n1) && n3Edges.contains(n2)) {

                    if (n1.startsWith("t") || n2.startsWith("t") || n3.startsWith("t"))
                        count++;
                }

            }
            return count;
        }
    }
}
