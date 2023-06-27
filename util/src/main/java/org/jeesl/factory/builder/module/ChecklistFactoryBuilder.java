package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.cl.EjbChecklistFactory;
import org.jeesl.factory.ejb.module.cl.EjbChecklistItemFactory;
import org.jeesl.factory.ejb.module.cl.EjbTracklistFactory;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.module.cl.JeeslClChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslClCategory;
import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.jeesl.interfaces.model.module.cl.JeeslClTracklist;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChecklistFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								CAT extends JeeslClCategory<L,?,R,CAT,?>,
								CL extends JeeslClChecklist<L,R,CAT>,
								
								CI extends JeeslClCheckItem<L,CL,M>,
								TL extends JeeslClTracklist<L,R>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<L,D,MT,?>>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(ChecklistFactoryBuilder.class);
	
	private final Class<R> cRealm; public Class<R> getClassRealm() {return cRealm;}
	
	private final Class<CL> cChecklist; public Class<CL> getClassChecklist() {return cChecklist;}
	private final Class<CAT> cTopic; public Class<CAT> getClassTopic() {return cTopic;}
	private final Class<CI> cChecklistItem; public Class<CI> getClassChecklistItem() {return cChecklistItem;}
	private final Class<TL> cTracklist; public Class<TL> getClassTracklist() {return cTracklist;}
	private final Class<M> cMarkup; public Class<M> getClassMarkup() {return cMarkup;}
	private final Class<MT> cMarkupType; public Class<MT> getClassMarkupType() {return cMarkupType;}
	
	public ChecklistFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<R> cRealm,
								final Class<CL> cChecklist,
								final Class<CAT> cTopic,
								final Class<CI> cChecklistItem,
								final Class<TL> cTracklist,
								final Class<M> cMarkup,
								final Class<MT> cMarkupType
								)
	{       
		super(cL,cD);
		this.cRealm = cRealm;
		this.cChecklist = cChecklist;
		this.cTopic = cTopic;
		this.cChecklistItem = cChecklistItem;
		this.cTracklist = cTracklist;
		this.cMarkup = cMarkup;
		this.cMarkupType = cMarkupType;
	}
	
	public EjbChecklistFactory<R,CAT,CL> ejbChecklist() {return new EjbChecklistFactory<>(this);}
	public EjbChecklistItemFactory<CL,CI,M,MT> ejbChecklistItem() {return new EjbChecklistItemFactory<>(this);}

	public EjbTracklistFactory<R,CAT,TL> ejbTracklist() {return new EjbTracklistFactory<>(this);}
}