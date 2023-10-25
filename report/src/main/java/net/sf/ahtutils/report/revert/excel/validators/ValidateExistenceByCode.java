package net.sf.ahtutils.report.revert.excel.validators;

import java.util.Hashtable;

import org.jeesl.api.controller.ValidationStrategy;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateExistenceByCode implements ValidationStrategy {
	
	final static Logger logger = LoggerFactory.getLogger(ValidateExistenceByCode.class);
	
	private JeeslFacade facade;
	
	private Hashtable<String, Object> tempPropertyStore;
	public  Hashtable<String, Object> getTempPropertyStore() {return tempPropertyStore;}
	public void setTempPropertyStore(Hashtable<String, Object> tempPropertyStore) {this.tempPropertyStore = tempPropertyStore;}

	@Override
	public Boolean validate(Object object, String parameterClass, String property) {
		
		String code = (String) object;
		
		Class  lutClass      = null;
                Object lookupEntity  = null;
		Boolean validated    = false;
                
		if(logger.isTraceEnabled()){logger.trace("Searching for Entity with Code " +code);}
		
		// Try to find the entity with given code in database
		if (facade != null)
		{
			try {
				lutClass = (Class) Class.forName(parameterClass);
				lookupEntity = facade.fByCode(lutClass, code);
                                validated = true;
			} catch (Exception e) {
				if(logger.isTraceEnabled())
				{
					logger.trace("An error occured while trying to load entity with code " +code +" from database! " +e.getMessage());
				}

				// If the entity is not found or some other error occurs, mark it as NOT validated
				validated = false;
			}
		}
		
    	// Return the result
    	return validated;
	}

	@Override
	public void setFacade(JeeslFacade facade) {
		this.facade = facade;
	}

}
