import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Part1 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Towels t = new Towels(ip);
        System.out.println(t.countSolvable());
    }

    private static class Towels {
        private final HashSet<String> AVAILABLE_PATTERNS = new HashSet<>();
        private final ArrayList<String> TOWELS = new ArrayList<>();
        private final HashMap<String, Boolean> CACHE = new HashMap<>();

        public Towels(InputParser ip) {
            String[] patterns = ip.getLine(0).split(", ");
            for (String p : patterns)
                AVAILABLE_PATTERNS.add(p);

            for (int i = 2; i < ip.lineCount(); i++)
                TOWELS.add(ip.getLine(i));
        }
      
        private boolean isSolvable(String towel) {
            if (CACHE.containsKey(towel))
                return CACHE.get(towel);

            if (towel.equals(""))
                return true;

            for (String p : AVAILABLE_PATTERNS) {
                if (towel.indexOf(p) == 0) {
                    String trimmedTowel = towel.substring(p.length());
                    boolean solvable = isSolvable(trimmedTowel);
                    CACHE.put(trimmedTowel, solvable);
                    if (solvable)
                        return true;
                }
            }

            return false;
        }

        public int countSolvable() {
            int c = 0;
            for (int i = 0; i < TOWELS.size(); i++) {
                if (isSolvable(TOWELS.get(i)))
                    c++;
            }
            return c;
        }
    }
}
