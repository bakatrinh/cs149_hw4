package cs149_hw4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProcessSimulation {
	
	LinkedList<Integer> freePgList = new LinkedList<>();//Free Page list
	LinkedList<Process> processList = new LinkedList<>();
	

	public void Simulation() {
		PagingSwapping ps = new PagingSwapping();
		
		/*to be List of threads*/
		List<PagingSwapping> obj = new ArrayList<>();
		
//		freePgList = ps.getFreePgList();
//		processList = ps.getProcessList();
		
//		ps.start();
		/*Initially, the process starts when there are at least 4 free pages in the Free page list.
		*Each process takes at least 4 pages memory to begin .Initially 25 processes start concurrently*/
		for(int i=0; i<25; i++){
			PagingSwapping psw = new PagingSwapping();
			obj.add(psw);
		}
		
		
	
//		int i=0;
//		while((freePgList.size() >= 4) && !processList.isEmpty() && i<25){
		
		
		for(int i=0; i<25; i++){
			obj.get(i).start();
		
		}
		

	} //end class ProcessSimulation
	
}	
	
	
	
	
	
	
	
	
	
	