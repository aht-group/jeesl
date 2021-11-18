package net.sf.ahtutils.report.revert.excel.strategies;

import java.math.BigDecimal;
import java.util.Hashtable;

import org.jeesl.api.controller.ImportStrategy;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadByNameStrategy implements ImportStrategy
{
	final static Logger logger = LoggerFactory.getLogger(LoadByNameStrategy.class);
	
	private JeeslFacade facade;
	
	private Hashtable<String, Object> tempPropertyStore;
	public  Hashtable<String, Object> getTempPropertyStore() {return tempPropertyStore;}
	public void setTempPropertyStore(Hashtable<String, Object> tempPropertyStore) {this.tempPropertyStore = tempPropertyStore;}

	@Override
	public Object handleObject(Object object, String parameterClass, String property) {
		
		String code = object.toString();
		logger.info("Code in Strategy: " +code);
		// Excel sometimes does bad formatting, this one fixes that behavior
		if (object.getClass().getName().equals("java.lang.Double"))
		{
						Double n			= (Double) object;
						// This version is not working! It produces results such as this:
						// Result 2147483647 taken from 3.57305101E9 (toString of n)
						// parameters[0]	= "" +n.intValue();
						BigDecimal bd = new BigDecimal(n);
						code	= bd.toPlainString();
						
						/* See above
						if (n % 1 == 0)
						{
							parameters[0]	= "" +n.intValue();
						}
						else
						{
							parameters[0]	= "" +n;
						}
						*/
		}
		
		logger.info("Code in Strategy after conversion: " +code);
		Class  lutClass      = null;
    	Object lookupEntity  = null;
    	
		// If there is no value found, return null or set the code to unknown
		if (code == null && !tempPropertyStore.containsKey("createEntityForUnknown"))
		{
			return null;
		}
		else if (code == null && tempPropertyStore.containsKey("createEntityForUnknown"))
		{
			code = "unknown";
		}
		
		if(logger.isTraceEnabled()){logger.trace("Searching for Entity with Code " +code);}
		
		// Try to find the entity with given code in database
		if (facade != null && (Boolean) getTempPropertyStore().get("lookup"))
		{
			try {
				lutClass = (Class) Class.forName(parameterClass);
				logger.info("lutClass " +lutClass.getName());
				lookupEntity = facade.fByName(lutClass, code);
			} catch (Exception e) {
				if(logger.isTraceEnabled())
				{
					logger.trace("An error occured while trying to load entity with code " +code +" from database! " +e.getMessage());
					logger.trace("Creating a new one!");
				}

				// If the entity is not found or some other error occurs, create a new object
				try {
					lookupEntity = lutClass.newInstance();
				} catch (InstantiationException ex) {
					logger.error(ex.getMessage());
				} catch (IllegalAccessException ex) {
					logger.error(ex.getMessage());
				}
			}
		} else
		{
			// If facade is null create a new object
			
			if(logger.isTraceEnabled())
			{
				logger.trace("Facade is null, creating a new instance of object with code " +code);
			}
			
			try {
				lutClass = (Class) Class.forName(parameterClass);
				lookupEntity = lutClass.newInstance();
			} catch (InstantiationException ex) {
				logger.error(ex.getMessage());
			} catch (IllegalAccessException ex) {
				logger.error(ex.getMessage());
			} catch (ClassNotFoundException ex) {
				logger.error(ex.getMessage());			}
		}
			
		// Since this is a code based strategy, invoke the setCode method with the given code
		Object[] parameters = new Object[1];
		parameters[0] = code;
		try {
			ReflectionUtil.simpleInvokeMethod("setName", parameters, lutClass, lookupEntity);
		} catch (Exception ex) {
			logger.error("Could not invoke setName method!");
			logger.error(ex.getMessage());
		}
		
    	// Return the result
    	return lookupEntity;
	}

	@Override
	public void setFacade(JeeslFacade facade) {
		this.facade = facade;
	}

}
