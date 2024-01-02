package net.sf.ahtutils.report.revert.excel.strategies;

import java.util.Hashtable;

import org.jeesl.api.controller.ImportStrategy;
import org.jeesl.controller.io.db.xml.UtilsIdMapper;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadByMappedCodeStrategy implements ImportStrategy {
	
	final static Logger logger = LoggerFactory.getLogger(LoadByMappedCodeStrategy.class);
	
	private Hashtable<String, Object> tempPropertyStore;
	public  Hashtable<String, Object> getTempPropertyStore() {return tempPropertyStore;}
	public void setTempPropertyStore(Hashtable<String, Object> tempPropertyStore) {this.tempPropertyStore = tempPropertyStore;}

	@Override
	public Object handleObject(Object object, String parameterClass, String property) {
		String code          = object.toString();
		Class<?>  lutClass   = null;
                Object lookupEntity  = null;
                if (logger.isTraceEnabled()) {logger.info("Searching for mapped " +parameterClass +" with code " +code);}
    	
    	try {
    		lutClass = (Class<?>) Class.forName(parameterClass);
	    	
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		UtilsIdMapper mapper = (UtilsIdMapper) this.tempPropertyStore.get("idMapper");
		if (mapper.isObjectMapped(lutClass, code))
		{
			lookupEntity = mapper.getMappedObject(lutClass, code);
                }
		else
		{
			try {
				lookupEntity = lutClass.newInstance();
			} catch (Exception e) {
				logger.error("Could not create new class instance of " +lutClass.getCanonicalName() +": " +e.getMessage());
			}
			try {
				ReflectionUtil.simpleInvokeMethod("setCode",
					      new Object[] { code },
					      lutClass,
					      lookupEntity);
			} catch (Exception e) {
				logger.error("Could not set Code for created " +lutClass.getSimpleName());
				logger.error(e.getMessage());
			}
			mapper.addObjectForCode(code, lookupEntity);
		}
    	return lookupEntity;
	}

	@Override
	public void setFacade(JeeslFacade facade) {
		logger.trace("The strategy " +this.getClass().getSimpleName() +" is not depending on database operations - no Facade needed!");
	}
}
