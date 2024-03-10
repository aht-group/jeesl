package org.jeesl.controller.web.g.io.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.io.StringUtil;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.controller.util.comparator.ejb.io.db.EjbIoDbConstraintComparator;
import org.jeesl.controller.util.comparator.ejb.io.db.EjbIoDbTableComparator;
import org.jeesl.controller.util.comparator.ejb.module.calendar.EjbWithRecordJtComparator;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.db.IoDbMetaFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.sql.psql.SqlConstraintFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.th.ThMultiFilter;
import org.jeesl.interfaces.bean.th.ThMultiFilterBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.io.db.JeeslIoDbMetaCallback;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumnType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraintType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaDifference;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSqlAction;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaUnique;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.EjbIoDbQuery;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.handler.th.ThMultiFilterHandler;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaTable;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslDbMetaGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								SNAP extends JeeslDbMetaSnapshot<SYSTEM,TAB,COL,CON>,
								TAB extends JeeslDbMetaTable<SYSTEM,SNAP>,
								COL extends JeeslDbMetaColumn<SNAP,TAB,COLT>,
								COLT extends JeeslDbMetaColumnType<L,D,COLT,?>,
								CON extends JeeslDbMetaConstraint<SNAP,TAB,COL,CONT,CUN>,
								CONT extends JeeslDbMetaConstraintType<L,D,CONT,?>,
								CUN extends JeeslDbMetaUnique<COL,CON>,
								DIFF extends JeeslDbMetaDifference<L,D,DIFF,?>,
								SQL extends JeeslDbMetaSqlAction<L,D,SQL,?>
