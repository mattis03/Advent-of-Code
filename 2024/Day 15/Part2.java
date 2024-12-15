import java.util.ArrayList;

public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        WareHouse w = new WareHouse(ip);
        for (char inst : w.INSTRUCTIONS) {
            switch (inst) {
                case '^': w.moveUp(); break;
                case '>': w.moveRight(); break;
                case 'v': w.moveDown(); break;
                case '<': w.moveLeft(); break;
            }
            //System.out.println(w);
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

            WIDTH = ip.getLine(0).length() * 2;
            HEIGHT = separator;
            MAP = new char[WIDTH][HEIGHT];

            for (int y = 0; y < HEIGHT; y++) {
                String row = ip.getLine(y);
                row = row.replace("#", "##");
                row = row.replace("O", "[]");
                row = row.replace(".", "..");
                row = row.replace("@", "@.");
                for (int x = 0; x < WIDTH; x++) {
                    char c = row.charAt(x);
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

        public void moveUp() {
            ArrayList<Integer> toMove = new ArrayList<>();
            if (canMoveUp(robotX, robotY, toMove)) {
                for (Integer pos : toMove) {
                    int x = pos & 0xff;
                    int y = pos >> 8;
                    MAP[x][y - 1] = MAP[x][y];
                    MAP[x][y] = '.';
                }
                robotY--;
            }
        }

        private boolean canMoveUp(int x, int y, ArrayList<Integer> toMove) {
            boolean canMove = false;
            switch (MAP[x][y - 1]) {
                case '.':
                    canMove = true;
                    break;
                case '#':
                    canMove = false;
                    break;
                case '[':
                    canMove = canMoveUp(x, y - 1, toMove) && canMoveUp(x + 1, y - 1, toMove);
                    break;
                case ']':
                    canMove = canMoveUp(x, y - 1, toMove) && canMoveUp(x - 1, y - 1, toMove);
                    break;
            }

            if (!toMove.contains(x | (y << 8)))
                toMove.add(x | (y << 8));

            return canMove;
        }

        public void moveDown() {
            ArrayList<Integer> toMove = new ArrayList<>();
            if (canMoveDown(robotX, robotY, toMove)) {
                for (Integer pos : toMove) {
                    int x = pos & 0xff;
                    int y = pos >> 8;
                    MAP[x][y + 1] = MAP[x][y];
                    MAP[x][y] = '.';
                }
                robotY++;
            }
        }

        private boolean canMoveDown(int x, int y, ArrayList<Integer> toMove) {
            boolean canMove = false;
            switch (MAP[x][y + 1]) {
                case '.':
                    canMove = true;
                    break;
                case '#':
                    canMove = false;
                    break;
                case '[':
                    canMove = canMoveDown(x, y + 1, toMove) && canMoveDown(x + 1, y + 1, toMove);
                    break;
                case ']':
                    canMove = canMoveDown(x, y + 1, toMove) && canMoveDown(x - 1, y + 1, toMove);
                    break;
            }

            if (!toMove.contains(x | (y << 8)))
                toMove.add(x | (y << 8));

            return canMove;
        }

        public void moveLeft() {
            if (moveLeft(robotX, robotY))
                MAP[robotX--][robotY] = '.';
        }

        private boolean moveLeft(int x, int y) {
            switch (MAP[x - 1][y]) {
                case '.':
                    MAP[x - 1][y] = MAP[x][y];
                    return true;
                case '#':
                    return false;
                default: // '[' || ']'
                    if (moveLeft(x - 1, y)) {
                        MAP[x - 1][y] = MAP[x][y];
                        return true;
                    }
                    return false;
            }
        }

        public void moveRight() {
            if (moveRight(robotX, robotY))
                MAP[robotX++][robotY] = '.';
        }

        private boolean moveRight(int x, int y) {
            switch (MAP[x + 1][y]) {
                case '.':
                    MAP[x + 1][y] = MAP[x][y];
                    return true;
                case '#':
                    return false;
                default: // '[' || ']'
                    if (moveRight(x + 1, y)) {
                        MAP[x + 1][y] = MAP[x][y];
                        return true;
                    }
                    return false;
            }
        }

        public long GPSsum() {
            long sum = 0;
            for (int y = 1; y < HEIGHT - 1; y++) {
                for (int x = 2; x < WIDTH - 2; x++) {
                    if (MAP[x][y] == '[')
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
