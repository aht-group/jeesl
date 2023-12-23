package org.jeesl.factory.txt.module.finance;

import java.text.DecimalFormat;
import java.util.Objects;

import org.jeesl.model.xml.module.finance.Counter;

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
		if(Objects.isNull(c) || Objects.isNull(c.getCounter())) {return "";}
		StringBuilder sb = new StringBuilder();
		sb.append(c.getCounter());
		return sb.toString();
	}
}
