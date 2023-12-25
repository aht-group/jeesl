package org.jeesl.util.query.xpath;

import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.finance.Counter;
import net.sf.ahtutils.xml.finance.Figures;
import net.sf.ahtutils.xml.finance.Finance;
import net.sf.ahtutils.xml.finance.Time;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class FiguresXpath
{
	final static Logger logger = LoggerFactory.getLogger(FiguresXpath.class);
	
	public static synchronized Finance getFinance(Figures figures,String code) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(figures);
		
		StringBuffer sb = new StringBuffer();
		sb.append("finance[@code='").append(code).append("']");
		
		@SuppressWarnings("unchecked")
		List<Finance> list = (List<Finance>)context.selectNodes(sb.toString());
		if(list.size()==0){throw new ExlpXpathNotFoundException("No "+Finance.class.getSimpleName()+" for code="+code);}
		else if(list.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Finance.class.getSimpleName()+" for code="+code);}
		return list.get(0);
	}
	
	public static <E extends Enum<E>> Figures getChild(Figures figures, E code) {return getChild(figures,code.toString());}
	public static Figures getChild(Figures figures, String code)
	{
		if(figures!=null)
		{
			for(Figures f : figures.getFigures())
			{
				if(f.getCode().equals(code)) {return f;}
			}
		}
		return null;
	}
	
	public static <E extends Enum<E>, C extends Enum<C>> Finance getFiguresFinance(Figures figures, E figureCode, C financeCode) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(figures);
		
		StringBuffer sb = new StringBuffer();
		sb.append("figures[@code='").append(figureCode.toString()).append("']");
		sb.append("/finance[@code='").append(financeCode).append("']");
		
		@SuppressWarnings("unchecked")
		List<Finance> list = (List<Finance>)context.selectNodes(sb.toString());
		if(list.size()==0){throw new ExlpXpathNotFoundException("No "+Finance.class.getSimpleName()+" for code="+figureCode+" - "+financeCode);}
		else if(list.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Finance.class.getSimpleName()+" for code="+figureCode+" - "+financeCode);}
		return list.get(0);
	}
	
	private static <E extends Enum<E>, C extends Enum<C>> Finance getFiguresStaff(Figures figures, E figureCode, C staffCode) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(figures);
		
		StringBuffer sb = new StringBuffer();
		sb.append("figures[@code='").append(figureCode.toString()).append("']");
		sb.append("/finance[@code='").append(staffCode).append("']");
		
		@SuppressWarnings("unchecked")
		List<Finance> list = (List<Finance>)context.selectNodes(sb.toString());
		if(list.size()==0){throw new ExlpXpathNotFoundException("No "+Finance.class.getSimpleName()+" for code="+figureCode+" - "+staffCode);}
		else if(list.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Finance.class.getSimpleName()+" for code="+figureCode+" - "+staffCode);}
		return list.get(0);
	}
	
	public static <E extends Enum<E>, C extends Enum<C>> Time getDatesTime(Figures figures, E figureCode, C datesCode) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(figures);
		
		StringBuffer sb = new StringBuffer();
		sb.append("figures[@code='").append(figureCode.toString()).append("']");
		sb.append("/time[@code='").append(datesCode).append("']");
		
		@SuppressWarnings("unchecked")
		List<Time> list = (List<Time>)context.selectNodes(sb.toString());
		if(list.size()==0){throw new ExlpXpathNotFoundException("No "+Finance.class.getSimpleName()+" for code="+figureCode+" - "+datesCode);}
		else if(list.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Finance.class.getSimpleName()+" for code="+figureCode+" - "+datesCode);}
		return list.get(0);
	}
	
	public static <E extends Enum<E>, C extends Enum<C>> Counter getFiguresCounter(Figures figures, E figureCode, C counterCode) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(figures);
		
		StringBuffer sb = new StringBuffer();
		sb.append("figures[@code='").append(figureCode.toString()).append("']");
		sb.append("/counter[@code='").append(counterCode).append("']");
		
		@SuppressWarnings("unchecked")
		List<Counter> list = (List<Counter>)context.selectNodes(sb.toString());
		if(list.size()==0){throw new ExlpXpathNotFoundException("No "+Counter.class.getSimpleName()+" for code="+figureCode+" - "+counterCode);}
		else if(list.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Counter.class.getSimpleName()+" for code="+figureCode+" - "+counterCode);}
		return list.get(0);
	}
	
	
	public static Finance getFinance(List<Finance> list, int nr) throws ExlpXpathNotFoundException
	{
		for(Finance f : list)
		{
			if(f.getNr()==nr){return f;}
		}
		throw new ExlpXpathNotFoundException("No "+Finance.class.getSimpleName()+" for nr="+nr);
		
	}
}