package org.jeesl.exception.jsf;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSessionInitialisationException extends RuntimeException implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSessionInitialisationException.class);
	 
	public JeeslSessionInitialisationException(String s) 
	{ 
		super(s);
	}

}