public class Part1 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");

        long sum = 0;
        for (int i = 0; i < ip.lineCount(); i++) {
            Calibration c = new Calibration(ip.getLine(i));

            if (c.isSolvable())
                sum += c.RESULT;
        }

        System.out.println(sum);
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

        public boolean isSolvable() {

            for (int i = 0; i < Math.pow(2, TERMS.length-1); i++) {

                long ack = TERMS[0];
                for (int j = 1; j < TERMS.length; j++) {
                    if ((i & (1 << (j-1))) == 0) {
                        ack += TERMS[j];
                    } else {
                        ack *= TERMS[j];
                    }
                }

                if (ack == RESULT)
                    return true;
            }
            return false;
        }
    }
}
