package org.jeesl.factory.builder.io.db;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaConstraintFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaSnapshotFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaTableFactory;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbMetaFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								
								MS extends JeeslDbMetaSnapshot<SYSTEM,MT,MC>,
								MT extends JeeslDbMetaTable<SYSTEM,MS>,
								MC extends JeeslDbMetaConstraint<SYSTEM,MS,MT>
								
>
			extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoDbMetaFactoryBuilder.class);
		
	private final Class<SYSTEM> cSystem; public Class<SYSTEM> getClassSsiSystem() {return cSystem;}
	private final Class<MS> cSnapshot; public Class<MS> getClassSnapshot(){return cSnapshot;}
	private final Class<MT> cMetaTable; public Class<MT> getClassMetaTable(){return cMetaTable;}
	private final Class<MC> cMetaConstraint; public Class<MC> getClassMetaConstraint(){return cMetaConstraint;}
	

	
	public IoDbMetaFactoryBuilder(final Class<L> cL, final Class<D> cD,
							final Class<SYSTEM> cSystem,
							final Class<MS> cSnapshot,
							final Class<MT> cMetaTable,
							final Class<MC> cMetaConstraint)
	{
		super(cL,cD);
		this.cSystem=cSystem;
		
		this.cSnapshot=cSnapshot;
		this.cMetaTable=cMetaTable;
		this.cMetaConstraint=cMetaConstraint;
		
	}	
	public EjbIoDbMetaSnapshotFactory<SYSTEM,MS> ejbSnapshot() {return new EjbIoDbMetaSnapshotFactory<>(cSnapshot);}
	public EjbIoDbMetaTableFactory<SYSTEM,MT> ejbTable() {return new EjbIoDbMetaTableFactory<>(cMetaTable);}
	public EjbIoDbMetaConstraintFactory<SYSTEM,MT,MC> ejbConstraint() {return new EjbIoDbMetaConstraintFactory<>(cMetaConstraint);}
}