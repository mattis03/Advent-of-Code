import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Part1 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));
        LinkedList<String> rules = new LinkedList<>();
        LinkedList<String> pages = new LinkedList<>();
        boolean split = false;
        while (sc.hasNext()) {
            String next = sc.nextLine();
            if (!next.contains("|") && !split) {
                split = true;
                continue;
            }

            if (split) {
                pages.add(next);
            } else {
                rules.add(next);
            }
        }
        sc.close();

        new RuleBook(rules, pages);
    }
}

class RuleBook {
    private final HashMap<Integer, LinkedList<Integer>> RULES = new HashMap<>();
    private final LinkedList<LinkedList<Integer>> PAGES = new LinkedList<>();

    public RuleBook(LinkedList<String> rules, LinkedList<String> pages) {
        for (String r : rules) {
            int key = Integer.parseInt(r.split("\\|")[0]);
            int val = Integer.parseInt(r.split("\\|")[1]);

            if (!RULES.containsKey(key))
                RULES.put(key, new LinkedList<Integer>());

            RULES.get(key).add(val);
        }
        
        for (String p : pages) {
            LinkedList<Integer> number = new LinkedList<>();
            for (String n : p.split(","))
                number.add(Integer.parseInt(n));
            PAGES.add(number);
        }

        int sum = 0;
        for (LinkedList<Integer> p : PAGES) {
            if (pageIsValid(p))
                sum += findMedian(p);
        }

        System.out.println(sum);
    }

    private boolean pageIsValid(LinkedList<Integer> page) {
        for (int i = 0; i < page.size() - 1; i++) {
            // Hämta ett tal i taget från page
            int n = page.get(i);
            // n måste följa regler för k
            for (int j = i + 1; j < page.size(); j++) {
                // index(n) < index(k)
                int k = page.get(j);

                LinkedList<Integer> kRules = RULES.get(k);

                // Regler för k innehåller n <=> k måste komma före n i page
                if (kRules.contains(n))
                    return false;
            }
        }
        return true;
    }

    private int findMedian(LinkedList<Integer> page) {
        return page.get(page.size() / 2);
    }
}