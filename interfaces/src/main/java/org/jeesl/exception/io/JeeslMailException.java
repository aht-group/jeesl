package org.jeesl.exception.io;

import java.io.Serializable;

public class JeeslMailException extends Exception implements Serializable
{
	private static final long serialVersionUID = 1;

	public JeeslMailException() 
	{ 
	} 
 
	public JeeslMailException(String s) 
	{ 
		super(s); 
	} 
}
