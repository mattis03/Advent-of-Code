import java.util.LinkedList;
import java.util.Scanner;

public class ScratchCard {
    private int id, matchcount, points;
    private LinkedList<Integer> winningNumbers = new LinkedList<>();
    private LinkedList<Integer> cardNumbers = new LinkedList<>();

    public ScratchCard(String s) {
        String[] strArr = s.split(":"); // Contains id in the first string and the card in the second string
        
        Scanner sc = new Scanner(strArr[0]);
        while (!sc.hasNextInt()) {
            sc.next();
        }
        id = sc.nextInt();
        sc.close();

        strArr = strArr[1].split("\\|"); // Contains winning numbers in the first string and card numbers in the second string

        // Save winning numbers
        sc = new Scanner(strArr[0]);
        while (sc.hasNextInt()) {
            winningNumbers.add(sc.nextInt());
        }
        sc.close();

        // Save card numbers
        sc = new Scanner(strArr[1]);
        while (sc.hasNextInt()) {
            cardNumbers.add(sc.nextInt());
        }
        sc.close();

        // Calculate score
        for (Integer i : cardNumbers) {
            if (winningNumbers.contains(i)) {
                if (points == 0) {
                    points = 1;
                } else {
                    points *= 2;
                }
                matchcount++;
            }
        }
    }

    public int getID() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public int getMatchesCount() {
        return matchcount;
    }
}