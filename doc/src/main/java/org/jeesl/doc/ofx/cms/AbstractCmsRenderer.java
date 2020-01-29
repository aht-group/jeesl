package org.jeesl.doc.ofx.cms;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoCmsFacade;
import org.jeesl.doc.ofx.cms.jeesl.JeeslCmsImageFactory;
import org.jeesl.doc.ofx.cms.jeesl.JeeslCmsParagraphFactory;
import org.jeesl.doc.ofx.cms.module.workflow.OfxSectionWorkflow;
import org.jeesl.doc.ofx.cms.system.status.JeeslCmsStatusListFactory;
import org.jeesl.doc.ofx.cms.system.status.JeeslCmsStatusTableFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsCategory;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.system.io.cms.JeeslIoCmsVisiblity;
import org.jeesl.interfaces.model.system.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.system.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslLocaleProvider;
import org.openfuxml.content.ofx.Section;
import org.openfuxml.content.ofx.Sections;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionsFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.interfaces.configuration.OfxTranslationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public abstract class AbstractCmsRenderer <L extends UtilsLang, D extends UtilsDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								CAT extends JeeslIoCmsCategory<L,D,CAT,?>,
								CMS extends JeeslIoCms<L,D,CAT,S,LOC>,
								V extends JeeslIoCmsVisiblity,
								S extends JeeslIoCmsSection<L,S>,
								E extends JeeslIoCmsElement<V,S,EC,ET,C,FC>,
								EC extends UtilsStatus<EC,L,D>,
								ET extends UtilsStatus<ET,L,D>,
								C extends JeeslIoCmsContent<V,E,MT>,
								MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
								FS extends JeeslFileStorage<L,D,?,?>,
								FC extends JeeslFileContainer<FS,?>,
								FM extends JeeslFileMeta<D,FC,?,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractCmsRenderer.class);
	
	protected final JeeslIoCmsFacade<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC,FM> fCms;
	protected final OfxTranslationProvider tp;
	
	private final JeeslCmsParagraphFactory<E,C> ofParagraph;
	private final JeeslCmsStatusTableFactory<E,C> ofTableStatus;
	private final JeeslCmsStatusListFactory<E,C> ofxListStatus;
	private final JeeslCmsImageFactory<E,C,FS,FC,FM> ofImage;
	private final OfxSectionWorkflow<L,LOC,E> ofxWorkflow;
	
	public AbstractCmsRenderer(OfxTranslationProvider tp, JeeslIoCmsFacade<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,MT,FC,FM> fCms, JeeslFileRepositoryHandler<FS,FC,FM> frh)
	{
		this.tp=tp;
		this.fCms = fCms;
		
		ofParagraph = new JeeslCmsParagraphFactory<E,C>();
		ofTableStatus = new JeeslCmsStatusTableFactory<E,C>();
		ofxListStatus = new JeeslCmsStatusListFactory<E,C>();
		ofImage = new JeeslCmsImageFactory<E,C,FS,FC,FM>(frh);
		ofxWorkflow = new OfxSectionWorkflow<>(tp);
	}
	
	public Sections build(JeeslLocaleProvider<LOC> lp, String localeCode, CMS cms) throws OfxAuthoringException
	{
		logger.info("Rendering "+cms.toString());
		S root = fCms.load(cms.getRoot(),true);
		
		Sections xml = XmlSectionsFactory.build();
		for(S section : root.getSections())
		{
			if(section.isVisible())
			{
				xml.getContent().add(buildSection(lp,localeCode, section));
			}
		}
		return xml;
	}
	
	public Section build(JeeslLocaleProvider<LOC> lp, String localeCode, S section) throws OfxAuthoringException
	{
		S root = fCms.load(section,true);
		return buildSection(lp,localeCode, root);
	}
 
	private Section buildSection(JeeslLocaleProvider<LOC> lp, String localeCode, S section) throws OfxAuthoringException
	{
		Section xml = XmlSectionFactory.build();
		xml.getContent().add(XmlTitleFactory.build(section.getName().get(localeCode).getLang()));
		
		List<E> elements = fCms.fCmsElements(section);
		for(E e : elements)
		{
			build(lp, localeCode,xml.getContent(),e);
//			xml.getContent().add(build(e));
		}
		
		for(S child : section.getSections())
		{
			if(child.isVisible())
			{
				xml.getContent().add(this.build(lp,localeCode,child));
			}
		}
		
		return xml;
	}
	
	protected abstract void build(JeeslLocaleProvider<LOC> lp, String localeCode, List<Serializable> list, E element) throws OfxAuthoringException;
	
	//Here we are handling all types which are available as generic renderer in JEESL 
	protected void buildJeesl(JeeslLocaleProvider<LOC> lp, String localeCode, List<Serializable> list, E element) throws OfxAuthoringException
	{
		if(element.getType().getCode().equals(JeeslIoCmsElement.Type.paragraph.toString())) {list.addAll(ofParagraph.build(localeCode,element).getContent());}
		else if(element.getType().getCode().equals(JeeslIoCmsElement.Type.image.toString())) {list.add(ofImage.build(localeCode,element));}
		else if(element.getType().getCode().equals(JeeslIoCmsElement.Type.sysStatusTable.toString())) {list.add(ofTableStatus.build(localeCode,element));}
		else if(element.getType().getCode().equals(JeeslIoCmsElement.Type.sysStatusList.toString())) {list.add(ofxListStatus.build(localeCode,element));}
		else if(element.getType().getCode().equals(JeeslIoCmsElement.Type.wfProcess.toString())) {list.add(ofxWorkflow.build(lp,element));}
		
		else {logger.warn("Unhandled "+element.getType().getCode());}
	}
}