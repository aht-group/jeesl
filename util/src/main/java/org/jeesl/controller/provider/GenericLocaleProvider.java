package org.jeesl.controller.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.factory.txt.system.status.TxtStatusFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleManager;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericLocaleProvider <LOC extends JeeslLocale<?,?,LOC,?>>
									implements JeeslLocaleManager<LOC>,JeeslLocaleProvider<LOC>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(GenericLocaleProvider.class);
	
	private final List<LOC> locales; @Override public List<LOC> getLocales() {return locales;}
	private final List<String> localeCodes; @Override public List<String> getLocaleCodes() {return localeCodes;}
	private final Map<String,LOC> mapLocales;

	public static <LOC extends JeeslLocale<?,?,LOC,?>> GenericLocaleProvider<LOC> instance(List<LOC> locales) {return new GenericLocaleProvider<>(locales);}
	
	public GenericLocaleProvider()
	{
		localeCodes = new ArrayList<>();
		mapLocales = new HashMap<String,LOC>();
		locales = new ArrayList<>();
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
		this.locales.clear();
		
		this.locales.addAll(locales);
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