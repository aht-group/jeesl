package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.db.EjbDbDumpFileFactory;
import org.jeesl.factory.ejb.io.db.EjbIoDumpFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaSnapshotFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaTableFactory;
import org.jeesl.interfaces.model.io.db.JeeslDbConnectionColumn;
import org.jeesl.interfaces.model.io.db.JeeslDbReplicationColumn;
import org.jeesl.interfaces.model.io.db.JeeslDbReplicationState;
import org.jeesl.interfaces.model.io.db.JeeslDbReplicationSync;
import org.jeesl.interfaces.model.io.db.JeeslDbStatementColumn;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								DUMP extends JeeslDbDump<SYSTEM,DF>,
								DF extends JeeslDbDumpFile<DUMP,DH,DS>,
								DH extends JeeslIoSsiHost<L,D,?>,
								DS extends JeeslDbDumpStatus<L,D,DS,?>,
								
								MS extends JeeslDbMetaSnapshot<SYSTEM>,
								MT extends JeeslDbMetaTable<SYSTEM>,
								
								CC extends JeeslDbConnectionColumn<L,D,CC,?>,
								
								SC extends JeeslDbStatementColumn<L,D,SC,?>,

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
	
	private final Class<MS> cSnapshot; public Class<MS> getClassSnapshot(){return cSnapshot;}
	private final Class<MT> cMetaTable; public Class<MT> getClassMetaTable(){return cMetaTable;}
	
	private final Class<CC> cConnectionColumn; public Class<CC> getClassConnectionColumn() {return cConnectionColumn;}
	private final Class<SC> cStatementColumn; public Class<SC> getClassStatementColumn() {return cStatementColumn;}
	private final Class<RC> cReplicationColumn; public Class<RC> getClassReplicationColumn() {return cReplicationColumn;}
	private final Class<RS> cReplicationState; public Class<RS> getClassReplicationState() {return cReplicationState;}
	private final Class<RY> cReplicationSync; public Class<RY> getClassReplicationSync() {return cReplicationSync;}
	
	public IoDbFactoryBuilder(final Class<L> cL, final Class<D> cD,
							final Class<DUMP> cDump, final Class<DF> cFile, final Class<DH> cHost, final Class<DS> cStatus,
							final Class<MS> cSnapshot,final Class<MT> cMetaTable,
							final Class<CC> cConnectionColumn,
							final Class<SC> cStatementColumn,
							final Class<RC> cReplicationColumn, final Class<RS> cReplicationState, final Class<RY> cReplicationSync)
	{
		super(cL,cD);
		
		this.cDump = cDump;
		this.cFile=cFile;
		this.cHost=cHost;
		this.cStatus=cStatus;
		
		this.cSnapshot=cSnapshot;
		this.cMetaTable=cMetaTable;
		
		this.cConnectionColumn=cConnectionColumn;
		this.cStatementColumn=cStatementColumn;
		this.cReplicationColumn=cReplicationColumn;
		this.cReplicationState=cReplicationState;
		this.cReplicationSync=cReplicationSync;
		
	}
	
	public EjbIoDumpFactory<SYSTEM,DUMP> dump(){return new EjbIoDumpFactory<>(cDump);}
	public EjbDbDumpFileFactory<DUMP,DF,DH,DS> dumpFile(){return new EjbDbDumpFileFactory<>(cFile);}
	
	public EjbIoDbMetaSnapshotFactory<SYSTEM,MS> ejbSnapshot() {return new EjbIoDbMetaSnapshotFactory<>(cSnapshot);}
	public EjbIoDbMetaTableFactory<SYSTEM,MT> ejbTable() {return new EjbIoDbMetaTableFactory<>(cMetaTable);}
}