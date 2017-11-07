package cs149_homework4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SetUp {
	final int PROCESS_TOTAL = 150;
	final int EACHRUNTIME = 60; //each run time 1 minute (=60sec)
	
	LinkedList<Process> tempPcessList = new LinkedList<>();
	LinkedList<Process> pcessList = new LinkedList<>();//list of all processes with name,pagesize,arrtime,serviceduration
	List<Integer>tempRandomProcessList = new ArrayList<>();
	LinkedList<Integer> freePgList = new LinkedList<>();//Free Page list
	
	
	/*Generate work load: generate PROCESS_TOTAL processes <process name,Process size in pages,arrival time,service duration>*/
	public LinkedList<Process> ProcessSetup(){
		Process p = new Process();	
		
		/*generate random number for process name, arrivalTime, processSize, and serviceDuration*/
		Random generator = new Random(System.currentTimeMillis());
		
		int randomProcessNumber;
		for (int i=0; i<PROCESS_TOTAL; i++) { 
			
			/*use do while loop to generate random process, and make sure the process is not repeated*/
			do {
				randomProcessNumber = generator.nextInt(PROCESS_TOTAL);
			}
			while (tempRandomProcessList.contains(randomProcessNumber));
			tempRandomProcessList.add(randomProcessNumber);
			
			/*generate process size in pages 5, 11, 17, 31 MB*/
			p.setProcessSize(randomProcessNumber);
			
			/*generate random arrival time, store them to arrivalTimeList and sort them*/
			p.setArrivalTime(generator.nextInt(EACHRUNTIME));
			
	        /*generate serviceDuration 1, 2, 3, 4, 5*/
			p.setServiceDuration(randomProcessNumber);
	   
			/*tempPcessList doesn't have process name item*/
		    tempPcessList.add(new Process(p.getProcessSize(),p.getArrivalTime(),p.getServiceDuration()));
		    
		} //end of for loop
		
		
		
		/*Sort pcessList follows arrival time*/
		Collections.sort(tempPcessList, new Comparator<Process>() {
	        public int compare(Process o1, Process o2) {
	            return o1.getArrivalTime()<o2.getArrivalTime()?-1:1;
	        }
	      });
		
		
		
		/*copy tempPcessList to pcessList and add process name to pcessList.  This will give the pcessList 
		 * sorted by name at the same time with sorted by arrival time.  This is the list we'll return and use*/
		for (int i=0; i<PROCESS_TOTAL; i++) { 
			/* convert process' names to official names use 26 upper letters,26 lower letters,and numbers up to (150 - 26 - 26) processes*/
			p.setProcessName(i);
			pcessList.add(new Process(p.getProcessName(),tempPcessList.get(i).getProcessSize(),
					tempPcessList.get(i).getArrivalTime(), tempPcessList.get(i).getServiceDuration()));
		}
		
	return pcessList;
	
	}//end of ProcessSetup
	
	
	/*Create Free Page linked list*/
	public void setFreePageList(){
		for(int i=0; i<100; i++){
			freePgList.add(1);  //Initially, there are 100 pages
		}
	}
	
	public LinkedList<Integer> getFreePageList(){
		return freePgList;
	}
	
}

