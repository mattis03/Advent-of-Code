public class Part1 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Area a = new Area(ip);
        while (a.step());
        System.out.println(a.countVisitedPositions());
    }
}

class Area {
    private static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;

    private final boolean[][] OBSTACLE_MAP;
    private final boolean[][] VISITED;

    private final int WIDTH, HEIGHT;

    private int guardX, guardY, guardDir;

    public Area(InputParser ip) {
        WIDTH = ip.getLine(0).length();
        HEIGHT = ip.lineCount();

        OBSTACLE_MAP = new boolean[WIDTH][HEIGHT];
        VISITED = new boolean[WIDTH][HEIGHT];

        for (int y = 0; y < HEIGHT; y++) {
            String line = ip.getLine(y);
            for (int x = 0; x < WIDTH; x++) {
                VISITED[x][y] = false;

                switch (line.charAt(x)) {
                    case '.':
                    OBSTACLE_MAP[x][y] = false;
                    break;
                    case '#':
                    OBSTACLE_MAP[x][y] = true;
                    break;
                    case '^':
                    OBSTACLE_MAP[x][y] = false;
                    VISITED[x][y] = true;
                    guardX = x;
                    guardY = y;
                    guardDir = UP;
                    break;
                }
            }
        }
    }

    // Returnerar false när vakten går utanför banan
    public boolean step() {
        int nextX = guardX, nextY = guardY;
        switch (guardDir) {
            case UP:
            nextY--;
            break;
            case RIGHT:
            nextX++;
            break;
            case DOWN:
            nextY++;
            break;
            case LEFT:
            nextX--;
            break;
        }

        if (nextX < 0 || nextX >= WIDTH || nextY < 0 || nextY >= HEIGHT)
            return false;

        // Kolla efter hinder
        if (OBSTACLE_MAP[nextX][nextY]) {
            guardDir = (guardDir+1)&0b11;
        }
        else {
            guardX = nextX;
            guardY = nextY;
            VISITED[nextX][nextY] = true;
        }

        return true;
    }

    public int countVisitedPositions() {
        int sum = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (VISITED[x][y])
                    sum++;
            }
        }
        return sum;
    }
}