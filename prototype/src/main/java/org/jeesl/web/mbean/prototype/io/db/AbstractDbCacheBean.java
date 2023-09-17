package org.jeesl.web.mbean.prototype.io.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.factory.builder.io.db.IoDbDumpFactoryBuilder;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatementColumn;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.json.io.db.JsonDbCacheStatistic;
import org.jeesl.model.json.io.db.pg.JsonPostgresStatement;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDbCacheBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SC extends JeeslDbStatementColumn<L,D,SC,?>>
						extends AbstractAdminBean<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDbCacheBean.class);
	
	@SuppressWarnings("unused")
	private JeeslIoDbFacade<?,?,?,?,?,?,?,?,?> fDb;
	@SuppressWarnings("unused")
	private final IoDbDumpFactoryBuilder<L,D,?,?,?,?,?> fbDb;
	
	private final Map<String,JsonDbCacheStatistic> systemStatistic; public Map<String,JsonDbCacheStatistic> getSystemStatistic() {return systemStatistic;}
	
	private final List<String> systemCaches; public List<String> getSystemCaches() {return systemCaches;}

	private final Map<String,SC> mapColumn; public Map<String,SC> getMapColumn() {return mapColumn;}
	
	private List<JsonPostgresStatement> statements; public List<JsonPostgresStatement> getStatements() {return statements;}
	
	public AbstractDbCacheBean(final IoDbDumpFactoryBuilder<L,D,?,?,?,?,?> fbDb)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.fbDb=fbDb;
		
		systemCaches = new ArrayList<>();
		systemStatistic = new HashMap<>();
		mapColumn = new HashMap<>();
	}
	
	public void postConstructDbCache(JeeslIoDbFacade<?,?,?,?,?,?,?,?,?> fDb)
	{
		this.fDb=fDb;
	}
	
	protected void addStatistic(String name, JsonDbCacheStatistic statistic)
	{
		systemCaches.add(name);
		systemStatistic.put(name, statistic);
	}
}