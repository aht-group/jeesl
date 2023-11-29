package org.jeesl.interfaces.model.with.system.locale;

import java.util.Map;

import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithLang<L extends JeeslLang> extends EjbWithId
{	
	public static String attributeName = "name";
	
	public Map<String,L> getName();
	public void setName(Map<String,L> name);
}