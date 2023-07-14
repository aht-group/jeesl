package org.jeesl.controller.web.c.io.db;

import org.jeesl.controller.web.g.io.db.JeeslDbMetaGwc;
import org.jeesl.factory.builder.io.IoDbFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.model.ejb.io.db.dump.IoDbDump;
import org.jeesl.model.ejb.io.db.dump.IoDbDumpFile;
import org.jeesl.model.ejb.io.db.dump.IoDbDumpStatus;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaSnapshot;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaTable;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

public class JeeslDbMetaWc extends JeeslDbMetaGwc<IoLang,IoDescription,IoLocale,IoSsiSystem,IoDbMetaSnapshot,IoDbMetaTable>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslDbMetaWc(IoDbFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbDump,IoDbDumpFile,IoSsiHost,IoDbDumpStatus,IoDbMetaSnapshot,IoDbMetaTable,?,?,?,?,?> fbDb,
						IoSsiCoreFactoryBuilder<IoLang,IoDescription,IoSsiSystem,?,IoSsiHost> fbSsi)
	{
		super(fbDb,fbSsi);	
	}
}