public class ProgressBar {

    private static int barLength = 50;
    private static SpinnerThread spinnerThread = null;
    private static boolean spinnerThreadRunning = false;
    private static int spinnerDelay = 200;

    public static void progressBar(int percentage) {
        int progress = (Math.clamp(percentage, 0, 100) * barLength) / 100;
        String percentageString = Math.clamp(percentage, 0, 100) + "%";

        String outStr = "";
        for (int i = 0; i < barLength; i++) {
            if (i == (barLength - percentageString.length()) / 2) {
                i += percentageString.length() - 1;
                outStr += percentageString;
                continue;
            }

            if (i < progress)
                outStr += '=';
            else
                outStr += ' ';
        }

        System.out.print("\r[" + outStr + "]");
    }

    public static void setProgressBarLength(int length) {
        barLength = length;
    }

    public static void startSpinner() {
        spinnerThreadRunning = true;
        spinnerThread = new SpinnerThread();
        spinnerThread.start();
    }

    public static void stopSpinner() {
        spinnerThreadRunning = false;
    }

    public static void setSpinnerFrametime(int frameTimeMs) {
        spinnerDelay = frameTimeMs;
    }

    private static class SpinnerThread extends Thread {
        @Override
        public void run() {
            int frame = 0;
            while (spinnerThreadRunning) {
                switch(frame++ % 4){
                    case 0: System.out.print("\r|"); break;
                    case 1: System.out.print("\r/"); break;
                    case 2: System.out.print("\r─"); break;
                    case 3: System.out.print("\r\\"); break;
                }

                try {
                    Thread.sleep(spinnerDelay);
                } catch (Exception e) {}
            }
        }
    }
}
