import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Day2 {
    public static void main (String[] args) throws FileNotFoundException{
        Scanner sc = new Scanner(new File("input"));
        int gameIDSum = 0;
        int gamePowerSum = 0;
        while (sc.hasNext()) {
            String row = sc.nextLine();
            Game game = new Game(row);
            if(game.isPossible(12, 13, 14)) gameIDSum += game.getID();
            gamePowerSum += game.power();
        }
        System.out.println("Game ID sum: " + gameIDSum);
        System.out.println("Power sum: " + gamePowerSum);
        sc.close();
    }
}

class Game {

    private int ID = 0;
    private LinkedList<Set> sets = new LinkedList<>();

    public Game(String s) {
        ID = Integer.parseInt(s.substring(5, s.indexOf(":")));
        s = s.substring(s.indexOf(":") + 1, s.length()); // Trim away "Game "
        String[] setStrings = s.split(";"); // Split the sets
        for (String str : setStrings) sets.add(new Set(str)); // Create all sets for the game and store them in a list
    }

    /**
     * A game is defined as impossible if any set of this game contains a greater number of cubes than
     * there are cubes of that color in the bag.
     * @param redCount - Number of red cubes in the bag.
     * @param greenCount - Number of green cubes in the bag
     * @param blueCount - Number of blue cubes in the bag
     * @return True if the game is possible given the cubes in the bag
     */
    public boolean isPossible(int redCount, int greenCount, int blueCount) {
        for (Set set : sets) {
            if(set.redCount() > redCount || set.greenCount() > greenCount || set.blueCount() > blueCount) return false;
        }
        return true;
    }

    /**
     * Calculates the smallest number of cubes needed in the bag for each color for the game to be possible
     * and multiplies the values for red, green and blue cubes together.
     * @return Power
     */
    public int power() {
        int maxRed = 0, maxGreen = 0, maxBlue = 0;
        for (Set set : sets) {
            maxRed = Math.max(maxRed, set.redCount());
            maxGreen = Math.max(maxGreen, set.greenCount());
            maxBlue = Math.max(maxBlue, set.blueCount());
        }
        return maxRed * maxBlue * maxGreen;
    }

    public int getID() { return ID; }

    class Set {
        
        private int redCount = 0, greenCount = 0, blueCount = 0;

        public Set(String s) {
            s = s.replace(',', ' '); // Remove commas
            Scanner sc = new Scanner(s);
            while(sc.hasNext()) {
                int value = Integer.parseInt(sc.next());
                String token = sc.next();
                switch(token) {
                    case "red": redCount = value; break;
                    case "green": greenCount = value; break;
                    case "blue": blueCount = value; break;
                }
            }
            sc.close();
        }

        public int redCount() { return redCount; }

        public int greenCount() { return greenCount; }

        public int blueCount() { return blueCount; }

    }
}