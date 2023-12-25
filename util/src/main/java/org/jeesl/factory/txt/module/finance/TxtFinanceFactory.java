package org.jeesl.factory.txt.module.finance;

import java.text.DecimalFormat;

import net.sf.ahtutils.xml.finance.Finance;

public class TxtFinanceFactory
{
	private final DecimalFormat df;
	
	public TxtFinanceFactory(DecimalFormat df)
	{
		this.df=df;
	}
	
	public String valueWithCurrency(Finance f)
	{
		if(f==null || !f.isSetValue()) {return "";}
		StringBuilder sb = new StringBuilder();
		
		if (f.isSetValue()) {sb.append(df.format(f.getValue()));}
		
		if(f.isSetCurrency())
		{
			if(f.getCurrency().isSetSymbol()) {sb.append(" ").append(f.getCurrency().getSymbol());}
			else if(f.getCurrency().isSetLabel()) {sb.append(" ").append(f.getCurrency().getLabel());}
		}		
		return sb.toString();
	}
	
	public String value(Finance f)
	{
		if(f==null || !f.isSetValue()) {return "";}
		StringBuilder sb = new StringBuilder();
		
		if (f.isSetValue()) {sb.append(df.format(f.getValue()));}
		
		if(f.isSetCurrency())
		{
			if(f.getCurrency().isSetSymbol()) {sb.append(" ").append(f.getCurrency().getSymbol());}
			else if(f.getCurrency().isSetLabel()) {sb.append(" ").append(f.getCurrency().getLabel());}
		}		
		return sb.toString();
	}
}
