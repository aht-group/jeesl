package org.jeesl.web.mbean.prototype.io.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.factory.builder.io.db.IoDbPgFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.interfaces.model.io.db.pg.connection.JeeslDbConnectionColumn;
import org.jeesl.interfaces.model.io.db.pg.connection.JeeslDbConnectionState;
import org.jeesl.interfaces.model.io.db.pg.connection.JeeslDbConnectionWait;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.json.io.db.pg.JsonPostgresConnection;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDbConnectionBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									CC extends JeeslDbConnectionColumn<L,D,CC,?>,
									CS extends JeeslDbConnectionState<L,D,CS,?>,
									CW extends JeeslDbConnectionWait<L,D,CW,?>>
						extends AbstractAdminBean<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDbConnectionBean.class);
	
	
	private JeeslIoDbFacade<SYSTEM,?,?,?,?,?,?,?,?,?> fDb;
	private final IoDbPgFactoryBuilder<L,D,SYSTEM,?,CC,CS,CW,?,?,?,?,?,?> fbPg;
	
	private final EjbCodeCache<CS> cacheState; public EjbCodeCache<CS> getCacheState() {return cacheState;}
	private final EjbCodeCache<CW> cacheWait; public EjbCodeCache<CW> getCacheWait() {return cacheWait;}

	private final Map<String,CC> mapColumn; public Map<String,CC> getMapColumn() {return mapColumn;}
	
	private List<JsonPostgresConnection> connections; public List<JsonPostgresConnection> getConnections() {return connections;}
	
	private final String dbName;
	
	public AbstractDbConnectionBean(String dbName, IoDbPgFactoryBuilder<L,D,SYSTEM,?,CC,CS,CW,?,?,?,?,?,?> fbPg)
	{
		super(fbPg.getClassL(),fbPg.getClassD());
		this.dbName=dbName;
		this.fbPg=fbPg;
		
		cacheState = EjbCodeCache.instance(fbPg.getClassConnectionState());
		cacheWait = EjbCodeCache.instance(fbPg.getClassConnectionWait());
		
		mapColumn = new HashMap<>();
	}
	
	public void postConstructDbReplication(JeeslIoDbFacade<SYSTEM,?,?,?,?,?,?,?,?,?> fDb)
	{
		this.fDb=fDb;
		cacheState.facade(fDb).load();
		cacheWait.facade(fDb).load();
		mapColumn.putAll(EjbCodeFactory.toMapCode(fDb.allOrderedPositionVisible(fbPg.getClassConnectionColumn())));
		refreshList(); 
	}
	
	protected void refreshList()
	{		
		connections = fDb.postgresConnections(dbName).getConnections();
	}
}