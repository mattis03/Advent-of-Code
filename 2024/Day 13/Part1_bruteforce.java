public class Part1_bruteforce {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        long tokens = 0;

        for (int i = 0; i < ip.lineCount(); i+=4) {
            String[] btnA =  ip.getLine(i).substring(10).split(", ");
            String[] btnB =  ip.getLine(i+1).substring(10).split(", ");
            String[] prize = ip.getLine(i+2).substring(7).split(", ");

            int AX = Integer.parseInt(btnA[0].substring(2, btnA[0].length()));
            int AY = Integer.parseInt(btnA[1].substring(2, btnA[1].length()));
            int BX = Integer.parseInt(btnB[0].substring(2, btnB[0].length()));
            int BY = Integer.parseInt(btnB[1].substring(2, btnB[1].length()));
            int PX = Integer.parseInt(prize[0].substring(2, prize[0].length()));
            int PY = Integer.parseInt(prize[1].substring(2, prize[1].length()));

            tokens += countTokens(AX, AY, BX, BY, PX, PY);
        }

        System.out.println(tokens);
    }

    private static long countTokens(long AX, long AY, long BX, long BY, long PX, long PY) {
        for (int b = 0; b < 100; b++) {
            for (int a = 0; a < 100; a++) {
                if (a*AX + b*BX == PX && a*AY + b*BY == PY)
                    return a * 3 + b;
            }
        }
        return 0;
    }
}
