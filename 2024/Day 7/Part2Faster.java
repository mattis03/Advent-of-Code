public class Part2Faster {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");

        long sum = 0;
        int nProcessed = 0;
        for (int i = 0; i < ip.lineCount(); i++) {
            Calibration c = new Calibration(ip.getLine(i));

            if (c.isSolvable())
                sum += c.RESULT;

            System.out.print("\rProcessed " + ++nProcessed + "/" + ip.lineCount());
        }

        System.out.println("\n" + sum);
    }

    private static class Calibration {
        private final long RESULT;
        private final long[] TERMS;

        public Calibration(String calibration) {
            RESULT = Long.parseLong(calibration.substring(0, calibration.indexOf(":")));
            String numbStr[] = calibration.substring(calibration.indexOf(":") + 2, calibration.length()).split(" ");
            TERMS = new long[numbStr.length];
            for (int i = 0; i < TERMS.length; i++)
                TERMS[i] = Long.parseLong(numbStr[i]);
        }

        private static final int LEFTBITMASK = 0xAAAAAAAA;

        public boolean isSolvable() {
            final int nCombinations = (int)Math.pow(4, TERMS.length-1);
            for (int i = 0; i < nCombinations; i++) {
                // Kolla om den ogiltiga bitkombinationen 11 förekommer (har ingen motsvarande operator)
                if (((i & (i << 1)) & LEFTBITMASK) != 0)
                    continue;

                long ack = TERMS[0];
                for (int j = 1; j < TERMS.length; j++) {
                    switch ((i >> (2*(j-1))) & 0b11) {
                        case 0b00:
                            ack += TERMS[j];
                        break;
                        case 0b01:
                            ack *= TERMS[j];
                        break;
                        case 0b10:
                            ack = Long.parseLong(ack + Long.toString(TERMS[j]));
                        break;
                    }
                }

                if (ack == RESULT)
                    return true;
            }
            return false;
        }
    }
}
