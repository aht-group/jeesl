package net.sf.ahtutils.report.revert.excel.strategies;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import org.jeesl.api.controller.ImportStrategy;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListFromCommaSeparatedStringStrategy implements ImportStrategy {
	
	final static Logger logger = LoggerFactory.getLogger(ListFromCommaSeparatedStringStrategy.class);
	
	private JeeslFacade facade;
	
	private Hashtable<String, Object> tempPropertyStore;
	public  Hashtable<String, Object> getTempPropertyStore() {return tempPropertyStore;}
	public void setTempPropertyStore(Hashtable<String, Object> tempPropertyStore) {this.tempPropertyStore = tempPropertyStore;}

	@Override
	public Object handleObject(Object object, String parameterClass, String property) {
		String value = object.toString();
		
	//	List<String> list = Arrays.asList(value.split("\\s*,\\s*"));
            List<String> list = Arrays.asList(value.split(","));
		
    	return list;
	}

	@Override
	public void setFacade(JeeslFacade facade) {
		this.facade = facade;
	}

}
