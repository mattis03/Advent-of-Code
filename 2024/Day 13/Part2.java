public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        long tokens = 0;

        for (int i = 0; i < ip.lineCount(); i+=4) {
            String[] btnA =  ip.getLine(i).substring(10).split(", ");
            String[] btnB =  ip.getLine(i+1).substring(10).split(", ");
            String[] prize = ip.getLine(i+2).substring(7).split(", ");

            long AX = Integer.parseInt(btnA[0].substring(2, btnA[0].length()));
            long AY = Integer.parseInt(btnA[1].substring(2, btnA[1].length()));
            long BX = Integer.parseInt(btnB[0].substring(2, btnB[0].length()));
            long BY = Integer.parseInt(btnB[1].substring(2, btnB[1].length()));
            long PX = Integer.parseInt(prize[0].substring(2, prize[0].length())) + 10000000000000L;
            long PY = Integer.parseInt(prize[1].substring(2, prize[1].length())) + 10000000000000L;

            tokens += countTokens(AX, AY, BX, BY, PX, PY);
        }

        System.out.println(tokens);
    }

    private static long countTokens(long AX, long AY, long BX, long BY, long PX, long PY) {
        long det = AX * BY - BX * AY;
        long aQuotient = BY * PX - BX * PY;
        long bQuotient = AX * PY - AY * PX;
        
        long aPresses = aQuotient / det;
        long bPresses = bQuotient / det;

        // Endast heltalslösningar är tillåtna
        if (aPresses * det != aQuotient || bPresses * det != bQuotient)
            return 0;

        return aPresses * 3 + bPresses;
    }
}