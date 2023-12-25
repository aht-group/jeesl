package org.jeesl.factory.txt.module.finance;

import java.text.DecimalFormat;

import net.sf.ahtutils.xml.finance.Counter;

public class TxtCounterFactory
{
	@SuppressWarnings("unused")
	private final DecimalFormat df;
	
	public TxtCounterFactory(DecimalFormat df)
	{
		this.df=df;
	}
	
	public String valueWithCurrency(Counter c)
	{
		if(c==null || !c.isSetCounter()) {return "";}
		StringBuilder sb = new StringBuilder();
		sb.append(c.getCounter());
		return sb.toString();
	}
}
