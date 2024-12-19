import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Towels t = new Towels(ip);
        System.out.println(t.countSolutions());
    }

    private static class Towels {
        private final HashSet<String> AVAILABLE_PATTERNS = new HashSet<>();
        private final ArrayList<String> TOWELS = new ArrayList<>();
        private final HashMap<String, Long> CACHE = new HashMap<>();

        public Towels(InputParser ip) {
            String[] patterns = ip.getLine(0).split(", ");
            for (String p : patterns)
                AVAILABLE_PATTERNS.add(p);

            for (int i = 2; i < ip.lineCount(); i++)
                TOWELS.add(ip.getLine(i));
        }
      
        private long combinations(String towel) {
            if (towel.equals(""))
                return 1;

            if (CACHE.containsKey(towel))
                return CACHE.get(towel);

            long combinations = 0;

            for (String p : AVAILABLE_PATTERNS) {
                if (towel.indexOf(p) == 0) {
                    String trimmedTowel = towel.substring(p.length());
                    long trimmedCombinations = combinations(trimmedTowel);
                    combinations += trimmedCombinations;
                    CACHE.put(trimmedTowel, trimmedCombinations);
                }
            }

            return combinations;
        }

        public long countSolutions() {
            long c = 0;
            for (int i = 0; i < TOWELS.size(); i++)
                c += combinations(TOWELS.get(i));
            return c;
        }
    }
}
