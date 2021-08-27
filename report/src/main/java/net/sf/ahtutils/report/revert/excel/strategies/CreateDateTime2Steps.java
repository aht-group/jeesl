package net.sf.ahtutils.report.revert.excel.strategies;

import java.util.Hashtable;
import net.sf.ahtutils.report.revert.excel.annotations.ImportStrategyInfo;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jeesl.api.controller.ImportStrategy;
import org.joda.time.LocalDate;

@ImportStrategyInfo(name = "Date and Time combined from two Columns", description = "Date and Time column must be specified both with this strategy", steps = 2)
public class CreateDateTime2Steps implements ImportStrategy {
	
	final static Logger logger = LoggerFactory.getLogger(CreateDateTime2Steps.class);
	
	private JeeslFacade facade;
	
	private Hashtable<String, Object> tempPropertyStore;
	public  Hashtable<String, Object> getTempPropertyStore() {return tempPropertyStore;}
	public void setTempPropertyStore(Hashtable<String, Object> tempPropertyStore) {this.tempPropertyStore = tempPropertyStore;}

	@Override
	public Object handleObject(Object object, String parameterClass, String property) {
		
		try {	
			if (!tempPropertyStore.containsKey("datePart"))
			{
				tempPropertyStore.put("datePart",     LocalDate.parse(object.toString()));
				logger.trace("Saved first coordinate " +tempPropertyStore.get("coordinate1of2").toString());
				return object;
			}
			else
			{
			    /*
				LocalDate datePart = (LocalDate) tempPropertyStore.get("datePart");
				LocalTime timePart = LocalTime.parse(object.toString());
				LocalDateTime dt = LocalDateTime.of(datePart, timePart);
				
    */
				Double     x = (Double)tempPropertyStore.get("coordinate1of2");
				tempPropertyStore.remove("coordinate1of2");
			
			}
		} catch (Exception e) {
			logger.error("An error occured while trying to import! " +e.getMessage());
		}
	    return null;
	}

	@Override
	public void setFacade(JeeslFacade facade) {
		this.facade = facade;
	}

}
