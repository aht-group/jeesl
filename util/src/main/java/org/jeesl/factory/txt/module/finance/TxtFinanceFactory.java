package org.jeesl.factory.txt.module.finance;

import java.text.DecimalFormat;
import java.util.Objects;

import org.jeesl.model.xml.module.finance.Figures;
import org.jeesl.model.xml.module.finance.Finance;
import org.jeesl.util.query.xpath.FiguresXpath;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TxtFinanceFactory
{
	private DecimalFormat df; public TxtFinanceFactory df(DecimalFormat value) {this.df=value; return this;}
	
	private String fallback;
	private boolean suffixSymbol; public TxtFinanceFactory suffixSymbol(boolean value) {this.suffixSymbol=value; return this;}
	private boolean suffixCurrency; public TxtFinanceFactory suffixCurrency(boolean value) {this.suffixCurrency=value; return this;}
	
	public static TxtFinanceFactory instance() {return new TxtFinanceFactory();}
	public TxtFinanceFactory()
	{
		fallback = "";
		suffixSymbol = false;
		
		df = new DecimalFormat("#.##");
	}
	
	public <E extends Enum<E>, C extends Enum<C>> String build(Figures figures, E figureCode, C financeCode)
	{
		Finance f = null;
		try {f = FiguresXpath.getFiguresFinance(figures,figureCode,financeCode);}
		catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {}
		return this.build(f);
	}
	
	public String build(Finance f)
	{
		if(f==null || Objects.isNull(f.getValue())) {return fallback;}
		StringBuilder sb = new StringBuilder();
		
		if(Objects.nonNull(f.getValue())) {sb.append(df.format(f.getValue()));}
		
		if(suffixSymbol && Objects.nonNull(f.getCurrency()))
		{
			if(Objects.nonNull(f.getCurrency().getSymbol())) {sb.append(" ").append(f.getCurrency().getSymbol());}
		}
		
		if(suffixCurrency && Objects.nonNull(f.getCurrency()))
		{
			if(Objects.nonNull(f.getCurrency().getLabel())) {sb.append(" ").append(f.getCurrency().getLabel());}
		}		
		
		return sb.toString();
	}
	
	public String valueWithSymbol(Finance f)
	{
		if(f==null || Objects.isNull(f.getValue())) {return "";}
		StringBuilder sb = new StringBuilder();
		
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
		if(f==null || Objects.isNull(f.getValue())) {return "";}
		StringBuilder sb = new StringBuilder();
		
		if(Objects.nonNull(f.getValue())) {sb.append(df.format(f.getValue()));}
		
		return sb.toString();
	}
	
	public String valueWithCurrency(Finance f)
	{
		if(f==null || Objects.isNull(f.getValue())) {return "";}
		StringBuilder sb = new StringBuilder();
		
		if(Objects.nonNull(f.getValue())) {sb.append(df.format(f.getValue()));}
		
		if(Objects.nonNull(f.getCurrency()))
		{
			if(Objects.nonNull(f.getCurrency().getSymbol())) {sb.append(" ").append(f.getCurrency().getSymbol());}
			else if(Objects.nonNull(f.getCurrency().getLabel())) {sb.append(" ").append(f.getCurrency().getLabel());}
		}		
		return sb.toString();
	}
}