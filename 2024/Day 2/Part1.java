import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Part1 {
    public static void main(String[] args) throws FileNotFoundException{
        Scanner sc = new Scanner(new File("input"));
        int nSafe = 0;
        while (sc.hasNext()) {
            String[] row = sc.nextLine().split(" ");
            int[] report = new int[row.length];
            for (int i = 0; i < row.length; i++)
                report[i] = Integer.parseInt(row[i]);
            if (isSafe(report)) nSafe++;
        }
        sc.close();

        System.out.println(nSafe);
    }

    public static boolean isSafe(int[] report) {
        int prevNum = report[0];
        if (report[0] == report[1]) return false;
        boolean increasing = report[1] > report[0];

        for (int i = 1; i < report.length; i++) {
            int currNum = report[i];

            if (currNum < prevNum && increasing)
                return false;
            if (currNum > prevNum && !increasing)
                return false;
            if (Math.abs(currNum - prevNum) < 1 || Math.abs(currNum - prevNum) > 3)
                return false;

            prevNum = currNum;
        }

        return true;
    }
}