package org.jeesl.factory.builder.io.db;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaColumnFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaConstraintFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaSchemaFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaSnapshotFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaTableFactory;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaUniqueFactory;
import org.jeesl.factory.sql.psql.SqlConstraintFactory;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumnType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraintType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaDifference;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSchema;
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
								SNAP extends JeeslDbMetaSnapshot<SYSTEM,SCHEMA,TAB,COL,CON>,
								SCHEMA extends JeeslDbMetaSchema<SYSTEM,SNAP>,
								TAB extends JeeslDbMetaTable<SYSTEM,SNAP,SCHEMA>,
								COL extends JeeslDbMetaColumn<SNAP,TAB,COLT>,
								COLT extends JeeslDbMetaColumnType<L,D,COLT,?>,
								CON extends JeeslDbMetaConstraint<SNAP,TAB,COL,CONT,UNQ>,
								CONT extends JeeslDbMetaConstraintType<L,D,CONT,?>,
								UNQ extends JeeslDbMetaUnique<COL,CON>,
								DIFF extends JeeslDbMetaDifference<L,D,DIFF,?>,
								SQL extends JeeslDbMetaSqlAction<L,D,SQL,?>
>
			extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoDbMetaFactoryBuilder.class);
		
	private final Class<SYSTEM> cSystem; public Class<SYSTEM> getClassSsiSystem() {return cSystem;}
	
	private final Class<SNAP> cSnapshot; public Class<SNAP> getClassSnapshot() {return cSnapshot;}
	private final Class<SCHEMA> cSchema; public Class<SCHEMA> getClassSchema() {return cSchema;}
	private final Class<TAB> cTable; public Class<TAB> getClassTable() {return cTable;}
	private final Class<COL> cMetaColumn; public Class<COL> getClassColumn() {return cMetaColumn;}
	private final Class<COLT> cColumnType; public Class<COLT> getClassColumnType() {return cColumnType;}
	private final Class<CON> cConstraint; public Class<CON> getClassConstraint() {return cConstraint;}
	private final Class<CONT> cConstraintType; public Class<CONT> getClassConstraintType() {return cConstraintType;}
	private final Class<UNQ> cUnique; public Class<UNQ> getClassUnique() {return cUnique;}
	private final Class<DIFF> cDifference; public Class<DIFF> getClassDifference() {return cDifference;}
	private final Class<SQL> cSql; public Class<SQL> getClassSql() {return cSql;}
	
	public IoDbMetaFactoryBuilder(final Class<L> cL, final Class<D> cD,
							final Class<SYSTEM> cSystem,
							final Class<SNAP> cSnapshot,
							final Class<SCHEMA> cSchema,
							final Class<TAB> cTable,
							final Class<COL> cMetaColumn,
							final Class<COLT> cColumnType,
							final Class<CON> cConstraint,
							final Class<CONT> cConstraintType,
							final Class<UNQ> cUnique,
							final Class<DIFF> cDifference,
							final Class<SQL> cSql)
	{
		super(cL,cD);
		this.cSystem=cSystem;
		
		this.cSnapshot=cSnapshot;
		this.cSchema=cSchema;
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
	public EjbIoDbMetaSchemaFactory<SYSTEM,SCHEMA> ejbSchema() {return new EjbIoDbMetaSchemaFactory<>(cSchema);}
	public EjbIoDbMetaTableFactory<SYSTEM,SCHEMA,TAB> ejbTable() {return new EjbIoDbMetaTableFactory<>(cTable);}
	public EjbIoDbMetaColumnFactory<TAB,COL> ejbColumn() {return new EjbIoDbMetaColumnFactory<>(cMetaColumn);}
	public EjbIoDbMetaConstraintFactory<TAB,COL,CON,CONT,UNQ> ejbConstraint() {return new EjbIoDbMetaConstraintFactory<>(cConstraint);}
	public EjbIoDbMetaUniqueFactory<COL,CON,UNQ> ejbUnique() {return new EjbIoDbMetaUniqueFactory<>(cUnique);}
	
	public SqlConstraintFactory<SCHEMA,TAB,CON> sqlConstraint() {return new SqlConstraintFactory<>();}
}