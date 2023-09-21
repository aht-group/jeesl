package org.jeesl.controller.web.c.io.db;

import org.jeesl.controller.web.io.db.JeeslDbStatementHistoryGwc;
import org.jeesl.factory.builder.io.db.IoDbPgFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.interfaces.controller.web.io.db.JeeslIoDbStatementHistoryCallback;
import org.jeesl.model.ejb.io.db.pg.connection.IoDbConnectionColumn;
import org.jeesl.model.ejb.io.db.pg.replication.IoDbReplicationColumn;
import org.jeesl.model.ejb.io.db.pg.replication.IoDbReplicationState;
import org.jeesl.model.ejb.io.db.pg.replication.IoDbReplicationSync;
import org.jeesl.model.ejb.io.db.pg.statement.IoDbStatement;
import org.jeesl.model.ejb.io.db.pg.statement.IoDbStatementColumn;
import org.jeesl.model.ejb.io.db.pg.statement.IoDbStatementGroup;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.ssi.core.IoSsiCredential;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

public class JeeslDbStatementHistoryWc extends JeeslDbStatementHistoryGwc<IoLang,IoDescription,IoLocale,IoSsiSystem,IoSsiHost,IoDbStatement,IoDbStatementGroup,IoDbStatementColumn>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslDbStatementHistoryWc(JeeslIoDbStatementHistoryCallback callback, String dbName)
	{
		super(callback,
				new IoDbPgFactoryBuilder<>(IoLang.class,IoDescription.class,IoDbConnectionColumn.class,IoDbStatement.class,IoDbStatementGroup.class,IoDbStatementColumn.class,IoDbReplicationColumn.class,IoDbReplicationState.class,IoDbReplicationSync.class),
				new IoSsiCoreFactoryBuilder<>(IoLang.class,IoDescription.class,IoSsiSystem.class,IoSsiCredential.class,IoSsiHost.class));
	}
}