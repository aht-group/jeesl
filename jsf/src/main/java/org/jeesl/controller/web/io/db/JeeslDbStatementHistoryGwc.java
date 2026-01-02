package org.jeesl.controller.web.io.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.db.IoDbPgFactoryBuilder;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.factory.ejb.io.db.pg.EjbDbStatementGroupFactory;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.factory.json.io.db.pg.JsonPostgresStatementFactory;
import org.jeesl.factory.json.io.db.pg.JsonPostgresStatementGroupFactory;
import org.jeesl.factory.txt.io.db.TxtSqlQueryFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.web.io.db.JeeslIoDbStatementHistoryCallback;
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
import org.jeesl.model.json.io.db.pg.statement.JsonPostgresStatementGroup;
import org.jeesl.util.query.json.JsonIoDbQueryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslDbStatementHistoryGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									HOST extends JeeslIoSsiHost<L,D,SYSTEM>,
									ST extends JeeslDbStatement<HOST,SG>,
									SG extends JeeslDbStatementGroup<SYSTEM>,
									SC extends JeeslDbStatementColumn<L,D,SC,?>>
						extends AbstractJeeslLocaleWebController<L,D,LOC>
						implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslDbStatementHistoryGwc.class);
	
	private final JeeslIoDbStatementHistoryCallback callback;
	
	private JeeslIoDbFacade<SYSTEM,?,?,?,?,?,?,?,?,?,?> fDb;
	private final IoDbPgFactoryBuilder<L,D,SYSTEM,HOST,?,?,?,ST,SG,SC,?,?,?> fbDb;
	private final IoSsiCoreFactoryBuilder<L,D,SYSTEM,?,HOST> fbSsi;
	
//	private final EjbDbStatementFactory<HOST,ST,SG> efStatement;
	private final EjbDbStatementGroupFactory<SYSTEM,SG> efGroup;
	
	private final SbSingleHandler<SYSTEM> sbhSystem; public final SbSingleHandler<SYSTEM> getSbhSystem() {return sbhSystem;}
	
	private final Map<String,SC> mapColumn; public Map<String,SC> getMapColumn() {return mapColumn;}
	private final Map<ST,String> mapSql; public Map<ST,String> getMapSql() {return mapSql;}

	private final List<SG> groups; public List<SG> getGroups() {return groups;}
	private final List<ST> statements; public List<ST> getStatements() {return statements;}
	
	private SG group; public SG getGroup() {return group;} public void setGroup(SG group) {this.group = group;}

	private ST statement; public ST getStatement() {return statement;} public void setStatement(ST statement) {this.statement = statement;}

	public JeeslDbStatementHistoryGwc(JeeslIoDbStatementHistoryCallback callback,
										final IoDbPgFactoryBuilder<L,D,SYSTEM,HOST,?,?,?,ST,SG,SC,?,?,?> fbDb,
										final IoSsiCoreFactoryBuilder<L,D,SYSTEM,?,HOST> fbSsi)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.callback=callback;
		this.fbDb=fbDb;
		this.fbSsi=fbSsi;
		
//		efStatement = fbDb.efStatement();
		efGroup  = fbDb.efGroup(); 
		
		sbhSystem = new SbSingleHandler<>(fbSsi.getClassSystem(),this);
		
		mapColumn = new HashMap<>();
		mapSql = new HashMap<>();
		
		groups = new ArrayList<>();
		statements = new ArrayList<>();
	}
	
	public void postConstructDbStatement(JeeslIoDbFacade<SYSTEM,?,?,?,?,?,?,?,?,?,?> fDb)
	{
		this.fDb=fDb;
		mapColumn.putAll(EjbCodeFactory.toMapCode(fDb.allOrderedPositionVisible(fbDb.getClassStatementColumn())));
		sbhSystem.setList(fDb.all(fbSsi.getClassSystem()));
		sbhSystem.setDefault();
		this.reloadGroups(); 
	}
	
	private void reset(boolean rGroups, boolean rGroup, boolean rStatements, boolean rStatement)
	{
		if(rGroups) {groups.clear();}
		if(rGroup) {group=null;}
		if(rStatements) {statements.clear(); mapSql.clear();}
		if(rStatement) {statement=null;}
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(fbSsi.getClassSystem().isAssignableFrom(item.getClass())) {this.reset(true,false,true,true); this.reloadGroups();}
		if(fbDb.getClassStatementGroup().isAssignableFrom(item.getClass())) {this.reset(false,false,false,true); this.reloadStatements();}
	}
	
	private void reloadGroups()
	{
		this.reset(true,false,false,false);
		if(sbhSystem.isSelected())
		{
			groups.addAll(fDb.allForParent(fbDb.getClassStatementGroup(),sbhSystem.getSelection()));
		}
	}
	
	public void addGroup()
	{
		group = efGroup.build(sbhSystem.getSelection(), groups);
	}
	
	public void selectGroup() throws JeeslConstraintViolationException, JeeslLockingException
	{
		this.reset(false,false,true,true);
		logger.info(AbstractLogMessage.selectEntity(group));
		
		this.reloadStatements();
	}
	
	public void saveGroup() throws JeeslConstraintViolationException, JeeslLockingException
	{
		group = fDb.save(group);
		this.reloadGroups();
	}
	
	private void reloadStatements()
	{
		this.reset(false,false,true,false);
		statements.addAll(fDb.allForParent(fbDb.getClassStatement(),group));
		for(ST s : statements)
		{
			mapSql.put(s,TxtSqlQueryFactory.toXhtml(TxtSqlQueryFactory.shortenIn(s.getSql())));
		}
		Collections.reverse(statements);
		logger.info(AbstractLogMessage.reloaded(fbDb.getClassStatement(),statements));
	}
	
	public void selectStatement()
	{
		logger.info(AbstractLogMessage.selectEntity(statement));
	}
	
	public void removeStatement() throws JeeslConstraintViolationException
	{
		fDb.rm(statement);
		this.reset(false,false,false,true);
		this.reloadStatements();
	}
	
	public void upload()
	{
		JsonPostgresStatementGroupFactory<SYSTEM,SG> jfGroup = fbDb.jfGroup(JsonIoDbQueryProvider.statementGroup());
		JsonPostgresStatementFactory<HOST,ST,SG> jfStatement = fbDb.jfStatement(JsonIoDbQueryProvider.statement());
		
		group = fDb.find(fbDb.getClassStatementGroup(),group);
		
		JsonPostgresStatementGroup json = jfGroup.build(group);
		json.setStatements(new ArrayList<>());
		for(ST st : statements)
		{
			json.getStatements().add(jfStatement.build(st));
		}
		
		callback.upload(json);
	}
}