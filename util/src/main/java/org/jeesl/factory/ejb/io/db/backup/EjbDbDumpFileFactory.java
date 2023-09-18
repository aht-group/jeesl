package org.jeesl.factory.ejb.io.db.backup;

import org.jeesl.interfaces.model.io.db.dump.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbDbDumpFileFactory<DUMP extends JeeslDbDump<?,FILE>,
								FILE extends JeeslDbDumpFile<DUMP,HOST,STATUS>,
								HOST extends JeeslIoSsiHost<?,?,?>,
								STATUS extends JeeslDbDumpStatus<?,?,STATUS,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbDbDumpFileFactory.class);
	
	private final Class<FILE> cFile;
    
	public EjbDbDumpFileFactory(final Class<FILE> cFile)
	{       
        this.cFile = cFile;
	}
	
	public FILE build(DUMP dump, HOST host, STATUS status)
	{
		FILE ejb = null;
		try
		{
			 ejb = cFile.newInstance();
			 ejb.setDump(dump);
			 ejb.setHost(host);
			 ejb.setStatus(status);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}