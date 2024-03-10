package org.jeesl.controller.web.c.io.db;

import org.jeesl.controller.web.g.io.db.JeeslDbExplainGwc;
import org.jeesl.factory.builder.io.db.IoDbMetaFactoryBuilder;
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

public class JeeslDbExplainWc extends JeeslDbExplainGwc<IoLang,IoDescription,IoLocale,IoSsiSystem,IoDbMetaSnapshot,IoDbMetaTable,IoDbMetaColumn,IoDbMetaColumnType,IoDbMetaConstraint,IoDbMetaConstraintType,IoDbMetaUnique,IoDbMetaDifference>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslDbExplainWc()
	{
		super(new IoDbMetaFactoryBuilder<>(IoLang.class,IoDescription.class,IoSsiSystem.class,IoDbMetaSnapshot.class,IoDbMetaTable.class,IoDbMetaColumn.class,IoDbMetaColumnType.class,IoDbMetaConstraint.class,IoDbMetaConstraintType.class,IoDbMetaUnique.class,IoDbMetaDifference.class,IoDbMetaSqlAction.class));
	}
}