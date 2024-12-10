import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Part1 {
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

        WordSoup ws = new WordSoup(matrix);
        System.out.println(ws.wordCount("XMAS"));
    }

}

class WordSoup {
    private final char[][] MATRIX;
    private final int WIDTH, HEIGHT;

    private String word, revWord;
    private int count;

    public WordSoup(char[][] matrix) {
        MATRIX = matrix;
        WIDTH = matrix[0].length;
        HEIGHT = matrix.length;
    }

    public int wordCount(String word) {
        this.word = word;
        this.revWord = "";
        for (int i = word.length()-1; i >= 0; i--)
            revWord += word.charAt(i);

        count = 0;

        countHorizontal();
        countVertical();
        countDiagonal1();
        countDiagonal2();

        return count;
    }

    private void countHorizontal() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x <= WIDTH - word.length(); x++) {
                String teststr = getHorizontalString(x, y);
                if (teststr.equals(word) || teststr.equals(revWord))
                    count++;
            }
        }
    }

    private void countVertical() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y <= HEIGHT - word.length(); y++) {
                String teststr = getVerticalString(x, y);
                if (teststr.equals(word) || teststr.equals(revWord))
                    count++;
            }
        }
    }

    private void countDiagonal1() {
        for (int x = 0; x <= WIDTH - word.length(); x++) {
            for (int y = 0; y <= HEIGHT - word.length(); y++) {
                String teststr = getDiagonal1String(x, y);
                if (teststr.equals(word) || teststr.equals(revWord))
                    count++;
            }
        }
    }

    private void countDiagonal2() {
        for (int x = word.length()-1; x < WIDTH; x++) {
            for (int y = 0; y <= HEIGHT - word.length(); y++) {
                String teststr = getDiagonal2String(x, y);
                if (teststr.equals(word) || teststr.equals(revWord))
                    count++;
            }
        }
    }
    
    private String getHorizontalString(int x, int y) {
        String retstr = "";
        for (int i = 0; i < word.length(); i++)
            retstr += getChar(x + i, y);
        return retstr;
    }

    private String getVerticalString(int x, int y) {
        String retStr = "";
        for (int i = 0; i < word.length(); i++)
            retStr += getChar(x, y + i);
        return retStr;
    }

    private String getDiagonal1String(int x, int y) {
        String retStr = "";
        for (int i = 0; i < word.length(); i++)
            retStr += getChar(x + i, y + i);
        return retStr;
    }

    private String getDiagonal2String(int x, int y) {
        String retStr = "";
        for (int i = 0; i < word.length(); i++)
            retStr += getChar(x - i, y + i);
        return retStr;
    }

    private char getChar(int x, int y) {
        return MATRIX[x][y];
    }
}