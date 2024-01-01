package org.jeesl.controller.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleManager;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacadeLocaleProvider <LOC extends JeeslLocale<?,?,LOC,?>>
									implements JeeslLocaleManager<LOC>,JeeslLocaleProvider<LOC>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(FacadeLocaleProvider.class);
	
	private final Class<LOC> c;
	
	private final List<LOC> locales; @Override public List<LOC> getLocales() {return locales;}
	private final List<String> localeCodes; @Override public List<String> getLocaleCodes() {return localeCodes;}
	private final Map<String,LOC> mapLocales;

	public static <LOC extends JeeslLocale<?,?,LOC,?>> FacadeLocaleProvider<LOC> instance(Class<LOC> c) {return new FacadeLocaleProvider<>(c);}
	
	public FacadeLocaleProvider(Class<LOC> c)
	{
		this.c=c;
		
		localeCodes = new ArrayList<>();
		mapLocales = new HashMap<String,LOC>();
		locales = new ArrayList<>();
	}
	
	public FacadeLocaleProvider<LOC> facade(JeeslFacade facade)
	{
		for(LOC loc : facade.all(c))
		{
			this.addLocale(loc);
		}
		return this;
	}
	
	@Override public void addLocale(LOC loc)
	{
		locales.add(loc);
		localeCodes.add(loc.getCode());
		mapLocales.put(loc.getCode(),loc);
	}
	
	@Override public boolean hasLocale(String localeCode){return mapLocales.containsKey(localeCode);}

	@Override public String getPrimaryLocaleCode()
	{
		if(!localeCodes.isEmpty()) {return localeCodes.get(0);}
		return null;
	}
}