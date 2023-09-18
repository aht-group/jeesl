package org.jeesl.factory.builder.io.db;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.db.backup.EjbDbDumpFileFactory;
import org.jeesl.factory.ejb.io.db.backup.EjbIoDumpFactory;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbDumpFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								DUMP extends JeeslDbDump<SYSTEM,DF>,
								DF extends JeeslDbDumpFile<DUMP,DH,DS>,
								DH extends JeeslIoSsiHost<L,D,?>,
								DS extends JeeslDbDumpStatus<L,D,DS,?>
>
			extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoDbDumpFactoryBuilder.class);
	
	private final Class<DUMP> cDump; public Class<DUMP> getClassDump(){return cDump;}
	private final Class<DF> cFile; public Class<DF> getClassDumpFile(){return cFile;}
	private final Class<DH> cHost; public Class<DH> getClassDumpHost(){return cHost;}
	private final Class<DS> cStatus; public Class<DS> getClassDumpStatus(){return cStatus;}
	
	public IoDbDumpFactoryBuilder(final Class<L> cL, final Class<D> cD,
							final Class<DUMP> cDump, final Class<DF> cFile, final Class<DH> cHost, final Class<DS> cStatus)
	{
		super(cL,cD);
		
		this.cDump = cDump;
		this.cFile=cFile;
		this.cHost=cHost;
		this.cStatus=cStatus;		
	}
	
	public EjbIoDumpFactory<SYSTEM,DUMP> dump(){return new EjbIoDumpFactory<>(cDump);}
	public EjbDbDumpFileFactory<DUMP,DF,DH,DS> dumpFile(){return new EjbDbDumpFileFactory<>(cFile);}
}