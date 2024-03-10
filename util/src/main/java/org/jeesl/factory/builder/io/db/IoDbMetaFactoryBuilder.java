package org.jeesl.factory.builder.io.db;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaColumnFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaConstraintFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaSnapshotFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaTableFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaUniqueFactory;
import org.jeesl.factory.sql.psql.SqlConstraintFactory;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumnType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraintType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaDifference;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSqlAction;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaUnique;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbMetaFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								SNAP extends JeeslDbMetaSnapshot<SYSTEM,TAB,COL,CON>,
								TAB extends JeeslDbMetaTable<SYSTEM,SNAP>,
								COL extends JeeslDbMetaColumn<SNAP,TAB,COLT>,
								COLT extends JeeslDbMetaColumnType<L,D,COLT,?>,
								CON extends JeeslDbMetaConstraint<SNAP,TAB,COL,CONT,CUN>,
								CONT extends JeeslDbMetaConstraintType<L,D,CONT,?>,
								CUN extends JeeslDbMetaUnique<COL,CON>,
								DIFF extends JeeslDbMetaDifference<L,D,DIFF,?>,
								SQL extends JeeslDbMetaSqlAction<L,D,SQL,?>
>
			extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoDbMetaFactoryBuilder.class);
		
	private final Class<SYSTEM> cSystem; public Class<SYSTEM> getClassSsiSystem() {return cSystem;}
	
	private final Class<SNAP> cSnapshot; public Class<SNAP> getClassSnapshot(){return cSnapshot;}
	private final Class<TAB> cTable; public Class<TAB> getClassTable(){return cTable;}
	private final Class<COL> cMetaColumn; public Class<COL> getClassMetaColumn(){return cMetaColumn;}
	private final Class<COLT> cColumnType; public Class<COLT> getClassColumnType(){return cColumnType;}
	private final Class<CON> cConstraint; public Class<CON> getClassConstraint(){return cConstraint;}
	private final Class<CONT> cConstraintType; public Class<CONT> getClassConstraintType() {return cConstraintType;}
	private final Class<CUN> cUnique; public Class<CUN> getClassUnique() {return cUnique;}
	private final Class<DIFF> cDifference; public Class<DIFF> getClassDifference(){return cDifference;}
	private final Class<SQL> cSql; public Class<SQL> getClassSql(){return cSql;}
	
	public IoDbMetaFactoryBuilder(final Class<L> cL, final Class<D> cD,
							final Class<SYSTEM> cSystem,
							final Class<SNAP> cSnapshot,
							final Class<TAB> cTable,
							final Class<COL> cMetaColumn,
							final Class<COLT> cColumnType,
							final Class<CON> cConstraint,
							final Class<CONT> cConstraintType,
							final Class<CUN> cUnique,
							final Class<DIFF> cDifference,
							final Class<SQL> cSql)
	{
		super(cL,cD);
		this.cSystem=cSystem;
		
		this.cSnapshot=cSnapshot;
		this.cTable=cTable;
		this.cMetaColumn=cMetaColumn;
		this.cColumnType=cColumnType;
		this.cConstraint=cConstraint;
		this.cConstraintType=cConstraintType;
		this.cUnique=cUnique;
		this.cDifference=cDifference;
		this.cSql=cSql;
	}

	public EjbIoDbMetaSnapshotFactory<SYSTEM,SNAP> ejbSnapshot() {return new EjbIoDbMetaSnapshotFactory<>(cSnapshot);}
	public EjbIoDbMetaTableFactory<SYSTEM,TAB> ejbTable() {return new EjbIoDbMetaTableFactory<>(cTable);}
	public EjbIoDbMetaColumnFactory<TAB,COL> ejbColumn() {return new EjbIoDbMetaColumnFactory<>(cMetaColumn);}
	public EjbIoDbMetaConstraintFactory<TAB,CON> ejbConstraint() {return new EjbIoDbMetaConstraintFactory<>(cConstraint);}
	public EjbIoDbMetaUniqueFactory<COL,CON,CUN> ejbUnique() {return new EjbIoDbMetaUniqueFactory<>(cUnique);}
	
	public SqlConstraintFactory<TAB,CON> sqlConstraint() {return new SqlConstraintFactory<>();}
}