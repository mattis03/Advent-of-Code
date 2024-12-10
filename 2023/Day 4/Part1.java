import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Part1 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));
        LinkedList<ScratchCard> cards = new LinkedList<>();

        while (sc.hasNext()) {
            String card = sc.nextLine();
            cards.add(new ScratchCard(card));
        }
        sc.close();

        int sum = 0;
        for (ScratchCard scratchCard : cards) {
            sum += scratchCard.getPoints();
        }
        System.out.println(sum);
    }
}