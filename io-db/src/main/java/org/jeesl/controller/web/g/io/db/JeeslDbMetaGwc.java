package org.jeesl.controller.web.g.io.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.controller.util.comparator.ejb.io.db.EjbIoDbTableComparator;
import org.jeesl.controller.util.comparator.ejb.module.calendar.EjbWithRecordJtComparator;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.db.IoDbMetaFactoryBuilder;
import org.jeesl.factory.ejb.io.db.meta.EjbIoDbMetaConstraintFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.EjbIoDbQuery;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslDbMetaGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								MS extends JeeslDbMetaSnapshot<SYSTEM,MT,MC>,
								MT extends JeeslDbMetaTable<SYSTEM,MS>,
								MC extends JeeslDbMetaConstraint<SYSTEM,MS,MT>
>
					extends AbstractJeeslWebController<L,D,LOC>
					implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslDbMetaGwc.class);
	
	private final IoDbMetaFactoryBuilder<L,D,SYSTEM,MS,MT,MC> fbDb;
	
	private JeeslIoDbFacade<SYSTEM,?,?,?,MT,MC> fDb;
	
	private final SbSingleHandler<SYSTEM> sbhSystem; public SbSingleHandler<SYSTEM> getSbhSystem() {return sbhSystem;}

	private final EjbIoDbMetaConstraintFactory<SYSTEM,MT,MC> efConstraint;
	
	private final Comparator<MS> cpSnapshot;
	private final Comparator<MT> cpTable;
	
	private final Map<MT,List<MC>> mapConstraint; public Map<MT, List<MC>> getMapConstraint() {return mapConstraint;}

	private final List<MS> snapshots; public List<MS> getSnapshots() {return snapshots;}
	private final List<MT> tables; public List<MT> getTables() {return tables;}
	
	private MS snapshot; public MS getSnapshot() {return snapshot;} public void setSnapshot(MS snapshot) {this.snapshot = snapshot;}
	private MS snapshotSource; public MS getSnapshotSource() {return snapshotSource;} public void setSnapshotSource(MS snapshotSource) {this.snapshotSource = snapshotSource;}
	private MS snapshotTarget; public MS getSnapshotTarget() {return snapshotTarget;} public void setSnapshotTarget(MS snapshotTarget) {this.snapshotTarget = snapshotTarget;}

	public JeeslDbMetaGwc(IoDbMetaFactoryBuilder<L,D,SYSTEM,MS,MT,MC> fbDb)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.fbDb = fbDb;
		
		sbhSystem = new SbSingleHandler<>(fbDb.getClassSsiSystem(),this);
		
		efConstraint = fbDb.ejbConstraint();
		
		cpSnapshot = new EjbWithRecordJtComparator<MS>().factory(EjbWithRecordJtComparator.Type.dsc); 
		cpTable = new EjbIoDbTableComparator<MT>().instance(EjbIoDbTableComparator.Type.code);
		
		mapConstraint = new HashMap<>();
		
		snapshots = new ArrayList<>();
		tables = new ArrayList<>();
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage, JeeslIoDbFacade<SYSTEM,?,?,?,MT,MC> fDb)
	{
		super.postConstructWebController(lp,bMessage);
		this.fDb = fDb;
		sbhSystem.setList(fDb.all(fbDb.getClassSsiSystem()));
		
		this.reloadSnapshots();
	}

	@Override public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
	
	public void reloadSnapshots()
	{
		mapConstraint.clear();
		snapshots.clear();
		tables.clear();
		
		if(sbhSystem.isSelected())
		{
			snapshots.addAll(fDb.allForParent(fbDb.getClassSnapshot(),sbhSystem.getSelection()));
			Collections.sort(snapshots,cpSnapshot);
			
			EjbIoDbQuery<SYSTEM> query = new EjbIoDbQuery<>();
			query.add(sbhSystem.getSelection());
			
			tables.addAll(fDb.fIoDbMetaTables(query));
			Collections.sort(tables,cpTable);
			
			mapConstraint.putAll(efConstraint.toMapConstraints(fDb.fIoDbMetaConstraints(query)));
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
	}
	public void magnetTarget() throws JeeslNotFoundException
	{
		snapshotTarget = fDb.find(fbDb.getClassSnapshot(), snapshot.getId());
		snapshot=null;
	}
}