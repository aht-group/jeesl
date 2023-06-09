package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.cl.EjbChecklistFactory;
import org.jeesl.factory.ejb.module.cl.EjbChecklistItemFactory;
import org.jeesl.interfaces.model.module.cl.JeeslChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslChecklistItem;
import org.jeesl.interfaces.model.module.cl.JeeslChecklistTopic;
import org.jeesl.interfaces.model.module.cl.JeeslClTracklist;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChecklistFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								CL extends JeeslChecklist<L,R,TO>,
								TO extends JeeslChecklistTopic<L,?,R,TO,?>,
								CI extends JeeslChecklistItem<L,CL>,
								TL extends JeeslClTracklist<L,R>>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(ChecklistFactoryBuilder.class);
	
	private final Class<R> cRealm; public Class<R> getClassRealm() {return cRealm;}
	
	private final Class<CL> cChecklist; public Class<CL> getClassChecklist() {return cChecklist;}
	private final Class<CI> cChecklistItem; public Class<CI> getClassChecklistItem() {return cChecklistItem;}
	private final Class<TL> cTracklist; public Class<TL> getClassTracklist() {return cTracklist;}
	
	public ChecklistFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<R> cRealm,
								final Class<CL> cChecklist,
								final Class<TO> cTopic,
								final Class<CI> cChecklistItem,
								final Class<TL> cTracklist
								)
	{       
		super(cL,cD);
		this.cRealm = cRealm;
		this.cChecklist = cChecklist;
		this.cChecklistItem = cChecklistItem;
		this.cTracklist = cTracklist;
	}
	
	public EjbChecklistFactory<R,CL> ejbChecklist() {return new EjbChecklistFactory<>(this);}
	public EjbChecklistItemFactory<CL,CI> ejbChecklistItem() {return new EjbChecklistItemFactory<>(this);}
//	public EjbItsFactory<R,I,IS> ejbIssue() {return new EjbItsFactory<>(this);}
}