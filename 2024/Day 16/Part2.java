import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Maze m = new Maze(ip);
        System.out.println(m.solve());
    }

    private static class Maze {
        private static class Node {
            public Node northNode, westNode, southNode, eastNode;
            public boolean visited = false;
            public boolean isEndNode = false;
        }

        private int lowestScore;
        private final HashMap<Node, HashMap<Integer, Integer>> LOWEST_SCORE = new HashMap<>();
        private final HashSet<Node> PATH_NODES = new HashSet<>();

        private final static int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;

        private final Node MAZE[][];
        private final int WIDTH, HEIGHT;
        private final Point START = new Point(), END = new Point();

        public Maze(InputParser ip) {
            WIDTH = ip.getLine(0).length();
            HEIGHT = ip.lineCount();
            MAZE = new Node[WIDTH][HEIGHT];

            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    char c = ip.getChar(x, y);
                    if (c != '#')
                        MAZE[x][y] = new Node();

                    if (c == 'S') {
                        START.x = x;
                        START.y = y;
                    }

                    if (c == 'E') {
                        END.x = x;
                        END.y = y;
                    }
                }
            }

            // Länka noder
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    if (MAZE[x][y] != null) {
                        if (MAZE[x][y - 1] != null)
                            MAZE[x][y].northNode = MAZE[x][y - 1];
                        if (MAZE[x + 1][y] != null)
                            MAZE[x][y].eastNode = MAZE[x + 1][y];
                        if (MAZE[x][y + 1] != null)
                            MAZE[x][y].southNode = MAZE[x][y + 1];
                        if (MAZE[x - 1][y] != null)
                            MAZE[x][y].westNode = MAZE[x - 1][y];
                    }
                }
            }

            MAZE[END.x][END.y].isEndNode = true;
        }

        public int solve() {
            ArrayList<Node> pathNodes = new ArrayList<>();
            lowestScore = Integer.MAX_VALUE;
            dfs(MAZE[START.x][START.y], 0, EAST, pathNodes);
            System.out.println("Node count: " + PATH_NODES.size());
            return lowestScore;
        }

        private void dfs(Node n, int score, int prevDir, ArrayList<Node> queue) {
            // Utforska inte vägar med högre poäng än den hittils lägsta poängen
            if (score > lowestScore)
                return;

            // Kolla om vi nått denna nod från samma riktning med lägre poäng tidigare
            if (LOWEST_SCORE.containsKey(n) && LOWEST_SCORE.get(n).containsKey(prevDir) && score > LOWEST_SCORE.get(n).get(prevDir))
                return;

            if (!LOWEST_SCORE.containsKey(n))
                LOWEST_SCORE.put(n, new HashMap<>());
            LOWEST_SCORE.get(n).put(prevDir, score);

            queue.add(n);

            if (n.isEndNode) {
                if (score < lowestScore)
                    lowestScore = score;

                if (score == 107468)
                    for (Node node : queue) {
                        PATH_NODES.add(node);
                    }
                return;
            }

            n.visited = true;

            switch (prevDir) {
                case NORTH:
                    if (n.northNode != null && !n.northNode.visited)
                        dfs(n.northNode, score + nextScore(prevDir, NORTH), NORTH, queue);
                    if (n.eastNode != null && !n.eastNode.visited)
                        dfs(n.eastNode, score + nextScore(prevDir, EAST), EAST, queue);
                    if (n.westNode != null && !n.westNode.visited)
                        dfs(n.westNode, score + nextScore(prevDir, WEST), WEST, queue);
                    break;
                case EAST:
                    if (n.eastNode != null && !n.eastNode.visited)
                        dfs(n.eastNode, score + nextScore(prevDir, EAST), EAST, queue);
                    if (n.northNode != null && !n.northNode.visited)
                        dfs(n.northNode, score + nextScore(prevDir, NORTH), NORTH, queue);
                    if (n.southNode != null && !n.southNode.visited)
                        dfs(n.southNode, score + nextScore(prevDir, SOUTH), SOUTH, queue);
                    break;
                case SOUTH:
                    if (n.southNode != null && !n.southNode.visited)
                        dfs(n.southNode, score + nextScore(prevDir, SOUTH), SOUTH, queue);
                    if (n.eastNode != null && !n.eastNode.visited)
                        dfs(n.eastNode, score + nextScore(prevDir, EAST), EAST, queue);
                    if (n.westNode != null && !n.westNode.visited)
                        dfs(n.westNode, score + nextScore(prevDir, WEST), WEST, queue);
                    break;
                case WEST:
                    if (n.westNode != null && !n.westNode.visited)
                        dfs(n.westNode, score + nextScore(prevDir, WEST), WEST, queue);
                    if (n.northNode != null && !n.northNode.visited)
                        dfs(n.northNode, score + nextScore(prevDir, NORTH), NORTH, queue);
                    if (n.southNode != null && !n.southNode.visited)
                        dfs(n.southNode, score + nextScore(prevDir, SOUTH), SOUTH, queue);
                    break;
            }

            n.visited = false;
            queue.remove(n);
        }

        private int nextScore(int prevDir, int nextDir) {
            if (prevDir == nextDir)
                return 1;
            return 1001;
        }

        public String toString() {
            String retStr = "";
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    if (MAZE[x][y] == null)
                        retStr += '█';
                    else if(!MAZE[x][y].visited)
                        retStr += ' ';
                    else
                        retStr += ".";
                }
                retStr += '\n';
            }
            return retStr;
        }
    }
}