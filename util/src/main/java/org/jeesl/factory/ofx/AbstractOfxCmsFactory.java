package org.jeesl.factory.ofx;

import java.util.List;

import org.jeesl.api.facade.io.JeeslIoCmsFacade;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsCategory;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsVisiblity;
import org.jeesl.interfaces.model.system.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.io.fr.JeeslFileMeta;
import org.openfuxml.content.ofx.Section;
import org.openfuxml.content.ofx.Sections;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionsFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public abstract class AbstractOfxCmsFactory <L extends UtilsLang,D extends UtilsDescription,
								CAT extends JeeslIoCmsCategory<L,D,CAT,?>,
								CMS extends JeeslIoCms<L,D,CAT,S,LOC>,
								V extends JeeslIoCmsVisiblity,
								S extends JeeslIoCmsSection<L,S>,
								E extends JeeslIoCmsElement<V,S,EC,ET,C,FC>,
								EC extends UtilsStatus<EC,L,D>,
								ET extends UtilsStatus<ET,L,D>,
								C extends JeeslIoCmsContent<V,E,MT>,
								MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
								FC extends JeeslFileContainer<?,?>,
								FM extends JeeslFileMeta<D,FC,?,?>,
								LOC extends UtilsStatus<LOC,L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractOfxCmsFactory.class);
	
	protected final String localeCode;
	protected final JeeslIoCmsFacade<L,D,CAT,CMS,V,S,E,EC,ET,C,MT,FC,FM,LOC> fCms;
	
	public AbstractOfxCmsFactory(String localeCode, JeeslIoCmsFacade<L,D,CAT,CMS,V,S,E,EC,ET,C,MT,FC,FM,LOC> fCms)
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