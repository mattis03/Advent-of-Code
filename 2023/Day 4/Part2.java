import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Part2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));
        LinkedList<ScratchCard> cards = new LinkedList<>();

        while (sc.hasNext()) {
            String card = sc.nextLine();
            cards.add(new ScratchCard(card));
        }
        sc.close();

        int[] copyCount = new int[cards.size()]; // Number of copies of each card, including the original
        Arrays.fill(copyCount, 1); // All cards have one copy from the beginning

        for (int currCard = 0; currCard < cards.size() - 1; currCard++) {

            int matchesCount = cards.get(currCard).getMatchesCount();
            // Add {copyCount[currCard]} copies of the next {matchesCount} cards in the list
            for (int j = 0; j < matchesCount; j++) {
                if(currCard + j + 1 >= cards.size()) break;
                copyCount[currCard + j + 1] += copyCount[currCard];
            }

        }

        int sum = 0;
        for (int i : copyCount) {
            sum += i;
        }
        System.out.println(sum);

    }
}
