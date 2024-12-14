import java.util.ArrayList;

public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Restroom r = new Restroom(ip);
        int secondsElapsed = 0;
        while(true) {
            while (!r.foundGroup()) {
                r.simulateSeconds(1);
                secondsElapsed++;
            }

            r.print();
            System.out.println("\n" + secondsElapsed);
            try {
                Thread.sleep(500);
            } catch (Exception e) {}
        }
    }

    private static class Restroom {
        private final int WIDTH = 101, HEIGHT = 103;
        private final ArrayList<Robot> ROBOT = new ArrayList<>();
        private final int[][] MAP;

        private class Robot {
            public int xPos, yPos;
            public final int xVel, yVel;

            public Robot(int xPos, int yPos, int xVel, int yVel) {
                this.xPos = xPos;
                this.yPos = yPos;
                this.xVel = xVel;
                this.yVel = yVel;
            }
        }
        
        public Restroom(InputParser ip) {
            MAP = new int[WIDTH][HEIGHT];
            for (String row : ip.lines()) {
                String[] pv = row.split(" ");
                String[] pxy = pv[0].split(",");
                String[] vxy = pv[1].split(",");
                
                int px = Integer.parseInt(pxy[0].substring(2));
                int py = Integer.parseInt(pxy[1]);
                int vx = Integer.parseInt(vxy[0].substring(2));
                int vy = Integer.parseInt(vxy[1]);

                ROBOT.add(new Robot(px, py, vx, vy));
                MAP[px][py]++;
            }
        }

        public void simulateSeconds(int seconds) {
            for (Robot r : ROBOT) {
                MAP[r.xPos][r.yPos]--;
                
                r.xPos = (r.xPos + seconds * r.xVel) % WIDTH;
                r.yPos = (r.yPos + seconds * r.yVel) % HEIGHT;
                
                while (r.xPos < 0)
                    r.xPos += WIDTH;
                while (r.yPos < 0)
                    r.yPos += HEIGHT;

                MAP[r.xPos][r.yPos]++;
            }
        }

        public boolean foundGroup() {
            // Kolla om det finns fler än 50 robotar brevid varandra
            for (Robot r : ROBOT) {
                boolean[][] visited = new boolean[WIDTH][HEIGHT];
                if (dfs(r.xPos, r.yPos, visited) > 50)
                    return true;
            }
            return false;
        }

        private int dfs(int x, int y, boolean[][] visited) {
            if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT || visited[x][y] || MAP[x][y] == 0)
                return 0;

            visited[x][y] = true;
            return MAP[x][y] + dfs(x + 1, y, visited) + dfs(x - 1, y, visited) + dfs(x, y + 1, visited) + dfs(x, y - 1, visited);            
        }

        public void print() {
            String s = "";
            for (int y = 0; y < HEIGHT; y++) {
                s += "\n";
                for (int x = 0; x < WIDTH; x++)
                    if (MAP[x][y] == 0)
                        s += ".";
                    else
                        s += "#";
            }
            System.out.println(s);
        }
    }
}