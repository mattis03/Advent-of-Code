import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Part2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));
        String timeStr = sc.nextLine().substring(5);
        String durStr = sc.nextLine().substring(11);
        sc.close();

        sc = new Scanner(timeStr);
        String time = "";
        while (sc.hasNext()) time += sc.nextInt();
        sc.close();

        sc = new Scanner(durStr);
        String duration = "";
        while (sc.hasNext()) duration += sc.nextInt();
        sc.close();

        System.out.println(calcWinCount(Long.parseLong(time), Long.parseLong(duration)));
    }

    // Calculate how many ways there are to beat the record
    public static long calcWinCount(long raceDuration, long raceRecord) {
        long winCount = 0;
        for (int btnDur = 0; btnDur < raceDuration; btnDur++) {
            // Check if the current button duration results in beating the record
            if (btnDur * (raceDuration - btnDur) > raceRecord) winCount++;
        }
        return winCount;
    }
}
