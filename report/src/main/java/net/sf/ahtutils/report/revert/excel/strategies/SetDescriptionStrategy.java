package net.sf.ahtutils.report.revert.excel.strategies;

import java.util.Hashtable;

import org.jeesl.api.controller.ImportStrategy;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.xml.io.locale.status.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetDescriptionStrategy implements ImportStrategy {
	
	final static Logger logger = LoggerFactory.getLogger(SetDescriptionStrategy.class);
	
	private JeeslFacade facade;
	
	private Hashtable<String, Object> tempPropertyStore;
	public  Hashtable<String, Object> getTempPropertyStore() {return tempPropertyStore;}
	public void setTempPropertyStore(Hashtable<String, Object> tempPropertyStore) {this.tempPropertyStore = tempPropertyStore;}

	@Override
	public Object handleObject(Object object, String parameterClass, String property) {
		
		String code = object.toString();
		
		Class  lutClass			 = null;
    	Description description  = new Description();
		
		description.setValue(code);
    	
		return description;
	}

	@Override
	public void setFacade(JeeslFacade facade) {
		this.facade = facade;
	}

}
