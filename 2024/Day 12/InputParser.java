import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;

public class InputParser {
    private final LinkedList<String> LINES = new LinkedList<>();

    public InputParser(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            while (sc.hasNext())
                LINES.add(sc.nextLine());
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