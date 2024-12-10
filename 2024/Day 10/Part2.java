import java.util.LinkedList;

public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        HikingTrails hk = new HikingTrails(ip);
        System.out.println(hk.calcTrailScore());
    }

    private static class HikingTrails {
        private final int MAP[][];
        private final LinkedList<int[]> ZERO_POSITIONS = new LinkedList<>();

        public HikingTrails(InputParser ip) {
            MAP = new int[ip.getLine(0).length()][ip.lineCount()];

            for (int y = 0; y < ip.lineCount(); y++) {
                for (int x = 0; x < ip.getLine(0).length(); x++) {
                    char c = ip.getLine(y).charAt(x);
                    if (c == '.') {
                        MAP[x][y] = -1;
                    } else {
                        MAP[x][y] = Integer.parseInt(ip.getLine(y).charAt(x) + "");
                        if (MAP[x][y] == 0)
                            ZERO_POSITIONS.add(new int[]{x, y});
                    }
                }
            }
        }

        private int calcScoreDFS(int x, int y) {
            if (MAP[x][y] == 9)
                return 1;
            
            int branchSum = 0;
            if (y - 1 >= 0 && MAP[x][y - 1] == MAP[x][y]+1)
                branchSum += calcScoreDFS(x, y - 1);

            if (x + 1 < MAP[0].length && MAP[x + 1][y] == MAP[x][y]+1)
                branchSum += calcScoreDFS(x + 1, y);

            if (y + 1 < MAP.length && MAP[x][y + 1] == MAP[x][y]+1)
                branchSum += calcScoreDFS(x, y + 1);

            if (x - 1 >= 0 && MAP[x - 1][y] == MAP[x][y]+1)
                branchSum += calcScoreDFS(x - 1, y);

            return branchSum;
        }

        public int calcTrailScore() {
            int sum = 0;
            for (int[] coord : ZERO_POSITIONS)
                sum += calcScoreDFS(coord[0], coord[1]);
            return sum;
        }
    }
}