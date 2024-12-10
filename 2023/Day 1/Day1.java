import java.io.File;
import java.util.Scanner;

import java.io.FileNotFoundException;

public class Day1 {
    public static void main(String[] args) throws FileNotFoundException {

        Scanner sc = new Scanner(new File("input"));

        int sum = 0;

        while(sc.hasNext()) {
            String l = sc.nextLine();
            l = replaceNumberWords(l);
            //System.out.println("First: " + findFirstDigit(l) + " Last: " + findLastDigit(l));
            sum += findFirstDigit(l) * 10 + findLastDigit(l);
        }
        sc.close();

        System.out.println(sum);
    }

    public static int findFirstDigit(String s) {
        for(int i = 0; i < s.length(); i++) {
            // Find and return the first digit
            if(isANumber(s.charAt(i))) {
                return Integer.parseInt(s.charAt(i) + "");
            }
        }

        throw new IllegalArgumentException("Input string does not contain a number");
    }

    public static int findLastDigit(String s) {
        for(int i = s.length() - 1; i >= 0; i--) {
            // Find and return the last digit
            if(isANumber(s.charAt(i))) {
                return Integer.parseInt(s.charAt(i) + "");
            }
        }

        throw new IllegalArgumentException("Input string does not contain a number");
    }

    public static boolean isANumber(char c) {
        String numbers = "0123456789";
        return numbers.indexOf(c) != -1;
    }

    public static String replaceNumberWords(String s) {
        String[] numbers = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        String modified = s;
        for(int i = 0; i <= s.length(); i++) {
            String subString = s.substring(0, i);

            for(int n = 0; n < 10; n++) {

                if(subString.contains(numbers[n])) {
                    modified = modified.replace(numbers[n], n + numbers[n]);
                }

            }
        }

        return modified;
    } 
}