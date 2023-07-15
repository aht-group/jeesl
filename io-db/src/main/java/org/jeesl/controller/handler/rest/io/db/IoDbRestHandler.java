package org.jeesl.controller.handler.rest.io.db;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.api.rest.i.io.JeeslIoDbRestInterface;
import org.jeesl.controller.handler.rest.io.IoDbRestGenericHandler;
import org.jeesl.factory.builder.io.db.IoDbDumpFactoryBuilder;
import org.jeesl.factory.builder.io.db.IoDbMetaFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.model.ejb.io.db.dump.IoDbDump;
import org.jeesl.model.ejb.io.db.dump.IoDbDumpFile;
import org.jeesl.model.ejb.io.db.dump.IoDbDumpStatus;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaConstraint;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaSnapshot;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaTable;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbRestHandler extends IoDbRestGenericHandler<IoLang,IoDescription,IoSsiSystem,IoDbDump,IoDbDumpFile,IoSsiHost,IoDbDumpStatus,IoDbMetaSnapshot,IoDbMetaTable,IoDbMetaConstraint>
					implements JeeslIoDbRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(IoDbRestHandler.class);
	
	public static IoDbRestHandler instance(IoDbDumpFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbDump,IoDbDumpFile,IoSsiHost,IoDbDumpStatus> fbDb,
			IoDbMetaFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbMetaSnapshot,IoDbMetaTable,IoDbMetaConstraint> fbDbMeta,
			IoSsiCoreFactoryBuilder<IoLang,IoDescription,IoSsiSystem,?,IoSsiHost> fbSsi,
			JeeslIoDbFacade<IoSsiSystem,IoDbDump,IoDbDumpFile,IoSsiHost,IoDbMetaTable,IoDbMetaConstraint> fDb,
			IoSsiSystem system)
	{
		return new IoDbRestHandler(fbDb,fbDbMeta,fbSsi,fDb,system);
	}
	
	private IoDbRestHandler(IoDbDumpFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbDump,IoDbDumpFile,IoSsiHost,IoDbDumpStatus> fbDb,
			IoDbMetaFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbMetaSnapshot,IoDbMetaTable,IoDbMetaConstraint> fbDbMeta,
			IoSsiCoreFactoryBuilder<IoLang,IoDescription,IoSsiSystem,?,IoSsiHost> fbSsi,
			JeeslIoDbFacade<IoSsiSystem,IoDbDump,IoDbDumpFile,IoSsiHost,IoDbMetaTable,IoDbMetaConstraint> fDb,
			IoSsiSystem system)
	{
		super(fbDb,fbDbMeta,fbSsi,fDb,system);
	}
}