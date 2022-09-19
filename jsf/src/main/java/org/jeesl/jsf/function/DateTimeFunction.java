package org.jeesl.jsf.function;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.DateUtil;

public final class DateTimeFunction
{
	final static Logger logger = LoggerFactory.getLogger(DateTimeFunction.class);
	
    private DateTimeFunction() {}
    
    public static Integer dateDifference(Date from, Date to)
    {
    	if(from==null || to==null){return null;}
    	LocalDate ldStart = DateUtil.toLocalDate(from);
    	LocalDate ldEnd = DateUtil.toLocalDate(to);

		return Long.valueOf(ChronoUnit.DAYS.between(ldStart, ldEnd)).intValue();
    }
    
    public static Integer dayDeviation(Date from, Date to, Integer ref)
    {
    	Integer actual = dateDifference(from, to);
    	if(actual==null || ref==null){return null;}
		return actual-ref;
    }
    
    public static Date plusDay(Date from, Integer days)
    {
    	if(from==null || days==null){return null;}
    	LocalDate ld = DateUtil.toLocalDate(from);
		return DateUtil.toDate(ld.plusDays(days));
    }
    
    public static String seconds2Minutes(Integer seconds)
    {
    	if(seconds==null) {return null;}
    	else
    	{
    		long lMinutes = TimeUnit.SECONDS.toMinutes(seconds);
    		long lSeconds = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds)));
    		StringBuilder sb = new StringBuilder();
    		sb.append(lMinutes);
    		sb.append(":");
    		if(lSeconds<10) {sb.append("0");}
    		sb.append(lSeconds);
        	return sb.toString();
    	}
    }
}
