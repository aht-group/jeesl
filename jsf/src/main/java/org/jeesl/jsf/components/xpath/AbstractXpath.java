package org.jeesl.jsf.components.xpath;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.faces.component.UIOutput;

import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractXpath extends UIOutput
{	
	final static Logger logger = LoggerFactory.getLogger(AbstractXpath.class);
	
	private Map<JeeslReportColumn,SimpleDateFormat> mapDateFormatter;
	private Map<String,DecimalFormat> mapDecimal;
	
	@Override public boolean getRendersChildren(){return true;}
	
	protected SimpleDateFormat getSimpleDateFormat(JeeslReportColumn c, JeeslStatus dt)
	{
		if(mapDateFormatter==null){mapDateFormatter = new HashMap<JeeslReportColumn,SimpleDateFormat>();}
		if(mapDateFormatter.containsKey(c)){return mapDateFormatter.get(c);}
		else
		{
			logger.info("Createing SDF with "+dt.getSymbol());
			SimpleDateFormat sdf = new SimpleDateFormat(dt.getSymbol());
			mapDateFormatter.put(c,sdf);
			return sdf;
		}
	}
	
	protected DecimalFormat getDecimalFormat(String pattern)
	{
		if(Objects.isNull(mapDecimal)) {mapDecimal = new HashMap<String,DecimalFormat>();}
		if(mapDecimal.containsKey(pattern)){return mapDecimal.get(pattern);}
		else
		{
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
			dfs.setDecimalSeparator(',');
			dfs.setGroupingSeparator('.'); 
			
			DecimalFormat df = new DecimalFormat(pattern);
			df.setDecimalFormatSymbols(dfs);
			mapDecimal.put(pattern,df);
			return df;
		}
	}
}