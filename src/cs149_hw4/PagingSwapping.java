package cs149_hw4;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class PagingSwapping implements Runnable{	
	final ReentrantLock lock = new ReentrantLock();
	LinkedList<Process> processList = new LinkedList<>();//List of all process<ProcessName,ProcessPage,ArrivalTime,ServiceDuration
	LinkedList<Page> allProcessPages = new LinkedList<>();//keep track all pages of all processes globally
	LinkedList<Integer>freePageList = new LinkedList<>();//Free Page list
	LinkedList<Page> procPageInMem = new LinkedList<>();//keep track pages in memory for each process
	LinkedList<LinkedList<Page>> allProcessInMemList = new LinkedList<>();//keep all of the above "procPageInMem" list.  
																			//Use this to call individual "procPageInMem" list
	LinkedList<Page> evictItemList = new LinkedList<>();
	ProcessSimulation ps; // reference to outside ProcessSimulation so we can count hits and miss
	
	private String procName;
	private int procSize;
	private int serDuration;
	private int selection;
	private int isEvicted;
	private Timer timer;
	private int recentUsed = 0;
	
	/*Constructor*/
	public PagingSwapping(Timer timer, ProcessSimulation ps){
		SetUp su = new SetUp();
		processList = su.ProcessSetup();
		freePageList = su.getFreePageList();
		for(int i=0; i<25; i++){
			freePageList.add(1);  //Initially, there are 100 pages
		}
		this.timer = timer;
		this.ps = ps;
		isEvicted=0;
		System.out.println("\nTry");
	}
	
	
	public void run(){
		lock.lock();
		try {
			while(!processList.isEmpty() && timer.getCurrent() < 60.01){
				referenceAndSwap(FirstPageInMem(0));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	/* This method is to generate and keeping tract all activities when a process starts */
	public int FirstPageInMem(int pageNum) throws InterruptedException{
		/*Print title*/
		if(isEvicted == 0){
			printColTittle();
		}
		
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
				allProcessPages.add(new Page(procName,pageNum, timeStamp, timeStampToCompu, freqUse,++recentUsed));
				
				/*add page 0, process name, process size, and serviceDuration to its process list so the 
				 * process doesn't need to access to global(shared) variables when it runs => processes can run parallel*/
				procPageInMem.add(new Page(procName, pageNum, procSize, serDuration));
				allProcessInMemList.add(procPageInMem);//keep track all processes in memory
				
				int processIndex = allProcessInMemList.size()-1;//index of "allProcessInMemList" and "allProcessPages"
				
				/*print content*/
				if(isEvicted == 0){
					evictItemList.add(new Page("none ", 0));	
				}
				String pEvict1 = evictItemList.getLast().getProcName();
				int pEvict2 = evictItemList.getLast().getPageNumber();
				String pEvict = pEvict1 +"/"+ String.valueOf(pEvict2);
				printContent(timeStamp, procName, pageNum, pageNum, pEvict);
				
				/*first time process runs, subtract 1 page from freePageList*/
				updateFreePageList("-");
	
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
		// there is no evicted page
		
		if(procIndexLocal != -1){ //if there is free memory and process already started
		
			int pageInMem = 0; //initially, pageInMem=0 since page 0 is in memory after "FirstPageInMem" completed
			int pageCount = 1; // 1 page (page 0) already is in memory
			int pageInMemTemp;
				int i = allProcessInMemList.size()-1;
				int j = procPageInMem.size() -1;
					
				int prSize = allProcessInMemList.get(i).get(j).getProcSize();//get process size
				String prName = allProcessInMemList.get(i).get(j).getProcName();//get process name
				int serDuration = allProcessInMemList.get(i).get(j).getServiceDuration();//get service duration		
				
				long startProcess = allProcessPages.get(allProcessPages.size()-1).getTimeStampToCompute();//time starts in "FirstPageInMem()"
				long startEachPage = getTimeStampToCompute();
				String timeStamp = getTimeStampToPrint();
				int processDuration = (int)(startEachPage - startProcess);
				
				/*Cycle through all the pages of 1 process*/
				while(pageCount < prSize && processDuration < (serDuration*1000)){
					Thread.sleep(100);
						if(freePageList.size() > 0){
							if(isEvicted == 0){
								evictItemList.add(new Page("none ", 0));	
							}
							pageInMemTemp= nextReferencePage(pageInMem, prSize, prName, evictItemList, timeStamp);
							pageInMem = pageInMemTemp;
							
							updateFreePageList("-");//subtract 1 page from FreePageList
							
							updateProcPageList(prName,pageInMem, prSize, serDuration);
							
							 /*Update "recently use" so it can be used in lru() */
	//						 int rUse = allProcessPages.get(i).getRecentlyUse();//need to refine this
							if (selection == 2) {
								allProcessPages.get(procIndexLocal).setRecentlyUse(++recentUsed);
							}
							
							//add a new page number to the global "all process" list
							 addNewPageToAllProcessPageList(prName,pageInMem, getTimeStampToPrint(),
														getTimeStampToCompute()); 
							 isEvicted = 0;
							 pageCount++;
							 /*Get time for the next page*/
							 startEachPage = getTimeStampToCompute();
							 timeStamp = getTimeStampToPrint();
							 /*total time spends so far*/
							 processDuration += (int)(startEachPage - startProcess);
							
						}
						else{ //no more free memory
							isEvicted = evictProcedure();
						}
					} //end of while loop
				if(processDuration > (serDuration*1000)){
					System.out.println("\n*-----Process reached service duration time limit");
				}
				else{
					System.out.println("\nProcess completed");
					removeProcess(procIndexLocal, allProcessPages, allProcessInMemList, procPageInMem, freePageList);
				}
		}//end of outer if.  Process either finished or reached service time limit
		else{ //if process started but there is no memory, steal a page and starts "FirstPageInMem" again (start process again)
			isEvicted = evictProcedure();
		}
	}
	
	
	 /*use random r for locality of reference: 0<= r <7 or 7<= r <=10 */	
	 public int nextReferencePage(int pgNum, int prcSize, String prcName, LinkedList<Page> evictP, String time) throws InterruptedException{
			int pgNumTemp = pgNum; 
			int yes=1; //yes=1 if page is in memory. Otherwise is 0. pgNum is current page so it's already in memory
			int size = prcSize-1;
			int r = RandomNumberGenerator(0,10);
			if(r<7){ //case1: 0<= r <7 		
				while(yes ==1){//while page already in mem
					pgNumTemp = computeTempPgNumForFirstCase(pgNumTemp,size);
					yes = isPageInMem(pgNumTemp, prcName, evictP, time);
					if (yes == 1) {
						ps.incrementHit();
					}
					else {
						ps.incrementMiss();
					}
				}
				pgNum = pgNumTemp;
			}
			else{ //case2: 7<= r <=10
				while(yes==1){//while page is already in mem
					pgNumTemp = computeTempPgNumForSecondCase(pgNumTemp, size);
					yes = isPageInMem(pgNumTemp, prcName, evictP, time);
					if (yes == 1) {
						ps.incrementHit();
					}
					else {
						ps.incrementMiss();
					}
				}
				pgNum = pgNumTemp;			 
			}

			return pgNum;
		}
		
	
	/* Get the menuChoice from Main class*/
	public void getMenuSelection(int mChoice){
		selection = mChoice;
    }
	
	public int evictProcedure(){
		evictItemList = stealPage(selection);
	    int isEvict = 1;//page evicted
		updateFreePageList("+");
		return isEvict;
	}
	
	
	/*StealPage happens when no more free memory*/
	public LinkedList<Page> stealPage(int choice){
		switch (choice){
			 case 0: 	break;
			 case 1: 	evictItemList = fifo(); break;	
	         case 2: 	evictItemList = lru();  break;
	         case 3: 	evictItemList = lfu();  break;
	         case 4: 	evictItemList = mfu();  break;
	         default:	evictItemList = Random(); break;
		}
		return evictItemList;
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
	 public int isPageInMem(int pgNum, String prName, LinkedList<Page> pEvict, String time) throws InterruptedException{
		 LinkedList<Page> temp = new LinkedList<>();	
		 lock.lock();
		try{
			 /*Print content of all pages in memory that referenced*/
			 for(int i=0; i < allProcessPages.size(); i++){ 
				 if(allProcessPages.get(i).getProcName().equals(prName)){
					 String pEvict1 = pEvict.getLast().getProcName();
					 int pEvict2 = pEvict.getLast().getPageNumber();
					 String ev = pEvict1 +"/"+ String.valueOf(pEvict2);
					 printContent(time, prName, pgNum, allProcessPages.get(i).getPageNumber(), ev);
				 }
			 }//end of loop of i
			 
			 for(int j=0; j < allProcessPages.size(); j++){ 
				 if(allProcessPages.get(j).getProcName().equals(prName) && (allProcessPages.get(j).getPageNumber() == pgNum)){
		 
					 /*Update "frequency use" so it can be used in lfu() and mfu() */
					 int freqUsage = allProcessPages.get(j).getFreqUse() +1 ;		 
					 temp.add(new Page(allProcessPages.get(j).getProcName(),allProcessPages.get(j).getPageNumber(),
							 allProcessPages.get(j).getTimeStamp(),allProcessPages.get(j).getTimeStampToCompute(),freqUsage));				
					 allProcessPages.set(j,temp.poll());
					 
					 return 1; //page is in memory
				 }
			 }//end for loop of j
			 
			 } finally {
			 lock.unlock();
		 }
		 return 0; //page is not in memory
	 }
	 
	 public void removeProcess(int index, LinkedList<Page> allProPages,LinkedList<LinkedList<Page>> allProInMemList, 
				LinkedList<Page> proPageInMem, LinkedList<Integer> freePgList){
		 lock.lock();
		 try{
			
			 String prName = allProInMemList.get(index).get(0).getProcName();
			 for(int i=0; i<allProPages.size(); i++){
				 if(allProPages.get(i).getProcName().equals(prName)){
					 allProPages.remove(i);
					 freePgList.add(1);
				 }
			 }
			 allProInMemList.remove(index);	
			
		 }finally{
			 lock.unlock();
		 }
	 }
	
	
	
	/*-- Helpers: ------------------------------------------------------------------------------------*/
	
	
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
		public int computeTempPgNumForFirstCase(int pnum, int maxNum){
			do{
				int diff = RandomNumberGenerator(-1, 1);
				if(pnum ==0 && diff>0){
					pnum = diff;
				}
				else if(pnum > 0){
					pnum += diff;
				}
			}while(pnum > maxNum);
			return pnum;
		}
		
		/*helper for nextReferencePage */
		public int computeTempPgNumForSecondCase(int pgNu, int maxNu){
			do{
				if(pgNu <= 2){
					pgNu= RandomNumberGenerator(pgNu+2, 10);
				}
				else{
					pgNu = RandomNumberGenerator(0, pgNu-2);
				}
			}while(pgNu > maxNu);
			return pgNu;
		}
		
		
	/* Return a long time stamp for FIFO algorithm to compute*/
	public long getTimeStampToCompute(){
		return System.currentTimeMillis();
	}
	
	/*Return a string time stamp for print out*/
	public String getTimeStampToPrint(){
		return timer.getCurrentString();
	}
	
	public void printColTittle(){
		System.out.println("\nTimestamp	  Process name		       Page-referenced		Page-in-memory"
							+ "			   Process/Page-evicted\n");
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
	
	public LinkedList<Page>fifo(){
		int fifo = getEarliestTimeStamp();
		evictItemList.add(allProcessPages.get(fifo));
		/* remove a page from memory (allProcessPage list) */
		allProcessPages.remove(fifo);

		/*remove pages from memory (procPageInMem list) */

		for (int i = 0; i < procPageInMem.size(); i++) {
			if (procPageInMem.get(i).getProcName().equals(evictItemList.get(0).getProcName())
					&& procPageInMem.get(i).getPageNumber() == evictItemList.get(0).getPageNumber())
				procPageInMem.remove(i);
		}

		return evictItemList;
	}
	
	public LinkedList<Page>lru(){
		int min =0;
		
		for (Page p : allProcessPages) {
			if(min >= p.getRecentlyUse()){
				min = p.getRecentlyUse();
				evictItemList.clear();;
				evictItemList.add(p);
			}
		}
		for (int i = 0; i < procPageInMem.size(); i++) {
			if (procPageInMem.get(i).getProcName().equals(evictItemList.get(0).getProcName())
					&& procPageInMem.get(i).getPageNumber() == evictItemList.get(0).getPageNumber())
				procPageInMem.remove(i);
		}
		return evictItemList;
	}
	
	private LinkedList<Page> lfu(){
		int lfu = getLeastFrequentlyUsed();
		evictItemList.add(allProcessPages.get(lfu));
		/* remove a page from memory (allProcessPage list) */
		allProcessPages.remove(lfu);

		/*remove pages from memory (procPageInMem list) */

		for (int i = 0; i < procPageInMem.size(); i++) {
			if (procPageInMem.get(i).getProcName().equals(evictItemList.get(0).getProcName())
					&& procPageInMem.get(i).getPageNumber() == evictItemList.get(0).getPageNumber())
				procPageInMem.remove(i);
		}

		return evictItemList;
	}
	
	public LinkedList<Page> mfu(){
		/*steal 1 page for process to continue*/
		int mfu = getMfu();
		evictItemList.add(allProcessPages.get(mfu));
		/*remove a page from memory (allProcessPages list)*/
		allProcessPages.remove(mfu);
		
		/*remove a page from memory (procPageInMem list) */
		for(int i=0; i<procPageInMem.size(); i++){
			if(procPageInMem.get(i).getProcName().equals(evictItemList.get(0).getProcName()) 
					&& procPageInMem.get(i).getPageNumber() == evictItemList.get(0).getPageNumber()){
				procPageInMem.remove(i);
			}
		}
		
		return evictItemList; //return an evicted page
	}
	
	public LinkedList<Page> Random() {
		/*steal 1 page for process to continue*/
		int rand = RandomNumberGenerator(0, allProcessPages.size()-1);
		evictItemList.add(allProcessPages.get(rand));
		/*remove a page from memory (allProcessPages list)*/
		allProcessPages.remove(rand);
		
		/*remove a page from memory (procPageInMem list) */
		for(int i=0; i<procPageInMem.size(); i++){
			if(procPageInMem.get(i).getProcName().equals(evictItemList.get(0).getProcName()) 
					&& procPageInMem.get(i).getPageNumber() == evictItemList.get(0).getPageNumber()){
				procPageInMem.remove(i);
			}
		}
		
		return evictItemList; //return an evicted page
	}
	
	public int getMfu() {
		int returnPage = 0;
		int highest = 0;
		for(int j=0; j < allProcessPages.size(); j++){ 
			 if(allProcessPages.get(j).getFreqUse() > highest){
				 returnPage = j;
				 highest = allProcessPages.get(j).getFreqUse();
			 }
		 }
		return returnPage;
	}

	private int getLeastFrequentlyUsed() {
		int returnPage = 0;
		int lowest = allProcessPages.get(0).getFreqUse();
		for(int i = 0; i < allProcessPages.size(); i++){
			if(allProcessPages.get(i).getFreqUse() < lowest){
				returnPage = i;
				lowest = allProcessPages.get(i).getFreqUse();
			}
		}
		return returnPage;
	}
	
	public int getEarliestTimeStamp() {
		int returnPage = 0;
		double earliest = allProcessPages.get(0).getTimeStampToCompute();
		for(int i = 1; i< allProcessPages.size(); i++) {
			if(allProcessPages.get(i).getTimeStampToCompute() > earliest) {
				returnPage = i;
				earliest = allProcessPages.get(i).getTimeStampToCompute();
			}
		}
		return returnPage;
	}
}



