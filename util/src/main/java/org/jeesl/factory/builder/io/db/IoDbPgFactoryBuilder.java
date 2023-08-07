package org.jeesl.factory.builder.io.db;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.interfaces.model.io.db.JeeslDbReplicationColumn;
import org.jeesl.interfaces.model.io.db.JeeslDbReplicationState;
import org.jeesl.interfaces.model.io.db.JeeslDbReplicationSync;
import org.jeesl.interfaces.model.io.db.JeeslDbStatementColumn;
import org.jeesl.interfaces.model.io.db.pg.JeeslDbConnectionColumn;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbPgFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								
								CC extends JeeslDbConnectionColumn<L,D,CC,?>,
								
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
	private final Class<SC> cStatementColumn; public Class<SC> getClassStatementColumn() {return cStatementColumn;}
	private final Class<RC> cReplicationColumn; public Class<RC> getClassReplicationColumn() {return cReplicationColumn;}
	private final Class<RS> cReplicationState; public Class<RS> getClassReplicationState() {return cReplicationState;}
	private final Class<RY> cReplicationSync; public Class<RY> getClassReplicationSync() {return cReplicationSync;}
	
	public IoDbPgFactoryBuilder(final Class<L> cL, final Class<D> cD,
							
							final Class<CC> cConnectionColumn,
							final Class<SC> cStatementColumn,
							final Class<RC> cReplicationColumn, final Class<RS> cReplicationState, final Class<RY> cReplicationSync)
	{
		super(cL,cD);
		
		this.cConnectionColumn=cConnectionColumn;
		this.cStatementColumn=cStatementColumn;
		this.cReplicationColumn=cReplicationColumn;
		this.cReplicationState=cReplicationState;
		this.cReplicationSync=cReplicationSync;
		
	}
}