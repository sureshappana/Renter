package com.classes.renter;

public class Message {

	String message;
	String name;
	String apt_no;
	String time;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getApt_no() {
		return apt_no;
	}
	public void setApt_no(String apt_no) {
		this.apt_no = apt_no;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Message(String message, String name, String apt_no, String time) {
		super();
		this.message = message;
		this.name = name;
		this.apt_no = apt_no;
		this.time = time;
	}
	
	
}
