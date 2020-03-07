package org.jeesl.factory.ejb.system.io.db;

import java.util.Date;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.io.db.JeeslDbDump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDumpFactory<SYSTEM extends JeeslIoSsiSystem<?,?>,
								DUMP extends JeeslDbDump<SYSTEM,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDumpFactory.class);
	
	private final Class<DUMP> cDumpFile;
    
	public EjbIoDumpFactory(final Class<DUMP> cDumpFile)
	{       
        this.cDumpFile = cDumpFile;
	}
	
	public DUMP build(SYSTEM system, net.sf.exlp.xml.io.File file)
	{
		return build(system,file.getName(),file.getSize(),file.getLastModifed().toGregorianCalendar().getTime());
    }
	
	public DUMP build(SYSTEM system, String name, long size, Date record)
	{
		DUMP ejb = null;
		try
		{
			 ejb = cDumpFile.newInstance();
			 ejb.setSystem(system);
			 ejb.setName(name);
			 ejb.setSize(size);
			 ejb.setRecord(record);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}