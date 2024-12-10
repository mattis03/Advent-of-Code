import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Part2 {
    public static final LinkedList<String> rows = new LinkedList<>();
    public static final String numStr = "0123456789";
    public static int currGearX = 0, currGearY = 0;

    public static class Gear {
        private int numberNeighborsCount = 0;
        private int product = 0;

        public void multiplyProduct(int n) {
            if(product == 0) {
                product = n;
            } else {
                product *= n;
            }
            numberNeighborsCount++;
        }

        public int getProduct() {
            return product;
        }

        public int getNeighborCount() {
            return numberNeighborsCount;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));
        while (sc.hasNext()) {
            rows.add(sc.nextLine());
        }
        sc.close();

        Gear[][] gears = new Gear[rows.size()][rows.get(0).length()];

        for(int row = 0; row < rows.size(); row++) {
            String r = rows.get(row);
            for(int col = 0; col < r.length(); col++) {

                int numberIndex = findNumber(row, col);

                // If no more numbers are found on the current row, move on to the next row
                if(numberIndex == -1) break;

                int number = extractNumber(row, col);
                int numberLength = Integer.toString(number).length();

                for(int i = 0; i < numberLength; i++) {
                    if (hasAdjacentGear(row, numberIndex + i)) {
                        if(gears[currGearY][currGearX] == null) {
                            Gear g = new Gear();
                            g.multiplyProduct(number);
                            gears[currGearY][currGearX] = g;
                        } else {
                            gears[currGearY][currGearX].multiplyProduct(number);
                        }
                        break;
                    }
                }

                col = numberIndex + numberLength - 1; // -1 to compensate for loop ++

            }
        }


        int sum = 0;

        for (Gear[] row : gears) {
            for (Gear g : row) {
                if(g == null) continue;
                if(g.getNeighborCount() == 2) sum += g.getProduct();
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

    public static boolean hasAdjacentGear(int row, int column) {
        for(int y = Math.max(0, row-1); y <= Math.min(rows.size()-1, row+1); y++) {
            String r = rows.get(y);
            for(int x = Math.max(0, column-1); x <= Math.min(r.length()-1, column+1); x++) {
                char c = r.charAt(x);
                // Only look for *
                if(c == '*') {
                    currGearX = x;
                    currGearY = y;
                    return true;
                }
            }
        }
        return false;
    }
}