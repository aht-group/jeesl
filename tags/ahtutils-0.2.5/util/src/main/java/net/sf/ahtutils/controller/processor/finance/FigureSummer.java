package net.sf.ahtutils.controller.processor.finance;

import java.util.List;

import net.sf.ahtutils.controller.factory.xml.finance.XmlFinanceFactory;
import net.sf.ahtutils.xml.finance.Finance;

import org.apache.commons.jxpath.JXPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}

