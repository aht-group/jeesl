package org.jeesl.controller.handler.rest.io.db;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.api.rest.i.io.JeeslIoDbRestInterface;
import org.jeesl.controller.handler.rest.io.IoDbRestGenericHandler;
import org.jeesl.factory.builder.io.IoDbFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.model.ejb.io.db.dump.IoDbDump;
import org.jeesl.model.ejb.io.db.dump.IoDbDumpFile;
import org.jeesl.model.ejb.io.db.dump.IoDbDumpStatus;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaSnapshot;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaTable;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbRestHandler extends IoDbRestGenericHandler<IoLang,IoDescription,IoSsiSystem,IoDbDump,IoDbDumpFile,IoSsiHost,IoDbDumpStatus,IoDbMetaSnapshot,IoDbMetaTable>
					implements JeeslIoDbRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(IoDbRestHandler.class);
	
	public static IoDbRestHandler instance(IoDbFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbDump,IoDbDumpFile,IoSsiHost,IoDbDumpStatus,IoDbMetaSnapshot,IoDbMetaTable,?,?,?,?,?> fbDb,
			IoSsiCoreFactoryBuilder<IoLang,IoDescription,IoSsiSystem,?,IoSsiHost> fbSsi,
			JeeslIoDbFacade<IoSsiSystem,IoDbDump,IoDbDumpFile,IoSsiHost,IoDbMetaTable> fDb,
			IoSsiSystem system)
	{
		return new IoDbRestHandler(fbDb,fbSsi,fDb,system);
	}
	
	private IoDbRestHandler(IoDbFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbDump,IoDbDumpFile,IoSsiHost,IoDbDumpStatus,IoDbMetaSnapshot,IoDbMetaTable,?,?,?,?,?> fbDb,
			IoSsiCoreFactoryBuilder<IoLang,IoDescription,IoSsiSystem,?,IoSsiHost> fbSsi,
			JeeslIoDbFacade<IoSsiSystem,IoDbDump,IoDbDumpFile,IoSsiHost,IoDbMetaTable> fDb,
			IoSsiSystem system)
	{
		super(fbDb,fbSsi,fDb,system);
	}
}