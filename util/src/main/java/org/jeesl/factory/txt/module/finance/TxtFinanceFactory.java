package org.jeesl.factory.txt.module.finance;

import java.text.DecimalFormat;
import java.util.Objects;

import org.jeesl.model.xml.module.finance.Finance;

public class TxtFinanceFactory
{
	private final DecimalFormat df;
	
	public TxtFinanceFactory(DecimalFormat df)
	{
		this.df=df;
	}
	
	public String valueWithCurrency(Finance f)
	{
		StringBuilder sb = new StringBuilder();
		
		if(Objects.isNull(f) || Objects.isNull(f.getValue())) {return "";}
		if(Objects.nonNull(f.getValue())) {sb.append(df.format(f.getValue()));}
		
		if(Objects.nonNull(f.getCurrency()))
		{
			if(Objects.nonNull(f.getCurrency().getSymbol())) {sb.append(" ").append(f.getCurrency().getSymbol());}
			else if(Objects.nonNull(f.getCurrency().getLabel())) {sb.append(" ").append(f.getCurrency().getLabel());}
		}
		return sb.toString();
	}
	
	public String value(Finance f)
	{
		StringBuilder sb = new StringBuilder();
		
		if(Objects.isNull(f) || Objects.isNull(f.getValue())) {return "";}
		if(Objects.nonNull(f.getValue())) {sb.append(df.format(f.getValue()));}
		
		if(Objects.nonNull(f.getCurrency()))
		{
			if(Objects.nonNull(f.getCurrency().getSymbol())) {sb.append(" ").append(f.getCurrency().getSymbol());}
			else if(Objects.nonNull(f.getCurrency().getLabel())) {sb.append(" ").append(f.getCurrency().getLabel());}
		}	
		return sb.toString();
	}
}
