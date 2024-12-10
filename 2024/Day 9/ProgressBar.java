public class ProgressBar {
    private static String s; //Used to show progress as a percentage
    private static int barLength = 50; //bar width in number of characters
    private static int l; //length of percentage string 
    private static double percentage, stepPercentage; //percentage - current percentage, stepPercentage - percentage equivalent of 1 character
    private static StringBuilder toPrint = new StringBuilder(); //Create a StringBuilder to put the progress bar together

    private static byte frame = 0; //Spinner animation frame

    public static void setBarLength(int length){
        barLength = length;
    }

    public static void progressBar(double part, double total, boolean showPercentage){
        percentage = 100*part/total;
        stepPercentage = (double)100/barLength;
        s = (int)percentage + "%";
        l = s.length();
        toPrint.setLength(0); //Clear the previous string
        toPrint.append('[');
        for(int i = 0; i < barLength; i++){
            if(i == (barLength>>1) - (l>>1) && showPercentage){
                toPrint.append(s);
            }
            else if(i < (barLength>>1) - (l>>1) || i >= (barLength>>1) + l-(l>>1) || !showPercentage){
                if(percentage >= (i + 1) * stepPercentage){
                    toPrint.append('=');
                } else {
                    toPrint.append(' ');
                }
            }
        }
        toPrint.append(']');
        System.out.print("\r" + toPrint);
    }

    public static void spinner(){
        switch(frame & 0x7){
            case 0: System.out.print("\r|"); break;
            case 1: System.out.print("\r/"); break;
            case 2: System.out.print("\r─"); break;
            case 3: System.out.print("\r\\"); break;
            case 4: System.out.print("\r|"); break;
            case 5: System.out.print("\r/"); break;
            case 6: System.out.print("\r─"); break;
            case 7: System.out.print("\r\\"); break;
        }
        frame++;
    }
}
