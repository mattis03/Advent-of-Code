import java.util.ArrayList;
import java.awt.Point;

public class Part1_2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Garden g = new Garden(ip);
        System.out.println("Part 1: " + g.calculateCostPart1());
        System.out.println("Part 2: " + g.calculateCostPart2());
    }

    private static class Garden {
        private class Plant {
            public final char ID;
            public boolean fenceUp, fenceRight, fenceDown, fenceLeft;

            public Plant(char id) {
                this.ID         = id;
                this.fenceUp    = false;
                this.fenceRight = false;
                this.fenceDown  = false;
                this.fenceLeft  = false;
            }
        }

        private final int WIDTH, HEIGHT;
        // 2D-Array med alla plantor
        private final Plant[][] MAP;
        // Hjälparray för att initialisera alla regioner samt beräkna antal sidor på en region
        private final boolean[][] MARKED;
        // Lista av med regioner, representerade som listor av punkter
        private final ArrayList<ArrayList<Point>> REGIONS = new ArrayList<>();

        public Garden(InputParser ip) {
            WIDTH = ip.getLine(0).length();
            HEIGHT = ip.lineCount();

            MAP = new Plant[WIDTH][HEIGHT];
            MARKED = new boolean[WIDTH][HEIGHT];
            clearMarked();
            
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    Plant p = new Plant(ip.getLine(y).charAt(x));
                    // Sätt upp staket mot kanten av kartan
                    if (x == 0)        p.fenceLeft  = true; 
                    if (x == WIDTH-1)  p.fenceRight = true;
                    if (y == 0)        p.fenceUp    = true; 
                    if (y == HEIGHT-1) p.fenceDown  = true;
                    // Sätt upp staket mot andra regioner
                    if (x > 0)        if (ip.getLine(y).charAt(x - 1) != p.ID) p.fenceLeft  = true;
                    if (x < WIDTH-1)  if (ip.getLine(y).charAt(x + 1) != p.ID) p.fenceRight = true;
                    if (y > 0)        if (ip.getLine(y - 1).charAt(x) != p.ID) p.fenceUp    = true;
                    if (y < HEIGHT-1) if (ip.getLine(y + 1).charAt(x) != p.ID) p.fenceDown  = true;
                    // Lägg till plantan i kartan
                    MAP[x][y] = p;
                }
            }
            // Skapa regioner, sparas i regions
            for (int y = 0; y < HEIGHT; y++)
                for (int x = 0; x < WIDTH; x++)
                    if (!MARKED[x][y])
                        initRegion(x, y);
        }

        private void clearMarked() {
            for (int y = 0; y < HEIGHT; y++)
                for (int x = 0; x < WIDTH; x++)
                    MARKED[x][y] = false;
        }

        private void initRegion(int startX, int startY) {
            ArrayList<Point> region = new ArrayList<>();
            char regionChar = MAP[startX][startY].ID;
            dfs(regionChar, startX, startY, region);
            REGIONS.add(region);
        }

        private void dfs(char regionChar, int x, int y, ArrayList<Point> region) {
            if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT)
                return;
            if (MARKED[x][y] || MAP[x][y].ID != regionChar)
                return;

            MARKED[x][y] = true;
            region.add(new Point(x, y));

            dfs(regionChar, x + 1, y, region);
            dfs(regionChar, x - 1, y, region);
            dfs(regionChar, x, y + 1, region);
            dfs(regionChar, x, y - 1, region);
        }

        public int calculateCostPart1() {
            int sum = 0;
            for (ArrayList<Point> region : REGIONS) {
                int area = region.size();
                int sides = 0;
                // Summera antalet staket varje planta har
                for (Point p : region) {
                    Plant plant = MAP[p.x][p.y];
                    if (plant.fenceUp)    sides++;
                    if (plant.fenceRight) sides++;
                    if (plant.fenceDown)  sides++;
                    if (plant.fenceLeft)  sides++;
                }
                sum += sides * area;
            }
            return sum;
        }

        public int calculateCostPart2() {
            int sum = 0;
            for (ArrayList<Point> region : REGIONS) {
                int area = region.size();
                int sides = countSides(region);
                sum += sides * area;
            }
            return sum;
        }

        // Använd MARKED och markera alla plantor i en region som har ett staket i en viss riktning (en riktning i taget).
        // Antalet sammanhängande plantor i varje rad kan sedan räknas == antalet sidor i regionen som pekar i en viss riktning 
        private int countSides(ArrayList<Point> region) {
            boolean prevState;
            int sideCount = 0;
            // Staket i övre kant
            clearMarked();
            for (Point p : region) {
                if (MAP[p.x][p.y].fenceUp)
                    MARKED[p.x][p.y] = true;
            }
            // Räkna sidor
            prevState = false;
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    if (MARKED[x][y] != prevState)
                        if (MARKED[x][y]) sideCount++;
                    prevState = MARKED[x][y];
                }
            }

            // Staket i nedre kant
            clearMarked();
            for (Point p : region) {
                if (MAP[p.x][p.y].fenceDown)
                    MARKED[p.x][p.y] = true;
            }
            // Räkna sidor
            prevState = false;
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    if (MARKED[x][y] != prevState)
                        if (MARKED[x][y]) sideCount++;
                    prevState = MARKED[x][y];
                }
            }

            // Staket i höger kant
            clearMarked();
            for (Point p : region) {
                if (MAP[p.x][p.y].fenceRight)
                    MARKED[p.x][p.y] = true;
            }
            // Räkna sidor
            prevState = false;
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    if (MARKED[x][y] != prevState)
                        if (MARKED[x][y]) sideCount++;
                    prevState = MARKED[x][y];
                }
            }

            // Staket i vänster kant
            clearMarked();
            for (Point p : region) {
                if (MAP[p.x][p.y].fenceLeft)
                    MARKED[p.x][p.y] = true;
            }
            // Räkna sidor
            prevState = false;
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    if (MARKED[x][y] != prevState)
                        if (MARKED[x][y]) sideCount++;
                    prevState = MARKED[x][y];
                }
            }

            return sideCount;
        }
    }
}
