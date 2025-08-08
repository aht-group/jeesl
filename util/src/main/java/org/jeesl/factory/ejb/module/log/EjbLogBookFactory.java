package org.jeesl.factory.ejb.module.log;

import org.jeesl.interfaces.model.module.journal.JeeslJournalDomain;
import org.jeesl.interfaces.model.module.journal.JeeslJournalBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbLogBookFactory<LOG extends JeeslJournalBook<SCOPE,?>,
							SCOPE extends JeeslJournalDomain<?,?,SCOPE,?>
>
{
	final static Logger logger = LoggerFactory.getLogger(EjbLogBookFactory.class);
	
	private final Class<LOG> cLog;
    
	public EjbLogBookFactory(final Class<LOG> cLog)
	{  
        this.cLog = cLog;
	}
	    
	public LOG build(SCOPE scope)
	{
		LOG ejb = null;
		try
		{
			ejb = cLog.newInstance();
			ejb.setScope(scope);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}