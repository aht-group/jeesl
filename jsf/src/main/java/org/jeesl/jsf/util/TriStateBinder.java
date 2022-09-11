package org.jeesl.jsf.util;

import org.jeesl.jsf.handler.PrimeFacesTriStateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriStateBinder
{
	final static Logger logger = LoggerFactory.getLogger(TriStateBinder.class);
	
	public TriStateBinder()
	{
		reset();
	}
	
	public void reset()
	{
		a = "";
	}
	
	private String a; 
	public String getA() {return a;}
	public void setA(String a) {this.a = a;}
	public void booleanToA(Boolean value) {a = PrimeFacesTriStateUtil.booleanToTri(value);}
	public Boolean aToBoolean() {return PrimeFacesTriStateUtil.triToBoolean(a);}
}