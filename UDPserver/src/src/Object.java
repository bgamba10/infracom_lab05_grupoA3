package src;

import java.io.Serializable;

public class Object implements Serializable{
	
	private Long time; 
	private int numObj; 
	private int numSend;
	private Long timeArrival; 

	public Long getTimeArrival() {
		return timeArrival;
	}

	public void setTimeArrival(Long timeArrival) {
		this.timeArrival = timeArrival;
	}

	public Object(int pNumbObj, int pNumSend) {
		numObj = pNumbObj; 
		numSend = pNumSend;
		time = System.currentTimeMillis(); 
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public int getNumObj() {
		return numObj;
	}

	public void setNumObj(int numObj) {
		this.numObj = numObj;
	}

	public int getNumSend() {
		return numSend;
	}

	public void setNumSend(int numSend) {
		this.numSend = numSend;
	}
	
	
}
