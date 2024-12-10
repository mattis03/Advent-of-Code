import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Part1 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));
        String inStr = "";
        while (sc.hasNext()) {
            inStr += sc.next();
        }
        sc.close();

        System.out.println(multiply(inStr));
    }

    public static int multiply(String s) {
        int productSum = 0;
        // Find all valid mul instructions
        int searchIndex = 0;
        while (s.indexOf("mul(", searchIndex) != -1) {
            int mulIndexStart = s.indexOf("mul(", searchIndex);
            int mulIndexEnd = s.indexOf(')', mulIndexStart);
            String numberString = s.substring(mulIndexStart + 4, mulIndexEnd);
            
            int res = calcProdut(numberString);
            if (res != -1) productSum += res;

            searchIndex = mulIndexStart + 1;
        }

        return productSum;
    }

    public static int calcProdut(String mulInstr) {
        String[] terms = mulInstr.split(",");
        if (terms.length != 2) return -1;

        int a, b;

        try {
            a = Integer.parseInt(terms[0]);
            b = Integer.parseInt(terms[1]);
            return a * b;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}