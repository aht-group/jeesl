package org.jeesl.api.facade.io;

import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.db.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.io.db.pg.JsonPostgres;

public interface JeeslIoDbFacade <L extends JeeslLang,D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								DUMP extends JeeslDbDump<SYSTEM,DF>,
								DF extends JeeslDbDumpFile<DUMP,DH,DS>,
								DH extends JeeslIoSsiHost<L,D,?>,
								DS extends JeeslDbDumpStatus<L,D,DS,?>>
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
}