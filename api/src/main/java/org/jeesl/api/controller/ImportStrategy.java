package org.jeesl.api.controller;

import java.util.Hashtable;

import org.jeesl.interfaces.facade.JeeslFacade;

public interface ImportStrategy
{
	public Object			    handleObject(Object object, String parameterClass, String property);
	public void			    setFacade(JeeslFacade facade);
	public void			    setTempPropertyStore(Hashtable<String, Object> tempProperties); 
	public Hashtable<String, Object>    getTempPropertyStore();
}
