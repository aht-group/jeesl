package org.jeesl.web.mbean.system;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.factory.builder.io.IoLocaleFactoryBuilder;
import org.jeesl.factory.txt.system.locale.TranslationFactory;
import org.jeesl.factory.txt.system.locale.TranslationMap;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.exlp.xml.io.Dir;

public class AbstractTranslationBean<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>>
									implements Serializable,JeeslTranslationBean<L,D,LOC>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractTranslationBean.class);
	private static final long serialVersionUID = 1L;

	private JeeslFacade fUtils;
	private IoLocaleFactoryBuilder<L,D,LOC> fbStatus;

	private TranslationMap tm;
	protected final List<String> langKeys; @Override public List<String> getLangKeys(){return langKeys;}

	protected final List<LOC> locales; @Override public List<LOC> getLocales() {return locales;}
	private final Map<String,LOC> mapLocales; public Map<String, LOC> getMapLocales() {return mapLocales;}

	public AbstractTranslationBean(IoLocaleFactoryBuilder<L,D,LOC> fbStatus)
	{
		langKeys = new ArrayList<String>();
		locales = new ArrayList<LOC>();
		this.fbStatus=fbStatus;
		mapLocales = new HashMap<String,LOC>();
	}

	@Override
	public void ping()
	{

	}


	protected void initMap(ClassLoader cl, String fXml, JeeslFacade fUtils)
	{
		this.fUtils=fUtils;
		initMap(cl,fXml);
	}
	protected void initMap(ClassLoader cl, String fXml)
    {
		StringBuilder sb = new StringBuilder();
		sb.append("Init "+TranslationMap.class.getSimpleName()+" with "+fXml);
		try
		{
			Dir dir = JaxbUtil.loadJAXB(cl,fXml,Dir.class);
			TranslationFactory tFactory = new TranslationFactory();
			for(net.sf.exlp.xml.io.File f : dir.getFile())
			{
				tFactory.add(cl,dir.getName()+"/"+f.getName());
			}
			tm = tFactory.gettMap();
			langKeys.clear();
			langKeys.addAll(tm.getLangKeys());
			sb.append(" ").append(StringUtils.join(langKeys, ", " ));
			logger.info(sb.toString());
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			sb.append(" ").append(e.getMessage());
			logger.error(sb.toString());
		}

		reloadLocales();
    }

	public void reloadLocales()
	{
		locales.clear();
		locales.addAll(fUtils.allOrderedPositionVisible(fbStatus.getClassLocale()));
		langKeys.clear();
		for(LOC loc : locales)
    	{
    		langKeys.add(loc.getCode());
    		mapLocales.put(loc.getCode(),loc);
    	}
		logger.info("Locales loaded "+locales.size());
	}

	public void overrideLangKeys(String... key)
	{
	    	langKeys.clear();
	    	for(String s : key)
	    	{
	    		langKeys.add(s);
	    	}
	}

    @Override public String get(String lang, String key)
    {
    	try {
    	return tm.translate(lang, key);
		} catch (Exception e) {
			logger.info(">>>Missing Translations lang= " + lang + " key= "+ key + "<<<<");
			return "";
		}
    }
}