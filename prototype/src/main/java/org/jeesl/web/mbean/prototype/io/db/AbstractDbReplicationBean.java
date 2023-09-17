package org.jeesl.web.mbean.prototype.io.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.factory.builder.io.db.IoDbPgFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.interfaces.model.io.db.pg.replication.JeeslDbReplicationColumn;
import org.jeesl.interfaces.model.io.db.pg.replication.JeeslDbReplicationState;
import org.jeesl.interfaces.model.io.db.pg.replication.JeeslDbReplicationSync;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.json.io.db.pg.JsonPostgresReplication;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.metachart.xml.chart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDbReplicationBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									RC extends JeeslDbReplicationColumn<L,D,RC,?>,
									RS extends JeeslDbReplicationState<L,D,RS,?>,
									RY extends JeeslDbReplicationSync<L,D,RY,?>>
						extends AbstractAdminBean<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDbReplicationBean.class);
	
	private JeeslIoDbFacade<SYSTEM,?,?,?,?,?,?,?,?> fDb;
	private final IoDbPgFactoryBuilder<L,D,?,?,RC,RS,RY> fbDb;
	
	private final Map<String,RC> mapColumn; public Map<String,RC> getMapColumn() {return mapColumn;}
	private final Map<String,RS> mapState; public Map<String,RS> getMapState() {return mapState;}
	private final Map<String,RY> mapSync; public Map<String,RY> getMapSync() {return mapSync;}
	
	private List<JsonPostgresReplication> replications; public List<JsonPostgresReplication> getReplications() {return replications;}
	
	protected Chart chart; public Chart getChart() {return chart;}
	
	public AbstractDbReplicationBean(final IoDbPgFactoryBuilder<L,D,?,?,RC,RS,RY> fbDb)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.fbDb=fbDb;

		mapColumn = new HashMap<>();
		mapState = new HashMap<>();
		mapSync = new HashMap<>();
	}
	
	public void postConstructDbReplication(JeeslIoDbFacade<SYSTEM,?,?,?,?,?,?,?,?> fDb)
	{
		this.fDb=fDb;
		
		mapColumn.putAll(EjbCodeFactory.toMapCode(fDb.allOrderedPositionVisible(fbDb.getClassReplicationColumn())));
		mapState.putAll(EjbCodeFactory.toMapCode(fDb.allOrderedPositionVisible(fbDb.getClassReplicationState())));
		mapSync.putAll(EjbCodeFactory.toMapCode(fDb.allOrderedPositionVisible(fbDb.getClassReplicationSync())));
		
		refreshList(); 
	}
	
	protected void refreshList()
	{		
		replications = fDb.postgresReplications().getReplications();
	}
}