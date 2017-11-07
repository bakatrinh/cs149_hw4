package cs149_hw4;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessSimulation {
	
	LinkedList<Integer> freePgList = new LinkedList<>();//Free Page list
	LinkedList<Process> processList = new LinkedList<>();
	

	public void Simulation(int menuChoice) throws InterruptedException {
		Timer timer = new Timer();
		PagingSwapping ps = new PagingSwapping(timer);
		ps.getMenuSelection(menuChoice);
		
		/*to be List of threads*/
		List<PagingSwapping> obj = new ArrayList<>();
		
		freePgList = ps.getFreePgList();
		processList = ps.getProcessList();
		System.out.println("\nList of processes will run: \n");
		for(Process p:processList){
			System.out.println(p);
		}
		
		Thread[] threadList = new Thread[25];
		// Create 25 threads
		for(int i = 0; i < threadList.length; i++) {
			threadList[i] = new Thread(ps);
		}
		timer.start();
		// Start 25 threads
		for(int i = 0; i < threadList.length; i++) {
			threadList[i].start();
		}
		// Wait for all 25 threads to be done
		for(int i = 0; i < threadList.length; i++) {
			threadList[i].join();
		}
		System.out.println("All Processes Done");
	} //end class ProcessSimulation
	
}	
	
	
	
	
	
	
	
	
	
	