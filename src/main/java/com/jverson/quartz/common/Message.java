package com.jverson.quartz.common;

import java.io.Serializable;

/**
 * @version  v1.0
 * @author   jiwenxing@jd.com <br/>
 */
public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static Message failure(String msg) {
		return new Message(false, msg);
	}
	public static Message failure(Exception e) {
		return new Message(false, e.getMessage());
	}
	public static Message failure() {
		return new Message(false);
	}
	public static Message success() {
		return new Message(true);
	}
	public static Message success(String msg) {
		return new Message(true, msg);
	}
	public Message(boolean valid, String msg) {
		super();
		this.valid = valid;
		this.msg = msg;
	}
	public Message(boolean valid) {
		super();
		this.valid = valid;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	boolean valid;
	String msg;
	Object data;

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.valid = true;
		this.data = data;
	}
	
}
