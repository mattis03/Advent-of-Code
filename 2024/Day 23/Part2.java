import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Graph g = new Graph(ip);
        g.findLargestNetwork();
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

        public void findLargestNetwork() {
            ArrayList<ArrayList<String>> combinations = new ArrayList<>();
            Object[] nodes = EDGES.keySet().toArray();

            for (Object n : nodes) {
                ArrayList<String> combination = new ArrayList<>();
                combination.add((String)n);
                combinations.add(combination);
            }

            while (combinations.size() != 0) {
                combinations = addDepth(combinations);
                combinations = removeInvalidCombinations(combinations);
                
                for (ArrayList<String> combination : combinations)
                    System.out.println(combination);
            }
        }

        public ArrayList<ArrayList<String>> removeInvalidCombinations(ArrayList<ArrayList<String>> combinations) {
            ArrayList<ArrayList<String>> newCombinations = new ArrayList<>();
            for (ArrayList<String> combination : combinations) {
                if (allNodesAreConnected(combination))
                    newCombinations.add(combination);
            }
            return newCombinations;
        }

        public boolean allNodesAreConnected(ArrayList<String> combination) {
            for (int i = 0; i < combination.size() - 1; i++) {
                String n1 = combination.get(i);
                HashSet<String> n1Edges = EDGES.get(n1); 
                for (int j = i + 1; j < combination.size(); j++) {
                    String n2 = combination.get(j);
                    if (!n1Edges.contains(n2))
                        return false;
                }
            }
            return true;
        }
        
        public ArrayList<ArrayList<String>> addDepth(ArrayList<ArrayList<String>> combinations) {
            Object[] nodes = EDGES.keySet().toArray();
            ArrayList<ArrayList<String>> newCombinations = new ArrayList<>();
            
            for (int i = 0; i < combinations.size(); i++) {
                for (Object node : nodes) {

                    // Lägg endast till noder i bokstavsordning
                    if (combinations.get(i).getLast().compareTo((String)node) > 0)
                        continue;

                    ArrayList<String> newCombination = new ArrayList<>();
                    for (String n : combinations.get(i))
                        newCombination.add(n);

                    newCombination.add((String)node);

                    //if (!hasDuplicate(newCombinations, newCombination))
                    newCombinations.add(newCombination);

                }
            }
            
            return newCombinations;
        }
    }
}
