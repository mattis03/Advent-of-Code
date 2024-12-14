import java.util.ArrayList;

public class Part1 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Restroom r = new Restroom(ip);
        r.simulateSeconds(100);
        System.out.println(r.safetyFactor());
    }

    private static class Restroom {
        private final int WIDTH = 101, HEIGHT = 103;
        private final ArrayList<Robot> ROBOT = new ArrayList<>();

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
            for (String row : ip.lines()) {
                String[] pv = row.split(" ");
                String[] pxy = pv[0].split(",");
                String[] vxy = pv[1].split(",");
                
                int px = Integer.parseInt(pxy[0].substring(2));
                int py = Integer.parseInt(pxy[1]);
                int vx = Integer.parseInt(vxy[0].substring(2));
                int vy = Integer.parseInt(vxy[1]);

                ROBOT.add(new Robot(px, py, vx, vy));
            }
        }

        public void simulateSeconds(int seconds) {
            for (Robot r : ROBOT) {
                r.xPos = (r.xPos + seconds * r.xVel) % WIDTH;
                r.yPos = (r.yPos + seconds * r.yVel) % HEIGHT;

                while (r.xPos < 0)
                    r.xPos += WIDTH;
                while (r.yPos < 0)
                    r.yPos += HEIGHT;
            }
        }

        public int safetyFactor() {
            int q1, q2, q3, q4;
            q1 = q2 = q3 = q4 = 0;

            for (Robot r : ROBOT) {
                // Ignorera robotar på mittlinjerna
                if (r.xPos == WIDTH / 2 || r.yPos == HEIGHT / 2)
                    continue;

                if (r.xPos < WIDTH / 2) {
                    if (r.yPos < HEIGHT / 2)
                        q1++;
                    else
                        q2++;
                } else {
                    if (r.yPos < HEIGHT / 2)
                        q3++;
                    else
                        q4++;
                }
            }
            return q1 * q2 * q3 * q4;
        }
    }
}