package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsContentFactory;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsElementFactory;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsFactory;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsMarkupFactory;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsSectionFactory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsCategory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElementType;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsVisiblity;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoCmsFactoryBuilder<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								CAT extends JeeslIoCmsCategory<L,D,CAT,?>,
								CMS extends JeeslIoCms<L,D,LOC,CAT,S>,
								V extends JeeslIoCmsVisiblity,
								S extends JeeslIoCmsSection<L,S>,
								E extends JeeslIoCmsElement<V,S,EC,ET,C,FC>,
								EC extends JeeslStatus<L,D,EC>,
								ET extends JeeslIoCmsElementType<L,D,ET,?>,
								C extends JeeslIoCmsContent<V,E,MT>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<L,D,MT,?>,
								FC extends JeeslFileContainer<?,FM>,
								FM extends JeeslFileMeta<D,FC,?,?>
								>
				extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoCmsFactoryBuilder.class);

	private final Class<LOC> cLoc; public Class<LOC> getClassLocale() {return cLoc;}
	private final Class<CAT> cCategory; public Class<CAT> getClassCategory() {return cCategory;}
	private final Class<CMS> cCms; public Class<CMS> getClassCms() {return cCms;}
	private final Class<S> cSection; public Class<S> getClassSection() {return cSection;}
	private final Class<E> cElement; public Class<E> getClassElement() {return cElement;}
	private final Class<EC> cElementCategory; public Class<EC> getClassElementCategory() {return cElementCategory;}
	private final Class<ET> cElementType; public Class<ET> getClassElementType() {return cElementType;}
	private final Class<C> cContent; public Class<C> getClassContent() {return cContent;}
	private final Class<M> cMarkup; public Class<M> getClassMarkup() {return cMarkup;}
	private final Class<MT> cMarkupType; public Class<MT> getClassMarkupType() {return cMarkupType;}
	private final Class<FM> cFileMeta; public Class<FM> getClassFileMeta() {return cFileMeta;}
    
	public IoCmsFactoryBuilder(final Class<L> cL, final Class<D> cD,final Class<LOC> cLoc,
				final Class<CAT> cCategory, final Class<CMS> cCms,
				final Class<S> cSection,
				final Class<E> cElement,final Class<EC> cElementCategory,final Class<ET> cElementType,
				final Class<C> cContent,
				final Class<M> cMarkup,
				final Class<MT> cMarkupType,
				final Class<FM> cFileMeta)
	{
		super(cL,cD);
		this.cLoc=cLoc;
		this.cCategory=cCategory;
		this.cCms=cCms;
		this.cSection=cSection;
		this.cElement=cElement;
		this.cElementCategory=cElementCategory;
		this.cElementType=cElementType;
		this.cContent=cContent;
		
		this.cMarkup=cMarkup;
		this.cMarkupType=cMarkupType;
		this.cFileMeta=cFileMeta;
	}
	
	public EjbIoCmsFactory<L,D,CAT,CMS,V,S,C,MT,LOC> ejbCms() {return new EjbIoCmsFactory<>(cCms);}
	public EjbIoCmsSectionFactory<L,S,FM> ejbSection() {return new EjbIoCmsSectionFactory<>(cSection);}
	public EjbIoCmsElementFactory<L,S,E> ejbElement() {return new EjbIoCmsElementFactory<>(cElement);}
	public EjbIoCmsContentFactory<LOC,E,C,MT> ejbContent() {return new EjbIoCmsContentFactory<>(cContent);}
	public EjbIoCmsMarkupFactory<M,MT> ejbMarkup() {return new EjbIoCmsMarkupFactory<>(cMarkup);}
}