package org.jeesl.factory.ejb.system.status;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.txt.system.status.TxtStatusFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleManager;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.xml.JaxbUtil;

public class EjbLangFactory<L extends JeeslLang> implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(EjbLangFactory.class);

    private final Class<L> cL;

    public static <L extends JeeslLang> EjbLangFactory<L> instance(final Class<L> cL) {return new EjbLangFactory<>(cL);}
    private EjbLangFactory(final Class<L> cL)
    {
        this.cL = cL;
    }

	public Map<String,L> getLangMap(Langs langs) throws JeeslConstraintViolationException
	{
		if(langs==null){throw new JeeslConstraintViolationException(Langs.class.getSimpleName()+" is null");}
		return getLangMap(langs.getLang());
	}

	public Map<String,L> getLangMap(List<Lang> langList) throws JeeslConstraintViolationException
	{
		if(langList.size()<1){throw new JeeslConstraintViolationException(Langs.class.getSimpleName()+" with 0 "+Lang.class.getSimpleName());}
		Map<String,L> map = new Hashtable<String,L>();
		for(Lang lang : langList)
		{
			L l = createLang(lang);
			map.put(l.getLkey(), l);
		}
		return map;
	}

	public <LOC extends JeeslLocale<L,?,LOC,?>> Map<String,L> build(JeeslLocaleManager<LOC> lp, Langs xLangs) throws JeeslConstraintViolationException
	{
		Map<String,L> map = new Hashtable<String,L>();
		if(xLangs!=null && !xLangs.getLang().isEmpty())
		{
			for(Lang xLang : xLangs.getLang())
			{
				if(lp.hasLocale(xLang.getKey())) {map.put(xLang.getKey(),createLang(xLang));}
			}
		}
		for(String key : lp.getLocaleCodes())
		{
			if(!map.containsKey(key)) {map.put(key,createLang(key,""));}
		}
		return map;
	}

	public <LOC extends JeeslLocale<L,?,LOC,?>> Map<String,L> buildEmpty(List<LOC> locales)
	{
		return createEmpty(TxtStatusFactory.toCodes(locales).toArray(new String[0]));
	}

	public Map<String,L> createEmpty(String[] keys)
	{
		Map<String,L> map = new Hashtable<String,L>();
		for(String key : keys)
		{
			map.put(key, createLang(key,""));
		}
		return map;
	}

	public Map<String,L> createEmptyWithDefault(String[] keys, String defaultTranslation)
	{
		Map<String,L> map = new Hashtable<String,L>();
		for(String key : keys)
		{
			map.put(key, createLang(key,defaultTranslation));
		}
		return map;
	}
	public Map<String,L> createLangMap(String key, String translation)
	{
		Map<String,L> map = new Hashtable<String,L>();
		map.put(key, createLang(key, translation));
		return map;
	}

	public Map<String,L> clone(Map<String,L> original)
	{
		Map<String,L> map = new Hashtable<String,L>();
		for(String key : original.keySet())
		{
			map.put(key, createLang(key, original.get(key).getLang()));
		}
		return map;
	}

	public L createLang(String key, String translation)
	{
		try
		{
			L l = cL.newInstance();
			l.setLkey(key);
			l.setLang(translation);
			return l;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		logger.error("Something went terribly wrong, see stacktrace. Unfortunately null is returned here!");
		return null;
	}

	public L createLang(Lang lang) throws JeeslConstraintViolationException
	{
		if(lang.getKey()==null){throw new JeeslConstraintViolationException("Key not set for: "+JaxbUtil.toString(lang));}
		if(lang.getTranslation()==null){throw new JeeslConstraintViolationException("Translation not set for: "+JaxbUtil.toString(lang));}
		return createLang(lang.getKey(), lang.getTranslation());
	}

	public <T extends EjbWithLang<L>, LOC extends JeeslStatus<L,D,LOC>, D extends JeeslDescription> T persistMissingLangs(JeeslFacade fUtils, List<LOC> locales, T ejb)
	{
		return persistMissingLangs(fUtils,TxtStatusFactory.toCodes(locales).toArray(new String[0]),ejb);
	}
	public <T extends EjbWithLang<L>> T persistMissingLangsForCode(JeeslFacade fUtils, List<String> codes, T ejb)
	{
		String[] localeCodes = new String[codes.size()];
		for(int i=0;i<codes.size();i++)
		{
			localeCodes[i] = codes.get(i);
		}
		return persistMissingLangs(fUtils,localeCodes,ejb);
	}

	public <T extends EjbWithLang<L>> T persistMissingLangs(JeeslFacade fUtils, String[] keys, T ejb)
	{
		if(Objects.isNull(ejb.getName())) {ejb.setName(new HashMap<>());}
		for(String key : keys)
		{
			if(!ejb.getName().containsKey(key))
			{
				try
				{
					L l = fUtils.persist(createLang(key, ""));
					ejb.getName().put(key, l);
					ejb = fUtils.update(ejb);
				}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
				catch (JeeslLockingException e) {e.printStackTrace();}
			}
		}
		return ejb;
	}
	
	public <T extends EjbWithLang<L>, LOC extends JeeslLocale<L,?,LOC,?>> T persistMissingLangs(JeeslFacade fUtils, JeeslLocaleProvider<LOC> lp, T ejb)
	{
		if(Objects.isNull(ejb.getName())) {ejb.setName(new HashMap<>());}
		try
		{
			boolean hasChanges = false;
			for(LOC locale : lp.getLocales())
			{
				if(!ejb.getName().containsKey(locale.getCode()))
				{
					
					L l = createLang(locale.getCode(), "");
					if(EjbIdFactory.isSaved(ejb)) {l = fUtils.persist(l);}
					ejb.getName().put(locale.getCode(), l);
					hasChanges=true;
				}
			}
			if(EjbIdFactory.isSaved(ejb) && hasChanges)
			{
				ejb = fUtils.update(ejb);
			}
		}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		return ejb;
	}

	public <LOC extends JeeslLocale<L,?,LOC,?>> Map<String,L> persistMissigLangs(JeeslFacade fUtils, JeeslLocaleProvider<LOC> lp, Map<String,L> map)
	{
		for(LOC loc : lp.getLocales())
		{
			if(!map.containsKey(loc.getCode()))
			{
				try
				{
					L l = fUtils.persist(createLang(loc.getCode(), ""));
					map.put(loc.getCode(), l);
				}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
			}
		}
		return map;
	}

	public <M extends EjbWithLang<L>> void rmLang(JeeslFacade fUtils, M ejb)
	{
		Map<String,L> langMap = ejb.getName();
		ejb.setName(null);

		try{ejb=fUtils.update(ejb);}
		catch (JeeslConstraintViolationException e) {logger.error("",e);}
		catch (JeeslLockingException e) {logger.error("",e);}

		for(L lang : langMap.values())
		{
			try {fUtils.rm(lang);}
			catch (JeeslConstraintViolationException e) {logger.error("",e);}
		}
	}
	
	public <W extends EjbWithLang<L>> void update(W ejb, Langs langs)
	{
		if(Objects.nonNull(langs))
		{
			for(Lang xml : langs.getLang())
			{
				if(ejb.getName().containsKey(xml.getKey()))
				{
					ejb.getName().get(xml.getKey()).setLang(xml.getTranslation());
				}
			}
		}
	}
}