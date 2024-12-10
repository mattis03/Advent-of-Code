import java.util.HashSet;

public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Area2 a = new Area2(ip);

        int loopCombinations = 0;
        
        for (int y = 0; y < a.HEIGHT; y++) {
            for (int x = 0; x < a.WIDTH; x++) {
                // Finns det redan ett hinder eller vakt
                if (a.getObstacle(x, y)) {
                    continue;
                }

                a.setObstacle(x, y, true);

                // Kolla om det nya hindret resulterar i en loop
                while (a.step()) {
                    if (a.hasLooped()) {
                        loopCombinations++;
                        break;
                    }
                }

                // Återställ hindret och vaktens position
                a.resetGuard();
                a.setObstacle(x, y, false);
                System.out.println("Finished obstacle (" + x + "," + y + ")");
            }
        }

        System.out.println(loopCombinations);
    }
}

class Area2 {
    private static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;

    private final boolean[][] OBSTACLE_MAP;

    public final int WIDTH, HEIGHT;

    private int guardX, guardY, guardDir;
    private int guardStartX, guardStartY, guardStartDir;

    private final HashSet<Integer> PREV_GUARD_STATES = new HashSet<>();

    public Area2(InputParser ip) {
        WIDTH = ip.getLine(0).length();
        HEIGHT = ip.lineCount();

        OBSTACLE_MAP = new boolean[WIDTH][HEIGHT];

        for (int y = 0; y < HEIGHT; y++) {
            String line = ip.getLine(y);
            for (int x = 0; x < WIDTH; x++) {
                switch (line.charAt(x)) {
                    case '.':
                    OBSTACLE_MAP[x][y] = false;
                    break;
                    case '#':
                    OBSTACLE_MAP[x][y] = true;
                    break;
                    case '^':
                    OBSTACLE_MAP[x][y] = false;
                    guardX = x;
                    guardY = y;
                    guardDir = UP;
                    break;
                }
            }
        }

        guardStartX = guardX;
        guardStartY = guardY;
        guardStartDir = guardDir;
    }
    
    // Returnerar false när vakten går utanför banan
    public boolean step() {
        PREV_GUARD_STATES.add(guardState());
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
        }

        return true;
    }

    public boolean hasLooped() {
        return PREV_GUARD_STATES.contains(guardState());
    }

    public void resetGuard() {
        PREV_GUARD_STATES.clear();
        guardX = guardStartX;
        guardY = guardStartY;
        guardDir = guardStartDir;
    }

    private int guardState() {
        return ((guardDir << 16) | ((guardY) << 8) | guardX);
    }

    public void setObstacle(int x, int y, boolean setReset) {
        OBSTACLE_MAP[x][y] = setReset;
    }

    public boolean getObstacle(int x, int y) {
        if (x == guardStartX && y == guardStartY)
            return true;
        return OBSTACLE_MAP[x][y];
    }
}