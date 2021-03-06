package cs149_hw4;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
		
        Scanner scan = new Scanner(System.in);
        clearScreen();
        boolean inMenu = true;
        while (inMenu == true) {
            int menuChoice = -1;
            boolean bError = true;
            while (bError) {
                System.out.println("Group 04 CS 149 HW #4");
                System.out.println("1. Generate 5 runs in FIFO");
                System.out.println("2. Generate 5 runs in LRU");
                System.out.println("3. Generate 5 runs in LFU");
                System.out.println("4. Generate 5 runs in MFU");
                System.out.println("5. Generate 5 runs random pick");
                System.out.println("0. Exit");
                System.out.println();
                System.out.print("Please enter a choice:\n ");
                try {
                    menuChoice = scan.nextInt();
                } catch (NoSuchElementException e) {
                    scan.next();
                }
                bError = false;
            }
            switch (menuChoice) {
                case 0:
                    inMenu = false;
                    scan.close();
                    System.out.println("\n==============================");
                    System.out.println("Program Done. Exiting.");
                    System.out.println("==============================\n");
                    break;

                case 1:
                    fifo(menuChoice);
                    break;

                case 2:
                    lru(menuChoice);
                    break;

                case 3:
                    lfu(menuChoice);
                    break;

                case 4:
                    mfu(menuChoice);
                    break;

                default:
                    random(menuChoice);
                    break;
            }
       }
    }
    
    

    public static void fifo(int menuChoice) throws InterruptedException {
        System.out.println("\n==============================");
        System.out.println("FIFO");
        System.out.println("==============================\n");

        ProcessSimulation processSimulation = new ProcessSimulation();
        processSimulation.Simulation(menuChoice);

        System.out.println("\n==============================");
        System.out.println("FIFO Done");
        System.out.println("==============================\n");
    }

    public static void lru(int menuChoice) throws InterruptedException {
        System.out.println("\n==============================");
        System.out.println("LRU");
        System.out.println("==============================\n");

        ProcessSimulation processSimulation = new ProcessSimulation();
        processSimulation.Simulation(menuChoice);

        System.out.println("\n==============================");
        System.out.println("LRU Done");
        System.out.println("==============================\n");
    }

    public static void lfu(int menuChoice) throws InterruptedException {
        System.out.println("\n==============================");
        System.out.println("LFU");
        System.out.println("==============================\n");

        ProcessSimulation processSimulation = new ProcessSimulation();
        processSimulation.Simulation(menuChoice);

        System.out.println("\n==============================");
        System.out.println("LFU Done");
        System.out.println("==============================\n");
    }

    public static void mfu(int menuChoice) throws InterruptedException {
        System.out.println("\n==============================");
        System.out.println("MFU");
        System.out.println("==============================\n");

        ProcessSimulation sim = new ProcessSimulation();
        sim.Simulation(menuChoice);

        System.out.println("\n==============================");
        System.out.println("MFU Done");
        System.out.println("==============================\n");
    }

    public static void random(int menuChoice) throws InterruptedException {
        System.out.println("\n==============================");
        System.out.println("Random");
        System.out.println("==============================\n");

        ProcessSimulation sim = new ProcessSimulation();
        sim.Simulation(menuChoice);
        

        System.out.println("\n==============================");
        System.out.println("Random Done");
        System.out.println("==============================\n");
    }

    public static void clearScreen() throws IOException, InterruptedException {
        final String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            Runtime.getRuntime().exec("clear");
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}
