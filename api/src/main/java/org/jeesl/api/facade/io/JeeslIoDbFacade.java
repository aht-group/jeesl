package org.jeesl.api.facade.io;

import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbBackupArchive;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbBackupFile;
import org.jeesl.interfaces.model.io.db.flyway.JeeslIoDbFlyway;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSchema;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaUnique;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.util.query.io.EjbIoDbQuery;
import org.jeesl.interfaces.util.query.io.JeeslIoDbQuery;
import org.jeesl.model.json.io.db.pg.JsonPostgres;

public interface JeeslIoDbFacade <SYSTEM extends JeeslIoSsiSystem<?,?>,
								DUMP extends JeeslDbBackupArchive<SYSTEM,DF>,
								DF extends JeeslDbBackupFile<DUMP,DH,?>,
								DH extends JeeslIoSsiHost<?,?,?>,
								
								SNAP extends JeeslDbMetaSnapshot<SYSTEM,SCHEMA,TAB,COL,CON>,
								SCHEMA extends JeeslDbMetaSchema<SYSTEM,SNAP>,
								TAB extends JeeslDbMetaTable<SYSTEM,SNAP,SCHEMA>,
								COL extends JeeslDbMetaColumn<?,TAB,?>,
								CON extends JeeslDbMetaConstraint<?,TAB,COL,?,CUN>,
								CUN extends JeeslDbMetaUnique<COL,CON>,

								FW extends JeeslIoDbFlyway
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
	
	void deleteIoDbSnapshot(SNAP snapshot) throws JeeslConstraintViolationException;
	List<SCHEMA> fIoDbMetaSchemas(JeeslIoDbQuery<SYSTEM,SNAP> query);
	List<TAB> fIoDbMetaTables(JeeslIoDbQuery<SYSTEM,SNAP> query);
	List<COL> fIoDbMetaColumns(JeeslIoDbQuery<SYSTEM,SNAP> query);
	List<CON> fIoDbMetaConstraints(JeeslIoDbQuery<SYSTEM,SNAP> query);
	List<CUN> fIoDbMetaUniques(JeeslIoDbQuery<SYSTEM,SNAP> query);
	
	List<FW> fIoDbFlyWay(EjbIoDbQuery<SYSTEM,SNAP> query);
}