package org.jeesl.exception.processing;

import java.io.Serializable;

public class UtilsProcessingException extends Exception implements Serializable
{
	private static final long serialVersionUID = 1;

	private String code;
	
	public String getCode() {
		return code;
	}

	public static UtilsProcessingException instance(String message) {return new UtilsProcessingException(message);}
	
	public UtilsProcessingException() 
	{ 

	}
 
	public UtilsProcessingException(String s) 
	{ 
		super(s); 
	}
	
	public UtilsProcessingException code(String code) {this.code=code; return this;}
//	public UtilsProcessingException message(String message) {
}
