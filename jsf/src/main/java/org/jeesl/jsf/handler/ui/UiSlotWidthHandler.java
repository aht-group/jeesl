package org.jeesl.jsf.handler.ui;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//jeesl.highlight:showcase
public class UiSlotWidthHandler implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(UiSlotWidthHandler.class);

	private int a; public int getA() {return a;}
	private int b; public int getB() {return b;}
	private int c; public int getC() {return c;}
	private int d; public int getD() {return d;}
	
	public boolean isShowA() {return a>0;}
	public boolean isShowB() {return b>0;}
	public boolean isShowC() {return c>0;}
	public boolean isShowD() {return d>0;}
	
	public UiSlotWidthHandler()
	{
		
	}

	public void set(int a) {this.set(a,0,0,0);}
	public void set(int a, int b) {this.set(a,b,0,0);}
	public void set(int a, int b, int c) {this.set(a,b,c,0);}
	
	public void set(int a, int b, int c, int d)
	{
		this.a=a;
		this.b=b;
		this.c=c;
		this.d=d;
	}
}
//jeesl.highlight:showcase