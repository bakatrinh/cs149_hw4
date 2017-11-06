package cs149_hw4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PagingSwapping extends Thread{	
	LinkedList<Process> processList = new LinkedList<>();//List of all process<ProcessName,ProcessPage,ArrivalTime,ServiceDuration
	LinkedList<LinkedList<Page> allProcessPages = new LinkedList<>();//keep track all pages of all processes
	LinkedList<Integer>freePageList = new LinkedList<>();//Free Page list
	LinkedList<Page> procPageInMem = new LinkedList<>();//keep track pages in memory for each process
	
	private String pName;
	private int pSize;
	private int serDuration;
	
	/*Constructor*/
	public PagingSwapping(){
		//ProcessSimulation ps = new ProcessSimulation();
		SetUp su = new SetUp();
		processList = su.ProcessSetup();
		freePageList = su.GetFreePageList();
	}
	
	public void run(){
		referenceAndSwap(FirstPageInMem(0));
	}
	
	public void referenceAndSwap(int pageInMem){
		updateFreePageList("-");//first time process runs, subtract 1 page from freepagelist
		updateAllProcessPageList("+",0,0);//first time process runs, the list add page 0 to index 0
		int pageCount = 0;
		while(pageCount < pSize -1){
			int pInMem= nextReferencePage(pageInMem);
			pageInMem = pInMem;
			if(isPageInMem(pInMem) == 0){//page is not in memory
				updateFreePageList("-");//update after first time process runs
				updateAllProcessPageList("+",0,0);//update after first time process runs
			}
			pageCount++;
		}
	}
	
	/* Linkedlist to keep track pages in memory for each process */
	public int FirstPageInMem(int pageNum){
		SetUp su = new SetUp();
		pName = processList.peek().getProcessName();
		pSize =  processList.peek().getProcessSize();
		serDuration =  processList.peek().getServiceDuration();
		
//		allProcessPages.add(processList.get(currentProcess).getProcessName().add(su.eachProcessPagesInMem(0)));
		allProcessPages.add(new Page(processList.poll().getProcessName()).add(new Page(su.eachProcessPagesInMem(pageNum))));
		int pageIndexInMem = 0; //this is the current page in mem, initially is 0. Used to reference the next page.
		return pageIndexInMem;
	}
	
	public void contProcessPagesInMem(){
		pSize = processList.get(0).getProcessSize();
		
	}	
	
	public void updateFreePageList(String change){
		if (change.equals("-")){
			freePageList.removeFirst();
		}
		else if(change.equals("+")){
			freePageList.add(1);
		}
	}
	
	public void updateAllProcessPageList(String chge, int index, int pgNum){
		if (chge.equals("-")){
			allProcessPages.processList.get(currentProcess).getProcessName().remove(index);
		}
		else if(chge.equals("+")){
			allProcessPages.add(new Page(pgNum));
		}
	}
	
	/* Generate a random number between min and max */
	 public int RandomNumberGenerator(int min, int max) {
		Random rand = new Random(System.currentTimeMillis());
		if(min<0){
			return ((int)Math.floor((Math.random() * 2)) > 0 ? 1 : -1) * (int)Math.floor((Math.random() * 2));
		}
		else{
			return rand.nextInt(max-min)+1;
		}
	}
	 
	 /* if page is in memory, return 1, otherwise return 2*/
	 public int isPageInMem(int pn){
		 //check if page is in memory
	 }

	 /*generate random number for r for locality of reference: 0<= r <7 or 7<= r <=10 */	
	public int nextReferencePage(int pgNum){	
		int r = RandomNumberGenerator(0,10);
		int diff;
		if(r<7){
			diff = RandomNumberGenerator(-1, 1);
			if(pgNum==0 && diff <0)
				return pgNum;
			else{
				return pgNum += diff;
			}
		}
		else{
			int j = RandomNumberGenerator(0,3);
			return pgNum+= j;//I need to tweak this part later
		}
	}
	
	public LinkedList<Integer> getFreePgList(){
		return freePageList;
	}
}



