package cs149_hw4;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class PagingSwapping extends Thread{	
	final ReentrantLock lock = new ReentrantLock();
	LinkedList<Process> processList = new LinkedList<>();//List of all process<ProcessName,ProcessPage,ArrivalTime,ServiceDuration
	LinkedList<Page> allProcessPages = new LinkedList<>();//keep track all pages of all processes globally
	LinkedList<Integer>freePageList = new LinkedList<>();//Free Page list
	LinkedList<Page> procPageInMem = new LinkedList<>();//keep track pages in memory for each process
	LinkedList<LinkedList<Page>> allProcessInMemList = new LinkedList<>();//keep all of the above "procPageInMem" list.  
																			//Use this to call individual "procPageInMem" list
	
	private String procName;
	private int procSize;
	private int serDuration;
	private int selection;
	
	/*Constructor*/
	public PagingSwapping(){
		SetUp su = new SetUp();
		processList = su.ProcessSetup();
		//freePageList = su.getFreePageList();
		for(int i=0; i<100; i++){
			freePageList.add(1);  //Initially, there are 100 pages
		}
	}
	
	
	public void run(){
//		int currentIndex = FirstPageInMem(0); 
//		while(currentIndex==-1){ //while no more free memory
//			stealPage(selection);
//		}
//		lock.lock();
		try {
			referenceAndSwap(FirstPageInMem(0));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		lock.unlock();

	}
	
	/* This method is to generate and keeping tract all activities when a process starts */
	public int FirstPageInMem(int pageNum){
		
		/*Print title*/
		printColTittle();
		
//Need a lock here
		lock.lock();
	try{
		if(freePageList.size()>0){
			
			
			/*Get information from the process at the head of the processList, then delete that process from the list*/
			procName = processList.peek().getProcessName();//retrieve Process name
			procSize =  processList.peek().getProcessSize();//retrieve Process size
			serDuration =  processList.poll().getServiceDuration();//retrieve serviceDuration and delete the process from the list
			
			long timeStampToCompu = getTimeStampToCompute();//store in the list to keep tract time for FIFO algorithm
			String timeStamp = getTimeStampToPrint();
			int freqUse = 1; //when process starts, set freqUse of page 0 to 1
//			int recentlyUse = 1; //when process starts, set recentlyUse of page 0 to 1.
			
			/*Add processName, the 1st page number (page 0), timeStamp, and freqUse to the "global" allProcessPages list*/
			allProcessPages.add(new Page(procName,pageNum, timeStamp, timeStampToCompu, freqUse));
			
			/*add page 0, process name, process size, and serviceDuration to its process list so the 
			 * process doesn't need to access to global(shared) variables when it runs => processes can run parallel*/
			procPageInMem.add(new Page(procName, pageNum, procSize, serDuration));
			allProcessInMemList.add(procPageInMem);//keep track all processes in memory
			
			int processIndex = allProcessInMemList.size()-1;//index of "allProcessInMemList" and "allProcessPages"
			
			/*print content*/
			String pEvict = "none";
			printContent(timeStamp, procName, pageNum, pageNum, pEvict);
			
			/*first time process runs, subtract 1 page from freePageList*/
			updateFreePageList("-");
			
//release lock here

			return processIndex; //index of the last element (process) of "allPocessInMemList"
		}//end if
	}finally{
		lock.unlock();
	}
		return -1; //no more free memory
	}//end method
	
	/*Continue to reference and swap pages until process completes or suspends*/
	public void referenceAndSwap(int procInd) throws InterruptedException{
		int procIndexLocal = procInd;
		int isEvicted = 0;// there is no evicted page
		LinkedList<Page> evictedItem = new LinkedList<>();
		
		while(procIndexLocal != -1){ //while there is free memory and process already started
		
			int pageInMem = 0; //initially, pageInMem=0 since page 0 is in memory after "FirstPageInMem" completed
			int pageCount = 1; // 1 page (page 0) already is in memory
			int pageInMemTemp;
	//lock here
			lock.lock();
			int prSize = allProcessInMemList.get(procIndexLocal).get(0).getProcSize();//get process size
			String prName = allProcessInMemList.get(procIndexLocal).get(0).getProcName();//get process name
			int serDuration = allProcessInMemList.get(procIndexLocal).get(0).getServiceDuration();//get service duration
			
			long startProcess = allProcessPages.get(procIndexLocal).getTimeStampToCompute();//time starts in "FirstPageInMem()"
	//release lock
	//		lock.unlock();
			while(pageCount < prSize){
				Thread.sleep(100);
	//need a lock here?
	//			lock.lock();
				long startEachPage = getTimeStampToCompute();
				String timeStamp = getTimeStampToPrint();
				int processDuration = (int)(startEachPage - startProcess);
				if( processDuration < serDuration){
					if(freePageList.size() > 0){
						if(isEvicted == 0){
							evictedItem.add(new Page("none", 0));	
						}
						pageInMemTemp= nextReferencePage(pageInMem, prSize, prName, evictedItem, timeStamp);
						pageInMem = pageInMemTemp;
						
						updateFreePageList("-");//subtract 1 page from FreePageList
						
						updateProcPageList(prName,pageInMem, prSize, serDuration);
						
						 /*Update "recently use" so it can be used in lru() */
//						 int rUse = allProcessPages.get(i).getRecentlyUse();//need to refine this
						
						//add a new page number to the global "all process" list
						 addNewPageToAllProcessPageList(prName,pageInMem, getTimeStampToPrint(),
													getTimeStampToCompute()); 
						pageCount++;
					}
					else{ //no more free memory
						evictedItem.clear();
						evictedItem = stealPage(selection);
						isEvicted = 1;//page evicted
						updateFreePageList("+");
					}
				}
				else{//if reach service duration limit

					/*print process finish*/
					//System.out.println("process reached service duration time");
					break;
				}
			
			}//end of "pageCount < prSize" while loop
		
		}//end of "procInd != -1" while loop
		
//		printProcess(); //print when process is done
//		removeProcess(procIndexLocal, allProcessPages, allProcessInMemList, procPageInMem, freePageList); //process done
//release the lock?
		lock.unlock();
	}
	
	
	 /*use random r for locality of reference: 0<= r <7 or 7<= r <=10 */	
	 public int nextReferencePage(int pgNum, int prcSize, String prcName, LinkedList<Page> evictP, String time){
			int pgNumTemp = pgNum; 
			int yes=1; //yes=1 if page is in memory. Otherwise is 0. pgNum is current page so it's already in memory
			int r = RandomNumberGenerator(0,10);
			
			if(r<7){ //case1: 0<= r <7 		
				while(yes ==1 || pgNumTemp > (prcSize-1)){//while page already in mem and pg num > process size
					pgNumTemp = computeTempPgNumForFirstCase(pgNumTemp);
					yes = isPageInMem(pgNumTemp, prcName, evictP, time);
				}
				pgNum = pgNumTemp;
			}
			else{ //case2: 7<= r <=10
				while(yes==1 || pgNumTemp > (prcSize-1)){//while page is already in mem or pg num > process size
					pgNumTemp = computeTempPgNumForSecondCase(pgNumTemp);
					yes = isPageInMem(pgNumTemp, prcName, evictP, time);
				}
				pgNum = pgNumTemp;			 
			}
			/*print content*/
			printContent(time, prcName, pgNum, pgNum, evictP.toString());
			return pgNum;
		}
		
	
	/* Get the menuChoice from Main class*/
	public void getMenuSelection(int sel){
		selection = sel;
    }
	
	/*StealPage happens when no more free memory*/
	public LinkedList<Page> stealPage(int choice){
		LinkedList<Page> evictItem = new LinkedList<>();
		switch (choice){
			 case 0: 	break;
			 case 1: 	fifo(); break;	
	         case 2: 	lru();  break;
	         case 3: 	lfu();  break;
	         case 4: 	mfu();  break;
	         default:	evictItem = random(); break;
		}
		return evictItem;
	}	
	
	public void updateFreePageList(String change){
		if (change.equals("-")){
			freePageList.removeFirst();//remove 1 page
		}
		else if(change.equals("+")){
			freePageList.add(1);//add 1 free page
		}
	}
	
	public void updateProcPageList(String prNa, int pgInMem, int prSize, int serDu){
		procPageInMem.add(new Page(prNa, pgInMem, prSize, serDu));	
	}
	
	public void addNewPageToAllProcessPageList(String prcsName, int pgNum, String time, long timeCompu){
		int freqUse = 1;	
		allProcessPages.add(new Page(prcsName, pgNum, time, timeCompu, freqUse));
	}
	
	 /* if page is in memory, return 1, otherwise return 0*/
	 public int isPageInMem(int pgNum, String prName, LinkedList<Page> pEvict, String time){
		 LinkedList<Page> temp = new LinkedList<>();
		 int i =0;
		 int j=0;
		 
		 /*Print content of all pages in memory of this process*/
		 while(i < allProcessPages.size()){ 
			 if(allProcessPages.get(i).getProcName().equals(prName)){
				 printContent(time, prName, pgNum, allProcessPages.get(i).getPageNumber(), pEvict.toString());
			 }
			 i++;
		 }
		 while(j < allProcessPages.size()){ 	 
			 if(allProcessPages.get(j).getProcName().equals(prName) 
					 && (allProcessPages.get(j).getPageNumber() == pgNum)){
	 
				 /*Update "frequency use" so it can be used in lfu() and mfu() */
				 int freqUsage = allProcessPages.get(j).getFreqUse() +1 ;		 
				 temp.add(new Page(allProcessPages.get(j).getProcName(),allProcessPages.get(j).getPageNumber(),
						 allProcessPages.get(j).getTimeStamp(),allProcessPages.get(j).getTimeStampToCompute(),freqUsage));				
				 allProcessPages.set(j,temp.poll());
				 
				 return 1; //page is in memory
			 }
			 j++;
		 }
		 return 0; //page is not in memory
	 }
	 
	 public void removeProcess(int index, LinkedList<Page> allProPages,LinkedList<LinkedList<Page>> allProInMemList, 
				LinkedList<Page> proPageInMem, LinkedList<Integer> freePgList){
		 String prName = proPageInMem.get(0).getProcName();
		 for(int i=0; i<allProPages.size(); i++){
			 if(allProPages.get(i).getProcName().equals(prName)){
				 allProPages.remove(i);
				 freePgList.add(1);
			 }
		 }
		 allProInMemList.remove(index);			
	 }
	
	
	
	/*-- Helpers: -------------------------*/
	
	
	/* Generate a random number between min and max.  This is a helper for nextReferencePage */
	 public int RandomNumberGenerator(int min, int max) {
		Random rand = new Random(System.currentTimeMillis());
		if(min<0){ //random from -1 to 1
			return ((int)Math.floor((Math.random() * 2)) > 0 ? 1 : -1) * (int)Math.floor((Math.random() * 2));
		}
		else{ //random from 0 to 10
			return rand.nextInt(max+1 - min)+min;
		}
	}
	 
	/*helper for nextReferencePage */
		public int computeTempPgNumForFirstCase(int pnum){
			int diff = RandomNumberGenerator(-1, 1);
			if(pnum ==0 && diff>0){
				pnum += diff;
			}
			else if(pnum > 0){
				pnum += diff;
			}
			return pnum;
		}
		
		public int computeTempPgNumForSecondCase(int pgNu){
			if(pgNu <= 2){
				pgNu= RandomNumberGenerator(pgNu+2, 10);
			}
			else{
				pgNu = RandomNumberGenerator(0, pgNu-2);
			}
			return pgNu;
		}
		
		
	/* Return a long time stamp for FIFO algorithm to compute*/
	public long getTimeStampToCompute(){
		return System.currentTimeMillis();
	}
	
	/*Return a string time stamp for print out*/
	public String getTimeStampToPrint(){
		Date instant = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat( "ss" );
		String time = sdf.format( instant );
		return time;
	}
	
	public void printColTittle(){
		System.out.println("\nTime-stamp	  Process name		       Page-referenced		Page-in-memory"
							+ "			   Page-evicted\n");
	}
	
	
	public void printContent(String timeStamp, String prName, int pgRef, int pgInMem, String pEvict){
		System.out.println(timeStamp+"			"+prName+"				"+pgRef+"			"+pgInMem+"				"
							+pEvict+"				");
	}
	
	
	public LinkedList<Integer> getFreePgList(){
		return freePageList;
	}
	
	public LinkedList<Process> getProcessList(){
		return processList;
	}
	
	public void fifo(){
		
	}
	
	public void  lru(){
		
	}
	
	public void  lfu(){
		
	}
	
	public void  mfu(){
		
	}
	
	public LinkedList<Page> random(){
		/*steal 1 page for process to continue*/
		LinkedList<Page> evictPgProc = new LinkedList<>();
		
		evictPgProc.add(allProcessPages.getLast());
		allProcessPages.removeLast();
		for(int i=0; i<procPageInMem.size(); i++){
			if(procPageInMem.get(i).getProcName().equals(evictPgProc.get(0).getProcName()) 
					&& procPageInMem.get(i).getPageNumber() == evictPgProc.get(0).getPageNumber()){
				procPageInMem.remove(i);
			}
		}
		return evictPgProc;
	}
	
}



