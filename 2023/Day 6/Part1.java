import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Part1 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input2"));
        String timeStr = sc.nextLine().substring(5);
        String durStr = sc.nextLine().substring(11);
        sc.close();

        LinkedList<Integer> time = new LinkedList<>();
        sc = new Scanner(timeStr);
        while (sc.hasNext()) time.add(sc.nextInt());
        sc.close();

        LinkedList<Integer> duration = new LinkedList<>();
        sc = new Scanner(durStr);
        while (sc.hasNext()) duration.add(sc.nextInt());
        sc.close();

        int ack = 1;
        for (int i = 0; i < time.size(); i++) {
            ack *= calcWinCount(time.get(i), duration.get(i));
        }

        System.out.println(ack);
    }

    // Calculate how many ways there are to beat the record
    public static int calcWinCount(int raceDuration, int raceRecord) {
        int winCount = 0;
        for (int btnDur = 0; btnDur < raceDuration; btnDur++) {
            // Check if the current button duration results in beating the record
            if (btnDur * (raceDuration - btnDur) > raceRecord) winCount++;
        }
        return winCount;
    }
}