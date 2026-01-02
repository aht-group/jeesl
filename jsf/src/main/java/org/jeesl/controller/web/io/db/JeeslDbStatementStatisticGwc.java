package org.jeesl.controller.web.io.db;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.db.IoDbPgFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.factory.ejb.io.db.pg.EjbDbStatementFactory;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.factory.txt.io.db.TxtSqlQueryFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatement;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatementColumn;
import org.jeesl.interfaces.model.io.db.pg.statement.JeeslDbStatementGroup;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslDbStatementStatisticGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									HOST extends JeeslIoSsiHost<L,D,SYSTEM>,
									ST extends JeeslDbStatement<HOST,SG>,
									SG extends JeeslDbStatementGroup<SYSTEM>,
									SC extends JeeslDbStatementColumn<L,D,SC,?>>
						extends AbstractJeeslLocaleWebController<L,D,LOC>
						implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslDbStatementStatisticGwc.class);
	
	private JeeslIoDbFacade<SYSTEM,?,?,?,?,?,?,?,?,?,?> fDb;
	private final IoDbPgFactoryBuilder<L,D,SYSTEM,HOST,?,?,?,ST,SG,SC,?,?,?> fbDb;
	
	private final EjbDbStatementFactory<HOST,ST,SG> efStatement;
	
	private final SbSingleHandler<SYSTEM> sbhSystem; public final SbSingleHandler<SYSTEM> getSbhSystem() {return sbhSystem;}
	private final SbSingleHandler<HOST> sbhHost; public final SbSingleHandler<HOST> getSbhHost() {return sbhHost;}
	private final SbSingleHandler<SG> sbhGroup; public final SbSingleHandler<SG> getSbhGroup() {return sbhGroup;}
	
	private final Map<Long,ST> mapDb; public Map<Long, ST> getMapDb() {return mapDb;}
	private final Map<String,SC> mapColumn; public Map<String,SC> getMapColumn() {return mapColumn;}
	
	private List<JsonPostgresStatement> statements; public List<JsonPostgresStatement> getStatements() {return statements;}
	
	private final String dbName;
	private LocalDateTime record;
	private JsonPostgresStatement statement; public JsonPostgresStatement getStatement() {return statement;} public void setStatement(JsonPostgresStatement statement) {this.statement = statement;}

	public JeeslDbStatementStatisticGwc(String dbName, final IoDbPgFactoryBuilder<L,D,SYSTEM,HOST,?,?,?,ST,SG,SC,?,?,?> fbDb, final IoSsiCoreFactoryBuilder<L,D,SYSTEM,?,HOST> fbSsi)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.dbName=dbName;
		this.fbDb=fbDb;
		
		efStatement = fbDb.efStatement();
		
		sbhSystem = new SbSingleHandler<>(fbSsi.getClassSystem(),this);
		sbhHost = new SbSingleHandler<>(fbSsi.getClassHost(),this);
		sbhGroup = new SbSingleHandler<>(fbDb.getClassStatementGroup(),this);
		
		mapDb = new HashMap<>();
		mapColumn = new HashMap<>();
	}
	
	public void postConstructDbStatement(JeeslIoDbFacade<SYSTEM,?,?,?,?,?,?,?,?,?,?> fDb)
	{
		this.fDb=fDb;
		mapColumn.putAll(EjbCodeFactory.toMapCode(fDb.allOrderedPositionVisible(fbDb.getClassStatementColumn())));
		refreshList(); 
	}
	public void configure(SYSTEM system, HOST host)
	{
		sbhSystem.setSelection(system);
		sbhHost.setSelection(host);
		
		sbhGroup.setList(fDb.allForParent(fbDb.getClassStatementGroup(),system));
		sbhGroup.setDefault();
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		
	}
	
	protected void refreshList()
	{
		mapDb.clear();
		record = LocalDateTime.now();
		statements = fDb.postgresStatements(dbName).getStatements();
		for(JsonPostgresStatement s : statements)
		{
			s.setXhtml(TxtSqlQueryFactory.toXhtml(TxtSqlQueryFactory.shortenIn(s.getSql())));
		}
	}
	
	public void selectStatement() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info("Select "+record.toString()+" "+statement.getId());
		if(mapDb.containsKey(statement.getId()))
		{
			fDb.rm(mapDb.get(statement.getId()));
			mapDb.remove(statement.getId());
		}
		else if(ObjectUtils.allNotNull(sbhHost.getSelection(),sbhGroup.getSelection()))
		{
			ST ejb = efStatement.build(sbhHost.getSelection(),sbhGroup.getSelection(),record,statement);
			ejb = fDb.save(ejb);
			mapDb.put(statement.getId(), ejb);
		}
		statement=null;
	}
}