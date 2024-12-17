import java.util.ArrayList;

public class Part2 {
    public static Computer c;

    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        c = new Computer(ip);

        // Populera listan över möjliga lösningar
        ArrayList<Long> possibleSolutions = generateSolutions(0);
        ArrayList<Long> temp = new ArrayList<>();
        for (int i = 0; i < c.PROGRAM.length - 1; i++) {
            // Töm temp
            temp.clear();
            // Kopiera possibleSolutions till temp
            for (Long s : possibleSolutions)
                temp.add(s);
            // Töm possibleSolutions
            possibleSolutions.clear();
            // Generera lösningar från temp och spara i possibleSolutions
            for (Long s : temp) {
                for (Long ps : generateSolutions(s)) {
                    possibleSolutions.add(ps);
                }
            }
        }

        // Hitta minsta lösningen för A
        long min = Long.MAX_VALUE;
        for (Long s : possibleSolutions) {
            if (s < min)
                min = s;
        }

        System.out.println(min);
    }

    // Testar 3 bitar i taget och returnerar en lista på hittils giltiga värden på A
    private static ArrayList<Long> generateSolutions(long currentA) {
        ArrayList<Long> possibleSolutions = new ArrayList<>();

        // Gör plats för 3 nya bitar (de tre minst signifikanta bitarna == 000)
        currentA <<= 3L;

        for (int i = 0; i <= 0b111; i++) {
            // Testa bitkombination
            c.reset(currentA + i);
            ArrayList<Long> out = c.run();
            // Kolla om detta A är en giltig lösning (output är samma som program)
            boolean ok = true;
            for (int k = 0; k < out.size(); k++) {
                long outputValue = out.get(out.size()-1 - k);
                long programValue = c.PROGRAM[c.PROGRAM.length-1 - k];

                if (outputValue != programValue) {
                    ok = false;
                    break;
                }
            }
            
            if (ok)
                possibleSolutions.add(currentA + i);
        }

/*         for (Long l : possibleSolutions) {
            c.reset(l);
            System.out.println(c.run());
        }
        System.out.println(); */

        return possibleSolutions;
    }

    private static class Computer {
        private static final int ADV = 0, BXL = 1, BST = 2, JNZ = 3, BXC = 4, OUT = 5, BDV = 6, CDV = 7;

        private long A, B, C;
        private int pc = 0;
        private final int PROGRAM[];

        private final long INIT_B, INIT_C;
        private final ArrayList<Long> OUTPUT = new ArrayList<>();

        public Computer(InputParser ip) {
            A = Long.parseLong(ip.getLine(0).split("Register A: ")[1]);
            B = Long.parseLong(ip.getLine(1).split("Register B: ")[1]);
            C = Long.parseLong(ip.getLine(2).split("Register C: ")[1]);
            INIT_B = B;
            INIT_C = C;
            String s[] = ip.getLine(4).split("Program: ")[1].split(",");
            PROGRAM = new int[s.length];
            for (int i = 0; i < PROGRAM.length; i++)
                PROGRAM[i] = Integer.parseInt(s[i]);
        }

        public void reset(long A) {
            this.A = A;
            B = INIT_B;
            C = INIT_C;
            pc = 0;
        }

        public ArrayList<Long> run() {
            OUTPUT.clear();
            while (doCycle());
            return OUTPUT;
        }

        private boolean doCycle() {
            if (pc+1 >= PROGRAM.length /* || OUTPUT.size() > PROGRAM.length */) // Halt
                return false;

            int opcode = PROGRAM[pc];
            long operand = PROGRAM[pc + 1];
            
            switch (opcode) {
                case ADV:
                    A >>>= comboOperand(operand);
                    break;
                case BXL:
                    B ^= operand;
                    break;
                case BST:
                    B = comboOperand(operand) & 0b111;
                    break;
                case JNZ:
                    if (A != 0) {
                        pc = (int)operand;
                        return true;
                    }
                    break;
                case BXC:
                    B ^= C;
                    break;
                case OUT:
                    long programOut = comboOperand(operand) & 0b111;
                    if (/* PROGRAM[OUTPUT.size()] != programOut ||  */operand == 7)
                        return false;
                    OUTPUT.add(programOut);
                    break;
                case BDV:
                    B = A >>> comboOperand(operand);
                    break;
                case CDV:
                    C = A >>> comboOperand(operand);
                    break;
            }

            pc += 2;
            return true;
        }

        private long comboOperand(long operand) {
            switch ((int)operand) {
                case 4:
                    return A;
                case 5:
                    return B;
                case 6:
                    return C;
                default:
                    return operand;
            }
        }
    }
}