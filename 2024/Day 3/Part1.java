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
        boolean mulEnabled = true;

        for (int i = 0; i < s.length() - 6; i++) {

            if (s.substring(i, i + 4).equals("mul(")) {
                if (!mulEnabled) continue;
                String numberString = s.substring(i + 4, s.indexOf(")", i));
                int res = calcProdut(numberString);
                if (res != -1) {
                    productSum += res;
                    i = s.indexOf(")", i);
                }

            } else if (s.substring(i, i + 7).equals("don't()")) {
                mulEnabled = false;
                i += 6;

            } else if (s.substring(i, i + 4).equals("do()")) {
                mulEnabled = true;
                i += 3;
            }
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