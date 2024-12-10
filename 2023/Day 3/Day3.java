import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Day3 {
    public static final LinkedList<String> rows = new LinkedList<>();
    public static final String numStr = "0123456789";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));
        while (sc.hasNext()) {
            rows.add(sc.nextLine());
        }
        sc.close();

        int sum = 0;

        for(int row = 0; row < rows.size(); row++) {
            String r = rows.get(row);
            for(int col = 0; col < r.length(); col++) {

                int numberIndex = findNumber(row, col);

                // If no more numbers are found on the current row, move on to the next row
                if(numberIndex == -1) break;

                int number = extractNumber(row, col);
                int numberLength = Integer.toString(number).length();

                boolean hasSymbol = false;
                for(int i = 0; i < numberLength; i++) {
                    if (hasAdjacentSymbol(row, numberIndex + i)) {
                        hasSymbol = true;
                        break;
                    }
                }

                if(hasSymbol) sum += number;

                col = numberIndex + numberLength - 1; // -1 to compensate for loop ++

            }
        }

        System.out.println(sum);
    }

    public static int findNumber(int row, int startCol) {
        String r = rows.get(row);
        for(int i = startCol; i < r.length(); i++) {
            if(numStr.contains(r.charAt(i) + "")) return i;
        }
        return -1;
    }

    public static int extractNumber(int row, int startCol) {
        String r = rows.get(row).substring(startCol);
        // Remove anything that isn't numbers
        for(int i = 0; i < r.length(); i++) {
            String c = r.charAt(i) + "";
            if(!numStr.contains(c)) {
                r = r.replace(c, " ");
            }
        }
        Scanner sc = new Scanner(r);
        while (!sc.hasNextInt()) sc.next();
        if(!sc.hasNextInt()) {
            sc.close();
            return -1;
        }
        int res = sc.nextInt();
        sc.close();
        return res;
    }

    public static boolean hasAdjacentSymbol(int row, int column) {
        for(int y = Math.max(0, row-1); y <= Math.min(rows.size()-1, row+1); y++) {
            String r = rows.get(y);
            for(int x = Math.max(0, column-1); x <= Math.min(r.length()-1, column+1); x++) {
                char c = r.charAt(x);
                // A symbol is not a number nor a dot
                if(!numStr.contains(c + "") && c != '.') return true;
            }
        }
        return false;
    }
}