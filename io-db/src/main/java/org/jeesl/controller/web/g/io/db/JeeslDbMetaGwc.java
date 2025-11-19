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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.util.comparator.ejb.io.db.EjbIoDbConstraintComparator;
import org.jeesl.controller.util.comparator.ejb.io.db.EjbIoDbTableComparator;
import org.jeesl.controller.util.comparator.ejb.module.calendar.EjbWithRecordJtComparator;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.db.IoDbMetaFactoryBuilder;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaConstraintFactory;
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
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSchema;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSqlAction;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaUnique;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.handler.th.ThMultiFilterHandler;
import org.jeesl.model.ejb.io.db.meta.IoDbMetaTable;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.jeesl.util.query.ejb.io.EjbIoDbQuery;
import org.openfuxml.factory.xml.table.XmlTableFactory;
import org.openfuxml.renderer.text.OfxTextRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslDbMetaGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								SNAP extends JeeslDbMetaSnapshot<SYSTEM,SCHEMA,TAB,COL,CON>,
								SCHEMA extends JeeslDbMetaSchema<SYSTEM,SNAP>,
								TAB extends JeeslDbMetaTable<SYSTEM,SNAP,SCHEMA>,
								COL extends JeeslDbMetaColumn<SNAP,TAB,COLT>,
								COLT extends JeeslDbMetaColumnType<L,D,COLT,?>,
								CON extends JeeslDbMetaConstraint<SNAP,TAB,COL,CONT,UNQ>,
								CONT extends JeeslDbMetaConstraintType<L,D,CONT,?>,
								UNQ extends JeeslDbMetaUnique<COL,CON>,
								DIFF extends JeeslDbMetaDifference<L,D,DIFF,?>,
								SQL extends JeeslDbMetaSqlAction<L,D,SQL,?>
