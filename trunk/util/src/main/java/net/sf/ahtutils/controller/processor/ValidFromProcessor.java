package net.sf.ahtutils.controller.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.ahtutils.model.interfaces.EjbWithValidFrom;
import net.sf.ahtutils.msgbundle.TranslationFactory;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidFromProcessor <T extends EjbWithValidFrom>
{
	private List<T> list;
	final static Logger logger = LoggerFactory.getLogger(TranslationFactory.class);
	
	public ValidFromProcessor(List<T> list)
	{
		this.list=list;
	}
	
	public List<T> getValid(Date d)
	{
		List<T> result = new ArrayList<T>();
		
		MutableDateTime dtCheck = new MutableDateTime(d);
		dtCheck.setDayOfMonth(1);
		dtCheck.setHourOfDay(0);
		dtCheck.setMinuteOfHour(0);
		dtCheck.setSecondOfMinute(0);
		dtCheck.setMillisOfSecond(0);
		
		logger.trace("Checlong after "+dtCheck+": "+list.size());
		
		boolean nextAdd = false;
		for(int i=0;i<list.size();i++)
		{
			T apsh = list.get(i);
			DateTime dt = new DateTime(apsh.getValidFrom());
			boolean isAfter = dt.isAfter(dtCheck);
			logger.trace(i+" is after="+isAfter+" nextAdd="+nextAdd+" results:"+result.size()+" apsh="+apsh.getValidFrom());
			
			if(isAfter)
			{
				result.add(apsh);
				if(dt.getDayOfMonth()!=1
				  && dt.getMonthOfYear()==dtCheck.getMonthOfYear()
				  && dt.getYear()==dtCheck.getYear())
				{
					nextAdd=true;
				}
			}
			else if(nextAdd)
			{
				result.add(apsh);
				nextAdd=false;
			}
		}
		if(result.size()==0){result.add(list.get(0));}
		logger.trace("results:"+result.size());
		return result;
	}
}
