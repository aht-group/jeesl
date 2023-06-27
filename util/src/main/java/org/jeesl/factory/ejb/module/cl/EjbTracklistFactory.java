package org.jeesl.factory.ejb.module.cl;

import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.cl.JeeslClCategory;
import org.jeesl.interfaces.model.module.cl.JeeslClTracklist;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTracklistFactory<R extends JeeslTenantRealm<?,?,R,?>,
								CAT extends JeeslClCategory<?,?,R,CAT,?>,
								TL extends JeeslClTracklist<?,R>
							>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTracklistFactory.class);
	
	private final ChecklistFactoryBuilder<?,?,R,CAT,?,?,TL,?,?> fbCl;
	
    public EjbTracklistFactory(ChecklistFactoryBuilder<?,?,R,CAT,?,?,TL,?,?> fbCl)
    {
        this.fbCl = fbCl;
    } 
	
    public <RREF extends EjbWithId> TL build(R realm, RREF rref)
    {
		try
		{
			TL ejb = fbCl.getClassTracklist().newInstance();
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
			
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		throw new RuntimeException("x");
    }
    
    public void converter(JeeslFacade facade, TL ejb)
    {
//    	if(Objects.nonNull(ejb.getTopic())) {ejb.setTopic(facade.find(fbCl.getClassTopic(),ejb.getTopic()));}
//    	if(task.getScope()!=null) {task.setScope(facade.find(fbTafu.getClassScope(),task.getScope()));}
    }
}