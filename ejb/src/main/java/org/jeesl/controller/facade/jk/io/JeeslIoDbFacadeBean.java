
package org.jeesl.controller.facade.jk.io;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.controller.facade.jk.JeeslFacadeBean;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.db.IoDbDumpFactoryBuilder;
import org.jeesl.factory.builder.io.db.IoDbMetaFactoryBuilder;
import org.jeesl.factory.json.system.io.db.JsonPostgresConnectionFactory;
import org.jeesl.factory.json.system.io.db.JsonPostgresFactory;
import org.jeesl.factory.json.system.io.db.JsonPostgresStatementFactory;
import org.jeesl.factory.sql.system.db.SqlDbPgStatFactory;
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
import org.jeesl.model.json.io.db.pg.JsonPostgresReplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class JeeslIoDbFacadeBean <SYSTEM extends JeeslIoSsiSystem<?,?>,
								DUMP extends JeeslDbBackupArchive<SYSTEM,DF>,
								DF extends JeeslDbBackupFile<DUMP,DH,?>,
								DH extends JeeslIoSsiHost<?,?,?>,
								SNAP extends JeeslDbMetaSnapshot<SYSTEM,TAB,COL,CON>,
								SCHEMA extends JeeslDbMetaSchema<SYSTEM,SNAP>,
								TAB extends JeeslDbMetaTable<SYSTEM,SNAP,SCHEMA>,
								COL extends JeeslDbMetaColumn<SNAP,TAB,?>,
								CON extends JeeslDbMetaConstraint<SNAP,TAB,COL,?,CUN>,
								CUN extends JeeslDbMetaUnique<COL,CON>,
								FW extends JeeslIoDbFlyway>
		extends JeeslFacadeBean implements JeeslIoDbFacade<SYSTEM,DUMP,DF,DH,SNAP,SCHEMA,TAB,COL,CON,CUN,FW>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslIoDbFacadeBean.class);
	
	private final IoDbDumpFactoryBuilder<?,?,SYSTEM,DUMP,DF,DH,?> fbDb;
	private final IoDbMetaFactoryBuilder<?,?,SYSTEM,SNAP,?,TAB,COL,?,CON,?,CUN,?,?> fbDbMeta;
	
	public JeeslIoDbFacadeBean(EntityManager em, final IoDbDumpFactoryBuilder<?,?,SYSTEM,DUMP,DF,DH,?> fbDb, IoDbMetaFactoryBuilder<?,?,SYSTEM,SNAP,?,TAB,COL,?,CON,?,CUN,?,?> fbDbMeta){this(em,fbDb,fbDbMeta,false);}
	public JeeslIoDbFacadeBean(EntityManager em, final IoDbDumpFactoryBuilder<?,?,SYSTEM,DUMP,DF,DH,?> fbDb, IoDbMetaFactoryBuilder<?,?,SYSTEM,SNAP,?,TAB,COL,?,CON,?,CUN,?,?> fbDbMeta, boolean handleTransaction)
	{
		super(em,handleTransaction);
		this.fbDb=fbDb;
		this.fbDbMeta=fbDbMeta;
	}
	
	@Override public List<DF> fDumpFiles(DH host) 
	{
		return this.allForParent(fbDb.getClassDumpFile(),JeeslDbBackupFile.Attributes.host, host);
	}
	
	@Override public DF fDumpFile(DUMP dump, DH host) throws JeeslNotFoundException
	{
		return this.oneForParents(fbDb.getClassDumpFile(), JeeslDbBackupFile.Attributes.dump,dump, JeeslDbBackupFile.Attributes.host,host);
	}
	
	@Override public String version()
	{
		Query q = em.createQuery("select version()");
		Object o = q.getSingleResult();
		return (String)o;
	}
	
	@Override public long countExact(Class<?> c)
	{
		Query q = em.createQuery("select count(*) FROM "+ c.getSimpleName());

		Object o = q.getSingleResult();
		return (Long)o;
	}
	
	@Override public long countEstimate(Class<?> c)
	{
		if(c.getAnnotation(javax.persistence.Table.class)!=null)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT CAST(reltuples AS BIGINT)");
			sb.append(" FROM pg_class");
			sb.append(" WHERE relname='").append(c.getAnnotation(javax.persistence.Table.class).name().toLowerCase()).append("';");
			
			Query q = em.createNativeQuery(sb.toString());
			for(Object o : q.getResultList())
	        {
				BigInteger i = (BigInteger)o;
	            return i.longValue();
	        }
		}
		else
		{
			return -1;
		}
		return -1;
	}
	
	@Override
	public Map<Class<?>, Long> count(List<Class<?>> list)
	{
		Map<Class<?>, Long> result = new Hashtable<Class<?>,Long>();
		for(Class<?> c : list)
		{
			result.put(c,countExact(c));
		}
		return result;
	}
	
	public JsonPostgres postgresConnections(String dbName)
	{
		JsonPostgres json = JsonPostgresFactory.build();
		int i=1;
		for(Object o : em.createNativeQuery(SqlDbPgStatFactory.connections(dbName)).getResultList())
		{
			Object[] array = (Object[])o;
			json.getConnections().add(JsonPostgresConnectionFactory.build(i, array));
			i++;
		}
		return json;
	}
	
	/**
	 *	This will only work when grant "pg_monitor" access to main user in this case "jeesl".
	 *  GRANT pg_monitor TO jeesl;"
	 */
	@Override
	public JsonPostgres postgresReplications()
	{
		List<String> fileds = new ArrayList<String>();
		fileds.add("pid");
		fileds.add("state");
		fileds.add("cast (client_addr as text)");
		fileds.add("extract('milliseconds' from write_lag) as wl");
		fileds.add("extract('milliseconds' from flush_lag) as fl");
		fileds.add("extract('milliseconds' from replay_lag) as rl");
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT "+StringUtils.join(fileds, ", "));
		sb.append(" FROM pg_stat_replication");						
		logger.info(sb.toString());
		
		
		JsonPostgres j = JsonPostgresFactory.build();
		for(Object o : em.createNativeQuery(sb.toString()).getResultList())
		{
			Object[] array = (Object[])o;
			debugDataTypes(array);
			
			JsonPostgresReplication json = new JsonPostgresReplication();
			json.setId(((Integer)array[0]).longValue());
			json.setState(((String)array[1]).toString());
			json.setClientAddr(((String)array[2].toString()));
			if (array[3]!=null) {json.setWriteLag(((BigInteger) array[3]).doubleValue());}  else {json.setWriteLag((double)0);}
			if (array[4]!=null) {json.setFlushLag(((BigInteger) array[4]).doubleValue());}  else {json.setFlushLag((double)0);}
			if (array[5]!=null) {json.setReplayLag(((BigInteger) array[5]).doubleValue());} else {json.setReplayLag((double)0);}
			j.getReplications().add(json);
		}
		
		return j;
	}
	
	@Override public JsonPostgres postgresStatements(String dbName)
	{		
		JsonPostgres json = JsonPostgresFactory.build();
		
		long i=1;
		for(Object o : em.createNativeQuery(SqlDbPgStatFactory.statements14(dbName)).getResultList())
		{
			Object[] array = (Object[])o;
			json.getStatements().add(JsonPostgresStatementFactory.build(i,array));
			i++;
		}		
		return json;
	}
	
	public static void debugDataTypes(Object[] array)
	{
		logger.info("*****************************");
		int i=0;
		for(Object o : array)
		{
			if(o!=null)
			{
				logger.info(i+" "+o.getClass().getName());
			}
			else
			{
				logger.info(i+" NULL");
			}
			i++;
		}
	}
	
	@Override
	public void deleteIoDbSnapshot(SNAP snapshot) throws JeeslConstraintViolationException
	{
		snapshot = em.find(fbDbMeta.getClassSnapshot(), snapshot.getId());
		snapshot.getTables().clear();
		snapshot.getColumns().clear();
		snapshot.getConstraints().clear();
		this.rmProtected(snapshot);
		
	}
	
	@Override
	public List<SCHEMA> fIoDbMetaSchemas(JeeslIoDbQuery<SYSTEM, SNAP> query)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override public List<TAB> fIoDbMetaTables(JeeslIoDbQuery<SYSTEM,SNAP> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TAB> cQ = cB.createQuery(fbDbMeta.getClassTable());
		Root<TAB> root = cQ.from(fbDbMeta.getClassTable());
		
		if(ObjectUtils.isNotEmpty(query.getCodeList()))
		{
			Expression<String> eCode = root.get(JeeslDbMetaTable.Attributes.code.toString());
			predicates.add(eCode.in(query.getCodeList()));
		}
		if(ObjectUtils.isNotEmpty(query.getSystems()))
		{
			Join<TAB,SYSTEM> jSystem = root.join(JeeslDbMetaTable.Attributes.system.toString());
			predicates.add(jSystem.in(query.getSystems()));
		}
		if(ObjectUtils.isNotEmpty(query.getSnapshots()))
		{
			ListJoin<TAB,SNAP> jSnapshot = root.joinList(JeeslDbMetaTable.Attributes.snapshots.toString());
			predicates.add(jSnapshot.in(query.getSnapshots()));
		}
		
		cQ.select(root);	    
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<COL> fIoDbMetaColumns(JeeslIoDbQuery<SYSTEM,SNAP> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<COL> cQ = cB.createQuery(fbDbMeta.getClassMetaColumn());
		Root<COL> root = cQ.from(fbDbMeta.getClassMetaColumn());
		
		if(ObjectUtils.isNotEmpty(query.getCodeList()))
		{
			Expression<String> eCode = root.get(JeeslDbMetaConstraint.Attributes.code.toString());
			predicates.add(eCode.in(query.getCodeList()));
		}
		if(ObjectUtils.isNotEmpty(query.getSystems()))
		{
			Join<COL,TAB> jTable = root.join(JeeslDbMetaColumn.Attributes.table.toString());
			Join<TAB,SYSTEM> jSystem = jTable.join(JeeslDbMetaTable.Attributes.system.toString());
			predicates.add(jSystem.in(query.getSystems()));
		}
		if(ObjectUtils.isNotEmpty(query.getSnapshots()))
		{
			ListJoin<TAB,SNAP> jSnapshot = root.joinList(JeeslDbMetaTable.Attributes.snapshots.toString());
			predicates.add(jSnapshot.in(query.getSnapshots()));
		}
		
		cQ.select(root);	    
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<CON> fIoDbMetaConstraints(JeeslIoDbQuery<SYSTEM,SNAP> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<CON> cQ = cB.createQuery(fbDbMeta.getClassConstraint());
		Root<CON> root = cQ.from(fbDbMeta.getClassConstraint());
		if(ObjectUtils.isNotEmpty(query.getRootFetches())) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		if(ObjectUtils.isNotEmpty(query.getCodeList()))
		{
			Expression<String> eCode = root.get(JeeslDbMetaConstraint.Attributes.code.toString());
			predicates.add(eCode.in(query.getCodeList()));
		}
		if(ObjectUtils.isNotEmpty(query.getSystems()))
		{
			Join<CON,TAB> jTable = root.join(JeeslDbMetaConstraint.Attributes.table.toString());
			Join<TAB,SYSTEM> jSystem = jTable.join(JeeslDbMetaTable.Attributes.system.toString());
			predicates.add(jSystem.in(query.getSystems()));
		}
		if(ObjectUtils.isNotEmpty(query.getSnapshots()))
		{
			ListJoin<CON,SNAP> jSnapshot = root.joinList(JeeslDbMetaConstraint.Attributes.snapshots.toString());
			predicates.add(jSnapshot.in(query.getSnapshots()));
		}
		
		cQ.select(root);	    
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.distinct(BooleanComparator.active(query.getDistinct()));
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<CUN> fIoDbMetaUniques(JeeslIoDbQuery<SYSTEM, SNAP> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<CUN> cQ = cB.createQuery(fbDbMeta.getClassUnique());
		Root<CUN> root = cQ.from(fbDbMeta.getClassUnique());
		if(ObjectUtils.isNotEmpty(query.getRootFetches())) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		Join<CUN,CON> jConstaint = root.join(JeeslDbMetaUnique.Attributes.constraint.toString());
		
		if(ObjectUtils.isNotEmpty(query.getCodeList()))
		{
			Expression<String> eCode = jConstaint.get(JeeslDbMetaConstraint.Attributes.code.toString());
			predicates.add(eCode.in(query.getCodeList()));
		}
		if(ObjectUtils.isNotEmpty(query.getSystems()))
		{
			Join<CON,TAB> jTable = jConstaint.join(JeeslDbMetaConstraint.Attributes.table.toString());
			Join<TAB,SYSTEM> jSystem = jTable.join(JeeslDbMetaTable.Attributes.system.toString());
			predicates.add(jSystem.in(query.getSystems()));
		}
		if(ObjectUtils.isNotEmpty(query.getSnapshots()))
		{
			ListJoin<CON,SNAP> jSnapshot = jConstaint.joinList(JeeslDbMetaConstraint.Attributes.snapshots.toString());
			predicates.add(jSnapshot.in(query.getSnapshots()));
		}
		
		cQ.select(root);	    
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<FW> fIoDbFlyWay(EjbIoDbQuery<SYSTEM, SNAP> query)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}