package org.jeesl.web.mbean.prototype.system;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.jeesl.api.bean.JeeslLabelBean;
import org.jeesl.api.facade.io.JeeslIoLabelFacade;
import org.jeesl.controller.handler.io.label.JeeslTranslationHandler;
import org.jeesl.controller.provider.FacadeTranslationProvider;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslTranslationProvider;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionMissingLabel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractLabelBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
								RA extends JeeslRevisionAttribute<L,D,RE,?,?>,
								RML extends JeeslRevisionMissingLabel>

					implements JeeslLabelBean<RE>,JeeslTranslationProvider<LOC>,JeeslLocaleProvider<LOC>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractLabelBean.class);

	private JeeslTranslationHandler<L,D,RE,RA,RML> th;
	private final IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,RA,?,?,?,RML> fbRevision;
	private FacadeTranslationProvider<L,D,LOC,RE,RA> ftp;

	private final Map<RE,Map<MultiKey,String>> mapXpath;
	
	public Map<String, Map<String,L>> getEntities() {return th.getEntities();}
	public Map<String, Map<String, Map<String,L>>> getLabels() {return th.getLabels();}
	public Map<String, Map<String, Map<String,D>>> getDescriptions() {return th.getDescriptions();}

	public Map<String,RE> getMapEntities() {return th.getMissingLabelHandler().getMapEntities();}
	@Override public List<RE> allEntities() {return th.allEntities();}

	private final List<LOC> locales; @Override public List<LOC> getLocales() {return locales;}
	private final Map<String,LOC> mapLocales; public Map<String, LOC> getMapLocales() {return mapLocales;}
	protected LOC fallbackLocale; public LOC getFallbackLocale() {return fallbackLocale;}
	
	public AbstractLabelBean(IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,RA,?,?,?,RML> fbRevision)
	{
		this.fbRevision=fbRevision;
		mapXpath = new HashMap<RE,Map<MultiKey,String>>();
		locales = new ArrayList<>();
		mapLocales = new HashMap<String,LOC>();
	}

	protected void postConstruct(JeeslIoLabelFacade<L,D,?,?,?,?,?,RE,?,RA,?,RML> fRevision)
	{
		th = new JeeslTranslationHandler<>(fbRevision,fRevision);
		ftp = new FacadeTranslationProvider<>(fbRevision,fRevision);
		fbRevision.ejbEntity().applyJscn(fRevision,th.allEntities());
	}

	protected void addLocales(List<LOC> locs) {for(LOC loc : locs) {this.addLocale(loc);}}
	protected void addLocale(LOC loc)
	{
		locales.add(loc);
		mapLocales.put(loc.getCode(),loc);
	}

	@Override public void reload(RE re)
	{
		th.reload(re);
		if(mapXpath.containsKey(re)) {mapXpath.remove(re);}
	}

	@Override public <E extends Enum<E>> String xpAttribute(String localeCode, Class<?> c, E code)
	{
		if(!th.getMissingLabelHandler().getMapEntities().containsKey(c.getSimpleName()))
		{
			logger.warn("Entity not handled in Engine: "+c.getSimpleName());
			return "@id";
		}

		RE re = th.getMissingLabelHandler().getMapEntities().get(c.getSimpleName());
		if(!mapXpath.containsKey(re)) {mapXpath.put(re, new HashMap<MultiKey,String>());}

		MultiKey key = new MultiKey(localeCode,code.toString());
		if(!mapXpath.get(re).containsKey(key))
		{
			mapXpath.get(re).put(key,ftp.xpAttribute(localeCode, c, code));
		}
		return mapXpath.get(re).get(key);
	}

	

	public void updateMissingLabels()
	{
		if(th != null)
		{
			this.th.getMissingLabelHandler().updateMissingLabels();
		}
	}
	
	//Method coming from  OfxTranslationProvider ... but required!
	@Override public List<String> getLocaleCodes() {return EjbCodeFactory.toListCode(locales);}
	
	@Override public void setContext(String localeCode, Class<?> c) {throw new UnsupportedOperationException("It's not allowed to set the context");}
	@Override public <E extends Enum<E>> String toLabel(E code) {throw new UnsupportedOperationException("It's not allowed to get Labels via context shortcut");}
	
	@Override public String tlEntity(String localeCode, Class<?> c)
	{
		return th.getEntities().get(c.getSimpleName()).get(localeCode).getLang();
	}
	@Override public <E extends Enum<E>> String tAttribute(String localeCode, Class<?> c, E code)
	{
//		logger.info("toLabel("+localeCode+","+c.getSimpleName()+","+code.toString()+")");
		return th.getLabels().get(c.getSimpleName()).get(code.toString()).get(localeCode).getLang();
	}
	@Override public <E extends Enum<E>> String toDescription(String localeCode, Class<?> c, E code)
	{
//		logger.info("toDescription("+localeCode+","+c.getSimpleName()+","+code.toString()+")");
		return th.getDescriptions().get(c.getSimpleName()).get(code.toString()).get(localeCode).getLang();
	}

	//This are some dummy-methods for the OfxTranslationProvider .. not required here
	@Override public boolean hasLocale(String localeCode) {logger.warn("NYI"); return false;}
	
	@Override public String tlEntity(Class<?> c) {throw new UnsupportedOperationException("It's not allowed to get Labels via context shortcut");}
	@Override public String tlEntity(String localeCode, String key) {logger.warn("NYI"); return null;}
	
	@Override public <E extends Enum<E>> String tAttribute(Class<?> c, E code) {throw new UnsupportedOperationException("It's not allowed to get Labels via context shortcut");}
	@Override public String tAttribute(String localeCode, String key1, String key2) {logger.warn("NYI"); return null;}
	
	@Override public String toDate(String localeCode, LocalDate record) {logger.warn("NYI"); return null;}
	@Override public String toDate(String localeCode, LocalDateTime ldt) {logger.warn("NYI"); return null;}
	@Override public String toDate(String localeCode, Date record) {logger.warn("NYI"); return null;}
	@Override public String toTime(String localeCode, Date record) {logger.warn("NYI"); return null;}
	@Override public String toTime(String localeCode, LocalDateTime ldt) {logger.warn("NYI"); return null;}
	@Override public String toCurrency(String localeCode, Double value) {logger.warn("NYI"); return null;}
	@Override public String toCurrency(String localeCode, boolean grouping, int decimals, Double value) {logger.warn("NYI"); return null;}
	@Override public void setLanguages(List<LOC> locales) {logger.warn("NYI");}
}