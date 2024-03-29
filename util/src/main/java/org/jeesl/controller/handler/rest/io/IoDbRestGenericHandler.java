package org.jeesl.controller.handler.rest.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.exlp.model.xml.io.Dir;
import org.exlp.model.xml.io.File;
import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.api.rest.i.io.JeeslIoDbRestInterface;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.db.IoDbDumpFactoryBuilder;
import org.jeesl.factory.builder.io.db.IoDbMetaFactoryBuilder;
import org.jeesl.factory.builder.io.db.IoDbPgFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.factory.ejb.io.db.backup.EjbDbDumpFileFactory;
import org.jeesl.factory.ejb.io.db.backup.EjbIoDumpFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaColumnFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaConstraintFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaSchemaFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaSnapshotFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaTableFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaUniqueFactory;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.factory.json.io.db.meta.JsonDbMetaSchemaFactory;
import org.jeesl.factory.json.io.db.meta.JsonDbMetaTableFactory;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbBackupArchive;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbBackupFile;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbBackupStatus;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumnType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraintType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSchema;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaUnique;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatement;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatementGroup;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaColumn;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaConstraint;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSchema;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSnapshot;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaTable;
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatement;
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatementGroup;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.jeesl.util.query.ejb.io.EjbIoDbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbRestGenericHandler<L extends JeeslLang,D extends JeeslDescription,
							SYSTEM extends JeeslIoSsiSystem<L,D>,
							DUMP extends JeeslDbBackupArchive<SYSTEM,FILE>,
							FILE extends JeeslDbBackupFile<DUMP,HOST,STATUS>,
							HOST extends JeeslIoSsiHost<L,D,SYSTEM>,
							STATUS extends JeeslDbBackupStatus<L,D,STATUS,?>,
							
							SNAP extends JeeslDbMetaSnapshot<SYSTEM,SCHEMA,TAB,COL,CON>,
							SCHEMA extends JeeslDbMetaSchema<SYSTEM,SNAP>,
							TAB extends JeeslDbMetaTable<SYSTEM,SNAP,SCHEMA>,
							COL extends JeeslDbMetaColumn<SNAP,TAB,COLT>,
							COLT extends JeeslDbMetaColumnType<L,D,COLT,?>,
							CON extends JeeslDbMetaConstraint<SNAP,TAB,COL,CONT,UNQ>,
							CONT extends JeeslDbMetaConstraintType<L,D,CONT,?>,
							UNQ extends JeeslDbMetaUnique<COL,CON>,
							
							ST extends JeeslDbStatement<HOST,SG>,
							SG extends JeeslDbStatementGroup<SYSTEM>>
					implements JeeslIoDbRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(IoDbRestGenericHandler.class);
	
	private final IoDbDumpFactoryBuilder<L,D,SYSTEM,DUMP,FILE,HOST,STATUS> fbDb;
	private final IoDbMetaFactoryBuilder<L,D,SYSTEM,SNAP,SCHEMA,TAB,COL,COLT,CON,CONT,UNQ,?,?> fbDbMeta;
	private final IoDbPgFactoryBuilder<L,D,SYSTEM,HOST,?,?,?,ST,SG,?,?,?,?> fbPg;
	private final IoSsiCoreFactoryBuilder<L,D,SYSTEM,?,HOST> fbSsi;
	
	private final JeeslIoDbFacade<SYSTEM,DUMP,FILE,HOST,SNAP,SCHEMA,TAB,COL,CON,UNQ,?> fDb;
	
	private final EjbCodeCache<CONT> cacheConstraintType;
	
	private EjbIoDumpFactory<SYSTEM,DUMP> efDump;
	private EjbDbDumpFileFactory<DUMP,FILE,HOST,STATUS> efDumpFile;
	private final EjbIoDbMetaSnapshotFactory<SYSTEM,SNAP> efSnapshot;
	private final EjbIoDbMetaSchemaFactory<SYSTEM,SCHEMA> efSchema;
	private final EjbIoDbMetaTableFactory<SYSTEM,SCHEMA,TAB> efTable;
	private final EjbIoDbMetaColumnFactory<TAB,COL> efColumn;
	private final EjbIoDbMetaConstraintFactory<TAB,COL,CON,CONT,UNQ> efConstraint;
	private final EjbIoDbMetaUniqueFactory<COL,CON,UNQ> efUnique;

	private final SYSTEM system;
	
	public IoDbRestGenericHandler(IoDbDumpFactoryBuilder<L,D,SYSTEM,DUMP,FILE,HOST,STATUS> fbDb,
							IoDbMetaFactoryBuilder<L,D,SYSTEM,SNAP,SCHEMA,TAB,COL,COLT,CON,CONT,UNQ,?,?> fbDbMeta,
							IoDbPgFactoryBuilder<L,D,SYSTEM,HOST,?,?,?,ST,SG,?,?,?,?> fbdbPg,
							IoSsiCoreFactoryBuilder<L,D,SYSTEM,?,HOST> fbSsi,
							JeeslIoDbFacade<SYSTEM,DUMP,FILE,HOST,SNAP,SCHEMA,TAB,COL,CON,UNQ,?> fDb,
							SYSTEM system)
	{
		this.fbDb=fbDb;
		this.fbDbMeta=fbDbMeta;
		this.fbPg=fbdbPg;
		
		this.fbSsi=fbSsi;
		this.fDb = fDb;
		
		this.system=system;
		
		cacheConstraintType = EjbCodeCache.instance(fbDbMeta.getClassConstraintType()).facade(fDb);
		
		efDump = fbDb.dump();
		efDumpFile = fbDb.dumpFile();
		efSnapshot = fbDbMeta.ejbSnapshot();
		efSchema = fbDbMeta.ejbSchema();
		efTable = fbDbMeta.ejbTable();
		efColumn = fbDbMeta.ejbColumn();
		efConstraint = fbDbMeta.ejbConstraint();
		efUnique = fbDbMeta.ejbUnique();
	}
		
	@Override public DataUpdate uploadDumps(Dir directory)
	{
		DataUpdateTracker dut = new DataUpdateTracker();
		
		STATUS eStatusStored;
		STATUS eStatusDeleted;
		
		try
		{
			eStatusStored = fDb.fByCode(fbDb.getClassDumpStatus(),JeeslDbBackupFile.Status.stored);
			eStatusDeleted = fDb.fByCode(fbDb.getClassDumpStatus(),JeeslDbBackupFile.Status.deleted);
		}
		catch (JeeslNotFoundException e) {dut.fail(e, true);return dut.toDataUpdate();}
		
		HOST eHost;
		try {eHost = fDb.fByCode(fbDb.getClassDumpHost(), directory.getCode());}
		catch (JeeslNotFoundException e)
		{
			try {eHost = fDb.persist(fbSsi.ejbHost().build(system,directory.getCode(),null));}
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
					if(Objects.nonNull(directory.getClassifier())) {eDump.setSystem(fDb.fByCode(fbSsi.getClassSystem(), directory.getClassifier()));}
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
		
		EjbCodeCache<COLT> cacheColumnType = EjbCodeCache.instance(fbDbMeta.getClassColumnType()).facade(fDb);
		
		try
		{
			SYSTEM eSystem = fDb.fByCode(fbSsi.getClassSystem(),snapshot.getSystem().getCode());
			SNAP eSnapshot = efSnapshot.build(eSystem);
			eSnapshot.setRecord(snapshot.getRecord());
			eSnapshot = fDb.save(eSnapshot);
			
			List<JsonPostgresMetaSchema> jSchemas = JsonDbMetaTableFactory.toSchemas(snapshot.getTables());
			EjbIoDbQuery<SYSTEM,SNAP> query = new EjbIoDbQuery<>();
			query.add(eSystem);
			query.codeList(JsonDbMetaSchemaFactory.toCodes(jSchemas));
			Map<String,SCHEMA> mapSchema = EjbCodeFactory.toMapNonUniqueCode(fDb.fIoDbMetaSchemas(query));
			for(JsonPostgresMetaSchema jSchema : jSchemas)
			{
				if(!mapSchema.containsKey(jSchema.getCode()))
				{
					SCHEMA eSchema = efSchema.build(eSystem,jSchema.getCode());
					mapSchema.put(jSchema.getCode(),fDb.save(eSchema));
				}
			}
			eSnapshot.getSchemas().addAll(mapSchema.values());
			eSnapshot = fDb.save(eSnapshot);
			
			query.reset();
			query.add(eSystem);
			query.codeList(JsonDbMetaTableFactory.toCodes(snapshot));
			
			Map<MultiKey<String>,TAB> mapDbTable = efTable.toMultiKeyMap(fDb.fIoDbMetaTables(query));
			Map<String,TAB> mapSnapshotTable = new HashMap<>();
			for(JsonPostgresMetaTable jTable : snapshot.getTables())
			{
				MultiKey<String> key = new MultiKey<String>(jTable.getScheme(),jTable.getCode());
				TAB eTable = null;
				if(mapDbTable.containsKey(key)) {eTable = mapDbTable.get(key);}
				else
				{
					SCHEMA schema = mapSchema.get(jTable.getScheme());
					eTable = efTable.build(eSystem,schema,jTable.getCode());
					eTable = fDb.save(eTable);
				} 
				
				mapSnapshotTable.put(jTable.getCode(),eTable);
			}
			eSnapshot.getTables().addAll(mapSnapshotTable.values());
			eSnapshot = fDb.save(eSnapshot);
			
			query.reset();
			query.add(eSystem);
			List<COL> columns = fDb.fIoDbMetaColumns(query);
			Map<String,Map<String,COL>> mapSystemColums = efColumn.toMapTableColumn(columns);
			Map<String,Map<String,COL>> mapSnapshotColums = new HashMap<>();
			for(JsonPostgresMetaTable jTable : snapshot.getTables())
			{
				TAB eTable = mapSnapshotTable.get(jTable.getCode());
				if(Objects.nonNull(jTable.getColumns()))
				{
					if(!mapSystemColums.containsKey(jTable.getCode())) {mapSystemColums.put(jTable.getCode(), new HashMap<>());}
					if(!mapSnapshotColums.containsKey(jTable.getCode())) {mapSnapshotColums.put(jTable.getCode(), new HashMap<>());}
					
					for(JsonPostgresMetaColumn jColumn : jTable.getColumns())
					{
						if(!mapSystemColums.get(jTable.getCode()).containsKey(jColumn.getCode()))
						{
							cacheColumnType.verify(jColumn.getType().getCode());
						
							COL col = efColumn.build(eTable,jColumn.getCode());
							col.setType(cacheColumnType.ejb(jColumn.getType().getCode()));
							col = fDb.save(col);
							mapSnapshotColums.get(jTable.getCode()).put(jColumn.getCode(), col);
							mapSystemColums.get(jTable.getCode()).put(jColumn.getCode(), col);
						}
						else {mapSnapshotColums.get(jTable.getCode()).put(jColumn.getCode(), mapSystemColums.get(jTable.getCode()).get(jColumn.getCode()));}
					}
				}
			}
			for(Map<String,COL> map : mapSnapshotColums.values()) {eSnapshot.getColumns().addAll(map.values());}
			eSnapshot = fDb.save(eSnapshot);
			
			query.reset();
			query.add(eSystem);
			query.addRootFetch(JeeslDbMetaConstraint.Attributes.uniques);
			List<CON> constraints = fDb.fIoDbMetaConstraints(query);
			
			Map<TAB,List<CON>> mapSystemConstraint = efConstraint.toMapConstraints(constraints);
			
			Map<String,Map<String,CON>> mapSnapshotConstraints = new HashMap<>();
			for(JsonPostgresMetaTable jTable : snapshot.getTables())
			{
				TAB eTable = mapSnapshotTable.get(jTable.getCode());
				if(!mapSystemConstraint.containsKey(eTable)) {mapSystemConstraint.put(eTable, new ArrayList<>());}
				if(!mapSnapshotConstraints.containsKey(jTable.getCode())) {mapSnapshotConstraints.put(jTable.getCode(), new HashMap<>());}
				
				List<CON> tableConstraints = mapSystemConstraint.get(eTable);
				logger.trace(eTable.getCode()+": "+fbDbMeta.getClassConstraint().getSimpleName()+": "+tableConstraints.size());
				
				for(JsonPostgresMetaConstraint jConstraint : jTable.getPrimaryKeys())
				{
					CON eConstraint = null;
					for(CON c : mapSystemConstraint.get(eTable))
					{
						if(cacheConstraintType.equals(c.getType(),JeeslDbMetaConstraintType.Code.pk) && efConstraint.equalsPk(c,jConstraint))
						{
							eConstraint = c;
						}
					}
					
					if(Objects.isNull(eConstraint))
					{
						eConstraint = efConstraint.build(eTable,jConstraint.getCode());
						eConstraint.setType(cacheConstraintType.ejb(JeeslDbMetaConstraintType.Code.pk));
						eConstraint.setColumnLocal(mapSystemColums.get(jTable.getCode()).get(jConstraint.getLocalColumn()));
						eConstraint = fDb.save(eConstraint);
						mapSystemConstraint.get(eTable).add(eConstraint);
					}
					mapSnapshotConstraints.get(jTable.getCode()).put(jConstraint.getCode(), eConstraint);
				}
				for(JsonPostgresMetaConstraint jConstraint : jTable.getForeignKeys())
				{
					CON eConstraint = null;
					for(CON c : mapSystemConstraint.get(eTable))
					{
						if(cacheConstraintType.equals(c.getType(),JeeslDbMetaConstraintType.Code.fk) && efConstraint.equals(c,jConstraint))
						{
							eConstraint = c;
						}
					}
					if(Objects.isNull(eConstraint))
					{
						eConstraint = efConstraint.build(eTable,jConstraint.getCode());
						eConstraint.setType(cacheConstraintType.ejb(JeeslDbMetaConstraintType.Code.fk));
						eConstraint.setColumnLocal(mapSystemColums.get(jTable.getCode()).get(jConstraint.getLocalColumn()));
						eConstraint.setColumnRemote(mapSystemColums.get(jConstraint.getRemoteTable()).get(jConstraint.getRemoteColumn()));
						eConstraint = fDb.save(eConstraint);
						mapSystemConstraint.get(eTable).add(eConstraint);
					}
					mapSnapshotConstraints.get(jTable.getCode()).put(jConstraint.getCode(), eConstraint);
				}
				for(JsonPostgresMetaConstraint jConstraint : jTable.getUniqueKeys())
				{
					CON eConstraint = null;
					for(CON c : mapSystemConstraint.get(eTable))
					{
						if(cacheConstraintType.equals(c.getType(),JeeslDbMetaConstraintType.Code.uk) && efConstraint.equalsUk(c,jConstraint))
						{
							eConstraint = c;
						}
					}
					if(Objects.isNull(eConstraint))
					{
						eConstraint = efConstraint.build(eTable,jConstraint.getCode());
						eConstraint.setType(cacheConstraintType.ejb(JeeslDbMetaConstraintType.Code.uk));
						eConstraint = fDb.save(eConstraint);
						
						for(JsonPostgresMetaColumn jUnique : jConstraint.getColumns())
						{
							COL eColumn = mapSystemColums.get(jTable.getCode()).get(jUnique.getCode());
							UNQ eUnique = efUnique.build(eConstraint, eColumn, jUnique.getPosition());
							fDb.save(eUnique);
						}
						mapSystemConstraint.get(eTable).add(eConstraint);
					}
					mapSnapshotConstraints.get(jTable.getCode()).put(jConstraint.getCode(), eConstraint);
				}
			}
			for(Map<String,CON> map : mapSnapshotConstraints.values()) {eSnapshot.getConstraints().addAll(map.values());}
			eSnapshot = fDb.save(eSnapshot);
		}
		catch (JeeslNotFoundException e) {dut.error(e);}
		catch (JeeslConstraintViolationException e) {dut.error(e);}
		catch (JeeslLockingException e) {dut.error(e);}
		return dut.toJson();
	}

	@Override
	public JsonSsiUpdate uploadStatementGroup(JsonPostgresStatementGroup json)
	{
//		JsonUtil.info(json);
		
		DataUpdateTracker dut = DataUpdateTracker.instance().start();
		
		SG eGroup = null;
		try
		{
			eGroup = fDb.fByCode(fbPg.getClassStatementGroup(),json.getCode());
		}
		catch (JeeslNotFoundException e)
		{
			try
			{
				SYSTEM eSystem = fDb.fByCode(fbSsi.getClassSystem(),json.getSystem().getCode());
				eGroup = fbPg.efGroup().build(eSystem, null);
				eGroup.setCode(json.getCode());
				eGroup.setName(json.getName());
				eGroup = fDb.save(eGroup);
			}
			catch (JeeslNotFoundException | JeeslConstraintViolationException | JeeslLockingException e1) {dut.error(e1);}
		}
		
		if(Objects.nonNull(eGroup))
		{
			for(JsonPostgresStatement jStatement : json.getStatements())
			{
				ST eStatement = null;
				try
				{
					eStatement = fDb.fByCode(fbPg.getClassStatement(),jStatement.getCode());
				}
				catch (JeeslNotFoundException e)
				{
					try
					{
						HOST eHost = fDb.fByCode(fbSsi.getClassHost(),jStatement.getHost().getCode());
						eStatement = fbPg.efStatement().build(eHost,eGroup,jStatement.getRecord(),jStatement);
						eStatement.setCode(jStatement.getCode());
						eStatement = fDb.save(eStatement);
					}
					catch (JeeslNotFoundException | JeeslConstraintViolationException | JeeslLockingException  e1) {dut.error(e1);}
				}
			}
		}
		return dut.toJson();
	}
}