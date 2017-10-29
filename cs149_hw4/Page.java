package cs149_hw4;

public class Page {
	private int pgContent;
	private int free;
	private int startAddress;
	private int length;
	
	public Page(){}
	
	Page(int pc){
		pgContent = pc;
	}
	
	public Page(int f, int a, int l){
		free=f; startAddress=a; length=l;
	}
	
	public int getPageContent(){return pgContent;}
	public int getMemoryStatus(){return free;}
	public int getStartAddress(){return startAddress;}
	public int getLength(){return length;}
	
	public void setMemoryStatus(int flag){
		free = flag;
	}
}
