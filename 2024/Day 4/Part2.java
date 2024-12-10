import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Part2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));
        LinkedList<String> rows = new LinkedList<>();
        while (sc.hasNext())
            rows.add(sc.nextLine());
        sc.close();

        char[][] matrix = new char[rows.get(0).length()][rows.size()];
        for (int y = 0; y < rows.size(); y++) {
            for (int x = 0; x < rows.get(0).length(); x++) {
                matrix[x][y] = rows.get(y).charAt(x);
            }
        }

        WordSoup2 ws = new WordSoup2(matrix);
        System.out.println(ws.xCount());
    }

}

class WordSoup2 {
    private final char[][] MATRIX;
    private final int WIDTH, HEIGHT;

    public WordSoup2(char[][] matrix) {
        MATRIX = matrix;
        WIDTH = matrix[0].length;
        HEIGHT = matrix.length;
    }

    public int xCount() {
        int count = 0;

        for (int x = 1; x < WIDTH - 1; x++) {
            for (int y = 1; y < HEIGHT -1; y++) {
                if (isX(x, y))
                    count++;
            }
        }

        return count;
    }

    public boolean isX(int x, int y) {
        if (getChar(x, y) != 'A')
            return false;
        if (!((getChar(x-1, y-1) == 'M' && getChar(x+1, y+1) == 'S') || (getChar(x-1, y-1) == 'S' && getChar(x+1, y+1) == 'M')))
            return false;
        if (!((getChar(x+1, y-1) == 'M' && getChar(x-1, y+1) == 'S') || (getChar(x+1, y-1) == 'S' && getChar(x-1, y+1) == 'M')))
            return false;

        return true;
    }


    private char getChar(int x, int y) {
        return MATRIX[x][y];
    }
}