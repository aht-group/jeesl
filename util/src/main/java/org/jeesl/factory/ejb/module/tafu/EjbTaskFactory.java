package org.jeesl.factory.ejb.module.tafu;

import java.util.Date;

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
			
			t.setRecordCreated(new Date());
			t.setRecordUpdated(new Date());
			t.setRecordShow(new Date());	
			
		    return t;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		throw new RuntimeException("x");
    }
}