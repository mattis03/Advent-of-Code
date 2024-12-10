import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

public class Part2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));
        
        LinkedList<Hand2> hands = new LinkedList<>();

        while (sc.hasNext()) {
            hands.add(new Hand2(sc.nextLine()));
        }
        sc.close();

        // Sort the hands, weakest first
        hands.sort(null);

        // Calculate sum of winnings
        int sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            sum += hands.get(i).getBet() * (i+1);
        }

        System.out.println(sum);
    }
}

class Hand2 implements Comparable<Hand2> {
    private static final int FIVE_OF_A_KIND = 6, FOUR_OF_A_KIND = 5, FULL_HOUSE = 4, THREE_OF_A_KIND = 3, TWO_PAIR = 2, ONE_PAIR = 1, HIGH_CARD = 0;

    private int[] cards = new int[5];
    private int bet;
    private int type;

    public Hand2(String hand) {
        String[] split = hand.split(" "); // [0]: Cards, [1]: Bet

        int jokerCount = 0;
        for (int i = 0; i < 5; i++) {
            char c = split[0].charAt(i);
            switch (c) {
                case 'A': cards[i] = 14; break;
                case 'K': cards[i] = 13; break;
                case 'Q': cards[i] = 12; break;
                case 'J': cards[i] = 1;  jokerCount++; break;
                case 'T': cards[i] = 10; break;
                default:  cards[i] = Integer.parseInt(c + ""); break;
            }
        }
        
        bet = Integer.parseInt(split[1]);

        type = classify();
        // Examine the strongest typing this hand can become if the hand has jokers 
        switch (jokerCount) {
            case 1:
                switch (type) {
                    case FOUR_OF_A_KIND:  type = FIVE_OF_A_KIND;  break;
                    case FULL_HOUSE:      type = FOUR_OF_A_KIND;  break;
                    case THREE_OF_A_KIND: type = FOUR_OF_A_KIND;  break;
                    case TWO_PAIR:        type = FULL_HOUSE;      break;
                    case ONE_PAIR:        type = THREE_OF_A_KIND; break;
                    case HIGH_CARD:       type = ONE_PAIR;        break;
                }
            break;
            case 2:
                switch (type) {
                    //case FOUR_OF_A_KIND // NA
                    case FULL_HOUSE:      type = FIVE_OF_A_KIND;  break;
                    //case THREE_OF_A_KIND // NA
                    case TWO_PAIR:        type = FOUR_OF_A_KIND;  break;
                    case ONE_PAIR:        type = THREE_OF_A_KIND; break;
                    //case HIGH_CARD // NA
                }
            break;
            case 3:
                switch (type) {
                    //case FOUR_OF_A_KIND // NA
                    case FULL_HOUSE:      type = FIVE_OF_A_KIND;  break;
                    case THREE_OF_A_KIND: type = FOUR_OF_A_KIND;  break;
                    //case TWO_PAIR // NA
                    //case ONE_PAIR // NA
                    //case HIGH_CARD // NA
                }
            break;
            case 4:
                switch (type) {
                    case FOUR_OF_A_KIND:  type = FIVE_OF_A_KIND;  break;
                    //case FULL_HOUSE // NA
                    //case THREE_OF_A_KIND // NA
                    //case TWO_PAIR // NA
                    //case ONE_PAIR // NA
                    //case HIGH_CARD // NA
                }
            break;
        }
    }

    private int classify() {
        Hashtable<Integer, Integer> values = new Hashtable<>();
        for (int i : cards) {
            if (values.containsKey(i)) {
                values.put(i, values.get(i) + 1);
            } else {
                values.put(i, 1);
            }
        }

        if (values.size() == 1) return FIVE_OF_A_KIND;
        if (values.size() == 5) return HIGH_CARD;
        if (values.size() == 2) {
            if (values.contains(4)) return FOUR_OF_A_KIND;
            if (values.contains(3)) return FULL_HOUSE;
        } else if (values.size() == 3) {
            if (values.contains(3)) return THREE_OF_A_KIND;
            if (values.contains(1)) return TWO_PAIR;
        }
        return ONE_PAIR;
    }

    public int getBet() {
        return bet;
    }

    /**
     * Compares two hands.
     * @param that - Another hand of cards
     * @return A value greater than 0 if this card is stronger, less than 0 if weaker, 0 if equal.
     */
    public int compareTo(Hand2 that) {
        if (this.type - that.type != 0) return this.type - that.type;
        // Compare card by card
        for (int i = 0; i < 5; i++) {
            if (this.cards[i] - that.cards[i] != 0) return this.cards[i] - that.cards[i];
        }
        return 0;
    }
}