package org.jeesl.web.mbean.prototype.module.tafu;

import java.io.Serializable;
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
import org.jeesl.factory.ejb.module.tafu.EjbTaskFactory;
import org.jeesl.factory.json.util.JsonDayFactory;
import org.jeesl.interfaces.bean.sb.SbSingleBean;
import org.jeesl.interfaces.bean.sb.SbToggleBean;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuScope;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuStatus;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuViewport;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.time.JeeslTimeDayOfWeek;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.util.time.JsonDay;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.jeesl.model.pojo.map.list.Nested2MapList;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractTafuDashboardBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
    										R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
    										T extends JeeslTafuTask<R,TS,SC,M>,
    										TS extends JeeslTafuStatus<L,D,TS,?>,
    										SC extends JeeslTafuScope<L,D,R,SC,?>,
    										VP extends JeeslTafuViewport<L,D,VP,?>,
    										DOW extends JeeslTimeDayOfWeek<L,D,DOW,?>,
    										M extends JeeslMarkup<MT>,
    										MT extends JeeslIoCmsMarkupType<L,D,MT,?>
    										>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable, SbSingleBean, SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractTafuDashboardBean.class);
	
	private JeeslTafuFacade<L,D,R,T,TS,SC,VP,DOW,M> fTafu;

	protected final TafuFactoryBuilder<L,D,R,T,TS,SC,VP,DOW,M,MT> fbTafu;
	
	private final EjbTaskFactory<R,T,TS,SC,M,MT> efTask;
	
    protected R realm;
    protected RREF rref; public RREF getRref() {return rref;}

    private EjbCodeCache<DOW> cacheDay;
    
    private final SbSingleHandler<VP> sbhViewport; public SbSingleHandler<VP> getSbhViewport() {return sbhViewport;}
    private final SbMultiHandler<DOW> sbhDow; public SbMultiHandler<DOW> getSbhDow() {return sbhDow;}

    private final Nested2MapList<SC,DOW,T> n2m; public Nested2MapList<SC, DOW, T> getN2m() {return n2m;}

	private final Map<DOW,Date> mapDate; public Map<DOW, Date> getMapDate() {return mapDate;}

    private final List<TS> status; public List<TS> getStatus() {return status;}
    private final List<SC> scopes; public List<SC> getScopes() {return scopes;}
	private final List<SC> rows; public List<SC> getRows() {return rows;}
	private final List<T> backlog; public List<T> getBacklog() {return backlog;}
	
	private T task; public T getTask() {return task;} public void setTask(T task) {this.task = task;}
	private SC emptyScope;
	private LocalDate dateViewportBegin,dateViewportEnd;
	
	private Date tmpShow; public Date getTmpShow() {return tmpShow;}public void setTmpShow(Date tmpShow) {this.tmpShow = tmpShow;}
	private Date tmpDue; public Date getTmpDue() {return tmpDue;} public void setTmpDue(Date tmpDue) {this.tmpDue = tmpDue;}
	
	public AbstractTafuDashboardBean(TafuFactoryBuilder<L,D,R,T,TS,SC,VP,DOW,M,MT> fbTafu)
	{
		super(fbTafu.getClassL(),fbTafu.getClassD());	
		this.fbTafu=fbTafu;
		
		efTask = fbTafu.ejbTask();
		
		sbhViewport = new SbSingleHandler<>(fbTafu.getClassViewport(),this);
		sbhDow = new SbMultiHandler<>(fbTafu.getClassDayOfWeek(),this);
		
		n2m = new Nested2MapList<>();
		mapDate = new HashMap<>();
		
		scopes = new ArrayList<>();
		rows = new ArrayList<>();
		status = new ArrayList<>();
		backlog = new ArrayList<>();
		
		try {
			emptyScope = fbTafu.getClassScope().newInstance();
			emptyScope.setId(-1);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void postConstructDashboard(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslTafuFacade<L,D,R,T,TS,SC,VP,DOW,M> fTafu,
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
		scopes.clear();scopes.addAll(fTafu.all(fbTafu.getClassScope(),realm,rref));scopes.add(emptyScope);
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
		rows.clear();
		backlog.clear();
		n2m.clear();
		
		backlog.addAll(fTafu.fTafuBacklog(realm,rref,dateViewportBegin,dateViewportEnd));
		
		
		for(T t : fTafu.fTafuActive(realm,rref,dateViewportBegin,dateViewportEnd))
		{
			DOW dow = cacheDay.ejb(t.getRecordShow().getDayOfWeek().getValue()+"");


			if(t.getScope()==null) {n2m.put(emptyScope,dow,t);}
			else  {n2m.put(t.getScope(),dow,t);}
		}
		rows.addAll(n2m.toL1());
    }
	
	public void selectTask(T task) {this.task=task; selectTask();}
	public void selectTask()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(task));}
		tmpShow = java.sql.Date.valueOf(task.getRecordShow());
		tmpDue=null; if(task.getRecordDue()!=null) {tmpDue = java.sql.Date.valueOf(task.getRecordDue());}
		
		if(task.getMarkup()==null)
		{
			MT type = fTafu.fByEnum(fbTafu.getClassMarkupType(),JeeslIoCmsMarkupType.Code.text);
			task.setMarkup(fbTafu.ejbMarkup().build(type));
			try {task = fTafu.save(task);}
			catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		}
		
		if(task.getMarkup().getType().getCode().equals(JeeslIoCmsMarkupType.Code.xhtml.toString()))
		{
			try
			{
				logger.info("Changing type to text");
				MT type = fTafu.fByEnum(fbTafu.getClassMarkupType(),JeeslIoCmsMarkupType.Code.text);
				task.getMarkup().setType(type);
				task = fTafu.save(task);
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		}
	}
	
	public void addTask()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.addEntity(fbTafu.getClassTask()));}
		
		MT type = fTafu.fByEnum(fbTafu.getClassMarkupType(),JeeslIoCmsMarkupType.Code.text);
		task = efTask.build(realm,rref,type);
		task.setStatus(fTafu.fByEnum(fbTafu.getClassStatus(),JeeslTafuStatus.Code.open));
		tmpShow = java.sql.Date.valueOf(task.getRecordShow());
		tmpDue=null; if(task.getRecordDue()!=null) {tmpDue = java.sql.Date.valueOf(task.getRecordDue());}
	}
	
	public void saveTask() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(task));}
		task.setRecordShow(tmpShow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		task.setRecordDue(null); if(tmpDue!=null) {task.setRecordDue(tmpDue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());}
		
		efTask.converter(fTafu,task);
		efTask.preSave(fTafu,task);
		task = fTafu.save(task);
		if(debugOnInfo) {logger.info(AbstractLogMessage.savedEntity(task));}
		reload();
	}
}