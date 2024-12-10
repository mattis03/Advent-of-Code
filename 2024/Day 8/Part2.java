import java.util.HashMap;
import java.util.LinkedList;

public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        AntennaGraph ag = new AntennaGraph(ip);
        System.out.println(ag.countAntiNodes());
    }

    private static class AntennaGraph {
        private final HashMap<Character, LinkedList<Point>> MAP = new HashMap<>();
        private final boolean[][] ANTINODE_MAP;

        public AntennaGraph(InputParser ip) {
            ANTINODE_MAP = new boolean[ip.getLine(0).length()][ip.lineCount()];
            for (int y = 0; y < ip.lineCount(); y++) {
                String line = ip.getLine(y);
                for (int x = 0; x < line.length(); x++) {
                    char c = line.charAt(x);
                    if (c != '.') {
                        if (!MAP.containsKey(c))
                            MAP.put(c, new LinkedList<>());
                        MAP.get(c).add(new Point(x, y));
                    }

                    ANTINODE_MAP[x][y] = false;
                }
            }
        }

        public int countAntiNodes() {
            for (LinkedList<Point> antennaPoints : MAP.values()) {
                // Varje pointList hör till varsin antenn
                for (int i = 0; i < antennaPoints.size(); i++) {
                    // Välj antennen som ska undersökas
                    Point origin = antennaPoints.get(i);

                    for (int j = 0; j < antennaPoints.size(); j++) {
                        if (i == j) 
                            continue;

                        Point antenna = antennaPoints.get(j);

                        Point vector = new Point(origin.x - antenna.x, origin.y - antenna.y);

                        Point antiNode = new Point(origin.x + vector.x, origin.y + vector.y);
                        // Sätt ut antinoder längs med vektorn
                        while (antiNode.x >= 0 && antiNode.x < ANTINODE_MAP[0].length && antiNode.y >= 0 && antiNode.y < ANTINODE_MAP.length) {
                            ANTINODE_MAP[antiNode.x][antiNode.y] = true;
                            antiNode.x += vector.x;
                            antiNode.y += vector.y;
                        }
                        // Vänd 180 grader och sätt ut antinoder längs med vektorn
                        antiNode.x -= vector.x;
                        antiNode.y -= vector.y;
                        while (antiNode.x >= 0 && antiNode.x < ANTINODE_MAP[0].length && antiNode.y >= 0 && antiNode.y < ANTINODE_MAP.length) {
                            ANTINODE_MAP[antiNode.x][antiNode.y] = true;
                            antiNode.x -= vector.x;
                            antiNode.y -= vector.y;
                        }
                    }
                }
            }

            // Räkna antinoder
            int count = 0;
            for (int y = 0; y < ANTINODE_MAP.length; y++) {
                for (int x = 0; x < ANTINODE_MAP[0].length; x++) {
                    if (ANTINODE_MAP[x][y])
                        count++;
                }
            }

            return count;
        }

        private static class Point {
            public Point(int x, int y) {
                this.x = x; this.y = y;
            }
            public int x, y;
        }
    }
}