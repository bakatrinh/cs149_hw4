package cs149_homework4;

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
		
		freePgList = ps.getFreePgList();
		processList = ps.getProcessList();
		System.out.println("\nList of processes will run: \n");
		for(Process p:processList){
			System.out.println(p);
		}

		Thread t1 = new Thread(ps);
		Thread t2 = new Thread(ps);
		Thread t3 = new Thread(ps);
		Thread t4 = new Thread(ps);
		Thread t5 = new Thread(ps);
		Thread t6 = new Thread(ps);
		Thread t7 = new Thread(ps);
		Thread t8 = new Thread(ps);
		Thread t9 = new Thread(ps);
		Thread t10 = new Thread(ps);
		Thread t11 = new Thread(ps);
		Thread t12 = new Thread(ps);
		Thread t13 = new Thread(ps);
		Thread t14 = new Thread(ps);
		Thread t15 = new Thread(ps);
		Thread t16 = new Thread(ps);
		Thread t17= new Thread(ps);
		Thread t18 = new Thread(ps);
		Thread t19 = new Thread(ps);
		Thread t20 = new Thread(ps);
		Thread t21 = new Thread(ps);
		Thread t22 = new Thread(ps);
		Thread t23 = new Thread(ps);
		Thread t24 = new Thread(ps);
		Thread t25 = new Thread(ps);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();
		t9.start();
		t10.start();
		t11.start();
		t12.start();
		t13.start();
		t14.start();
		t15.start();
		t16.start();
		t17.start();
		t18.start();
		t19.start();
		t20.start();
		t21.start();
		t22.start();
		t23.start();
		t24.start();
		t25.start();
		
		
		
		/*Initially, the process starts when there are at least 4 free pages in the Free page list.
		*Each process takes at least 4 pages memory to begin .Initially 25 processes start concurrently*/
//		for(int i=0; i<25; i++){
//			PagingSwapping psw = new PagingSwapping();
//			obj.add(psw);
//		}
		
		
	
//		int i=0;
//		while((freePgList.size() >= 4) && i<=processList.size() && i<25){
//		for(int i=0; i<10; i++){
//			obj.get(i).start();
		
		
		

	} //end class ProcessSimulation
	
}	
	
	
	
	
	
	
	
	
	
	