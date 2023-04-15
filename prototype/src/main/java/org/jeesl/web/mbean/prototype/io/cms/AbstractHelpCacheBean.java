package org.jeesl.web.mbean.prototype.io.cms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jeesl.api.bean.cache.JeeslHelpCacheBean;
import org.jeesl.api.facade.io.JeeslIoCmsFacade;
import org.jeesl.controller.provider.GenericLocaleProvider;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoCmsFactoryBuilder;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.controller.JeeslCmsRenderer;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsCategory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElementType;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsVisiblity;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineHelp;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.openfuxml.content.ofx.Section;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHelpCacheBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										VIEW extends JeeslSecurityView<L,D,?,?,?,?>,
										CTX extends JeeslSecurityContext<L,D>,
										M extends JeeslSecurityMenu<L,VIEW,CTX,M>,
										OH extends JeeslSecurityOnlineHelp<VIEW,CMS,S>,
										CAT extends JeeslIoCmsCategory<L,D,CAT,?>,
										CMS extends JeeslIoCms<L,D,LOC,CAT,S>,
										V extends JeeslIoCmsVisiblity,
										S extends JeeslIoCmsSection<L,S>,
										E extends JeeslIoCmsElement<V,S,EC,ET,C,FC>,
										EC extends JeeslStatus<L,D,EC>,
										ET extends JeeslIoCmsElementType<L,D,ET,?>,
										C extends JeeslIoCmsContent<V,E,MT>,
										MT extends JeeslIoMarkupType<L,D,MT,?>,
										FC extends JeeslFileContainer<?,FM>,
										FM extends JeeslFileMeta<D,FC,?,?>
										>
					implements JeeslHelpCacheBean<VIEW>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractHelpCacheBean.class);
	
	private final IoCmsFactoryBuilder<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC,FM> fbCms;
	private final SecurityFactoryBuilder<L,D,?,?,VIEW,?,?,?,?,M,?,?,OH,CMS,S,?> fbSecurity;
	
	private JeeslCmsRenderer<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC> ofx;
	private JeeslIoCmsFacade<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,?,MT,FC,FM> fCms;
	
	private final Map<VIEW,Map<String,Section>> mapSection;
	
	protected boolean activeOnlineHelp; public boolean isActiveOnlineHelp() {return activeOnlineHelp;}

	private boolean debugOnInfo; protected void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	public AbstractHelpCacheBean(IoCmsFactoryBuilder<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC,FM> fbCms,
								SecurityFactoryBuilder<L,D,?,?,VIEW,?,?,?,?,M,?,?,OH,CMS,S,?> fbSecurity)
	{
		this.fbCms=fbCms;
		this.fbSecurity=fbSecurity;
		
		activeOnlineHelp = true;
		mapSection = new HashMap<>();
	}
	
	protected void postConstructCms(JeeslIoCmsFacade<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,?,MT,FC,FM> fCms,
									JeeslCmsRenderer<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC> ofx)
	{
		this.fCms=fCms;
		this.ofx=ofx;
	}
	
	@Override public void clearCache(VIEW view)
	{
		logger.info("Invalidate Cache "+view.toString());
		if(mapSection.containsKey(view)) {mapSection.remove(view);}
	}
	
	public Section cache(String localeCode, VIEW view) throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info("Requesting "+localeCode+" "+view.toString());}
		if(!mapSection.containsKey(view)) {mapSection.put(view, new HashMap<String,Section>());}
		
		if(mapSection.get(view).containsKey(localeCode)) {return mapSection.get(view).get(localeCode);}
		else
		{
			Section section = XmlSectionFactory.build();
			
			for(OH help : fCms.allForParent(fbSecurity.getClassOnlineHelp(),view))
			{
				try
				{
					LOC locale = fCms.fByCode(fbCms.getClassLocale(), localeCode);
					GenericLocaleProvider<LOC> lp = new GenericLocaleProvider<>();
					lp.setLocales(Arrays.asList(locale));
					
					S eSection = help.getSection();
					Section child = ofx.build(lp,localeCode,eSection);
					
					section.getContent().add(child);
				}
				catch (OfxAuthoringException e){e.printStackTrace();}
				catch (JeeslNotFoundException e) {e.printStackTrace();}
			}
			
			mapSection.get(view).put(localeCode, section);
			
			return section;
		}		
	}
}