>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements SbSingleBean,ThMultiFilterBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslDbMetaGwc.class);
	
	private final IoDbMetaFactoryBuilder<L,D,SYSTEM,SNAP,SCHEMA,TAB,COL,COLT,CON,CONT,UNQ,DIFF,SQL> fbDb;
	
	private JeeslIoDbFacade<SYSTEM,?,?,?,SNAP,SCHEMA,TAB,COL,CON,UNQ,?> fDb;
	private final JeeslIoDbMetaCallback callback;
	
	private final SbSingleHandler<SYSTEM> sbhSystem; public SbSingleHandler<SYSTEM> getSbhSystem() {return sbhSystem;}
	
	private final JsonTuple1Handler<SNAP> thSnapshot; public JsonTuple1Handler<SNAP> getThSnapshot() {return thSnapshot;}
	private final JsonTuple1Handler<SYSTEM> thSystem; public JsonTuple1Handler<SYSTEM> getThSystem() {return thSystem;}
	
	private final ThMultiFilterHandler<DIFF> thFilterTable; public ThMultiFilterHandler<DIFF> getThFilterTable() {return thFilterTable;}
	private final ThMultiFilterHandler<DIFF> thFilterColumn; public ThMultiFilterHandler<DIFF> getThFilterColumn() {return thFilterColumn;}
	private final ThMultiFilterHandler<DIFF> thFilterConstraint; public ThMultiFilterHandler<DIFF> getThFilterConstraint() {return thFilterConstraint;}
	private final ThMultiFilterHandler<SQL> thAction; public ThMultiFilterHandler<SQL> getThAction() {return thAction;}
	
	private final EjbIoDbMetaConstraintFactory<TAB,COL,CON,CONT,UNQ> efConstraint;
	private final SqlConstraintFactory<SCHEMA,TAB,CON> sfConstraint;
	
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
	
	public JeeslDbMetaGwc(JeeslIoDbMetaCallback callback, IoDbMetaFactoryBuilder<L,D,SYSTEM,SNAP,SCHEMA,TAB,COL,COLT,CON,CONT,UNQ,DIFF,SQL> fbDb)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.callback = callback;
		this.fbDb = fbDb;
		
		efConstraint = fbDb.ejbConstraint();
		
		sbhSystem = new SbSingleHandler<>(fbDb.getClassSsiSystem(),this);
		
		thSnapshot = JsonTuple1Handler.instance(fbDb.getClassSnapshot());
		thSystem = JsonTuple1Handler.instance(fbDb.getClassSsiSystem());
		
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

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage, JeeslIoDbFacade<SYSTEM,?,?,?,SNAP,SCHEMA,TAB,COL,CON,UNQ,?> fDb)
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
			
			thSnapshot.load(fDb.tpIoDbBySnapshot(query));
			thSystem.load(fDb.tpIoDbBySystem(query));
			
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
	
	public void selectSnapshot() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(snapshot));
		if(Objects.isNull(snapshotSource)) {this.magnetSource();}
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
	
	public void flipSnapshots() throws JeeslNotFoundException
	{
		if(ObjectUtils.allNotNull(snapshotSource,snapshotTarget))
		{
			long idSrc = snapshotSource.getId();
			long idDst = snapshotTarget.getId();
			
			snapshotSource = fDb.find(fbDb.getClassSnapshot(), idDst);
			snapshotTarget = fDb.find(fbDb.getClassSnapshot(), idSrc);
		}
		this.reloadSnapshot();
		this.reloadFilter();
		this.reloadSqlAction();
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
				logger.info(fbDb.getClassColumn().getSimpleName()+": "+snapshotSource.toString()+":"+setSourceColumn.size()+" -> "+snapshotTarget.toString()+":"+setTargetColumn.size());
				logger.info(fbDb.getClassConstraint().getSimpleName()+": System:"+systemConstraints.size()+" "+snapshotSource.toString()+":"+setSourceConstraint.size()+" -> "+snapshotTarget.toString()+":"+setTargetConstraint.size());
			}
		}
	}
	
	public void reloadFilter()
	{
		snapshotTables.clear();
		mapColumn.clear(); mapConstraint.clear();
		mapDiffTable.clear(); mapDiffColumn.clear(); mapDiffConstraint.clear();
		
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
			List<String> sqlPkDropSrc = new ArrayList<>();
			List<String> sqlPkDropDuplicate = new ArrayList<>();
			
			List<String> sqlFkDropSrc = new ArrayList<>();
			List<String> sqlFkDropDuplicate = new ArrayList<>();
			
			List<String> sqlUkDropSrc = new ArrayList<>();
			List<String> sqlUkDropDuplicate = new ArrayList<>();
			
			boolean processTable = true;
//			processTable = t.getCode().startsWith("ioattribute");
//			processTable = t.getId()==1943;
			if(processTable && this.getMapConstraint().containsKey(t))
			{
				logger.info(IoDbMetaTable.class.getSimpleName()+" "+t.toString());
				List<CON> constraints = this.getMapConstraint().get(t);
				for(CON c : constraints)
				{
					if(this.constraintMatches(JeeslDbMetaConstraintType.Code.pk,c,JeeslDbMetaDifference.Code.both))
					{
						logger.trace("Drop other (deleted) Src.PK if this PK is availabel in both snapshot.");
						List<CON> others = this.toOtherConstraint(JeeslDbMetaConstraintType.Code.uk,c,constraints,JeeslDbMetaDifference.Code.deleted);
//						sqlPkDropDuplicate.addAll(sfConstraint.drop(others));
					}
					if(this.constraintMatches(JeeslDbMetaConstraintType.Code.pk,c,JeeslDbMetaDifference.Code.added))
					{
						logger.trace("Drop other (deleted) Src.PK if this PK is added to snapshot.");
						List<CON> others = this.toOtherConstraint(JeeslDbMetaConstraintType.Code.pk,c,constraints,JeeslDbMetaDifference.Code.deleted);
						if(others.size()>0) {sqlPkDropSrc.add(sfConstraint.rename(others.get(0),c));}
						if(others.size()>1) {sqlPkDropSrc.addAll(sfConstraint.drop(others.subList(1,others.size())));}
					}
					
					if(this.constraintMatches(JeeslDbMetaConstraintType.Code.fk,c,JeeslDbMetaDifference.Code.both))
					{
						logger.trace("Drop other (deleted) Src.FK if this is available in both Snapshots");
						sqlFkDropSrc.addAll(sfConstraint.drop(this.toOtherConstraint(JeeslDbMetaConstraintType.Code.fk,c,constraints,JeeslDbMetaDifference.Code.deleted)));
					}
					if(this.constraintMatches(JeeslDbMetaConstraintType.Code.fk,c,JeeslDbMetaDifference.Code.added))
					{
						logger.trace("Rename Src.FK to be aligned with added FK");
						List<CON> others = this.toOtherConstraint(JeeslDbMetaConstraintType.Code.fk,c,constraints,JeeslDbMetaDifference.Code.deleted);
						if(others.size()>0) {sqlFkDropDuplicate.add(sfConstraint.rename(others.get(0),c));}
						if(others.size()>1) {sqlFkDropDuplicate.addAll(sfConstraint.drop(others.subList(1,others.size())));}
					}
					
					if(this.constraintMatches(JeeslDbMetaConstraintType.Code.uk,c,JeeslDbMetaDifference.Code.both))
					{
						logger.info("Drop other (deleted) Src.UK if this is available in both Snapshots");
						List<CON> others = this.toOtherConstraint(JeeslDbMetaConstraintType.Code.uk,c,constraints,JeeslDbMetaDifference.Code.deleted);
						sqlUkDropSrc.addAll(sfConstraint.drop(others));
					}
					if(this.constraintMatches(JeeslDbMetaConstraintType.Code.uk,c,JeeslDbMetaDifference.Code.added))
					{
						logger.trace("Drop other (deleted) Src.UK if this PK is added to snapshot.");
						List<CON> others = this.toOtherConstraint(JeeslDbMetaConstraintType.Code.uk,c,constraints,JeeslDbMetaDifference.Code.deleted);
						if(others.size()>0) {sqlUkDropDuplicate.add(sfConstraint.rename(others.get(0),c));}
						if(others.size()>1) {sqlUkDropDuplicate.addAll(sfConstraint.drop(others.subList(1,others.size())));}
					}
				}
			}

			List<String> list = new ArrayList<>();
			for(SQL sql : thAction.getSelected())
			{
				switch(JeeslDbMetaSqlAction.Code.valueOf(sql.getCode()))
				{
					case pk: list.addAll(sqlPkDropSrc); list.addAll(sqlPkDropDuplicate); break;
					case fk: list.addAll(sqlFkDropSrc); list.addAll(sqlFkDropDuplicate); break;
					case uk: list.addAll(sqlUkDropSrc); list.addAll(sqlUkDropDuplicate); break;
					
					default: break;
				}
			}
			mapAction.put(t,list);
		}
		sqlToClipboard();
	}
	
	private boolean constraintMatches(JeeslDbMetaConstraintType.Code type, CON c, JeeslDbMetaDifference.Code difference)
	{
		return cacheType.equals(c.getType(),type) && cacheDiff.equals(mapDiffConstraint.get(c),difference);
	}
	private List<CON> toOtherConstraint(JeeslDbMetaConstraintType.Code type, CON constraint, List<CON> list, JeeslDbMetaDifference.Code difference)
	{
		List<CON> others = new ArrayList<>();
		for(CON other : list)
		{
			boolean eqType = cacheType.equals(other.getType(),type);
			boolean notSame = !constraint.equals(other);
			boolean eqContent = efConstraint.equals(constraint,other);
			boolean matchDiff = cacheDiff.equals(mapDiffConstraint.get(other),difference);
			logger.info("\t\t"+other.toString()+" eqType:"+eqType+" notSame:"+notSame+" eqContent:"+eqContent+" matchDiff:"+matchDiff);
			if(eqType && notSame && eqContent && matchDiff) {others.add(other);}
		}
		logger.info("\tOthers: "+others.size());
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
	
	public void cleanSnapshots() throws JeeslConstraintViolationException
	{
		EjbIoDbQuery<SYSTEM,SNAP> qSystem = new EjbIoDbQuery<>();
		qSystem.add(sbhSystem.getSelection());
		
		List<SCHEMA> tablesSchema = fDb.fIoDbMetaSchemas(qSystem);
		List<TAB> tablesSystem = fDb.fIoDbMetaTables(qSystem);
		List<COL> columnsSystem = fDb.fIoDbMetaColumns(qSystem);
		List<CON> constraintsSystem = fDb.fIoDbMetaConstraints(qSystem);
		
		List<SCHEMA> schemasSnapshot = new ArrayList<>();
		List<TAB> tablesSnapshot = new ArrayList<>();
		List<COL> columnsSnapshot = new ArrayList<>();
		List<CON> constraintsSnapshot = new ArrayList<>();
		
		if(ObjectUtils.isNotEmpty(snapshots))
		{
			EjbIoDbQuery<SYSTEM,SNAP> qSnapshot = new EjbIoDbQuery<>();
			qSnapshot.addSnapshots(snapshots);
			tablesSnapshot.addAll(fDb.fIoDbMetaTables(qSnapshot));
			columnsSnapshot.addAll(fDb.fIoDbMetaColumns(qSnapshot));
			constraintsSnapshot.addAll(fDb.fIoDbMetaConstraints(qSnapshot));
		}		
		
		List<SCHEMA> schemasRemove = new ArrayList<>(CollectionUtils.subtract(tablesSchema,schemasSnapshot));
		List<TAB> tablesRemove = new ArrayList<>(CollectionUtils.subtract(tablesSystem,tablesSnapshot));
		List<COL> columnsRemove = new ArrayList<>(CollectionUtils.subtract(columnsSystem,columnsSnapshot));
		List<CON> constraintsRemove = new ArrayList<>(CollectionUtils.subtract(constraintsSystem,constraintsSnapshot));
		
		XmlTableFactory xt = XmlTableFactory.instance("Elements");
		xt.header("Type").header(fbDb.getClassSsiSystem().getSimpleName()).header(fbDb.getClassSnapshot().getSimpleName()).header("Remove");
		xt.cell(fbDb.getClassSchema().getSimpleName()).cell(tablesSchema.size()).cell(schemasSnapshot.size()).cell(schemasRemove.size()).row();
		xt.cell(fbDb.getClassTable().getSimpleName()).cell(tablesSystem.size()).cell(tablesSnapshot.size()).cell(tablesRemove.size()).row();
		xt.cell(fbDb.getClassColumn().getSimpleName()).cell(columnsSystem.size()).cell(columnsSnapshot.size()).cell(columnsRemove.size()).row();
		xt.cell(fbDb.getClassConstraint().getSimpleName()).cell(constraintsSystem.size()).cell(constraintsSnapshot.size()).cell(constraintsRemove.size()).row();
		
		OfxTextRenderer.silent(xt.getTable(),System.out);
		
		fDb.rm(constraintsRemove);
		fDb.rm(columnsRemove);
		fDb.rm(tablesRemove);
		fDb.rm(schemasRemove);
		
		this.reloadSystem();
	}
}