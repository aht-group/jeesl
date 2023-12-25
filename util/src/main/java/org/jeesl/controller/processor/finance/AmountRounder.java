package org.jeesl.controller.processor.finance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.finance.Finance;

public class AmountRounder
{
	final static Logger logger = LoggerFactory.getLogger(AmountRounder.class);
	
	public static double zero(double value)
	{
		return Math.round(value);
	}
	
	public static double one(double value)
	{
		return Math.round(value*10)/10.0d;
	}
	
	public static double flex(int decimals, double value)
	{
		double power = 10^decimals;
		return Math.round(value*power)/power;
	}
	
	public static Finance two(Finance finance)
	{
		finance.setValue(two(finance.getValue()));
		return finance;
	}
	
	public static double two(double value)
	{
		return Math.round(value*100)/100.0d;
	}
	
	public static double percentTwo(int a, int b)
	{
		return percentTwo(Integer.valueOf(a).doubleValue(),Integer.valueOf(b).doubleValue());
	}
	
	public static double percentZero(int a, int b)
	{
		return percentZero(Integer.valueOf(a).doubleValue(),Integer.valueOf(b).doubleValue());
	}
	
	public static Double percentTwo(double a, double b)
	{
		if(b==0) {return null;}
		double ratio = a/b;
		return two(100d*ratio);
	}
	
	public static double percentZero(double a, double b)
	{
		double ratio = a/b;
		return zero(100d*ratio);
	}
}