package cs149_hw4;

public class Page {
	private int pgNumber;
	private String processName;
//	private int free;
//	private int startAtPageAddress;
//	private int length;
	
	/*Constructor*/
	public Page(){}
	
	/*Constructor*/
	Page(int pnum){
		pgNumber = pnum;
	}

	/*Constructor*/
	Page(String pname){
		processName = pname;
	}
	
	
	/*Constructor*/
//	public Page(int f, int s, int l){
//		free=f; startAtPageAddress=s; length=l;
//	}
	
	/*Get methods*/
	public int getPageNumber(){return pgNumber;}
//	public int getMemoryStatus(){return free;}
//	public int getStartAtPageAddress(){return startAtPageAddress;}
//	public int getLength(){return length;}
	
	
	/*Set methods*/
	public void setPageNumber(int pageC){
		pgNumber = pageC;
	}
	
//	public void setMemoryStatus(int isFree){
//		free = isFree; //1 if free
//	}
	
//	public void setStartAtPageAddress(int atPage){
//		startAtPageAddress = atPage;
//	}
//	
//	public void setLength(int l){
//		length = l;
//	}
}
