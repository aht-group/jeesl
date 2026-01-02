package org.jeesl.controller.web.g.module.tafu;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslTafuFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.TafuFactoryBuilder;
import org.jeesl.factory.ejb.module.tafu.EjbTaskFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.module.calendar.unit.JeeslCalendarDayOfWeek;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuScope;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuStatus;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuViewport;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslLocaleDirection;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.model.pojo.map.generic.Nested2List;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslTafuDashboardGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
    										R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
    										T extends JeeslTafuTask<R,TS,SC,M>,
    										TS extends JeeslTafuStatus<L,D,TS,?>,
    										SC extends JeeslTafuScope<L,D,R,SC,?>,
    										VP extends JeeslTafuViewport<L,D,VP,?>,
    										DOW extends JeeslCalendarDayOfWeek<L,D,DOW,?>,
    										M extends JeeslIoMarkup<MT>,
    										MT extends JeeslIoMarkupType<L,D,MT,?>
    										>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable, SbSingleBean, SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslTafuDashboardGwc.class);
	
	private JeeslTafuFacade<L,D,R,T,TS,SC,VP,DOW,M> fTafu;

	protected final TafuFactoryBuilder<L,D,R,T,TS,SC,VP,DOW,M,MT> fbTafu;
	
	private final EjbTaskFactory<R,T,TS,SC,M,MT> efTask;
	
    protected R realm;
    protected RREF rref; public RREF getRref() {return rref;}

