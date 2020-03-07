package org.jeesl.web.mbean.prototype.io.cms;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jeesl.api.bean.JeeslCmsCacheBean;
import org.jeesl.api.facade.io.JeeslIoCmsFacade;
import org.jeesl.controller.provider.GenericLocaleProvider;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoCmsFactoryBuilder;
import org.jeesl.interfaces.controller.JeeslCmsRenderer;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsCategory;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsVisiblity;
import org.jeesl.interfaces.model.system.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.openfuxml.content.ofx.Section;
import org.openfuxml.exception.OfxAuthoringException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringUtil;
import net.sf.exlp.util.xml.JaxbUtil;

public abstract class AbstractCmsCacheBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										CAT extends JeeslIoCmsCategory<L,D,CAT,?>,
										CMS extends JeeslIoCms<L,D,CAT,S,LOC>,
										V extends JeeslIoCmsVisiblity,
										S extends JeeslIoCmsSection<L,S>,
										E extends JeeslIoCmsElement<V,S,EC,ET,C,FC>,
										EC extends JeeslStatus<EC,L,D>,
										ET extends JeeslStatus<ET,L,D>,
										C extends JeeslIoCmsContent<V,E,MT>,
										MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
										FC extends JeeslFileContainer<?,FM>,
										FM extends JeeslFileMeta<D,FC,?,?>
										>
					implements Serializable,JeeslCmsCacheBean<S>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractCmsCacheBean.class);
	
	private final IoCmsFactoryBuilder<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC,FM> fbCms;
	private JeeslCmsRenderer<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC> ofx;
	private JeeslIoCmsFacade<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC,FM> fCms;
	
	private final Map<Long,S> mapId;
	private final Map<S,Map<String,Section>> mapSection;
	private boolean debugOnInfo; protected void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	public AbstractCmsCacheBean(IoCmsFactoryBuilder<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC,FM> fbCms)
	{
		this.fbCms=fbCms;
		
		mapSection = new HashMap<S,Map<String,Section>>();
		mapId = new HashMap<Long,S>();
	}
	
	protected void postConstructCms(JeeslIoCmsFacade<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC,FM> fCms,
									JeeslCmsRenderer<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC> ofx)
	{
		this.fCms=fCms;
		this.ofx=ofx;
	}
	
	public void clearCache(S section)
	{
		logger.info("Invalidation Section "+section.toString());
		if(mapSection.containsKey(section)) {mapSection.remove(section);}
	}
	
	public Section buildById(String localeCode, long id) throws JeeslNotFoundException
	{
		if(mapId.containsKey(id)) {return buildBySection(localeCode,mapId.get(id));}
		else
		{
			S section = fCms.find(fbCms.getClassSection(),id);
			mapId.put(id,section);
			return buildBySection(localeCode,section);
		}
	}
	
	public Section buildBySection(String localeCode, S section)
	{
		if(section==null) {logger.warn("Section is NULL"); return new Section();}
		else
		{
			if(debugOnInfo) {logger.info("Requesting "+localeCode+" "+section.toString());}
			if(!mapSection.containsKey(section)) {mapSection.put(section, new HashMap<String,Section>());}
			
//			logger.info("Cached section:"+mapSection.containsKey(section));
//			logger.info("Cached locale:"+mapSection.get(section).containsKey(localeCode));
			
			if(mapSection.get(section).containsKey(localeCode)) {return mapSection.get(section).get(localeCode);}
			else
			{
				section = fCms.find(fbCms.getClassSection(),section);
				if(debugOnInfo) {logger.info("Not in cache, Trrancoding Section: " + section.toString());}
				Section ofxSection = null;
				try
				{
					LOC locale = fCms.fByCode(fbCms.getClassLocale(), localeCode);
					GenericLocaleProvider<L,D,LOC> lp = new GenericLocaleProvider<L,D,LOC>();
					lp.setLocales(Arrays.asList(locale));
					
					ofxSection = ofx.build(lp,localeCode,section);
					if(debugOnInfo)
					{
						logger.info(StringUtil.stars());
						if(section.getName().containsKey("en")) {logger.info(section.getName().get("en").getLang());}
						
						logger.info(StringUtil.stars());
						JaxbUtil.info(ofxSection);
					}
					mapSection.get(section).put(localeCode, ofxSection);
				}
				catch (OfxAuthoringException e){e.printStackTrace();}
				catch (JeeslNotFoundException e) {e.printStackTrace();}
				return ofxSection;
			}		
		}
	}
}