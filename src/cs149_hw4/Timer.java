package cs149_hw4;

import java.text.DecimalFormat;

public class Timer {
    private long start;
    private long end;

    static double toSeconds(long nanos) {
        return nanos / 1000000000.0;
    }

    void start() {
        end = -1;
        start = System.nanoTime();
    }

    double getCurrent() {
        return toSeconds(System.nanoTime() - start);
    }
    
    String getCurrentString() {
    		DecimalFormat df = new DecimalFormat("####0.00");
    		return df.format(toSeconds(System.nanoTime() - start));
    }

    double stop() {
        end = System.nanoTime();
        return toSeconds(end - start); 
    }

    boolean isRunning() {
        return end == -1;
    }
}