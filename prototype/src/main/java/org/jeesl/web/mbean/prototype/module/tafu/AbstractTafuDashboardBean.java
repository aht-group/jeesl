package org.jeesl.web.mbean.prototype.module.tafu;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslTafuFacade;
import org.jeesl.controller.handler.sb.SbMultiHandler;
import org.jeesl.controller.handler.sb.SbSingleHandler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.TafuFactoryBuilder;
import org.jeesl.factory.json.util.JsonDayFactory;
import org.jeesl.interfaces.bean.sb.SbSingleBean;
import org.jeesl.interfaces.bean.sb.SbToggleBean;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuStatus;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuViewport;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.time.JeeslTimeDayOfWeek;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.util.time.JsonDay;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractTafuDashboardBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
    										R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
    										T extends JeeslTafuTask<R,TS>,
    										TS extends JeeslTafuStatus<L,D,TS,?>,
    										VP extends JeeslTafuViewport<L,D,VP,?>,
    										DOW extends JeeslTimeDayOfWeek<L,D,DOW,?>
    										>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable, SbSingleBean, SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractTafuDashboardBean.class);
	
	private JeeslTafuFacade<L,D,R,T,TS,VP,DOW> fTafu;

	protected final TafuFactoryBuilder<L,D,R,T,TS,VP,DOW> fbTafu;
	
    protected R realm;
    protected RREF rref; public RREF getRref() {return rref;}

    private EjbCodeCache<DOW> cacheDay;
    
    private final SbSingleHandler<VP> sbhViewport; public SbSingleHandler<VP> getSbhViewport() {return sbhViewport;}
    private final SbMultiHandler<DOW> sbhDow; public SbMultiHandler<DOW> getSbhDow() {return sbhDow;}

    private final Nested2Map<JsonDay,DOW,T> n2m; public Nested2Map<JsonDay, DOW, T> getN2m() {return n2m;}

	private final Map<DOW,Date> mapDate; public Map<DOW, Date> getMapDate() {return mapDate;}

    private final List<TS> status; public List<TS> getStatus() {return status;}
    private final List<JsonDay> rows; public List<JsonDay> getRows() {return rows;}
	private final List<T> backlog; public List<T> getBacklog() {return backlog;}
	
	private T task; public T getTask() {return task;} public void setTask(T task) {this.task = task;}
	private LocalDate dateViewportBegin,dateViewportEnd;
	
	private Date tmpShow; public Date getTmpShow() {return tmpShow;}public void setTmpShow(Date tmpShow) {this.tmpShow = tmpShow;}
	private Date tmpDue; public Date getTmpDue() {return tmpDue;} public void setTmpDue(Date tmpDue) {this.tmpDue = tmpDue;}
	
	public AbstractTafuDashboardBean(TafuFactoryBuilder<L,D,R,T,TS,VP,DOW> fbTafu)
	{
		super(fbTafu.getClassL(),fbTafu.getClassD());	
		this.fbTafu=fbTafu;
		
		sbhViewport = new SbSingleHandler<>(fbTafu.getClassViewport(),this);
		sbhDow = new SbMultiHandler<>(fbTafu.getClassDayOfWeek(),this);
		
		n2m = new Nested2Map<JsonDay,DOW,T>();
		mapDate = new HashMap<>();
		
		rows = new ArrayList<>();
		status = new ArrayList<>();
		backlog = new ArrayList<>();
	}

	protected void postConstructDashboard(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslTafuFacade<L,D,R,T,TS,VP,DOW> fTafu,
									R realm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fTafu=fTafu;
		this.realm=realm;
		
		sbhDow.setList(fTafu.allOrderedPositionVisible(fbTafu.getClassDayOfWeek()));
		sbhDow.selectNone();
		
		cacheDay = new EjbCodeCache<>(fbTafu.getClassDayOfWeek(),sbhDow.getList());
		status.addAll(fTafu.allOrderedPositionVisible(fbTafu.getClassStatus()));
		

		sbhViewport.setList(fTafu.allOrderedPositionVisible(fbTafu.getClassViewport()));
		sbhViewport.setDefault();
		sbhViewport.debug();
	}
	
	protected void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		reloadViewport();
		reload();
	}
	
	@Override public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reloadViewport();
		reload();
	}
	
	@Override public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
		
	}
	
	public void cancelTask()
	{
		reset(true);
	}
	private void reset(boolean rTask)
	{
		if(rTask) {task=null;}
	}
	
	private void reloadViewport()
	{
		LocalDate ldNow = LocalDate.now();
		
		logger.info("Day of Week: "+ldNow.getDayOfWeek().getValue());
		
		switch(JeeslTafuViewport.Code.valueOf(sbhViewport.getSelection().getCode()))
		{
			case workingWeek: sbhDow.selectNone(); for(int i=0;i<5;i++) {sbhDow.preSelect(sbhDow.getList().get(i));} break;
			case fullWeek: sbhDow.selectAll(); break;
		}
		
		
		for(int i=1;i<=sbhDow.getSelected().size();i++)
		{
			int offset = i-ldNow.getDayOfWeek().getValue();
			LocalDate ld = ldNow.plusDays(offset);
			Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
			if(debugOnInfo) {logger.info(ld.getDayOfWeek().getValue()+" "+ld.toString());}
			DOW dow = cacheDay.ejb(ld.getDayOfWeek().getValue()+"");
			if(dow!=null)
			{
				if(debugOnInfo) {logger.info(dow.toString()+" "+ld.toString());}
				mapDate.put(dow,d);
			}
		}
		
		dateViewportBegin = mapDate.get(sbhDow.getList().get(0)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		dateViewportEnd = dateViewportBegin.plusDays(7);
		
		logger.info("Viewport");
		logger.info("\tdateVpBegin"+dateViewportBegin.toString());
		logger.info("\tdateViewportEnd"+dateViewportEnd.toString());
	}
	
	protected void reload()
	{
		Map<DOW,Integer> mapFilling = new HashMap<>();
		
		rows.clear();
		backlog.clear();
		n2m.clear();
		
		backlog.addAll(fTafu.fTafuBacklog(realm,rref,dateViewportBegin));
		
		
		for(T t : fTafu.fTafuActive(realm,rref,dateViewportBegin,dateViewportEnd))
		{
			DOW dow = cacheDay.ejb(t.getRecordShow().getDayOfWeek().getValue()+"");
			if(!mapFilling.containsKey(dow)) {mapFilling.put(dow,1);}
			else {mapFilling.put(dow,mapFilling.get(dow)+1);}
			if(rows.size()<mapFilling.get(dow))
			{
				JsonDay jd = JsonDayFactory.build();
				jd.setId(rows.size()+1);
				rows.add(jd);
			}
			n2m.put(rows.get(mapFilling.get(dow)-1),dow,t);
		}
    }
	
	public void selectTask(T task) {this.task=task; selectTask();}
	public void selectTask()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(task));}
		tmpShow = java.sql.Date.valueOf(task.getRecordShow());
		tmpDue=null; if(task.getRecordDue()!=null) {tmpDue = java.sql.Date.valueOf(task.getRecordDue());}
	}
	
	public void addTask()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.addEntity(fbTafu.getClassTask()));}
		task = fbTafu.ejbTask().build(realm,rref);
		task.setStatus(fTafu.fByEnum(fbTafu.getClassStatus(),JeeslTafuStatus.Code.open));
		tmpShow = java.sql.Date.valueOf(task.getRecordShow());
		tmpDue=null; if(task.getRecordDue()!=null) {tmpDue = java.sql.Date.valueOf(task.getRecordDue());}
	}
	
	public void saveTask() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(task));}
		task.setRecordShow(tmpShow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		task.setRecordDue(null); if(tmpDue!=null) {task.setRecordDue(tmpDue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());}
		
		task = fTafu.save(task);
		if(debugOnInfo) {logger.info(AbstractLogMessage.savedEntity(task));}
		reload();
	}
}