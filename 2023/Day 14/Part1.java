import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Part1 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));
        String s = "";
        while (sc.hasNext()) {
            s += sc.nextLine() + "\n";
        }
        sc.close();

        Platform p = new Platform(s.split("\n"));

        for (long i = 0; i < 1000000000; i++) {
            p.slideNorth();
            p.slideWest();
            p.slideSouth();
            p.slideEast();
        }
        System.out.println(p.calculateLoad());
    }
}

class Platform {
    private static final int AIR = 0, OBSTACLE = 1, ROCK = 2;

    private final int[][] platform;
    private final int width, height;

    public Platform(String[] rows) {
        height = rows.length;
        width = rows[0].length();
        platform = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                switch (rows[y].charAt(x)) {
                    case '.': setElement(x, y, AIR); break;
                    case '#': setElement(x, y, OBSTACLE); break;
                    case 'O': setElement(x, y, ROCK); break;
                }
            }
        }
    }

    public void slideNorth() {
        // Repeat for every column
        for (int x = 0; x < width; x++) {
            // Move an element at a specific y-position until it gets stuck
            for (int y = 1; y < height; y++) {
                int c = getElement(x, y);
                if (c != ROCK) continue;
                int newYPos = y - 1;
                while (getElement(x, newYPos) == AIR) {
                    setElement(x, newYPos, ROCK);
                    setElement(x, newYPos + 1, AIR);
                    newYPos--;
                    if (newYPos < 0) break;
                }
            }
        }
    }

    public void slideSouth() {
        for (int x = 0; x < width; x++) {
            for (int y = height-2; y >= 0; y--) {
                int c = getElement(x, y);
                if (c != ROCK) continue;
                int newYPos = y + 1;
                while (getElement(x, newYPos) == AIR) {
                    setElement(x, newYPos, ROCK);
                    setElement(x, newYPos - 1, AIR);
                    newYPos++;
                    if (newYPos >= height) break;
                }
            }
        }
    }

    public void slideEast() {
        for (int y = 0; y < height; y++) {
            for (int x = width-2; x >= 0; x--) {
                int c = getElement(x, y);
                if (c != ROCK) continue;
                int newXpos = x + 1;
                while (getElement(newXpos, y) == AIR) {
                    setElement(newXpos, y, ROCK);
                    setElement(newXpos - 1, y, AIR);
                    newXpos++;
                    if (newXpos >= width) break;
                }
            }
        }
    }

    public void slideWest() {
        for (int y = 0; y < height; y++) {
            for (int x = 1; x < width; x++) {
                int c = getElement(x, y);
                if (c != ROCK) continue;
                int newXpos = x - 1;
                while (getElement(newXpos, y) == AIR) {
                    setElement(newXpos, y, ROCK);
                    setElement(newXpos + 1, y, AIR);
                    newXpos--;
                    if (newXpos < 0) break;
                }
            }
        }
    }

    public int calculateLoad() {
        int res = 0;
        for (int y = 0; y < height; y++ ){
            for (int x = 0; x < width; x++) {
                if (getElement(x, y) == ROCK) {
                    res += height - y;
                }
            }
        }
        return res;
    }

    private int getElement(int x, int y) {
        return platform[y][x];
    }

    private void setElement(int x, int y, int element) {
        platform[y][x] = element;
    }

    public String toString() {
        String retStr = "";
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                retStr += getElement(x, y);
            }
            retStr += "\n";
        }
        return retStr;
    }
}