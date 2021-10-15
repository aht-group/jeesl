package org.jeesl.factory.ejb.module.tafu;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.jeesl.factory.builder.module.TafuFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuScope;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuStatus;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTaskFactory<R extends JeeslTenantRealm<?,?,R,?>, 
							T extends JeeslTafuTask<R,TS,SC,M>,
							TS extends JeeslTafuStatus<?,?,TS,?>,
							SC extends JeeslTafuScope<?,?,R,SC,?>,
							M extends JeeslMarkup<MT>,
							MT extends JeeslIoCmsMarkupType<?,?,MT,?>
							>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTaskFactory.class);
	
	private final TafuFactoryBuilder<?,?,R,T,TS,SC,?,?,M,MT> fbTafu;
	
    public EjbTaskFactory(TafuFactoryBuilder<?,?,R,T,TS,SC,?,?,M,MT> fbTafu)
    {
        this.fbTafu = fbTafu;
    } 
	
    public <RREF extends EjbWithId> T build(R realm, RREF rref, MT type)
    {
		try
		{
			T t = fbTafu.getClassTask().newInstance();
			t.setRealm(realm);
			t.setRref(rref.getId());
			
			t.setRecordCreated(LocalDateTime.now());
			t.setRecordUpdated(LocalDateTime.now());
			t.setRecordShow(LocalDate.now());
			
			t.setMarkup(fbTafu.ejbMarkup().build(type));
			
		    return t;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		throw new RuntimeException("x");
    }
    
    public void converter(JeeslFacade facade, T task)
    {
    	if(task.getStatus()!=null) {task.setStatus(facade.find(fbTafu.getClassStatus(),task.getStatus()));}
    	if(task.getScope()!=null) {task.setScope(facade.find(fbTafu.getClassScope(),task.getScope()));}
    }
    
    public void preSave(JeeslFacade facade, T task)
    {
    	if(task.getName()==null || task.getName().trim().isEmpty()) {task.setName("---");}
    	task.setRecordUpdated(LocalDateTime.now());
    	if(EjbIdFactory.isSaved(task))
    	{
    		 T previous = facade.find(fbTafu.getClassTask(),task);
    		 
    		 boolean thisResolved = task.getStatus().getCode().equals(JeeslTafuStatus.Code.resolved.toString());
    		 boolean thisClosed = task.getStatus().getCode().equals(JeeslTafuStatus.Code.closed.toString());
    		 boolean beforeUnresolved = !previous.getStatus().getCode().equals(JeeslTafuStatus.Code.resolved.toString());
    		 
    		 if(thisResolved && beforeUnresolved) {task.setRecordResolved(task.getRecordUpdated());}
    		 
    		 if(!(thisResolved || thisClosed)) {task.setRecordResolved(null);}
    	}
    }
}