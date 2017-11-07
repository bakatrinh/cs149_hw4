package cs149_hw4;

import java.util.LinkedList;

public class Page {
	private int pgNumber;
	private int processSize;
	private int ServiceDuration;
	private String processName;
	private String timeStamp;
	private long timeStampToCompute;
	private int freqUse;
	private int recentlyUse;
	
	/*Constructors*/
	
	public Page(){}
	
	public Page(String prName, int pnum){
		processName = prName;
		pgNumber = pnum;
	}
	
	public Page(String prName, int pnum, int prSize, int serDu){
		pgNumber = pnum;
		processSize = prSize;
		ServiceDuration = serDu;
		processName = prName;
	}
	
	public Page(String pname, int pNum, String tstamp, long timeComp, int fUse){
		processName = pname;
		pgNumber=pNum;
		timeStamp = tstamp;
		timeStampToCompute = timeComp;
		freqUse = fUse;
//		recentlyUse = rUse;
	}
	
	/*Get methods*/
	public int getPageNumber(){return pgNumber;}
	public int getProcSize(){ return processSize;}
	public int getServiceDuration(){return ServiceDuration;}
	public String getProcName(){return processName;}
	public String getTimeStamp(){return timeStamp;}
	public long getTimeStampToCompute(){return timeStampToCompute;}
	public int getFreqUse(){return freqUse;}
	public int getRecentlyUse(){return recentlyUse;}
	
	
	/*Set methods*/
	
	public void setPageNumber(int pgnu){
		pgNumber = pgnu;
	}
	public void setProcSize(int prs){
		processSize = prs;
	}
	public void setServiceDuration(int sd){
		ServiceDuration = sd;
	}
	public void setProcName(String pna){
		processName = pna;
	}
	public void setTimeStamp(String ts){
		timeStamp = ts;
	}
	public void setTimeStampToCompute(long tstc){
		timeStampToCompute = tstc;
	}
	public void setFreqUse(int fu){
		freqUse = fu;
	}
	public void setRecentlyUse(int ru){
		recentlyUse = ru;
	}
}
