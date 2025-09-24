package org.jeesl.factory.ejb.module.ts;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTsTransactionFactory<TRANSACTION extends JeeslTsTransaction<SOURCE,?,USER,?>,
								SOURCE extends EjbWithLangDescription<?,?>, 
								USER extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTsTransactionFactory.class);
	
	final Class<TRANSACTION> cTransaction;
    
	public EjbTsTransactionFactory(final Class<TRANSACTION> cTransaction)
	{       
        this.cTransaction=cTransaction;
	}
	
	public TRANSACTION build(USER user, SOURCE source)
	{
		TRANSACTION ejb = null;
		try
		{
			ejb = cTransaction.getDeclaredConstructor().newInstance();
			ejb.setUser(user);
			ejb.setRecord(new Date());
			ejb.setSource(source);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
		return ejb;
	}
	
	public TRANSACTION build(USER user, SOURCE source, String reference)
	{
		TRANSACTION ejb = this.build(user, source);
		ejb.setSource(source);
		ejb.setReference(reference);
		return ejb;
	}
}