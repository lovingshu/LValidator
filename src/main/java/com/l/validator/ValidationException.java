package com.l.validator;

/**
 * 功能描述:Validation Excepion base model
 * @author: l.sl
 */
public class ValidationException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private int co;
	private String ex;
	
	public int getCo() {
		return co;
	}
	
	public void setCo(int co) {
		this.co = co;
	}
	
	public String getEx() {
		return ex;
	}
	
	public void setEx(String ex) {
		this.ex = ex;
	}
	
	public ValidationException(int co, String ex) {
		super();
		this.co = co;
		this.ex = ex;
	}
	
	@Override
	public String toString() {
		return this.ex+"("+this.co+")";
	}
}