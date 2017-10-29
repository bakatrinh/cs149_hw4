package cs149_hw4;

import java.util.LinkedList;

public class SwappingPagingTester {

    public static void main(String[] args) {
        SetUp su = new SetUp();
        LinkedList<Process> processList = new LinkedList<>();
        processList = su.ProcessSetup();

        Process pr = new Process();


        LinkedList<Page> pageManageList = new LinkedList<>();
//		pageManageList = su.PageManagementSetup();


//		if(pageManageList.get(0).getMemoryStatus() == 1 && pageManageList.get(0).getLength()>= 4 ){
//			for(int i=0; i<25; i++){
//				int pageNeedPerProcess = processList.get(i).getProcessSize();
//			}
//			pageManageList.set(pageManageList.get(0).getMemoryStatus(), );
//		}

        int count = 0;
        for (Process a : processList) {
            System.out.println(a + " ");
            count++;
        }
        System.out.println("count: " + count);

//		Iterator<Process> i = processList.iterator();
//		while (i.hasNext()) {
//			Process p = i.next();
//		    System.out.println("Process size: " + p.getProcessSize());
//		    System.out.println("Page Content:");
//		    Iterator<Integer> pagei = p.getProcessSize().iterator();
//		    while (pagei.hasNext()) {
//		        Integer page = pagei.next();
//		        System.out.print(page+" ");
//		    }
//		}

    } //end main
} //end SwappingPagingTester

