import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Process {
    private int arrivalTime;
    private int processSize;
    private int serviceDuration;
    private String processName;

    public Process() {
    }

    public Process(String n, int ps, int aT, int sD) {
        processName = n;
        processSize = ps;
        arrivalTime = aT;
        serviceDuration = sD;
    }

    public String toString() {
        return "Process: " + processName + " [processSize: " + processSize + " ArrivalTime: " + arrivalTime +
                ", serviceDuration: " + serviceDuration + "]";
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceDuration() {
        return serviceDuration;
    }

    public int getProcessSize() {
        return processSize;
    }

    public String getProcessName() {
        return processName;
    }

    /*set arrival time*/
    public void setArrivalTime(int arr) {
        arrivalTime = arr;
    }

    /*generate serviceDuration 1, 2, 3, 4, 5*/
    public void setServiceDuration(int randProcessNum) {
        serviceDuration = (randProcessNum % 5) + 1;
    }

    /*generate process size in pages 5, 11, 17, 31 MB*/
    public void setProcessSize(int randProcessNum) {

        int prSize = (randProcessNum % 4);
        switch (prSize) {
            case 0:
                prSize = 5;
                break;
            case 1:
                prSize = 11;
                break;
            case 2:
                prSize = 17;
                break;
            case 3:
                prSize = 31;
                break;
            default:
                prSize = 5;
                break;
        }
        processSize = prSize;
    }

    /* convert process' names to official names: 26 upper letters, 26 lower letters, and numbers up to (150 - 26 - 26) processes*/
    public void setProcessName(int randProcessNum) {
        String names = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        String convertToName;
        if (randProcessNum >= 0 && randProcessNum <= 51) {
            convertToName = names.substring(randProcessNum, randProcessNum + 1); //convert to A-Za-z
        } else {
            convertToName = Integer.toString(randProcessNum - 52); //1 to TOTALPROCESSES
        }

        processName = convertToName;
    }

}
