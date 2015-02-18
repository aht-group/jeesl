package net.sf.ahtutils.report.revert.excel;

import java.util.Hashtable;

import net.sf.ahtutils.interfaces.facade.UtilsFacade;

public interface ImportStrategy {
	
	public Object 					 handleObject(Object object, String parameterClass);
	public void   					 setFacade(UtilsFacade facade);
	public void   					 setTempPropertyStore(Hashtable<String, Object> tempProperties);
	public Hashtable<String, Object> getTempPropertyStore();
}
