package org.jeesl.controller.processor.arithmetic;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;

public class NullCalculator
{
	public static Double add(Double a, Double b)
	{
		if(ObjectUtils.allNull(a,b)) {return null;}
		else
		{
			BigDecimal result = new BigDecimal(0);
			if(a!=null) {result = result.add(new BigDecimal(a));}
			if(b!=null) {result = result.add(new BigDecimal(b));}
			return result.doubleValue();
		}
	}
	public static Double add(Double a, Double b, Double c)
	{
		if(ObjectUtils.allNull(a,b,c)) {return null;}
		else
		{
			BigDecimal result = new BigDecimal(0);
			if(a!=null) {result = result.add(new BigDecimal(a));}
			if(b!=null) {result = result.add(new BigDecimal(b));}
			if(c!=null) {result = result.add(new BigDecimal(c));}
			return result.doubleValue();
		}
	}
	
	public static Integer add(Integer a, Integer b)
	{
		if(a==null && b==null) {return null;}
		else
		{
			BigDecimal result = new BigDecimal(0);
			if(a!=null) {result = result.add(new BigDecimal(a));}
			if(b!=null) {result = result.add(new BigDecimal(b));}
			return result.intValue();
		}
	}
	
	public static Double subtract(Double a, Double b)
	{
		if(a==null && b==null) {return null;}
		else
		{
			BigDecimal result = new BigDecimal(0);
			if(a!=null) {result = result.add(new BigDecimal(a));}
			if(b!=null) {result = result.subtract(new BigDecimal(b));}
			return result.doubleValue();
		}
	}
	
	
	public static Double multiply(Double a, Integer b)
	{
		if(a==null || b==null) {return null;}
		else
		{
			BigDecimal result = new BigDecimal(a);
			return result.multiply(new BigDecimal(b)).doubleValue();
		}
	}
	public static Integer multiply(Integer a, Integer b)
	{
		if(a==null || b==null) {return null;}
		else
		{
			BigDecimal result = new BigDecimal(a);
			return result.multiply(new BigDecimal(b)).intValue();
		}
	}
	
	public static double toDor0(Double value)
	{
		if(value==null) {return 0;}
		else {return value.doubleValue();}
	}
}