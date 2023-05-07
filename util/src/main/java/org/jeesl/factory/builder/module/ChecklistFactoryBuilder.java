package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.cl.EjbChecklistFactory;
import org.jeesl.interfaces.model.module.cl.JeeslChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslChecklistTopic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChecklistFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								CL extends JeeslChecklist<L,R,TO>,
								TO extends JeeslChecklistTopic<L,?,R,TO,?>>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(ChecklistFactoryBuilder.class);
	
	private final Class<R> cRealm; public Class<R> getClassRealm() {return cRealm;}
	
	private final Class<CL> cChecklist; public Class<CL> getClassChecklist() {return cChecklist;}
	
	public ChecklistFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<R> cRealm,
								final Class<CL> cChecklist,
								final Class<TO> cTopic
								)
	{       
		super(cL,cD);
		this.cRealm = cRealm;
		this.cChecklist = cChecklist;
	}
	
	public EjbChecklistFactory<R,CL> ejbChecklist() {return new EjbChecklistFactory<>(this);}
//	public EjbTaskFactory<R,T,TS,SC,M,MT> ejbTask() {return new EjbTaskFactory<>(this);}
//	public EjbItsFactory<R,I,IS> ejbIssue() {return new EjbItsFactory<>(this);}
}