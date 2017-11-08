package cs149_hw4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProcessSimulation {
	
	LinkedList<Integer> freePgList = new LinkedList<>();//Free Page list
	LinkedList<Process> processList = new LinkedList<>();
	int[] hitMissTracker; // index 0 is hit, index 1 is miss
	double hitMissRatioTotal;
	ArrayList<int[]> hitMissTrackerRun;
	static final int runTracker = 5;

	public void Simulation(int menuChoice) throws InterruptedException {
		
		// reset our counters to 0
		hitMissTracker = new int[2];
		hitMissTracker[0] = 0;
		hitMissTracker[1] = 0;
		hitMissRatioTotal = 0;
		hitMissTrackerRun = new ArrayList<>();
		
		for (int runCounter = 0; runCounter < runTracker; runCounter++) {
			Timer timer = new Timer();
			PagingSwapping ps = new PagingSwapping(timer, this);
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
			System.out.println("Run # "+(runCounter+1)+": is done. Hit: "+hitMissTracker[0]+" Miss: "+hitMissTracker[1]);
			double tempHitMissRatio = hitMissTracker[0]/hitMissTracker[1];
			int[] tempCopyHitMissTracker = new int[2];
			tempCopyHitMissTracker[0] = hitMissTracker[0];
			tempCopyHitMissTracker[1] = hitMissTracker[1];
			hitMissTrackerRun.add(tempCopyHitMissTracker);
			hitMissRatioTotal = hitMissRatioTotal + tempHitMissRatio;
			hitMissTracker[0] = 0;
			hitMissTracker[1] = 0;
		}
		System.out.println("----------------------------\n");
		System.out.println("Overall across "+runTracker+" runs:");
		int run = 1;
		for (int[] e : hitMissTrackerRun) {
			System.out.println("Run #"+run+" - Hit: "+e[0]+" Miss: "+e[1]);
			run++;
		}
		System.out.println("Average Hit Miss Ratio: "+(hitMissRatioTotal/runTracker));
	} //end class ProcessSimulation
	
	public void incrementHit() {
		hitMissTracker[0]++;
	}
	
	public void incrementMiss() {
		hitMissTracker[1]++;
	}
	
}	
	
	
	
	
	
	
	
	
	
	