>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements SbSingleBean,ThMultiFilterBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslDbMetaGwc.class);
	
	private final IoDbMetaFactoryBuilder<L,D,SYSTEM,SNAP,TAB,COL,COLT,CON,CONT,CUN,DIFF,SQL> fbDb;
	
	private JeeslIoDbFacade<SYSTEM,?,?,?,SNAP,TAB,COL,CON,CUN,?> fDb;
	private final JeeslIoDbMetaCallback callback;
	
	private final SbSingleHandler<SYSTEM> sbhSystem; public SbSingleHandler<SYSTEM> getSbhSystem() {return sbhSystem;}
	
	private final ThMultiFilterHandler<DIFF> thFilterTable; public ThMultiFilterHandler<DIFF> getThFilterTable() {return thFilterTable;}
	private final ThMultiFilterHandler<DIFF> thFilterColumn; public ThMultiFilterHandler<DIFF> getThFilterColumn() {return thFilterColumn;}
	private final ThMultiFilterHandler<DIFF> thFilterConstraint; public ThMultiFilterHandler<DIFF> getThFilterConstraint() {return thFilterConstraint;}
	private final ThMultiFilterHandler<SQL> thAction; public ThMultiFilterHandler<SQL> getThAction() {return thAction;}
	
	private final SqlConstraintFactory<TAB,CON> sfConstraint;
	
	private final EjbCodeCache<DIFF> cacheDiff;
	private final EjbCodeCache<CONT> cacheType;
	
	private final Comparator<SNAP> cpSnapshot;
	private final Comparator<TAB> cpTable;
	private final Comparator<CON> cpConstraint;
	
	private final Map<TAB,List<COL>> mapColumn; public Map<TAB,List<COL>> getMapColumn() {return mapColumn;}
	private final Map<TAB,List<CON>> mapConstraint; public Map<TAB,List<CON>> getMapConstraint() {return mapConstraint;}
	private final Map<TAB,DIFF> mapDiffTable; public Map<TAB, DIFF> getMapDiffTable() {return mapDiffTable;}
	private final Map<COL,DIFF> mapDiffColumn; public Map<COL,DIFF> getMapDiffColumn() {return mapDiffColumn;}
	private final Map<CON,DIFF> mapDiffConstraint; public Map<CON,DIFF> getMapDiffConstraint() {return mapDiffConstraint;}
	private final Map<TAB,List<String>> mapAction; public Map<TAB,List<String>> getMapAction() {return mapAction;}
	
	private final Set<TAB> setSourceTable, setTargetTable;
	private final Set<COL> setSourceColumn, setTargetColumn;
	private final Set<CON> setSourceConstraint, setTargetConstraint;
	
	private final List<SNAP> snapshots; public List<SNAP> getSnapshots() {return snapshots;}
	private final List<TAB> systemTables;
	private final List<TAB> snapshotTables; public List<TAB> getSnapshotTables() {return snapshotTables;}
	private List<TAB> filteredTables; public void setFilteredTables(List<TAB> filteredTables) {this.filteredTables = filteredTables;}
	public List<TAB> getFilteredTables() {return filteredTables;}
	private final List<COL> systemColumn;
	private final List<CON> systemConstraints;
	
	private SNAP snapshot; public SNAP getSnapshot() {return snapshot;} public void setSnapshot(SNAP snapshot) {this.snapshot = snapshot;}
	private SNAP snapshotSource; public SNAP getSnapshotSource() {return snapshotSource;} public void setSnapshotSource(SNAP snapshotSource) {this.snapshotSource = snapshotSource;}
	private SNAP snapshotTarget; public SNAP getSnapshotTarget() {return snapshotTarget;} public void setSnapshotTarget(SNAP snapshotTarget) {this.snapshotTarget = snapshotTarget;}

	private String sqlClipboard; public String getSqlClipboard() {return sqlClipboard;} public void setSqlClipboard(String sqlClipboard) {this.sqlClipboard = sqlClipboard;}
	
	public JeeslDbMetaGwc(JeeslIoDbMetaCallback callback, IoDbMetaFactoryBuilder<L,D,SYSTEM,SNAP,TAB,COL,COLT,CON,CONT,CUN,DIFF,SQL> fbDb)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.callback = callback;
		this.fbDb = fbDb;
		
		sbhSystem = new SbSingleHandler<>(fbDb.getClassSsiSystem(),this);
		
		thFilterTable = new ThMultiFilterHandler<>(this);
		thFilterColumn = new ThMultiFilterHandler<>(this);
		thFilterConstraint = new ThMultiFilterHandler<>(this);
		thAction = new ThMultiFilterHandler<>(this);
		
		sfConstraint = fbDb.sqlConstraint();
		
		cacheDiff = EjbCodeCache.instance(fbDb.getClassDifference());
		cacheType= EjbCodeCache.instance(fbDb.getClassConstraintType());
		
		cpSnapshot = new EjbWithRecordJtComparator<SNAP>().factory(EjbWithRecordJtComparator.Type.dsc); 
		cpTable = new EjbIoDbTableComparator<TAB>().instance(EjbIoDbTableComparator.Type.code);
		cpConstraint = new EjbIoDbConstraintComparator<COL,CON>().instance(EjbIoDbConstraintComparator.Type.col);
		
		mapColumn = new HashMap<>();
		mapConstraint = new HashMap<>();
		mapDiffTable = new HashMap<>();
		mapDiffColumn = new HashMap<>();
		mapDiffConstraint = new HashMap<>();
		mapAction = new HashMap<>();
		
		setSourceTable = new HashSet<>(); setTargetTable = new HashSet<>();
		setSourceColumn = new HashSet<>(); setTargetColumn = new HashSet<>();
		setSourceConstraint = new HashSet<>(); setTargetConstraint = new HashSet<>();
		
		snapshots = new ArrayList<>();
		
		systemTables = new ArrayList<>(); snapshotTables = new ArrayList<>();
		systemColumn = new ArrayList<>();
		systemConstraints = new ArrayList<>();
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage, JeeslIoDbFacade<SYSTEM,?,?,?,SNAP,TAB,COL,CON,CUN,?> fDb)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fDb = fDb;
		sbhSystem.setList(fDb.all(fbDb.getClassSsiSystem()));
		sbhSystem.setDefault();
		
		thFilterTable.setList(fDb.all(fbDb.getClassDifference()));
		thFilterColumn.setList(fDb.all(fbDb.getClassDifference()));
		thFilterConstraint.setList(fDb.all(fbDb.getClassDifference()));
		thAction.setList(fDb.all(fbDb.getClassSql()));
		
		thFilterTable.preSelect(fbDb.getClassDifference(),JeeslDbMetaDifference.Code.deleted,JeeslDbMetaDifference.Code.added);
		thFilterColumn.preSelect(fbDb.getClassDifference(),JeeslDbMetaDifference.Code.deleted,JeeslDbMetaDifference.Code.added);
		thFilterConstraint.preSelect(fbDb.getClassDifference(),JeeslDbMetaDifference.Code.deleted,JeeslDbMetaDifference.Code.added);
		
		cacheDiff.facade(fDb);
		cacheType.facade(fDb);
		
		this.reloadSystem();
		this.reloadSnapshot();
	}

	@Override public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		callback.callbackSystemSelected();
		this.reloadSystem();
		this.reloadSnapshot();
		this.reloadFilter();
		this.reloadSqlAction();
	}
	
	@Override public void filtered(ThMultiFilter filter) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info("Received "+filter.getClass().getSimpleName());
		if(filter.equals(thAction)) {this.reloadSqlAction();}
		else {this.reloadFilter();}
	}
	
	public void reloadSystem()
	{
		snapshots.clear();
		systemTables.clear(); systemColumn.clear(); systemConstraints.clear();
		
		if(sbhSystem.isSelected())
		{
			snapshots.addAll(fDb.allForParent(fbDb.getClassSnapshot(),sbhSystem.getSelection()));
			Collections.sort(snapshots,cpSnapshot);
			
			EjbIoDbQuery<SYSTEM,SNAP> query = new EjbIoDbQuery<>();
			query.add(sbhSystem.getSelection());
			
			systemTables.addAll(fDb.fIoDbMetaTables(query));
			Collections.sort(systemTables,cpTable);
			
			systemColumn.addAll(fDb.fIoDbMetaColumns(query));
			
			query.addRootFetch(JeeslDbMetaConstraint.Attributes.uniques);
			query.distinct(true);
			systemConstraints.addAll(fDb.fIoDbMetaConstraints(query));
		}
		
		if(ObjectUtils.isNotEmpty(snapshots))
		{
			snapshotSource = snapshots.get(snapshots.size()-1);
			snapshotTarget = snapshots.get(0);
		}
	}
	
	public void selectSnapshot()
	{
		logger.info(AbstractLogMessage.selectEntity(snapshot));
	}
	
	public void magnetSource() throws JeeslNotFoundException
	{
		snapshotSource = fDb.find(fbDb.getClassSnapshot(), snapshot.getId());
		snapshot=null;
		this.reloadSnapshot();
		this.reloadFilter();
		this.reloadSqlAction();
	}
	public void saveSource() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		snapshotSource = fDb.save(snapshotSource);
		EjbIdFactory.replace(snapshots,snapshotSource);
	}
	public void deleteSource() throws JeeslNotFoundException, JeeslConstraintViolationException
	{
		snapshots.remove(snapshotSource);
		fDb.deleteIoDbSnapshot(snapshotSource);
		
		snapshotSource = null;
		snapshot=null;
		snapshotTables.clear();
	}
	
	public void magnetTarget() throws JeeslNotFoundException
	{
		snapshotTarget = fDb.find(fbDb.getClassSnapshot(), snapshot.getId());
		snapshot=null;
		this.reloadSnapshot();
		this.reloadFilter();
		this.reloadSqlAction();
	}
	public void saveTarget() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		snapshotTarget = fDb.save(snapshotTarget);
		EjbIdFactory.replace(snapshots,snapshotTarget);
	}
	public void deleteTarget() throws JeeslNotFoundException, JeeslConstraintViolationException
	{
		snapshots.remove(snapshotTarget);
		fDb.deleteIoDbSnapshot(snapshotTarget);
		snapshotTarget = null;
		
		snapshot=null;
		snapshotTables.clear();
	}
	
	public void reloadSnapshot()
	{
		setSourceTable.clear(); setTargetTable.clear();
		setSourceColumn.clear(); setTargetColumn.clear();
		setSourceConstraint.clear(); setTargetConstraint.clear();
		
		if(sbhSystem.isSelected())
		{
			if(Objects.nonNull(snapshotSource))
			{
				EjbIoDbQuery<SYSTEM,SNAP> query = new EjbIoDbQuery<>();
				query.add(sbhSystem.getSelection());
				query.add(snapshotSource);
				
				setSourceTable.addAll(fDb.fIoDbMetaTables(query));
				setSourceColumn.addAll(fDb.fIoDbMetaColumns(query));
				setSourceConstraint.addAll(fDb.fIoDbMetaConstraints(query));
			}
			
			if(Objects.nonNull(snapshotTarget))
			{
				EjbIoDbQuery<SYSTEM,SNAP> query = new EjbIoDbQuery<>();
				query.add(sbhSystem.getSelection());
				query.add(snapshotTarget);
				
				setTargetTable.addAll(fDb.fIoDbMetaTables(query));
				setTargetColumn.addAll(fDb.fIoDbMetaColumns(query));
				setTargetConstraint.addAll(fDb.fIoDbMetaConstraints(query));
			}
			
			if(ObjectUtils.allNotNull(snapshotSource,snapshotTarget))
			{
				logger.info(fbDb.getClassTable().getSimpleName()+": "+snapshotSource.toString()+":"+setSourceTable.size()+" -> "+snapshotTarget.toString()+":"+setTargetTable.size());
				logger.info(fbDb.getClassMetaColumn().getSimpleName()+": "+snapshotSource.toString()+":"+setSourceColumn.size()+" -> "+snapshotTarget.toString()+":"+setTargetColumn.size());
				logger.info(fbDb.getClassConstraint().getSimpleName()+": System:"+systemConstraints.size()+" "+snapshotSource.toString()+":"+setSourceConstraint.size()+" -> "+snapshotTarget.toString()+":"+setTargetConstraint.size());
			}
		}
	}
	
	public void reloadFilter()
	{
		snapshotTables.clear();
		mapColumn.clear(); mapConstraint.clear();
		mapDiffTable.clear(); mapDiffColumn.clear(); mapDiffConstraint.clear();
		
		Set<DIFF> filter1 = new HashSet<>(thFilterTable.getSelected());
		for(TAB t : systemTables)
		{
			DIFF d = null;
			if(setSourceTable.contains(t) && setTargetTable.contains(t)) {d = cacheDiff.ejb(JeeslDbMetaDifference.Code.both);}
			else if(setSourceTable.contains(t) && !setTargetTable.contains(t)) {d = cacheDiff.ejb(JeeslDbMetaDifference.Code.deleted);}
			else if(!setSourceTable.contains(t) && setTargetTable.contains(t)) {d = cacheDiff.ejb(JeeslDbMetaDifference.Code.added);}
			
			if(Objects.nonNull(d) )
			{
				mapDiffTable.put(t,d);
				if(thFilterTable.isSelected(d)) {snapshotTables.add(t);}
			}
		}
		
		for(COL c : systemColumn)
		{
			DIFF d = null;
			if(setSourceColumn.contains(c) && setTargetColumn.contains(c)) {d = cacheDiff.ejb(JeeslDbMetaDifference.Code.both);}
			else if(setSourceColumn.contains(c) && !setTargetColumn.contains(c)) {d = cacheDiff.ejb(JeeslDbMetaDifference.Code.deleted);}
			else if(!setSourceColumn.contains(c) && setTargetColumn.contains(c)) {d = cacheDiff.ejb(JeeslDbMetaDifference.Code.added);}
			
			if(Objects.nonNull(d) )
			{
				mapDiffColumn.put(c,d);
				if(getThFilterColumn().isSelected(d))
				{
					if(!snapshotTables.contains(c.getTable())) {snapshotTables.add(c.getTable());}
					if(!mapColumn.containsKey(c.getTable())) {mapColumn.put(c.getTable(), new ArrayList<>());}
					mapColumn.get(c.getTable()).add(c);
				}
			}
		}
		for(CON c : systemConstraints)
		{
			DIFF d = null;
			if(setSourceConstraint.contains(c) && setTargetConstraint.contains(c)) {d = cacheDiff.ejb(JeeslDbMetaDifference.Code.both);}
			else if(setSourceConstraint.contains(c) && !setTargetConstraint.contains(c)) {d = cacheDiff.ejb(JeeslDbMetaDifference.Code.deleted);}
			else if(!setSourceConstraint.contains(c) && setTargetConstraint.contains(c)) {d = cacheDiff.ejb(JeeslDbMetaDifference.Code.added);}
			
			if(Objects.nonNull(d))
			{
				mapDiffConstraint.put(c,d);
				if(thFilterTable.isSelected(d)) {if(!snapshotTables.contains(c.getTable())) {snapshotTables.add(c.getTable());}}
				if(thFilterConstraint.isSelected(d))
				{
					if(!mapConstraint.containsKey(c.getTable())) {mapConstraint.put(c.getTable(), new ArrayList<>());}
					mapConstraint.get(c.getTable()).add(c);
				}	
			}
		}
		Collections.sort(snapshotTables,cpTable);
		for(List<CON> l : mapConstraint.values()) {Collections.sort(l,cpConstraint);}
		logger.info("reloadedFilter()");
		logger.info("\t"+fbDb.getClassConstraint().getSimpleName()+": System:"+systemConstraints.size()+" filter:"+mapConstraint.size());
	}
	
	public void reloadSqlAction()
	{
		if(Objects.nonNull(filteredTables)) {logger.info("Filtered :"+filteredTables.size());}
		logger.info("Reloading Actions ");
		mapAction.clear();
		
		for(TAB t : this.getSnapshotTables())
		{
			List<String> sqlFkDropSrc = new ArrayList<>();
			List<String> sqlFkRenameSrcToDst = new ArrayList<>();
			
			boolean processTable = true;
//			processTable = t.getCode().startsWith("ioattribute");
//			processTable = t.getId()==1730;
			if(processTable && this.getMapConstraint().containsKey(t))
			{
				logger.trace(IoDbMetaTable.class.getSimpleName()+" "+t.toString());
				List<CON> constraints = this.getMapConstraint().get(t);
				for(CON c : constraints)
				{
					if(this.constraintMatches(c,JeeslDbMetaConstraintType.Code.fk,JeeslDbMetaDifference.Code.both))
					{
						logger.trace("Drop duplicate Src.FK if this is availabel in both Snapshots");
						sqlFkDropSrc.addAll(sfConstraint.drop(this.toOtherConstraint(c,constraints,JeeslDbMetaConstraintType.Code.fk,JeeslDbMetaDifference.Code.deleted)));
					}
					if(this.constraintMatches(c,JeeslDbMetaConstraintType.Code.fk,JeeslDbMetaDifference.Code.added))
					{
						logger.trace("Rename Src.FK to be aligned with added FK");
						List<CON> others = this.toOtherConstraint(c,constraints,JeeslDbMetaConstraintType.Code.fk,JeeslDbMetaDifference.Code.deleted);
						if(others.size()>0) {sqlFkRenameSrcToDst.add(sfConstraint.rename(others.get(0),c));}
						if(others.size()>1) {sqlFkRenameSrcToDst.addAll(sfConstraint.drop(others.subList(1,others.size())));}
					}
					if(this.constraintMatches(c,JeeslDbMetaConstraintType.Code.uk,JeeslDbMetaDifference.Code.both))
					{
//						if(t.getId()==177)
						{
							logger.trace(c.getCode());
							CON cNew = null;
							if(Objects.nonNull(cNew))
							{
								StringBuilder sb = new StringBuilder();
								sb.append("ALTER TABLE ").append(t.getCode());
								
								{
									sb.append(" RENAME CONSTRAINT ");
									sb.append(c.getCode());
									sb.append(" TO ").append(cNew.getCode());
									
								}
								
								{
									sb.append(" DROP CONSTRAINT IF EXISTS ");
									sb.append(c.getCode());
								}
								sb.append(";");
								System.out.println(sb.toString());
							}
						}
					}
				}
			}
			
			List<String> list = new ArrayList<>();
			for(SQL sql : thAction.getSelected())
			{
				switch(JeeslDbMetaSqlAction.Code.valueOf(sql.getCode()))
				{
				case fkRenameSrcToDst: list.addAll(sqlFkRenameSrcToDst); break;
					case fkDropDuplicate: list.addAll(sqlFkDropSrc); break;
					
					default: break;
				}
			}
			mapAction.put(t,list);
		}
		sqlToClipboard();
	}
	
	private boolean constraintMatches(CON c, JeeslDbMetaConstraintType.Code type, JeeslDbMetaDifference.Code difference)
	{
		return cacheType.equals(c.getType(),type)
			&& cacheDiff.equals(this.getMapDiffConstraint().get(c),difference);
	}
	private List<CON> toOtherConstraint(CON constraint, List<CON> list, JeeslDbMetaConstraintType.Code type, JeeslDbMetaDifference.Code difference)
	{
		List<CON> others = new ArrayList<>();
		for(CON other : list)
		{
			if(cacheType.equals(other.getType(),type) && !constraint.equals(other))
			{
				logger.trace(other.getTable().getCode()+" "+other.getType().getCode()+" "+other.getCode());
				if(constraint.getColumnLocal().equals(other.getColumnLocal())
					&& constraint.getColumnRemote().equals(other.getColumnRemote())
					&& cacheDiff.equals(mapDiffConstraint.get(other),difference))
				{
					others.add(other);
				}
			}
		}
		return others;
	}
	
	public void sqlToClipboard()
	{
		List<String> list = new ArrayList<>();
		logger.info("sqlToClipboard ");
		if(Objects.isNull(filteredTables))
		{
			for(TAB t : snapshotTables)
			{
				if(mapAction.containsKey(t)) {list.addAll(mapAction.get(t));}
			}
		}
		else
		{
			for(TAB t : filteredTables)
			{
				if(mapAction.containsKey(t)) {list.addAll(mapAction.get(t));}
			}
		}
		sqlClipboard = String.join("\n",list);
		logger.info(sqlClipboard);
	}
}