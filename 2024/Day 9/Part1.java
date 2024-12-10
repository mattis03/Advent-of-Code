public class Part1 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");

        Disk d = new Disk(ip);
        d.compress();
        System.out.println(d.checksum());
    }

    private static class Disk {
        private final static int FREE_SPACE = -1;

        private final int[] DISK;

        public Disk(InputParser ip) {
            // Läs filformatet
            final int FORMAT[] = new int[ip.getLine(0).length()];
            int diskSize = 0;
            for (int i = 0; i < ip.getLine(0).length(); i++) {
                FORMAT[i] = Integer.parseInt(ip.getLine(0).charAt(i) + "");
                diskSize += FORMAT[i];
            }

            // Skapa disken utifrån formatet
            DISK = new int[diskSize];
            int diskIndex = 0;
            int blockIndex = 0;
            for (int i = 0; i < FORMAT.length; i++) {
                if (i % 2 == 0) {
                    // FORMAT[i] beskriver längden av en fil
                    for (int j = 0; j < FORMAT[i]; j++)
                        DISK[diskIndex++] = blockIndex;

                    blockIndex++;
                } else {
                    // FORMAT[i] beskriver längden av en tomt utrymme
                    for (int j = 0; j < FORMAT[i]; j++)
                        DISK[diskIndex++] = FREE_SPACE;

                }
            }
        }

        public void compress() {
            int freeSpaceIndex = 0;
            for (int i = DISK.length - 1; i >= 0 && freeSpaceIndex < i; i--) {
                if (DISK[i] == FREE_SPACE)
                    continue;

                // Hitta nästa plats med tomt utrymme
                while (DISK[freeSpaceIndex] != FREE_SPACE)
                    freeSpaceIndex++;

                // Flytta elementet längst bak till den först platsen med tomt utrymme
                DISK[freeSpaceIndex++] = DISK[i];
                DISK[i] = FREE_SPACE;
            }
        }

        public long checksum() {
            long sum = 0;
            for (int i = 0; i < DISK.length; i++) {
                if (DISK[i] == FREE_SPACE)
                    break;
                sum += DISK[i] * i;
            }
            return sum;
        }
    }
}
