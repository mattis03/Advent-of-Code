public class Part1 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        long sum = 0;
        for (String n : ip.lines()) {
            int secretNumber = Integer.parseInt(n);
            
            for (int i = 0; i < 2000; i++)
                secretNumber = nextNumber(secretNumber);

            sum += secretNumber;
        }
        System.out.println(sum);
    }

    private static int nextNumber(int n) {
        int res;

        res = n << 6;  // Multiplicera med 64
        n ^= res;      // Mixa
        n &= 0xffffff; // Trimma
        res = n >>> 5; // Dividera med 32
        n ^= res;      // Mixa
        n &= 0xffffff; // Trimma
        res = n << 11; // Multiplicera med 2048
        n ^= res;      // Mixa
        n &= 0xffffff; // Trimma
        
        return n;
    }
}