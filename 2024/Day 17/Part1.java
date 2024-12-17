public class Part1 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Computer c = new Computer(ip);

        while (c.doCycle());
    }

    private static class Computer {
        private static final int ADV = 0, BXL = 1, BST = 2, JNZ = 3, BXC = 4, OUT = 5, BDV = 6, CDV = 7;

        private int A, B, C;
        private int pc = 0;
        private final int PROGRAM[];

        public Computer(InputParser ip) {
            A = Integer.parseInt(ip.getLine(0).split("Register A: ")[1]);
            B = Integer.parseInt(ip.getLine(1).split("Register B: ")[1]);
            C = Integer.parseInt(ip.getLine(2).split("Register C: ")[1]);
            String s[] = ip.getLine(4).split("Program: ")[1].split(",");
            PROGRAM = new int[s.length];
            for (int i = 0; i < PROGRAM.length; i++)
                PROGRAM[i] = Integer.parseInt(s[i]);
        }

        private boolean doCycle() {
            if (pc+1 >= PROGRAM.length) // Halt
                return false;

            int opcode = PROGRAM[pc];
            int operand = PROGRAM[pc + 1];
            int comboOperand = comboOperand(operand);

            switch (opcode) {
                case ADV:
                    A /= Math.pow(2, comboOperand);
                    break;
                case BXL:
                    B ^= operand;
                    break;
                case BST:
                    B = comboOperand & 0b111;
                    break;
                case JNZ:
                    if (A != 0) {
                        pc = operand;
                        return true;
                    }
                    break;
                case BXC:
                    B = B ^ C;
                    break;
                case OUT:
                    System.out.print((comboOperand & 0b111) + ",");
                    break;
                case BDV:
                    B = (int)(A / Math.pow(2, comboOperand));
                    break;
                case CDV:
                    C = (int)(A / Math.pow(2, comboOperand));
                    break;
            }

            pc += 2;
            return true;
        }

        private int comboOperand(int operand) {
            switch (operand) {
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