//    private EjbCodeCache<DOW> cacheDay;
    
    private final SbSingleHandler<VP> sbhViewport; public SbSingleHandler<VP> getSbhViewport() {return sbhViewport;}
    private final SbMultiHandler<DOW> sbhDow; public SbMultiHandler<DOW> getSbhDow() {return sbhDow;}

    private final PositionComparator<SC> cpScope;
    
    private final Nested2List<SC,DOW,T> n2m; public Nested2List<SC, DOW, T> getN2m() {return n2m;}
   
	private final Map<DOW,Date> mapDate; public Map<DOW, Date> getMapDate() {return mapDate;}

    private final List<TS> status; public List<TS> getStatus() {return status;}
    private final List<SC> scopes; public List<SC> getScopes() {return scopes;}
	private final List<SC> rows; public List<SC> getRows() {return rows;}
	private final List<T> backlog; public List<T> getBacklog() {return backlog;}
	private final List<DOW> postpone; public List<DOW> getPostpone() {return postpone;}

	private T task; public T getTask() {return task;} public void setTask(T task) {this.task = task;}
	private SC emptyScope;
	private LocalDate ldBegin,ldEnd;
	private String componentDir; public String getComponentDir() {return componentDir;} public void setComponentDir(String componentDir) {this.componentDir = componentDir;}
	
	public JeeslTafuDashboardGwc(TafuFactoryBuilder<L,D,R,T,TS,SC,VP,DOW,M,MT> fbTafu)
	{
		super(fbTafu.getClassL(),fbTafu.getClassD());	
		this.fbTafu=fbTafu;
		
		efTask = fbTafu.ejbTask();
		cpScope = new PositionComparator<SC>();
		
		sbhViewport = new SbSingleHandler<>(fbTafu.getClassViewport(),this);
		sbhDow = new SbMultiHandler<>(fbTafu.getClassDayOfWeek(),this);
		
		n2m = new Nested2List<>();
		mapDate = new HashMap<>();
		
		postpone = new ArrayList<>();
		scopes = new ArrayList<>();
		rows = new ArrayList<>();
		status = new ArrayList<>();
		backlog = new ArrayList<>();
		
		try
		{
			emptyScope = fbTafu.getClassScope().newInstance();
			emptyScope.setId(-1);
			emptyScope.setPosition(Integer.MAX_VALUE);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		componentDir = JeeslLocaleDirection.Code.ltr.toString();
	}

	public void postConstructDashboard(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			JeeslTafuFacade<L,D,R,T,TS,SC,VP,DOW,M> fTafu,
									R realm)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fTafu=fTafu;
		this.realm=realm;
		
		sbhDow.setList(fTafu.allOrderedPositionVisible(fbTafu.getClassDayOfWeek()));
		sbhDow.selectNone();
		
		postpone.addAll(sbhDow.getList());
		Collections.rotate(postpone,-1*(LocalDate.now().getDayOfWeek().getValue()-1)-1);
//		Collections.reverse(postpone);
		
		for(DOW d : postpone)
		{
			logger.info(d.toString());
		}
		
		status.addAll(fTafu.allOrderedPositionVisible(fbTafu.getClassStatus()));
		
		sbhViewport.setList(fTafu.allOrderedPositionVisible(fbTafu.getClassViewport()));
		sbhViewport.setDefault();
		sbhViewport.debug(false);
		
		ldBegin = LocalDate.now();		
		logger.info("Start "+ldBegin+" "+ldBegin.getDayOfWeek().getValue());
		if(ldBegin.getDayOfWeek().getValue()>5) {sbhViewport.setDefault(JeeslTafuViewport.Code.fullWeek);}
		ldBegin = ldBegin.minusDays(ldBegin.getDayOfWeek().getValue()-1);
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		scopes.clear(); scopes.addAll(fTafu.all(fbTafu.getClassScope(),realm,rref));scopes.add(emptyScope);
		reloadViewport();
		reload();
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reloadViewport();
		reload();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
		
	}
	
	public void cancelTask() {reset(true);}
	private void reset(boolean rTask)
	{
		if(rTask) {task=null;}
	}
	
	public void navigateWeek(int i)
	{
		ldBegin = ldBegin.plusWeeks(i);
		logger.info("Adjusted "+ldBegin);
		reloadViewport();
		reload();
	}
	
	private void reloadViewport()
	{
		sbhDow.selectNone();
		switch(JeeslTafuViewport.Code.valueOf(sbhViewport.getSelection().getCode()))
		{
			case workingWeek: ldEnd = ldBegin.plusDays(4); sbhDow.preSelect(0,4); break;
			case fullWeek: ldEnd = ldBegin.plusDays(6); sbhDow.preSelect(0,6); break;
		}
		
		LocalDate ld = LocalDate.from(ldBegin);
		while(!ld.isAfter(ldEnd))
		{
			Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
			if(debugOnInfo) {logger.info(ld.getDayOfWeek().getValue()+" "+ld.toString());}
			DOW dow = sbhDow.getList().get(ld.getDayOfWeek().getValue()-1);
			if(dow!=null)
			{
				if(debugOnInfo) {logger.info(dow.toString()+" "+ld.toString());}
				mapDate.put(dow,d);
			}
			ld = ld.plusDays(1);
		}
		
		logger.info("Viewport: "+ldBegin.toString()+" -- "+ldEnd.toString());
	}
	
	protected void reload()
	{
		rows.clear();
		backlog.clear();
		n2m.clear();
		
		backlog.addAll(fTafu.fTafuBacklog(realm,rref,ldBegin,ldEnd));
		
		for(T t : fTafu.fTafuActive(realm,rref,ldBegin,ldEnd))
		{
			DOW dow = sbhDow.getList().get(t.getRecordShow().getDayOfWeek().getValue()-1);

			if(t.getScope()==null) {n2m.put(emptyScope,dow,t);}
			else  {n2m.put(t.getScope(),dow,t);}
		}
		rows.addAll(n2m.toL1());
		Collections.sort(rows,cpScope);
    }
	
	public void selectTask(T task) {this.task=task; selectTask();}
	public void selectTask()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(task));}
		
		if(task.getMarkup()==null)
		{
			MT type = fTafu.fByEnum(fbTafu.getClassMarkupType(),JeeslIoMarkupType.Code.xhtml);
			task.setMarkup(fbTafu.ejbMarkup().build(type));
			try {task = fTafu.save(task);}
			catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		}
		
		if(task.getMarkup().getType().getCode().equals(JeeslIoMarkupType.Code.text.toString()))
		{
			try
			{
				MT type = fTafu.fByEnum(fbTafu.getClassMarkupType(),JeeslIoMarkupType.Code.xhtml);
				task.getMarkup().setType(type);
				task = fTafu.save(task);
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		}
	}
	
	public void addTask()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbTafu.getClassTask()));}
		
		MT type = fTafu.fByEnum(fbTafu.getClassMarkupType(),JeeslIoMarkupType.Code.xhtml);
		task = efTask.build(realm,rref,type);
		task.setStatus(fTafu.fByEnum(fbTafu.getClassStatus(),JeeslTafuStatus.Code.open));
	}
	
	public void saveTask() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(task));}
		
		efTask.converter(fTafu,task);
		efTask.preSave(fTafu,task);
		task = fTafu.save(task);
		if(debugOnInfo) {logger.info(AbstractLogMessage.savedEntity(task));}
		reload();
	}
	
	public void moveTask(int delta) throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info("Move Task: "+delta);
		task.setRecordShow(LocalDate.now().plusDays(delta));
		this.saveTask();
	}
	
	public void postpone(DOW dow) throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info("Postpone to "+dow+" "+postpone.indexOf(dow));
		task.setRecordShow(LocalDate.now().plusDays(1+postpone.indexOf(dow)));
		this.saveTask();
	}
}