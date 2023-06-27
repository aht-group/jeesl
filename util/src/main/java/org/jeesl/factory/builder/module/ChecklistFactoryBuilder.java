package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.cl.EjbCheckItemFactory;
import org.jeesl.factory.ejb.module.cl.EjbChecklistFactory2;
import org.jeesl.factory.ejb.module.cl.EjbTrackItemFactory;
import org.jeesl.factory.ejb.module.cl.EjbTracklistFactory2;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.module.cl.JeeslClCategory;
import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.jeesl.interfaces.model.module.cl.JeeslClChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslClTrackItem;
import org.jeesl.interfaces.model.module.cl.JeeslClTrackStatus;
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
								TL extends JeeslClTracklist<L,R,CL>,
								TI extends JeeslClTrackItem<CI,TL,?>,
								TS extends JeeslClTrackStatus<L,D,TS,?>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<L,D,MT,?>>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(ChecklistFactoryBuilder.class);
	
	private final Class<R> cRealm; public Class<R> getClassRealm() {return cRealm;}
	
	private final Class<CAT> cTopic; public Class<CAT> getClassTopic() {return cTopic;}
	
	private final Class<CL> cCheckList; public Class<CL> getClassCheckList() {return cCheckList;}
	private final Class<CI> cCheckItem; public Class<CI> getClassCheckItem() {return cCheckItem;}
	
	
	private final Class<TL> cTrackList; public Class<TL> getClassTrackList() {return cTrackList;}
	private final Class<TI> cTrackItem; public Class<TI> getClassTrackItem() {return cTrackItem;}
//	TS extends JeeslClTrackStatus<L,D,TS,?>
	
	private final Class<M> cMarkup; public Class<M> getClassMarkup() {return cMarkup;}
	private final Class<MT> cMarkupType; public Class<MT> getClassMarkupType() {return cMarkupType;}
	
	public ChecklistFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<R> cRealm,
								final Class<CL> cChecklist,
								final Class<CAT> cTopic,
								final Class<CI> cChecklistItem,
								final Class<TL> cTracklist,
								final Class<TI> cTrackItem,
								final Class<TS> cTrackStatus,
								final Class<M> cMarkup,
								final Class<MT> cMarkupType
								)
	{       
		super(cL,cD);
		this.cRealm = cRealm;
		this.cCheckList = cChecklist;
		this.cTopic = cTopic;
		this.cCheckItem = cChecklistItem;
		this.cTrackList = cTracklist;
		this.cTrackItem = cTrackItem;
		this.cMarkup = cMarkup;
		this.cMarkupType = cMarkupType;
	}
	
	public EjbChecklistFactory2<R,CAT,CL> ejbChecklist() {return new EjbChecklistFactory2<>(this);}
	public EjbCheckItemFactory<CL,CI,M,MT> ejbChecklistItem() {return new EjbCheckItemFactory<>(this);}

	public EjbTracklistFactory2<R,CAT,TL> ejbTrackList() {return new EjbTracklistFactory2<>(this);}
	public EjbTrackItemFactory<CI,TL,TI> ejbTrackItem() {return new EjbTrackItemFactory<>(this);}
}