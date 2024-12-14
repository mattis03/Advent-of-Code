import java.util.Iterator;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class InputParser {
    private final ArrayList<String> LINES = new ArrayList<>();
    private String singleString = "";

    public InputParser(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            while (sc.hasNext()) {
                String nextLine = sc.nextLine();
                LINES.add(nextLine);
                singleString += nextLine + "\n";
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("File read error");
            System.exit(1);
        }
    }

    public int lineCount() {
        return LINES.size();
    }

    public String getLine(int lineIndex) {
        return LINES.get(lineIndex);
    }

    public char getChar(int x, int y) {
        return LINES.get(y).charAt(x);
    }

    public String singleString() {
        return singleString;
    }

    public Iterable<String> lines() {
        return new LineIterator();
    }

    private class LineIterator implements Iterable<String> {
        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {
                int currentIndex = 0;
        
                @Override
                public boolean hasNext() {
                    return currentIndex < LINES.size();
                }
        
                @Override
                public String next() {
                    return LINES.get(currentIndex++);
                }
            };
        }
    }
}