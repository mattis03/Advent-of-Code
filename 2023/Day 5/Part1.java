import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Part1 {
    public static void main(String[] args) throws FileNotFoundException {
        MapSolver ms = new MapSolver(new File("input"));

        long minValue = Long.MAX_VALUE;
        for (Long seed : ms.SEEDS) {
            long location = ms.seedToLocation(seed); 
            if (location < minValue) minValue = location;
        }

        System.out.println("Part 1: " + minValue);
        System.out.println("Part 2: " + findMinP2(ms));
    }

    // Part 2
    public static long findMinP2(MapSolver ms) {
        LinkedList<Long> start = new LinkedList<>();
        LinkedList<Long> length = new LinkedList<>();
        int pairCount = ms.SEEDS.size() / 2;
        // Save ranges
        for (int i = 0; i < pairCount; i++) {
            start.add(ms.SEEDS.get(0));
            length.add(ms.SEEDS.get(1));
            ms.SEEDS.remove();
            ms.SEEDS.remove();
        }

        // Find minimum value
/*         long min = Long.MAX_VALUE;
        for (int i = 0; i < start.size(); i++) {

            long currStart = start.get(i);
            long currEnd = currStart + length.get(i);

            for(long j = currStart; j < currEnd; j++) {
                long location = ms.seedToLocation(j);
                if (location < min) min = location;
            }

        } */

        LinkedList<SolverThread> threads = new LinkedList<>();
        for (int i = 0; i < start.size(); i++) {
            SolverThread st = new SolverThread(ms, start.get(i), length.get(i));
            threads.add(st);
            st.start();
        }

        while (isRunning(threads)) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
        }

        return getMinValue(threads);
    }

    public static boolean isRunning(LinkedList<SolverThread> st) {
        for (SolverThread solverThread : st) {
            if(solverThread.isAlive()) return true;
        }
        return false;
    }

    public static long getMinValue(LinkedList<SolverThread> st) {
        long min = Long.MAX_VALUE;
        for (SolverThread solverThread : st) {
            if (solverThread.minValue < min) min = solverThread.minValue;
        }
        return min;
    }
}

class SolverThread extends Thread {
    private final long start, length;
    private final MapSolver msRef;
    public long minValue = Long.MAX_VALUE;

    public SolverThread(MapSolver ms, long start, long length) {
        this.start = start;
        this.length = length;
        msRef = ms;
    }

    public void run() {
        for(long j = start; j < start + length; j++) {
            long location = msRef.seedToLocation(j);
            if (location < minValue) minValue = location;
        }
    }
}

class MapSolver {
    
    public final LinkedList<Long> SEEDS = new LinkedList<>();
    
    private final LinkedList<Long[]> SEED_SOIL = new LinkedList<>();
    private final LinkedList<Long[]> SOIL_FERT = new LinkedList<>();
    private final LinkedList<Long[]> FERT_WATER = new LinkedList<>();
    private final LinkedList<Long[]> WATER_LIGHT = new LinkedList<>();
    private final LinkedList<Long[]> LIGHT_TEMP = new LinkedList<>();
    private final LinkedList<Long[]> TEMP_HUMIDITY = new LinkedList<>();
    private final LinkedList<Long[]> HUMIDITY_LOCATION = new LinkedList<>();

    public MapSolver(File input) throws FileNotFoundException {
        Scanner sc = new Scanner(input);
      
        // Read "seeds"
        sc.next();
        while (sc.hasNextLong()) { SEEDS.add(sc.nextLong()); }
        // Read "seeds-soil"
        moveToPhrase(sc, "seed-to-soil");
        scanMapLine(sc, SEED_SOIL);
        // Read "soil-to-fertilizer"
        moveToPhrase(sc, "soil-to-fertilizer");
        scanMapLine(sc, SOIL_FERT);
        // Read "fertilizer-to-water"
        moveToPhrase(sc, "fertilizer-to-water");
        scanMapLine(sc, FERT_WATER);
        // Read "water-to-light"
        moveToPhrase(sc, "water-to-light");
        scanMapLine(sc, WATER_LIGHT);
        // Read "light-to-temperature"
        moveToPhrase(sc, "light-to-temperature");
        scanMapLine(sc, LIGHT_TEMP);
        // Read "temperature-to-humidity"
        moveToPhrase(sc, "temperature-to-humidity");
        scanMapLine(sc, TEMP_HUMIDITY);
        // Read "humidity-to-location"
        moveToPhrase(sc, "humidity-to-location");
        scanMapLine(sc, HUMIDITY_LOCATION);
        
        sc.close();
    }

    // Moves the scanner up to a certain string
    private void moveToPhrase(Scanner scanner, String phrase) {
        while (scanner.hasNext()) {
            if (scanner.next().equals(phrase)) break;
        }
        scanner.next();
    }

    // Scans the next line of numbers and stores them in a list
    private void scanMapLine(Scanner sc, LinkedList<Long[]> list) {
        while (sc.hasNextLong()) {
            // Store 3 numbers at a time
            list.add(new Long[]{sc.nextLong(), sc.nextLong(), sc.nextLong()});
        }
    }

    // Map a number using a mapping list
    private long mapNumber(long number, LinkedList<Long[]> mappings) {
        for (Long[] range : mappings) {
            long dest_start = range[0];
            long src_start = range[1];
            long length = range[2];
            // Translate the number if it is in range
            if (number >= src_start && number < src_start + length) {
                return number + (dest_start - src_start); 
            }
        }
        // Return the original number if it can't be mapped using the provided list
        return number;
    }

    // Map a seed to a location
    public long seedToLocation(long seed) {
        long soil = mapNumber(seed, SEED_SOIL);
        long fert = mapNumber(soil, SOIL_FERT);
        long water = mapNumber(fert, FERT_WATER);
        long light = mapNumber(water, WATER_LIGHT);
        long temp = mapNumber(light, LIGHT_TEMP);
        long humidity = mapNumber(temp, TEMP_HUMIDITY);
        long location = mapNumber(humidity, HUMIDITY_LOCATION);
        return location;
    }
}