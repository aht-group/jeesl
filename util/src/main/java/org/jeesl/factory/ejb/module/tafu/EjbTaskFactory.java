package org.jeesl.factory.ejb.module.tafu;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTaskFactory<R extends JeeslTenantRealm<?,?,R,?>, 
							T extends JeeslTafuTask<R,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTaskFactory.class);
	
	private final Class<T> cTask;
	
    public EjbTaskFactory(final Class<T> cTask)
    {
        this.cTask = cTask;
    } 
	
    public <RREF extends EjbWithId> T build(R realm, RREF rref)
    {
		try
		{
			T t = cTask.newInstance();
			t.setRealm(realm);
			t.setRref(rref.getId());
			
			t.setRecordCreated(LocalDateTime.now());
			t.setRecordUpdated(LocalDateTime.now());
			t.setRecordShow(LocalDate.now());	
			
		    return t;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		throw new RuntimeException("x");
    }
}