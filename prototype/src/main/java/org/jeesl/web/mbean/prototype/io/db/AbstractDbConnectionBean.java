package org.jeesl.web.mbean.prototype.io.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.factory.builder.io.db.IoDbDumpFactoryBuilder;
import org.jeesl.factory.builder.io.db.IoDbPgFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.interfaces.model.io.db.pg.JeeslDbConnectionColumn;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.json.io.db.pg.JsonPostgresConnection;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDbConnectionBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									CC extends JeeslDbConnectionColumn<L,D,CC,?>>
						extends AbstractAdminBean<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDbConnectionBean.class);
	
	
	private JeeslIoDbFacade<SYSTEM,?,?,?,?,?,?> fDb;
	private final IoDbPgFactoryBuilder<L,D,CC,?,?,?,?> fbDbPg;
	
	private final Map<String,CC> mapColumn; public Map<String,CC> getMapColumn() {return mapColumn;}
	
	private List<JsonPostgresConnection> connections; public List<JsonPostgresConnection> getConnections() {return connections;}
	
	private final String dbName;
	
	public AbstractDbConnectionBean(String dbName, IoDbPgFactoryBuilder<L,D,CC,?,?,?,?> fbDbPg)
	{
		super(fbDbPg.getClassL(),fbDbPg.getClassD());
		this.dbName=dbName;
		this.fbDbPg=fbDbPg;
		
		mapColumn = new HashMap<>();
	}
	
	public void postConstructDbReplication(JeeslIoDbFacade<SYSTEM,?,?,?,?,?,?> fDb)
	{
		this.fDb=fDb;
		mapColumn.putAll(EjbCodeFactory.toMapCode(fDb.allOrderedPositionVisible(fbDbPg.getClassConnectionColumn())));
		refreshList(); 
	}
	
	protected void refreshList()
	{		
		connections = fDb.postgresConnections(dbName).getConnections();
	}
}