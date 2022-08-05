package org.jeesl.exception.processing;

import java.io.Serializable;

public class JeeslWorkflowException extends Exception implements Serializable
{
	private static final long serialVersionUID = 1;

//	private final List<JeeslConstraint<?,?,?,?,?,?,?,?>> constraints;
 
	public JeeslWorkflowException(String s) 
	{ 
		super(s);
//		constraints = new ArrayList<>();
	} 
}
