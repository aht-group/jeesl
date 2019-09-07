package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.system.io.db.EjbDbDumpFileFactory;
import org.jeesl.factory.ejb.system.io.db.EjbIoDumpFactory;
import org.jeesl.interfaces.model.system.io.db.JeeslDbConnectionColumn;
import org.jeesl.interfaces.model.system.io.db.JeeslDbDump;
import org.jeesl.interfaces.model.system.io.db.JeeslDbDumpFile;
import org.jeesl.interfaces.model.system.io.db.JeeslDbDumpHost;
import org.jeesl.interfaces.model.system.io.db.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.system.io.db.JeeslDbReplicationColumn;
import org.jeesl.interfaces.model.system.io.db.JeeslDbReplicationState;
import org.jeesl.interfaces.model.system.io.db.JeeslDbReplicationSync;
import org.jeesl.interfaces.model.system.io.ssi.JeeslIoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;

public class IoDbFactoryBuilder<L extends UtilsLang,D extends UtilsDescription,
								SYSTEM extends JeeslIoSsiSystem,
								DUMP extends JeeslDbDump<SYSTEM,DF>,
								DF extends JeeslDbDumpFile<DUMP,DH,DS>,
								DH extends JeeslDbDumpHost<L,D,DH,?>,
								DS extends JeeslDbDumpStatus<L,D,DS,?>,
								
								CC extends JeeslDbConnectionColumn<L,D,CC,?>,

								RC extends JeeslDbReplicationColumn<L,D,RC,?>,
								RS extends JeeslDbReplicationState<L,D,RS,?>,
								RY extends JeeslDbReplicationSync<L,D,RY,?>
>
			extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoDbFactoryBuilder.class);
	
	private final Class<DUMP> cDump; public Class<DUMP> getClassDump(){return cDump;}
	private final Class<DF> cFile; public Class<DF> getClassDumpFile(){return cFile;}
	private final Class<DH> cHost; public Class<DH> getClassDumpHost(){return cHost;}
	private final Class<DS> cStatus; public Class<DS> getClassDumpStatus(){return cStatus;}
	private final Class<CC> cConnectionColumn; public Class<CC> getClassConnectionColumn() {return cConnectionColumn;}
	private final Class<RC> cReplicationColumn; public Class<RC> getClassReplicationColumn() {return cReplicationColumn;}
	private final Class<RS> cReplicationState; public Class<RS> getClassReplicationState() {return cReplicationState;}
	private final Class<RY> cReplicationSync; public Class<RY> getClassReplicationSync() {return cReplicationSync;}
	
	public IoDbFactoryBuilder(final Class<L> cL, final Class<D> cD,
							final Class<DUMP> cDump, final Class<DF> cFile, final Class<DH> cHost, final Class<DS> cStatus,
							final Class<CC> cConnectionColumn,
							final Class<RC> cReplicationColumn, final Class<RS> cReplicationState, final Class<RY> cReplicationSync)
	{
		super(cL,cD);
		
		this.cDump = cDump;
		this.cFile=cFile;
		this.cHost=cHost;
		this.cStatus=cStatus;
		this.cConnectionColumn=cConnectionColumn;
		this.cReplicationColumn=cReplicationColumn;
		this.cReplicationState=cReplicationState;
		this.cReplicationSync=cReplicationSync;
		
	}
	
	public EjbIoDumpFactory<SYSTEM,DUMP> dump(){return new EjbIoDumpFactory<>(cDump);}
	public EjbDbDumpFileFactory<DUMP,DF,DH,DS> dumpFile(){return new EjbDbDumpFileFactory<>(cFile);}
}