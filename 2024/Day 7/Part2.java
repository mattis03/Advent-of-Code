import java.util.LinkedList;

public class Part2 {
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

        public boolean isSolvable() {
            return generatePossibleResults(TERMS).contains(RESULT);
        }

        private LinkedList<Long> generatePossibleResults(long[] terms) {
            LinkedList<Long> retList = new LinkedList<>();
            if (terms.length == 1) {
                retList.add(terms[0]);
                return retList;
            }

            for (int i = 0; i < Math.pow(4, terms.length-1); i++) {
                long ack = terms[0];
                boolean skip = false;
                for (int j = 1; j < terms.length; j++) {
                    switch ((i >> (2*(j-1))) & 0b11) {
                        case 0b00:
                            ack += terms[j];
                        break;
                        case 0b01:
                            ack *= terms[j];
                        break;
                        case 0b10:
                            ack = Long.parseLong(Long.toString(ack) + Long.toString(terms[j]));
                        break;
                        case 0b11:
                            skip = true;
                        break;
                    }
                    
                    if (skip)
                        break;
                }

                if (skip)
                    continue;

                retList.add(ack);
            }

            return retList;
        }
    }
}
