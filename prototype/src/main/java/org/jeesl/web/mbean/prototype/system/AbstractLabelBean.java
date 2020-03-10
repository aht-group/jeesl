package org.jeesl.web.mbean.prototype.system;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.jeesl.api.bean.JeeslLabelBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.controller.handler.system.TranslationHandler;
import org.jeesl.controller.provider.FacadeTranslationProvider;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.interfaces.controller.handler.JeeslTranslationProvider;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractLabelBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
								RA extends JeeslRevisionAttribute<L,D,RE,?,?>>
								
					implements JeeslLabelBean<RE>,JeeslTranslationProvider<LOC>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractLabelBean.class);
	
	private TranslationHandler<L,D,RE,RA> th;
	private final IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,RA,?,?,?> fbRevision;
	private FacadeTranslationProvider<L,D,LOC,RE,RA> ftp;

	private final Map<RE,Map<MultiKey,String>> mapXpath;
	public Map<String, Map<String,L>> getEntities() {return th.getEntities();}
	public Map<String, Map<String, Map<String,L>>> getLabels() {return th.getLabels();}
	public Map<String, Map<String, Map<String,D>>> getDescriptions() {return th.getDescriptions();}
	
	public Map<String,RE> getMapEntities() {return th.getMapEntities();}
	@Override public List<RE> allEntities() {return th.allEntities();}
	
	public AbstractLabelBean(IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,RA,?,?,?> fbRevision)
	{
		this.fbRevision=fbRevision;
		mapXpath = new HashMap<RE,Map<MultiKey,String>>();
	}
	
	protected void postConstruct(JeeslIoRevisionFacade<L,D,?,?,?,?,?,RE,?,RA,?,?,?> fRevision)
	{		
		th = new TranslationHandler<L,D,RE,RA>(fRevision,fbRevision.getClassEntity());
		if(fbRevision!=null)
		{
			ftp = new FacadeTranslationProvider<>(fbRevision,fRevision);
		}
		
	}
	
	@Override public void reload(RE re)
	{
		th.reload(re);
		if(mapXpath.containsKey(re)) {mapXpath.remove(re);}
	}
	
	@Override
	public <E extends Enum<E>> String xpAttribute(String localeCode, Class<?> c, E code)
	{
		if(!th.getMapEntities().containsKey(c.getSimpleName()))
		{
			logger.warn("Entity not handled in Engine: "+c.getSimpleName());
			return "@id";
		}
		
		RE re = th.getMapEntities().get(c.getSimpleName());
		if(!mapXpath.containsKey(re)) {mapXpath.put(re, new HashMap<MultiKey,String>());}
		
		MultiKey key = new MultiKey(localeCode,code.toString());
		if(!mapXpath.get(re).containsKey(key))
		{
			mapXpath.get(re).put(key,ftp.xpAttribute(localeCode, c, code));
		}
		return mapXpath.get(re).get(key);
	}
	
	@Override
	public String tlEntity(String localeCode, Class<?> c)
	{
//		logger.info("Getting entity ["+localeCode+"] for "+c.getSimpleName());
		if(!th.getMapEntities().containsKey(c.getSimpleName()))
		{
			logger.warn("Entity not handled in Engine: "+c.getSimpleName());
			return "-NO.TRANSLATION-";
		}
		
		return th.getMapEntities().get(c.getSimpleName()).getName().get(localeCode).getLang();
	}
	
	@Override
	public boolean hasLocale(String localeCode)
	{
		logger.warn("NYI");
		return false;
	}
	@Override
	public List<String> getLocaleCodes()
	{
		logger.warn("NYI");
		return null;
	}
	@Override
	public String tlEntity(String localeCode, String key)
	{
		logger.warn("NYI");
		return null;
	}
	@Override
	public String tlAttribute(String localeCode, String key1, String key2)
	{
		logger.warn("NYI");
		return null;
	}
	@Override
	public <E extends Enum<E>> String tlAttribute(String localeCode, Class<?> c, E code)
	{
		logger.warn("NYI");
		return null;
	}
	@Override
	public String toDate(String localeCode, Date record)
	{
		logger.warn("NYI");
		return null;
	}
	@Override
	public String toTime(String localeCode, Date record)
	{
		logger.warn("NYI");
		return null;
	}
	@Override
	public String toCurrency(String localeCode, Double value)
	{
		logger.warn("NYI");
		return null;
	}
	@Override
	public String toCurrency(String localeCode, boolean grouping, int decimals, Double value)
	{
		logger.warn("NYI");
		return null;
	}
	@Override
	public void setLanguages(List<LOC> locales)
	{
		logger.warn("NYI");
	}
}