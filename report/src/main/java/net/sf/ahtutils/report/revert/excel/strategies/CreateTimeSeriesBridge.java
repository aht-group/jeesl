package net.sf.ahtutils.report.revert.excel.strategies;

import java.math.BigDecimal;
import java.util.Hashtable;
import net.sf.ahtutils.report.revert.excel.configuration.JeeslDataImporterConstants;

import org.jeesl.api.controller.ImportStrategy;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTimeSeriesBridge implements ImportStrategy
{
    final static Logger logger = LoggerFactory.getLogger(CreateTimeSeriesBridge.class);

    private JeeslFacade facade; @Override public void setFacade(JeeslFacade facade) {this.facade = facade;}
    private Hashtable<String, Object> tempPropertyStore; public  Hashtable<String, Object> getTempPropertyStore() {return tempPropertyStore;} public void setTempPropertyStore(Hashtable<String, Object> tempPropertyStore) {this.tempPropertyStore = tempPropertyStore;}
	
    @Override
    public Object handleObject(Object object, String parameterClass, String property) {

	// The content of the Excel cell will be treated as a code to load an EjbWithCode from database to be used as referenced object in TsBridge
	String code = object.toString();

	// See what class must be used to build the object referenced by the bridge (e.g. a station or province)
	// and load the TimeSeries Bridge class from the project specific data model
	Class              bridgeEntityClass        = (Class)   tempPropertyStore.get(JeeslDataImporterConstants.timeSeriesVariables.TsBridgeEntitiyClass.toString());
	Class              tsDomainModelEntityClass = (Class)   tempPropertyStore.get(JeeslDataImporterConstants.timeSeriesVariables.TsDomainModelEntityClass.toString());

	//logger.info("Code found : " +code + " to construct a TimeSeries bridge connecting data to objects of type " +bridgeEntityClass.getSimpleName() + " to be used as a reference in " +tsDomainModelBridgeClass.getSimpleName());

	// Cast the given Facade to a TimeSeries facade
	JeeslTsFacade      tsFacade                 = (JeeslTsFacade)   facade;

	// Load the object by code - may be a measuring station, a checkpoint to measure waste, a city or distrct
	Object entity   = createEjbWithCode(object, bridgeEntityClass);
	logger.info("Found entity: " +((EjbWithCode) entity).getCode());
	try {
	    // Construct the TimeSeries related environment
	    JeeslTsEntityClass tsDomainModelEntityObj = (JeeslTsEntityClass) tsFacade.fByCode(tsDomainModelEntityClass, entity.getClass().getCanonicalName());
	    logger.info("Using this entity class object: " +tsDomainModelEntityObj.getCode());
	    JeeslTsBridge bridge = tsFacade.fcBridge(bridgeEntityClass, tsDomainModelEntityObj, (EjbWithId) entity);
	    
	    // Put the bridge to the context for use in later stage (find or create TimeSeries and create Data point)
	    tempPropertyStore.put("TsBridge", bridge);
	    return bridge;
        } catch (JeeslConstraintViolationException ex) {
            logger.error("Could not build bridge: " +ex.getMessage());
	} catch (JeeslNotFoundException ex) {
            logger.error("Could not build bridge: " +ex.getMessage());
	}
	    
        // Return the result
        return "";
    }
    
    public Object createEjbWithCode(Object code, Class targetClass)
    {
	// Excel sometimes does bad formatting, this one fixes that behavior
	if (code.getClass().getName().equals("java.lang.Double"))
	{
	    Double      n   = (Double) code;
	    BigDecimal  bd  = new BigDecimal(n);
	    code            = bd.toPlainString();
	}
	//logger.info("Code in Strategy after conversion: " +code);

	// Try to find the entity with given code in database
	Object ejbWithCode = null;
	if (facade != null)
	{
	    try {
		ejbWithCode = facade.fByCode(targetClass, code.toString());
	    } catch (Exception e) {
		//logger.error("Could not load EjbWithCode " +targetClass.getSimpleName() +e.getMessage());
		try {
		    // If the entity is not found or some other error occurs, create a new object
		    ejbWithCode = targetClass.newInstance();
		} catch (InstantiationException ex) {
		    //logger.error("Could not load or instantiate EjbWithCode " +targetClass.getSimpleName());
		} catch (IllegalAccessException ex) {
		    //logger.error("Could not load or instantiate EjbWithCode " +targetClass.getSimpleName());
		}
	    }
	}
	return ejbWithCode;
    }
}