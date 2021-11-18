package net.sf.ahtutils.report.revert.excel.strategies;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Hashtable;
import javax.xml.datatype.XMLGregorianCalendar;
import net.sf.ahtutils.report.revert.excel.annotations.ImportStrategyInfo;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jeesl.api.controller.ImportStrategy;

@ImportStrategyInfo(name = "Date and Time combined from two Columns", description = "Date and Time column must be specified both with this strategy", steps = 2)
public class CreateDateTime2Steps implements ImportStrategy {
	
	final static Logger logger = LoggerFactory.getLogger(CreateDateTime2Steps.class);
	
	private JeeslFacade facade;
	
	private Hashtable<String, Object> tempPropertyStore;
	public  Hashtable<String, Object> getTempPropertyStore() {return tempPropertyStore;}
	public void setTempPropertyStore(Hashtable<String, Object> tempPropertyStore) {this.tempPropertyStore = tempPropertyStore;}

	@Override
	public Object handleObject(Object object, String parameterClass, String property) {
		Date utilDate = (Date) object;
		try {	
			if (!tempPropertyStore.containsKey("datePart"))
			{
				tempPropertyStore.put("datePart",     utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				if (logger.isTraceEnabled()){logger.trace("Saved date part " +tempPropertyStore.get("datePart").toString());}
				ConvertToXmlCalendarStrategy helper = new ConvertToXmlCalendarStrategy();
				XMLGregorianCalendar calender = (XMLGregorianCalendar)helper.handleObject(Date.from(new Date().toInstant()), "","");
				return calender;
			}
			else
			{
				LocalDate datePart = (LocalDate) tempPropertyStore.get("datePart");
				LocalTime timePart = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
				LocalDateTime dt = LocalDateTime.of(datePart, timePart);
				if (logger.isTraceEnabled()){logger.trace("Added time part " +timePart.toString());}
				
				tempPropertyStore.remove("datePart");
				ConvertToXmlCalendarStrategy helper = new ConvertToXmlCalendarStrategy();
				XMLGregorianCalendar calender = (XMLGregorianCalendar)helper.handleObject(Date.from(dt.atZone(ZoneId.systemDefault()).toInstant()), "","");
				return calender;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return null;
	}

	@Override
	public void setFacade(JeeslFacade facade) {
		this.facade = facade;
	}

}
