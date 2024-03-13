package org.jeesl.controller.handler.rest.io.db;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.api.rest.i.io.JeeslIoDbRestInterface;
import org.jeesl.controller.handler.rest.io.IoDbRestGenericHandler;
import org.jeesl.factory.builder.io.db.IoDbDumpFactoryBuilder;
import org.jeesl.factory.builder.io.db.IoDbMetaFactoryBuilder;
import org.jeesl.factory.builder.io.db.IoDbPgFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.model.ejb.io.db.backup.IoDbBackupArchive;
import org.jeesl.model.ejb.io.db.backup.IoDbBackupFile;
import org.jeesl.model.ejb.io.db.backup.IoDbBackupStatus;
import org.jeesl.model.ejb.io.db.flyway.IoDbFlyway;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaColumn;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaColumnType;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaConstraint;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaConstraintType;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaDifference;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaSchema;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaSnapshot;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaSqlAction;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaTable;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaUnique;
import org.jeesl.model.ejb.io.db.pg.connection.IoDbConnectionColumn;
import org.jeesl.model.ejb.io.db.pg.connection.IoDbConnectionState;
import org.jeesl.model.ejb.io.db.pg.connection.IoDbConnectionWait;
import org.jeesl.model.ejb.io.db.pg.replication.IoDbReplicationColumn;
import org.jeesl.model.ejb.io.db.pg.replication.IoDbReplicationState;
import org.jeesl.model.ejb.io.db.pg.replication.IoDbReplicationSync;
import org.jeesl.model.ejb.io.db.pg.statement.IoDbStatement;
import org.jeesl.model.ejb.io.db.pg.statement.IoDbStatementColumn;
import org.jeesl.model.ejb.io.db.pg.statement.IoDbStatementGroup;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.ssi.core.IoSsiCredential;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbRestHandler extends IoDbRestGenericHandler<IoLang,IoDescription,IoSsiSystem,IoDbBackupArchive,IoDbBackupFile,IoSsiHost,IoDbBackupStatus,IoDbMetaSnapshot,IoDbMetaSchema,IoDbMetaTable,IoDbMetaColumn,IoDbMetaColumnType,IoDbMetaConstraint,IoDbMetaConstraintType,IoDbMetaUnique,IoDbStatement,IoDbStatementGroup>
					implements JeeslIoDbRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(IoDbRestHandler.class);
	
	public static IoDbRestHandler instance(IoDbDumpFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbBackupArchive,IoDbBackupFile,IoSsiHost,IoDbBackupStatus> fbDb,
											IoDbMetaFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbMetaSnapshot,IoDbMetaSchema,IoDbMetaTable,IoDbMetaColumn,IoDbMetaColumnType,IoDbMetaConstraint,IoDbMetaConstraintType,IoDbMetaUnique,IoDbMetaDifference,IoDbMetaSqlAction> fbDbMeta,
											JeeslIoDbFacade<IoSsiSystem,IoDbBackupArchive,IoDbBackupFile,IoSsiHost,IoDbMetaSnapshot,IoDbMetaSchema,IoDbMetaTable,IoDbMetaColumn,IoDbMetaConstraint,IoDbMetaUnique,IoDbFlyway> fDb,
											IoSsiSystem system)
	{
		return new IoDbRestHandler(fbDb,fbDbMeta,fDb,system);
	}
	
	private IoDbRestHandler(IoDbDumpFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbBackupArchive,IoDbBackupFile,IoSsiHost,IoDbBackupStatus> fbDb,
							IoDbMetaFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbMetaSnapshot,IoDbMetaSchema,IoDbMetaTable,IoDbMetaColumn,IoDbMetaColumnType,IoDbMetaConstraint,IoDbMetaConstraintType,IoDbMetaUnique,IoDbMetaDifference,IoDbMetaSqlAction> fbDbMeta,
							JeeslIoDbFacade<IoSsiSystem,IoDbBackupArchive,IoDbBackupFile,IoSsiHost,IoDbMetaSnapshot,IoDbMetaSchema,IoDbMetaTable,IoDbMetaColumn,IoDbMetaConstraint,IoDbMetaUnique,IoDbFlyway> fDb,
							IoSsiSystem system)
	{
		super(fbDb,fbDbMeta,
				new IoDbPgFactoryBuilder<>(IoLang.class,IoDescription.class,IoDbConnectionColumn.class,IoDbConnectionState.class,IoDbConnectionWait.class,IoDbStatement.class,IoDbStatementGroup.class,IoDbStatementColumn.class,IoDbReplicationColumn.class,IoDbReplicationState.class,IoDbReplicationSync.class),
				new IoSsiCoreFactoryBuilder<>(IoLang.class,IoDescription.class,IoSsiSystem.class,IoSsiCredential.class,IoSsiHost.class)
				,fDb,system);
		
	}
}