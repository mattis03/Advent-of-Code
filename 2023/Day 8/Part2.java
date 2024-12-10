import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Part2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));

        Traversal t = new Traversal(sc.nextLine());
        sc.nextLine();
        NodeList2 nl = new NodeList2();
        while (sc.hasNext()) {
            nl.addNode(sc.nextLine());
        }
        sc.close();

        System.out.println("Part 2: " + nl.allXXXAtoXXXZ(t));
    }
}

class NodeList2 {
    private class Node {
        public String ID, L, R;
    }

    private LinkedList<Node> nodes = new LinkedList<>();

    public void addNode(String nodeStr) {
        Node n = new Node();
        n.ID = nodeStr.substring(0, 3);
        n.L = nodeStr.substring(7, 10);
        n.R = nodeStr.substring(12, 15);
        nodes.add(n);
    }

    public long allXXXAtoXXXZ(Traversal t) {
        HashMap<String, Integer> nodeMap = new HashMap<>();
        int[] L = new int[nodes.size()];
        int[] R = new int[nodes.size()];
        LinkedList<Integer> A = new LinkedList<>();
        LinkedList<Integer> Z = new LinkedList<>();
        // Assign an int to each node
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            nodeMap.put(n.ID, i);
            if (n.ID.charAt(2) == 'A') A.add(i);
            if (n.ID.charAt(2) == 'Z') Z.add(i);
        }

        for (int i = 0; i < L.length; i++) {
            Node n = nodes.get(i);
            L[i] = nodeMap.get(n.L);
            R[i] = nodeMap.get(n.R);
        }

        // Convert lists into arrays
        int[] currNodes = new int[A.size()];
        for (int i = 0; i < currNodes.length; i++) {
            currNodes[i] = A.get(i);
        }
        int[] Znodes = new int[Z.size()];
        for (int i = 0; i < Znodes.length; i++) {
            Znodes[i] = Z.get(i);
        }

        long steps = 0;
        boolean end = false;
        // TODO: Find when every path has gotten stuck in a loop (visiting an already visited node && same index in traversal cycle)
        // Build binary number representing path (0=regular node, 1=node that ends with Z) and use bitshifts to check if all 1s line up
        // Observation: Each cycle contains exactly 1 Z-node
        // Move {smallest cycle size} steps at a time
        LinkedList<State> states = new LinkedList<>();
        boolean foundCycle = false;
        do {
            
            if (t.nextStepLeft()) {
                for (int i = 0; i < currNodes.length; i++) {
                    currNodes[i] = L[currNodes[i]];
                }
            } else {
                for (int i = 0; i < currNodes.length; i++) {
                    currNodes[i] = R[currNodes[i]];
                }
            }

            State s = new State(currNodes[5], t.getCurrentIndex());
            if (states.contains(s)) foundCycle = true;
            states.add(s);
            int c = 0;
            if (foundCycle) {
                int firstOccurence = states.indexOf(s);
                for (int i = states.indexOf(s) + 1; i < states.size(); i++) {
                    if (Z.contains(states.get(i).currNode)) c++;

                    if (states.get(i).equals(s)) {
                        //System.out.println(i - firstOccurence);
                        break;
                    }
                }
                System.out.println(c);
            }

            // Check if every path has reached a Z-node
            for (int i = 0; i < currNodes.length; i++) {
                end = false;
                for (int j = 0; j < Znodes.length; j++) {
                    if (currNodes[i] == Znodes[j]) {
                        end = true;
                        break; // Found match -> Check next currNode
                    }
                }
                if (!end) break; // Brake and continue main loop if any currNode is not a Z-node
            }

            steps++;
        } while (!end);

        return steps;
    }

    private class State {
        private final int currNode, currCyclePos;

        public State(int currNode, int currCyclePos) {
            this.currNode = currNode;
            this.currCyclePos = currCyclePos;
        }

        @Override
        public boolean equals(Object obj) {
            State s2 = (State) obj;
            return this.currCyclePos == s2.currCyclePos && this.currNode == s2.currNode;
        }
    }
}