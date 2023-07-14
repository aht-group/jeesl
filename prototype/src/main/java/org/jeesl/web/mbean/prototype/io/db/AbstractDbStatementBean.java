package org.jeesl.web.mbean.prototype.io.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.factory.builder.io.IoDbFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.factory.txt.system.io.db.TxtSqlQueryFactory;
import org.jeesl.interfaces.model.io.db.JeeslDbStatementColumn;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.json.io.db.pg.JsonPostgresStatement;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDbStatementBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									SC extends JeeslDbStatementColumn<L,D,SC,?>>
						extends AbstractAdminBean<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDbStatementBean.class);
	
	
	private JeeslIoDbFacade<SYSTEM,?,?,?,?> fDb;
	private final IoDbFactoryBuilder<L,D,SYSTEM,?,?,?,?,?,?,?,SC,?,?,?> fbDb;
	
	private final Map<String,SC> mapColumn; public Map<String,SC> getMapColumn() {return mapColumn;}
	
	private List<JsonPostgresStatement> statements; public List<JsonPostgresStatement> getStatements() {return statements;}
	
	private final String dbName;
	
	public AbstractDbStatementBean(String dbName, final IoDbFactoryBuilder<L,D,SYSTEM,?,?,?,?,?,?,?,SC,?,?,?> fbDb)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.dbName=dbName;
		this.fbDb=fbDb;
		
		mapColumn = new HashMap<>();
	}
	
	public void postConstructDbStatement(JeeslIoDbFacade<SYSTEM,?,?,?,?> fDb)
	{
		this.fDb=fDb;
		mapColumn.putAll(EjbCodeFactory.toMapCode(fDb.allOrderedPositionVisible(fbDb.getClassStatementColumn())));
		refreshList(); 
	}
	
	protected void refreshList()
	{		
		statements = fDb.postgresStatements(dbName).getStatements();
		for(JsonPostgresStatement s : statements)
		{
			s.setSql(TxtSqlQueryFactory.shortenIn(s.getSql()));
		}
	}
}