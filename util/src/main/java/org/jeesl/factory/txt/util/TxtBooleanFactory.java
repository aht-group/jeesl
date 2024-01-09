package org.jeesl.factory.txt.util;

public class TxtBooleanFactory
{
	private String sTrue; public TxtBooleanFactory setTue(String sTrue) {this.sTrue = sTrue; return this;}
	private String sFalse; public TxtBooleanFactory setFalse(String sFalse) {this.sFalse = sFalse; return this;}
	
	public static TxtBooleanFactory instance() {return new TxtBooleanFactory();}
	public TxtBooleanFactory()
	{
		sTrue = "Yes";
		sFalse = "No";
	}
	
	public String build(boolean value)
	{
		if(value) {return sTrue;}
		else {return sFalse;}
	}
	
	public static String yesNo(boolean value)
	{
		if(value) {return "Yes";}
		else {return "No";}
	}
	
	public static int zeroOne(boolean value)
	{
		if(value) {return 1;}
		else {return 0;}
	}
}