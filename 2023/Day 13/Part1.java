import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Part1 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));
        
        String in = "";
        while (sc.hasNext()) {
            in += sc.nextLine() + "\n";
        }
        sc.close();
        String[] p = in.split("\n\n");

        LinkedList<Pattern> patterns = new LinkedList<>();
        for (String pStr : p) {
            patterns.add(new Pattern(pStr));
        }

        long sum = 0;
        for (Pattern pat : patterns) {
            if (pat.COUNT_COLUMNS != -1) sum += pat.COUNT_COLUMNS;
            if (pat.COUNT_ROWS != -1) sum += pat.COUNT_ROWS * 100;
        }
        System.out.println("Part 1: " + sum);

        long sum2 = 0;
        for (Pattern pat : patterns) {
            LinkedList<Pattern> permuations = pat.smudgePermuatations();

            for (Pattern perm : permuations) {

                int originalCol = pat.COUNT_COLUMNS;
                int originalRow = pat.COUNT_ROWS;

                if (perm.COUNT_COLUMNS != -1 && perm.COUNT_COLUMNS != originalCol) {
                    sum2 += perm.COUNT_COLUMNS;
                    if (perm.COUNT_ROWS != -1 && perm.COUNT_ROWS != originalRow) {
                        sum2 += 100 * perm.COUNT_COLUMNS;
                    }
                    break;
                }
                if (perm.COUNT_ROWS != -1 && perm.COUNT_ROWS != originalRow) {
                    sum2 += 100 * perm.COUNT_ROWS;
                    if (perm.COUNT_COLUMNS != -1 && perm.COUNT_COLUMNS != originalCol) {
                        sum2 += perm.COUNT_COLUMNS;
                    }
                    break;
                }

            }

        }
        System.out.println("Part 2: " + sum2);
    }
}

class Pattern {
    public final int HORIZONTAL_REFLECTION_LINE, VERTICAL_REFLECTION_LINE;
    public final int COUNT_ROWS, COUNT_COLUMNS;

    private final String[] normal, rotated;

    public Pattern(String pStr) {
        normal = pStr.split("\n");
        
        // Rotate the normal +90deg for checking vertical mirror lines
        rotated = new String[normal[0].length()];
        for (int i = 0; i < rotated.length; i++) {
            String r = "";
            for (int j = normal.length-1; j >= 0; j--) {
                r += normal[j].charAt(i);
            }
            rotated[i] = r;
        }

        HORIZONTAL_REFLECTION_LINE = findMirrorLine(normal);
        VERTICAL_REFLECTION_LINE = findMirrorLine(rotated);

        if (HORIZONTAL_REFLECTION_LINE != -1) COUNT_ROWS = HORIZONTAL_REFLECTION_LINE+1;
        else COUNT_ROWS = -1;
        if (VERTICAL_REFLECTION_LINE != -1) COUNT_COLUMNS = VERTICAL_REFLECTION_LINE+1;
        else COUNT_COLUMNS = -1;
    }

    private int findMirrorLine(String[] p) {
        for (int i = 0; i < p.length - 1; i++) {
            // Find a pair of adjacent identical lines (potential mirror line)
            if (p[i].equals(p[i + 1])) {
                // Check if the pattern is identical on both sides of these lines
                int stepsFromLine = 0;
                boolean isMirrorLine = true;
                // Check lines up to pattern high or low edge
                while ((i-stepsFromLine) >= 0 && (i+1+stepsFromLine) < p.length) {
                    if (!p[i-stepsFromLine].equals(p[i+1+stepsFromLine])) {
                        isMirrorLine = false;
                        break;
                    }
                    stepsFromLine++;
                }
                // If the while looped passed, the line between i and i+1 is a mirror line
                if (isMirrorLine) return i;
            }
        }
        return -1;
    }

    public LinkedList<Pattern> smudgePermuatations() {
        LinkedList<Pattern> permutations = new LinkedList<>();
        for (int i = 0; i < normal.length; i++) {
            for (int j = 0; j < normal[0].length(); j++) {
                // Flip a character
                normal[i] = flipChar(normal[i], j);

                // Merge the strings
                String pStr = "";
                for (int k = 0; k < normal.length; k++) {
                    pStr += normal[k] + "\n";
                }
                // Add a new pattern based on the modified string
                permutations.add(new Pattern(pStr));
                
                // Flip the character back
                normal[i] = flipChar(normal[i], j);
            }
        }

        return permutations;
    }

    private String flipChar(String s, int index) {
        char[] cArr = s.toCharArray();
        if (cArr[index] == '#') {
            cArr[index] = '.';
        } else {
            cArr[index] = '#';
        }
        String retStr = "";
        for (int i = 0; i < cArr.length; i++) {
            retStr += cArr[i];
        }
        return retStr;
    }
}