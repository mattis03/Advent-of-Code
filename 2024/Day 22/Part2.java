public class Part2 {
    private static int prices[][];
    private static int priceChanges[][];

    public static void main(String[] args) {
        InputParser ip = new InputParser("input");

        prices = new int[ip.lineCount()][2000];
        priceChanges = new int[ip.lineCount()][2000];

        for (int i = 0; i < ip.lineCount(); i++) {
            int prevSecretNumber = Integer.parseInt(ip.getLine(i));
            
            for (int j = 0; j < 2000; j++) {
                int secretNumber = nextNumber(prevSecretNumber);
                prices[i][j] = secretNumber % 10;
                priceChanges[i][j] = prices[i][j] - prevSecretNumber % 10;
                prevSecretNumber = secretNumber;
            }
        }

        int count = 0;
        long maxBananas = 0;
        for (int i = -9; i <= 9; i++) {
            for (int j = -9; j <= 9; j++) {
                for (int k = -9; k <= 9; k++) {
                    for (int l = -9; l <= 9; l++) {
                        long bananas = buyBananas(new int[]{i, j, k, l});
                        if (bananas > maxBananas)
                            maxBananas = bananas;
                        ProgressBar.progressBar((int)(100 * (count+1) / (19*19*19*19.0)));
                        count++;
                    }
                }
            }
        }
        System.out.println("\n" + maxBananas);
    }

    private static int buyBananas(int[] sequence) {
        int sum = 0;
        for (int i = 0; i < priceChanges.length; i++) {
            int[] buyerPriceChanges = priceChanges[i];
            for (int j = 0; j < buyerPriceChanges.length - 4; j++) {
                if (buyerPriceChanges[j] == sequence[0] && buyerPriceChanges[j+1] == sequence[1] && buyerPriceChanges[j+2] == sequence[2] && buyerPriceChanges[j+3] == sequence[3]) {
                    sum += prices[i][j+3];
                    break;
                }
            }
        }
        return sum;
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