package org.jeesl.controller.processor.finance;

import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.jeesl.factory.xml.domain.finance.XmlFinanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.finance.Figures;
import net.sf.ahtutils.xml.finance.Finance;

public class FigureSummer
{
	final static Logger logger = LoggerFactory.getLogger(FigureSummer.class);
	
	@SuppressWarnings("unchecked")
	public static Finance sum(String resultCode, Object o)
	{
		double sum = 0;
		JXPathContext context = JXPathContext.newContext(o);
		List<Finance> list = (List<Finance>)context.selectNodes("//finance");
		logger.trace("Elements found: "+list.size());
		for(Finance f : list)
		{
			sum=sum+f.getValue();
		}
		return XmlFinanceFactory.create(resultCode, sum);
	}
	
	public static <E extends Enum<E>> void add(Figures figures, E code, Integer value) {FigureSummer.add(figures, code.toString(), value);}
	public static void add(Figures figures, String code, Integer value)
	{
		if(value!=null)
		{
			for(Finance f : figures.getFinance())
			{
				if(f.getCode().equals(code)){add(f,value.intValue());}
			}
		}
	}
	
	public static <E extends Enum<E>> void add(Figures figures, E code, Double value) {FigureSummer.add(figures, code.toString(), value);}
	public static void add(Figures figures, String code, Double value)
	{
		if(value!=null)
		{
			for(Finance f : figures.getFinance())
			{
				if(f.getCode().equals(code)){add(f,value);}
			}
		}
	}
	
	public static void add(Finance finance, double value)
	{
		finance.setValue(finance.getValue()+value);
	}
	
	public static double substract(Finance a, Finance b)
	{
		return a.getValue()-b.getValue();
	}
}