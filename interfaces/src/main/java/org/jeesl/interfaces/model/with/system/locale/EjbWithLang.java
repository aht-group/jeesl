package org.jeesl.interfaces.model.with.system.locale;

import java.util.Map;

import org.jeesl.interfaces.model.system.locale.JeeslLang;

public interface EjbWithLang<L extends JeeslLang>
{	
	public static String attributeName = "name";
	
	public Map<String,L> getName();
	public void setName(Map<String,L> name);
}