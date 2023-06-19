package org.jeesl.factory.ejb.module.cl;

import java.util.List;
import java.util.Objects;

import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.cl.JeeslChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslChecklistTopic;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbChecklistFactory<R extends JeeslTenantRealm<?,?,R,?>, 
								CL extends JeeslChecklist<?,R,TO>,
								TO extends JeeslChecklistTopic<?,?,R,TO,?>
							>
{
	final static Logger logger = LoggerFactory.getLogger(EjbChecklistFactory.class);
	
	private final ChecklistFactoryBuilder<?,?,R,CL,TO,?,?,?,?> fbCl;
	
    public EjbChecklistFactory(ChecklistFactoryBuilder<?,?,R,CL,TO,?,?,?,?> fbCl)
    {
        this.fbCl = fbCl;
    } 
	
    public <RREF extends EjbWithId> CL build(R realm, RREF rref, List<CL> list)
    {
		try
		{
			CL ejb = fbCl.getClassChecklist().newInstance();
			EjbPositionFactory.next(ejb,list);
			
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
			
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		throw new RuntimeException("x");
    }
    
    public void converter(JeeslFacade facade, CL ejb)
    {
    	if(Objects.nonNull(ejb.getTopic())) {ejb.setTopic(facade.find(fbCl.getClassTopic(),ejb.getTopic()));}
//    	if(task.getScope()!=null) {task.setScope(facade.find(fbTafu.getClassScope(),task.getScope()));}
    }
}