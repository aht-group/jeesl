package net.sf.ahtutils.report.revert.excel.strategies;

import java.util.Hashtable;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.api.controller.ImportStrategy;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstLastNameStrategy implements ImportStrategy {
	
	final static Logger logger = LoggerFactory.getLogger(FirstLastNameStrategy.class);
	
	private JeeslFacade facade;
	
	private Hashtable<String, Object> tempPropertyStore;
	public  Hashtable<String, Object> getTempPropertyStore() {return tempPropertyStore;}
	public void setTempPropertyStore(Hashtable<String, Object> tempPropertyStore) {this.tempPropertyStore = tempPropertyStore;}

	@Override
	public Object handleObject(Object object, String parameterClass, String property) {
		String nameCellContent	= object.toString();
		Class  lutClass			= null;
    	String name				= "";
		logger.info(property);
		if (property.equals("FirstName"))
		{
			logger.info("Setting First Name.");
			String[] nameParts = StringUtils.split(nameCellContent, " ");
			name = nameParts[nameParts.length];
		}
		else if (property.equals("LastName"))
		{
			logger.info("Setting Last Name.");
			String[] nameParts = StringUtils.split(nameCellContent);
			name = nameParts[0];
		}
    	return name;
	}

	@Override
	public void setFacade(JeeslFacade facade) {
		this.facade = facade;
	}

}
