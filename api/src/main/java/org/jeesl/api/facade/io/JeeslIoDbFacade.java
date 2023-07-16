package org.jeesl.api.facade.io;

import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.util.query.io.EjbIoDbQuery;
import org.jeesl.model.json.io.db.pg.JsonPostgres;

public interface JeeslIoDbFacade <SYSTEM extends JeeslIoSsiSystem<?,?>,
								DUMP extends JeeslDbDump<SYSTEM,DF>,
								DF extends JeeslDbDumpFile<DUMP,DH,?>,
								DH extends JeeslIoSsiHost<?,?,?>,
								
								TAB extends JeeslDbMetaTable<SYSTEM,?>,
								COL extends JeeslDbMetaColumn<?,TAB,?>,
								MC extends JeeslDbMetaConstraint<?,TAB>
							>
		extends JeeslFacade
{
	List<DF> fDumpFiles(DH host);
	DF fDumpFile(DUMP dump, DH host) throws JeeslNotFoundException;
	
	String version();
	long countExact(Class<?> c);
	Map<Class<?>,Long> count(List<Class<?>> list);
	long countEstimate(Class<?> c);
	
	JsonPostgres postgresReplications();
	JsonPostgres postgresConnections(String dbName);
	JsonPostgres postgresStatements(String dbName);
	
	List<TAB> fIoDbMetaTables(EjbIoDbQuery<SYSTEM> query);
	List<COL> fIoDbMetaColumns(EjbIoDbQuery<SYSTEM> query);
	List<MC> fIoDbMetaConstraints(EjbIoDbQuery<SYSTEM> query);
}