package org.jeesl.controller.handler.rest.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.api.rest.i.io.JeeslIoDbRestInterface;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.db.IoDbDumpFactoryBuilder;
import org.jeesl.factory.builder.io.db.IoDbMetaFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.factory.ejb.io.db.EjbDbDumpFileFactory;
import org.jeesl.factory.ejb.io.db.EjbIoDumpFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaConstraintFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaSnapshotFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaTableFactory;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.factory.json.io.db.meta.JsonDbMetaTableFactory;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.util.query.io.EjbIoDbQuery;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaConstraint;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSnapshot;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaTable;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.sync.DataUpdate;
import net.sf.exlp.xml.io.Dir;
import net.sf.exlp.xml.io.File;

public class IoDbRestGenericHandler<L extends JeeslLang,D extends JeeslDescription,
							SYSTEM extends JeeslIoSsiSystem<L,D>,
							DUMP extends JeeslDbDump<SYSTEM,FILE>,
							FILE extends JeeslDbDumpFile<DUMP,HOST,STATUS>,
							HOST extends JeeslIoSsiHost<L,D,SYSTEM>,
							STATUS extends JeeslDbDumpStatus<L,D,STATUS,?>,
							
							MS extends JeeslDbMetaSnapshot<SYSTEM,MT,MC>,
							MT extends JeeslDbMetaTable<SYSTEM,MS>,
							MC extends JeeslDbMetaConstraint<SYSTEM,MS,MT>>
					implements JeeslIoDbRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(IoDbRestGenericHandler.class);
	
	private final IoDbDumpFactoryBuilder<L,D,SYSTEM,DUMP,FILE,HOST,STATUS> fbDb;
	private final IoDbMetaFactoryBuilder<L,D,SYSTEM,MS,MT,MC> fbDbMeta;
	private final IoSsiCoreFactoryBuilder<L,D,SYSTEM,?,HOST> fbSsi;
	
	private final JeeslIoDbFacade<SYSTEM,DUMP,FILE,HOST,MT,MC> fDb;
	
	private EjbIoDumpFactory<SYSTEM,DUMP> efDump;
	private EjbDbDumpFileFactory<DUMP,FILE,HOST,STATUS> efDumpFile;
	private final EjbIoDbMetaSnapshotFactory<SYSTEM,MS> efSnapshot;
	private final EjbIoDbMetaTableFactory<SYSTEM,MT> efTable;
	private final EjbIoDbMetaConstraintFactory<SYSTEM,MT,MC> efConstraint;

	private final SYSTEM system;
	
	public IoDbRestGenericHandler(IoDbDumpFactoryBuilder<L,D,SYSTEM,DUMP,FILE,HOST,STATUS> fbDb,
							IoDbMetaFactoryBuilder<L,D,SYSTEM,MS,MT,MC> fbDbMeta,
							IoSsiCoreFactoryBuilder<L,D,SYSTEM,?,HOST> fbSsi,
							JeeslIoDbFacade<SYSTEM,DUMP,FILE,HOST,MT,MC> fDb,
							SYSTEM system)
	{

		this.fbDb=fbDb;
		this.fbDbMeta=fbDbMeta;
		
		this.fbSsi=fbSsi;
		this.fDb = fDb;
		
		this.system=system;
		
		efDump = fbDb.dump();
		efDumpFile = fbDb.dumpFile();
		efSnapshot = fbDbMeta.ejbSnapshot();
		efTable = fbDbMeta.ejbTable();
		efConstraint = fbDbMeta.ejbConstraint();
	}
		
	@Override public DataUpdate uploadDumps(Dir directory)
	{
		DataUpdateTracker dut = new DataUpdateTracker();
		
		STATUS eStatusStored;
		STATUS eStatusDeleted;
		
		try
		{
			eStatusStored = fDb.fByCode(fbDb.getClassDumpStatus(),JeeslDbDumpFile.Status.stored);
			eStatusDeleted = fDb.fByCode(fbDb.getClassDumpStatus(),JeeslDbDumpFile.Status.deleted);
		}
		catch (JeeslNotFoundException e) {dut.fail(e, true);return dut.toDataUpdate();}
		
		HOST eHost;
		try{eHost = fDb.fByCode(fbDb.getClassDumpHost(), directory.getCode());}
		catch (JeeslNotFoundException e)
		{
			try{eHost = fDb.persist(fbSsi.ejbHost().build(system,directory.getCode(),null));}
			catch (JeeslConstraintViolationException e1) {dut.fail(e1, true);return dut.toDataUpdate();}
		}
		
		Set<FILE> setExisting = new HashSet<FILE>(fDb.fDumpFiles(eHost));
		
		for(File xFile : directory.getFile())
		{
			DUMP eDump;
			try{eDump = fDb.fByName(fbDb.getClassDump(), xFile.getName());}
			catch (JeeslNotFoundException e)
			{
				try
				{
					eDump = efDump.build(system,xFile);
					if(directory.isSetClassifier()) {eDump.setSystem(fDb.fByCode(fbSsi.getClassSystem(), directory.getClassifier()));}
					eDump = fDb.persist(eDump);
					
				}
				catch (JeeslConstraintViolationException e1) {dut.fail(e1, true);return dut.toDataUpdate();}
				catch (JeeslNotFoundException e1) {dut.fail(e1, true);return dut.toDataUpdate();}
			}
			FILE eFile;
			try {eFile = fDb.fDumpFile(eDump,eHost);}
			catch (JeeslNotFoundException e)
			{
				try {eFile = fDb.persist(efDumpFile.build(eDump,eHost,eStatusStored));}
				catch (JeeslConstraintViolationException e1) {dut.fail(e1, true);return dut.toDataUpdate();}
			}
			
			try
			{
				if(setExisting.contains(eFile)){setExisting.remove(eFile);}
				eFile.setStatus(eStatusStored);
				eFile = fDb.save(eFile);
			}
			catch (JeeslConstraintViolationException e) {dut.fail(e,true);return dut.toDataUpdate();}
			catch (JeeslLockingException e) {dut.fail(e,true);return dut.toDataUpdate();}
		}
		
		for(FILE f : new ArrayList<FILE>(setExisting))
		{
			try
			{
				f.setStatus(eStatusDeleted);
				f = fDb.save(f);
			}
			catch (JeeslConstraintViolationException e) {dut.fail(e,true);}
			catch (JeeslLockingException e) {dut.fail(e,true);}
		}
		
		return dut.toDataUpdate();
	}

	@Override
	public JsonSsiUpdate uploadMetaSnapshot(JsonPostgresMetaSnapshot snapshot)
	{
		DataUpdateTracker dut = DataUpdateTracker.instance().start();
		try
		{
			SYSTEM eSystem = fDb.fByCode(fbSsi.getClassSystem(),snapshot.getSystem().getCode());
			MS eSnapshot = efSnapshot.build(eSystem);
			eSnapshot.setRecord(snapshot.getRecord());
			eSnapshot = fDb.save(eSnapshot);
			
			EjbIoDbQuery<SYSTEM> query = new EjbIoDbQuery<>();
			query.add(eSystem);
			query.codeList(JsonDbMetaTableFactory.toCodes(snapshot));
			
			List<MT> tables = fDb.fIoDbMetaTables(query);
			Map<String,MT> mapTable = EjbCodeFactory.toMapNonUniqueCode(tables);
			for(JsonPostgresMetaTable jTable : snapshot.getTables())
			{
				if(!mapTable.containsKey(jTable.getCode()))
				{
					MT t = efTable.build(eSystem,jTable.getCode());
					mapTable.put(jTable.getCode(),fDb.save(t));
				}
			}
			eSnapshot.getTables().addAll(mapTable.values());
			eSnapshot = fDb.save(eSnapshot);
			
			query.reset();
			query.add(eSystem);
			List<MC> constraints = fDb.fIoDbMetaConstraints(query);
			Map<String,Map<String,MC>> mapConstraint = efConstraint.toMapTableConstraint(constraints);
			for(JsonPostgresMetaTable jTable : snapshot.getTables())
			{
				if(Objects.nonNull(jTable.getForeignKeys()))
				{
					if(!mapConstraint.containsKey(jTable.getCode())) {mapConstraint.put(jTable.getCode(), new HashMap<>());}
					for(JsonPostgresMetaConstraint jConstraint : jTable.getForeignKeys())
					{
						if(!mapConstraint.get(jTable.getCode()).containsKey(jConstraint.getCode()))
						{
							MC mc = efConstraint.build(eSystem,mapTable.get(jTable.getCode()),jConstraint.getCode());
							mapConstraint.get(jTable.getCode()).put(jConstraint.getCode(), fDb.save(mc));
						}
					}
				}
			}
			for(Map<String,MC> map : mapConstraint.values()) {eSnapshot.getConstraints().addAll(map.values());}
			eSnapshot = fDb.save(eSnapshot);
		}
		catch (JeeslNotFoundException e) {dut.error(e);}
		catch (JeeslConstraintViolationException e) {dut.error(e);}
		catch (JeeslLockingException e) {dut.error(e);}
		return dut.toJson();
	}
}