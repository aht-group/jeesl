package org.jeesl.factory.ofx;

import java.util.List;

import org.jeesl.api.facade.io.JeeslIoCmsFacade;
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
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionsFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.model.xml.core.ofx.Sections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOfxCmsFactory <L extends JeeslLang, D extends JeeslDescription,
								CAT extends JeeslIoCmsCategory<L,D,CAT,?>,
								CMS extends JeeslIoCms<L,D,LOC,CAT,S>,
								V extends JeeslIoCmsVisiblity,
								S extends JeeslIoCmsSection<L,S>,
								E extends JeeslIoCmsElement<V,S,EC,ET,C,FC>,
								EC extends JeeslStatus<L,D,EC>,
								ET extends JeeslIoCmsElementType<L,D,ET,?>,
								C extends JeeslIoCmsContent<V,E,MT>,
								MT extends JeeslIoMarkupType<L,D,MT,?>,
								FC extends JeeslFileContainer<?,?>,
								FM extends JeeslFileMeta<D,FC,?,?>,
								LOC extends JeeslLocale<L,D,LOC,?>>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractOfxCmsFactory.class);
	
	protected final String localeCode;
	protected final JeeslIoCmsFacade<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,?,MT,FC,FM> fCms;
	
	public AbstractOfxCmsFactory(String localeCode, JeeslIoCmsFacade<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,?,MT,FC,FM> fCms)
	{
		this.localeCode=localeCode;
		this.fCms = fCms;
	}
	
	public Sections build(CMS cms)
	{
		logger.info("Rendering "+cms.toString());
		S root = fCms.load(cms.getRoot(),true);
		
		Sections xml = XmlSectionsFactory.build();
		for(S section : root.getSections())
		{
			if(section.isVisible())
			{
				xml.getContent().add(build(section));
			}
		}
		return xml;
	}
 
	private Section build(S section)
	{
		Section xml = XmlSectionFactory.build();
		xml.getContent().add(XmlTitleFactory.build(section.getName().get(localeCode).getLang()));
		
		List<E> elements = fCms.fCmsElements(section);
		for(E e : elements)
		{
			xml.getContent().add(build(e));
		}
		
		for(S child : section.getSections())
		{
			if(child.isVisible())
			{
				xml.getContent().add(this.build(child));
			}
		}
		
		return xml;
	}
	
	protected abstract Section build(E element);
}