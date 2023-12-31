package org.jeesl.exception.io;

import java.io.Serializable;

public class JeeslNetworkException extends Exception implements Serializable
{
	private static final long serialVersionUID = 1;

	public JeeslNetworkException() 
	{ 
	} 
 
	public JeeslNetworkException(String s) 
	{ 
		super(s); 
	} 
}
