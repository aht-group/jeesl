package org.jeesl.controller.web.c.io.db;

import org.jeesl.controller.web.io.db.JeeslDbStatementStatisticGwc;
import org.jeesl.factory.builder.io.db.IoDbPgFactoryBuilder;
import org.jeesl.model.ejb.io.db.pg.connection.IoDbConnectionColumn;
import org.jeesl.model.ejb.io.db.pg.replication.IoDbReplicationColumn;
import org.jeesl.model.ejb.io.db.pg.replication.IoDbReplicationState;
import org.jeesl.model.ejb.io.db.pg.replication.IoDbReplicationSync;
import org.jeesl.model.ejb.io.db.pg.statement.IoDbStatementColumn;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

public class JeeslDbStatementStatisticWc extends JeeslDbStatementStatisticGwc<IoLang,IoDescription,IoLocale,IoSsiSystem,IoDbStatementColumn>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslDbStatementStatisticWc(String dbName, IoDbPgFactoryBuilder<IoLang,IoDescription,IoDbConnectionColumn,IoDbStatementColumn,IoDbReplicationColumn,IoDbReplicationState,IoDbReplicationSync> fbDb)
	{
		super(dbName,fbDb);	
	}
}