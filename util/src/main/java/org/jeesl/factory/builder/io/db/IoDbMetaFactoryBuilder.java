package org.jeesl.factory.builder.io.db;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaColumnFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaConstraintFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaSnapshotFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaTableFactory;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumnType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaDifference;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbMetaFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								MS extends JeeslDbMetaSnapshot<SYSTEM,TAB,COL,MC>,
								TAB extends JeeslDbMetaTable<SYSTEM,MS>,
								COL extends JeeslDbMetaColumn<MS,TAB,COLT>,
								COLT extends JeeslDbMetaColumnType<L,D,COLT,?>,
								MC extends JeeslDbMetaConstraint<MS,TAB>,
								DIFF extends JeeslDbMetaDifference<L,D,DIFF,?>
								
>
			extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoDbMetaFactoryBuilder.class);
		
	private final Class<SYSTEM> cSystem; public Class<SYSTEM> getClassSsiSystem() {return cSystem;}
	
	private final Class<MS> cSnapshot; public Class<MS> getClassSnapshot(){return cSnapshot;}
	private final Class<TAB> cTable; public Class<TAB> getClassTable(){return cTable;}
	private final Class<COL> cMetaColumn; public Class<COL> getClassMetaColumn(){return cMetaColumn;}
	private final Class<COLT> cColumnType; public Class<COLT> getClassColumnType(){return cColumnType;}
	private final Class<MC> cMetaConstraint; public Class<MC> getClassMetaConstraint(){return cMetaConstraint;}
	private final Class<DIFF> cDifference; public Class<DIFF> getClassDifference(){return cDifference;}
	

	
	public IoDbMetaFactoryBuilder(final Class<L> cL, final Class<D> cD,
							final Class<SYSTEM> cSystem,
							final Class<MS> cSnapshot,
							final Class<TAB> cTable,
							final Class<COL> cMetaColumn,
							final Class<COLT> cColumnType,
							final Class<MC> cMetaConstraint,
							final Class<DIFF> cDifference)
	{
		super(cL,cD);
		this.cSystem=cSystem;
		
		this.cSnapshot=cSnapshot;
		this.cTable=cTable;
		this.cMetaColumn=cMetaColumn;
		this.cColumnType=cColumnType;
		this.cMetaConstraint=cMetaConstraint;
		this.cDifference=cDifference;
		
	}	
	public EjbIoDbMetaSnapshotFactory<SYSTEM,MS> ejbSnapshot() {return new EjbIoDbMetaSnapshotFactory<>(cSnapshot);}
	public EjbIoDbMetaTableFactory<SYSTEM,TAB> ejbTable() {return new EjbIoDbMetaTableFactory<>(cTable);}
	public EjbIoDbMetaColumnFactory<TAB,COL> ejbColumn() {return new EjbIoDbMetaColumnFactory<>(cMetaColumn);}
	public EjbIoDbMetaConstraintFactory<TAB,MC> ejbConstraint() {return new EjbIoDbMetaConstraintFactory<>(cMetaConstraint);}
}