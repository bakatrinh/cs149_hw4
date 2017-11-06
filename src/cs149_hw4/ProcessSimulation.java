package cs149_hw4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProcessSimulation {
	
	LinkedList<Integer> freePageList = new LinkedList<>();//Free Page list
	
	public void Simulation(){
		PagingSwapping ps = new PagingSwapping();
		
		/*to be List of threads*/
	//	List<PagingSwapping> threads = new ArrayList<>();
		
		freePageList = ps.getFreePgList();
		
		/*Initially, the process starts when there are at least 4 free pages in the Free page list.
		*Each process takes at least 4 pages memory to begin .Initially 25 processes start concurrently*/
		for(int i=0; i<25; i++){
			int count=0;
			int index = 0;
			while (count<4 && index<100){
				if(isPageFree(index) == 1){
					count++;
				}
				index++;
			}
			if(count>=4){
				ps.start();
//				threads.add(ps);//collect threads
			}
		}
		
		
//		int count=0;
//		for (Process a:processList){
//			System.out.println(a + " ");
//			count++;
//		}
//		System.out.println("count: "+count);
//	
//	}
		
		
	}//end of Simulation
	
	
	
	
	public int isPageFree (int index){
		/*if there is free page, return 1.  Otherwise, return 0*/
		if(freePageList.get(index) == 1){
			return 1;
		}
		return 0;
	}

} //end class ProcessSimulation
	
	
	
	
	
	
	
	
	
	
	
	