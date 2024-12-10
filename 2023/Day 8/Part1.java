import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Part1 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));

        Traversal t = new Traversal(sc.nextLine());
        sc.nextLine();
        NodeList nl = new NodeList();
        while (sc.hasNext()) {
            nl.addNode(sc.nextLine());
        }
        sc.close();

        System.out.println("Part 1: " + nl.AAAtoZZZ(t));
    }
}

class NodeList {
    private class Node {
        public String L, R;
    }

    private HashMap<String, Node> nodeMap = new HashMap<>();

    public void addNode(String nodeStr) {
        String id = nodeStr.substring(0, 3);
        Node n = new Node();
        n.L = nodeStr.substring(7, 10);
        n.R = nodeStr.substring(12, 15);
        nodeMap.put(id, n);
    }

    public int AAAtoZZZ(Traversal t) {
        int steps = 0;
        Node currNode = nodeMap.get("AAA");
        String next;
        do {
            next = t.nextStepLeft() ? currNode.L : currNode.R;
            currNode = nodeMap.get(next);
            steps++;
        } while (!next.equals("ZZZ"));
        return steps;
    }
}

class Traversal {
    private int index = 0;
    private boolean[] directions;

    public Traversal(String s) {
        directions = new boolean[s.length()];
        for (int i = 0; i < s.length(); i++) {
            directions[i] = s.charAt(i) == 'L';
        }
    }

    public boolean nextStepLeft() {
        boolean retVal = directions[index];
        index = (index + 1) % directions.length;
        return retVal;
    }

    public int getCurrentIndex() {
        return index;
    }
}