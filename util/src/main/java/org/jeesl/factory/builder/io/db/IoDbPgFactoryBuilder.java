package org.jeesl.factory.builder.io.db;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.db.pg.EjbDbStatementFactory;
import org.jeesl.factory.ejb.io.db.pg.EjbDbStatementGroupFactory;
import org.jeesl.factory.json.io.db.pg.JsonPostgresStatementFactory;
import org.jeesl.factory.json.io.db.pg.JsonPostgresStatementGroupFactory;
import org.jeesl.interfaces.model.io.db.pg.JeeslDbConnectionColumn;
import org.jeesl.interfaces.model.io.db.pg.replication.JeeslDbReplicationColumn;
import org.jeesl.interfaces.model.io.db.pg.replication.JeeslDbReplicationState;
import org.jeesl.interfaces.model.io.db.pg.replication.JeeslDbReplicationSync;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatement;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatementColumn;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatementGroup;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatement;
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatementGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbPgFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								HOST extends JeeslIoSsiHost<L,D,SYSTEM>,

								CC extends JeeslDbConnectionColumn<L,D,CC,?>,
								
								ST extends JeeslDbStatement<HOST,SG>,
								SG extends JeeslDbStatementGroup<SYSTEM>,
								SC extends JeeslDbStatementColumn<L,D,SC,?>,

								RC extends JeeslDbReplicationColumn<L,D,RC,?>,
								RS extends JeeslDbReplicationState<L,D,RS,?>,
								RY extends JeeslDbReplicationSync<L,D,RY,?>
>
			extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoDbPgFactoryBuilder.class);
	
	private final Class<CC> cConnectionColumn; public Class<CC> getClassConnectionColumn() {return cConnectionColumn;}
	
	private final Class<ST> cStatement; public Class<ST> getClassStatement() {return cStatement;}
	private final Class<SG> cStatementGroup; public Class<SG> getClassStatementGroup() {return cStatementGroup;}
	private final Class<SC> cStatementColumn; public Class<SC> getClassStatementColumn() {return cStatementColumn;}
	
	private final Class<RC> cReplicationColumn; public Class<RC> getClassReplicationColumn() {return cReplicationColumn;}
	private final Class<RS> cReplicationState; public Class<RS> getClassReplicationState() {return cReplicationState;}
	private final Class<RY> cReplicationSync; public Class<RY> getClassReplicationSync() {return cReplicationSync;}
	
	public IoDbPgFactoryBuilder(final Class<L> cL, final Class<D> cD,
							
							final Class<CC> cConnectionColumn,
							final Class<ST> cStatement, final Class<SG> cStatementGroup, final Class<SC> cStatementColumn,
							final Class<RC> cReplicationColumn, final Class<RS> cReplicationState, final Class<RY> cReplicationSync)
	{
		super(cL,cD);
		
		this.cConnectionColumn=cConnectionColumn;
		
		this.cStatement=cStatement;
		this.cStatementGroup=cStatementGroup;
		this.cStatementColumn=cStatementColumn;
		
		this.cReplicationColumn=cReplicationColumn;
		this.cReplicationState=cReplicationState;
		this.cReplicationSync=cReplicationSync;
	}
	
	public EjbDbStatementFactory<HOST,ST,SG> efStatement() {return new EjbDbStatementFactory<>(cStatement);}
	public EjbDbStatementGroupFactory<SYSTEM,SG> efGroup() {return new EjbDbStatementGroupFactory<>(cStatementGroup);}
	
	public JsonPostgresStatementGroupFactory<SYSTEM,SG> jfGroup(JsonPostgresStatementGroup json) {return new JsonPostgresStatementGroupFactory<>(json);}
	public JsonPostgresStatementFactory<HOST,ST,SG> jfStatement(JsonPostgresStatement json) {return new JsonPostgresStatementFactory<>(json);}
}