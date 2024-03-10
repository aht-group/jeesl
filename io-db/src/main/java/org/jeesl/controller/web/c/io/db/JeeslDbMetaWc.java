package org.jeesl.controller.web.c.io.db;

import org.jeesl.controller.web.g.io.db.JeeslDbMetaGwc;
import org.jeesl.factory.builder.io.db.IoDbMetaFactoryBuilder;
import org.jeesl.interfaces.controller.web.io.db.JeeslIoDbMetaCallback;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaColumn;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaColumnType;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaConstraint;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaConstraintType;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaDifference;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaSnapshot;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaSqlAction;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaTable;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaUnique;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

public class JeeslDbMetaWc extends JeeslDbMetaGwc<IoLang,IoDescription,IoLocale,IoSsiSystem,IoDbMetaSnapshot,IoDbMetaTable,IoDbMetaColumn,IoDbMetaColumnType,IoDbMetaConstraint,IoDbMetaConstraintType,IoDbMetaUnique,IoDbMetaDifference,IoDbMetaSqlAction>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslDbMetaWc(JeeslIoDbMetaCallback callback, IoDbMetaFactoryBuilder<IoLang,IoDescription,IoSsiSystem,IoDbMetaSnapshot,IoDbMetaTable,IoDbMetaColumn,IoDbMetaColumnType,IoDbMetaConstraint,IoDbMetaConstraintType,IoDbMetaUnique,IoDbMetaDifference,IoDbMetaSqlAction> fbDb)
	{
		super(callback,fbDb);	
	}
}