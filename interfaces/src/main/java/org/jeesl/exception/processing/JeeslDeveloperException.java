package org.jeesl.exception.processing;

import java.io.Serializable;

public class JeeslDeveloperException extends RuntimeException implements Serializable
{
	private static final long serialVersionUID = 1;
 
	public JeeslDeveloperException(String s) 
	{ 
		super(s); 
	} 
}
