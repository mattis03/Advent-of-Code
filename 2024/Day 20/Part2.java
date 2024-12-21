import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        new RaceTrack(ip);
    }

    private static class RaceTrack {
        private final boolean TRACK[][];
        private final int WIDTH, HEIGHT;
        private final Point START = new Point(), END = new Point();

        private final ArrayList<Integer> QUEUE = new ArrayList<>();
        private final HashMap<Integer, Integer> VISITED = new HashMap<>();

        private final int CHEAT_STEPS = 20;

        public RaceTrack(InputParser ip) {
            WIDTH = ip.getLine(0).length();
            HEIGHT = ip.lineCount();
            TRACK = new boolean[WIDTH][HEIGHT];
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    char c = ip.getChar(x, y);
                    TRACK[x][y] = c == '#' ? true : false;
                    if (c == 'S')
                        START.setLocation(x, y);
                    if (c == 'E')
                        END.setLocation(x, y);
                }
            }

            // Cachea den kortaste vägen som går att ta utan genvägar och spara dess längd
            QUEUE.clear();
            QUEUE.add(START.x | (START.y << 8));
            VISITED.clear();
            VISITED.put(START.x | (START.y << 8), 0);
            bfs();

            System.out.println(pathCountSave100());
        }

        public int pathCountSave100() {
            int pathCount = 0;
            // Gå igenom alla noder på vägen
            for (int pos : VISITED.keySet()) {
                int x = pos & 0xff;
                int y = pos >>> 8;
                int posSteps = VISITED.get(pos);
                // Testa alla möjliga genvägar från denna noden
                for (int yOff = -CHEAT_STEPS; yOff <= CHEAT_STEPS; yOff++) {
                    for (int xOff = -(CHEAT_STEPS-Math.abs(yOff)); xOff <= CHEAT_STEPS-Math.abs(yOff); xOff++) {

                        int cheatX = x + xOff;
                        int cheatY = y + yOff;

                        if (!VISITED.containsKey(cheatX | (cheatY << 8)))
                            continue;

                        int cheatSteps = VISITED.get(cheatX | (cheatY << 8));

                        int stepSavings = cheatSteps - posSteps - (Math.abs(x - cheatX) + Math.abs(y - cheatY));

                        if (stepSavings >= 100)
                            pathCount++;

                    }
                }
            }

            return pathCount;
        }

        public int bfs() {
            while (!QUEUE.isEmpty()) {
                int pos = QUEUE.removeFirst();
                if ((pos&0xff) == END.x && pos>>>8 == END.y)
                    return VISITED.get(pos);

                bfs(pos);
            }

            return -1;
        }

        private void bfs(int pos) {
            int x = pos & 0xff;
            int y = pos >>> 8;
            final int BRANCHES[] = {x | ((y-1)<<8), x | ((y+1)<<8), (x+1) | (y<<8), (x-1) | (y<<8)};

            for (int b : BRANCHES) {
                if (!VISITED.containsKey(b) && !TRACK[b&0xff][b>>>8]) {
                    QUEUE.add(b);
                    VISITED.put(b, VISITED.get(pos) + 1);
                }
            }
        }

        public void print(HashMap<Integer, Integer> visited) {
            String retStr = "";
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    if (visited.containsKey(x | (y << 8)))
                        retStr += '.';
                    else
                        retStr += TRACK[x][y] ? '#' : ' ';
                }
                retStr += '\n';
            }
            System.out.println(retStr);
        }
    }
}