package org.jeesl.controller.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.factory.txt.system.status.TxtStatusFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslLocaleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericLocaleProvider <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>>
									implements JeeslLocaleProvider<LOC>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(GenericLocaleProvider.class);
	
	private final List<String> localeCodes; @Override public List<String> getLocaleCodes() {return localeCodes;}
	private final Map<String,LOC> mapLocales;

	
	public GenericLocaleProvider()
	{
		localeCodes = new ArrayList<>();
		mapLocales = new HashMap<String,LOC>();
	}
	public GenericLocaleProvider(List<LOC> locales)
	{
		this();
		this.setLocales(locales);
	}
	
	@Override public void addLocale(LOC loc)
	{
		localeCodes.add(loc.getCode());
		mapLocales.put(loc.getCode(),loc);
	}
	
	public void setLocales(List<LOC> locales)
	{
		localeCodes.clear();
		mapLocales.clear();
		
		localeCodes.addAll(TxtStatusFactory.toCodes(locales));
		mapLocales.putAll(EjbCodeFactory.toMapCode(locales));
	}

	@Override public boolean hasLocale(String localeCode){return mapLocales.containsKey(localeCode);}

	@Override public String getPrimaryLocaleCode()
	{
		if(!localeCodes.isEmpty()) {return localeCodes.get(0);}
		return null;
	}
	
}