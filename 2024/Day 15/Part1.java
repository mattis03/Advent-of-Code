import java.util.ArrayList;

public class Part1 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        WareHouse w = new WareHouse(ip);
        
        for (char inst : w.INSTRUCTIONS) {
            switch (inst) {
                case '^': w.moveUp(w.robotX, w.robotY); break;
                case '>': w.moveRight(w.robotX, w.robotY); break;
                case 'v': w.moveDown(w.robotX, w.robotY); break;
                case '<': w.moveLeft(w.robotX, w.robotY); break;
            }
        }

        System.out.println(w.GPSsum());
    }

    private static class WareHouse {
        public final int WIDTH, HEIGHT;
        public final char[][] MAP;
        public final ArrayList<Character> INSTRUCTIONS = new ArrayList<>();
        public int robotX, robotY;

        public WareHouse(InputParser ip) {
            int separator;
            for (separator = 0; !ip.getLine(separator).equals(""); separator++);

            WIDTH = ip.getLine(0).length();
            HEIGHT = separator;
            MAP = new char[WIDTH][HEIGHT];

            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    char c = ip.getChar(x, y);
                    MAP[x][y] = c;
                    if (c == '@') {
                        robotX = x;
                        robotY = y;
                    }
                }
            }

            for (int i = separator + 1; i < ip.lineCount(); i++)
                for (char c : ip.getLine(i).toCharArray())
                    INSTRUCTIONS.add(c);
        }

        private boolean moveUp(int x, int y) {
            switch (MAP[x][y - 1]) {
                case '.':
                    if (MAP[x][y] == '@')
                        robotY--;
                    MAP[x][y - 1] = MAP[x][y];
                    MAP[x][y] = '.';
                    return true;
                case '#':
                    return false;
                default: // 'O'
                    if (moveUp(x, y - 1)) {
                        if (MAP[x][y] == '@')
                            robotY--;
                        MAP[x][y - 1] = MAP[x][y];
                        MAP[x][y] = '.';
                        return true;
                    }
                    return false;
            }
        }

        private boolean moveRight(int x, int y) {
            switch (MAP[x + 1][y]) {
                case '.':
                    if (MAP[x][y] == '@')
                        robotX++;
                    MAP[x + 1][y] = MAP[x][y];
                    MAP[x][y] = '.';
                    return true;
                case '#':
                    return false;
                default: // 'O'
                    if (moveRight(x + 1, y)) {
                        if (MAP[x][y] == '@')
                        robotX++;
                        MAP[x + 1][y] = MAP[x][y];
                        MAP[x][y] = '.';
                        return true;
                    }
                    return false;
            }
        }

        private boolean moveDown(int x, int y) {
            switch (MAP[x][y + 1]) {
                case '.':
                    if (MAP[x][y] == '@')
                        robotY++;
                    MAP[x][y + 1] = MAP[x][y];
                    MAP[x][y] = '.';
                    return true;
                case '#':
                    return false;
                default: // 'O'
                    if (moveDown(x, y + 1)) {
                        if (MAP[x][y] == '@')
                            robotY++;
                        MAP[x][y + 1] = MAP[x][y];
                        MAP[x][y] = '.';
                        return true;
                    }
                    return false;
            }
        }

        private boolean moveLeft(int x, int y) {
            switch (MAP[x - 1][y]) {
                case '.':
                    if (MAP[x][y] == '@')
                        robotX--;
                    MAP[x - 1][y] = MAP[x][y];
                    MAP[x][y] = '.';
                    return true;
                case '#':
                    return false;
                default: // 'O'
                    if (moveLeft(x - 1, y)) {
                        if (MAP[x][y] == '@')
                            robotX--;
                        MAP[x - 1][y] = MAP[x][y];
                        MAP[x][y] = '.';
                        return true;
                    }
                    return false;
            }
        }

        public long GPSsum() {
            long sum = 0;
            for (int y = 1; y < HEIGHT - 1; y++) {
                for (int x = 1; x < WIDTH - 1; x++) {
                    if (MAP[x][y] == 'O')
                        sum += 100 * y + x;
                }
            }
            return sum;
        }

        public String toString() {
            String retStr = "";
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++)
                    retStr += MAP[x][y];
                retStr += "\n";
            }
            return retStr;
        }
    }
}
