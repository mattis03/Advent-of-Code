import java.util.HashMap;

public class Part1_2 {
    public static final HashMap<Long, Long> CACHE = new HashMap<>();
    public static int maxRecursionDepth;

    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        String[] numbers = ip.getLine(0).split(" ");
        
        long start = System.currentTimeMillis();       
        long sum25 = 0, sum75 = 0;
        maxRecursionDepth = 25;
        for (String stone : numbers)
            sum25 += countStones(Long.parseLong(stone), 0);
        maxRecursionDepth = 75;
        CACHE.clear();
        for (String stone : numbers)
            sum75 += countStones(Long.parseLong(stone), 0);
        long end = System.currentTimeMillis();
        
        System.out.println("Part 1: " + sum25 + "\nPart 2: " + sum75);
        System.out.println("Done in " + (end - start) + "ms");
    }

    public static long countStones(long stone, int depth) {
        // Returnera 1 om maxdjupet nåtts (nått lövnod)
        if (depth == maxRecursionDepth)
            return 1;

        // Kolla om stenantalet redan har genererats för denna sten på detta djup
        long thisKey = generateStoneKey(stone, depth);
        if (CACHE.containsKey(thisKey))
            return CACHE.get(thisKey);

        // Antal stenar som genereras från denna sten
        long sum = 0;

        // Om stenen inte är cachad måste den genereras
        if (stone == 0) {
            sum += countStones(1, depth + 1);
        } else {
            int length = lengthOfLong(stone);
            if (length % 2 == 0) {
                sum += countStones(upperHalf(stone, length), depth + 1) + countStones(lowerHalf(stone, length), depth + 1);
            } else {
                sum += countStones(stone * 2024, depth + 1);
            }
        }

        // Lägg till sten till cache
        CACHE.put(thisKey, sum);

        return sum;
    }

    public static long generateStoneKey(long stone, long depth) {
        return depth | (stone << 7L); // Förutsatt att depth < 128
    }

    public static int lengthOfLong(long l) {
        int length = 0;
        do {
            length++;
            l /= 10;
        } while (l != 0);
        return length;
    }
    
    public static long upperHalf(long l, int length) {
        for (int i = 0; i < length / 2; i++)
            l /= 10;
        return l;
    }
    
    public static long lowerHalf(long l, int length) {
        return l % (long)Math.pow(10, length / 2);
    }
}
