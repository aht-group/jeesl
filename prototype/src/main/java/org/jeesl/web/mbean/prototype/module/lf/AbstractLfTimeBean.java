package org.jeesl.web.mbean.prototype.module.lf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslLogframeFacade;
import org.jeesl.controller.handler.module.lf.IntervalCalendarHandler;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.LfFactoryBuilder;
import org.jeesl.interfaces.bean.lf.LfTimeBean;
import org.jeesl.interfaces.model.module.lf.JeeslLfConfiguration;
import org.jeesl.interfaces.model.module.lf.JeeslLfLogframe;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfIndicator;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfIndicatorLevel;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfIndicatorType;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfUnit;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfVerificationSource;
import org.jeesl.interfaces.model.module.lf.time.JeeslLfTimeElement;
import org.jeesl.interfaces.model.module.lf.time.JeeslLfTimeGroup;
import org.jeesl.interfaces.model.module.lf.time.JeeslLfTimeInterval;
import org.jeesl.interfaces.model.module.lf.value.JeeslLfValue;
import org.jeesl.interfaces.model.module.lf.value.JeeslLfValueType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLfTimeBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
												R extends JeeslTenantRealm<L,D,R,?>,
												LF extends JeeslLfLogframe<L,D,R,LFI,IL,IT>,
												LFI extends JeeslLfIndicator<LF,IL,IT,IU,IV,TG,LFM>,
												IL extends JeeslLfIndicatorLevel<L, D,R, IL, ?>,
												IT extends JeeslLfIndicatorType<L, D,R, IT, ?>,
												IU extends JeeslLfUnit<L,D,R,IU,?>,
												IV extends JeeslLfVerificationSource<L,D,R,IV,?>,
												TG extends JeeslLfTimeGroup<L,TI>,
												TI extends JeeslLfTimeInterval<L,D,TI,?>,
												TE extends JeeslLfTimeElement<L,TG>,
												LFM extends JeeslLfValue<LFI,VT,TG,TE>,
												VT extends JeeslLfValueType<L,D,VT,?>,
												LFC extends JeeslLfConfiguration<LF,IT>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,LfTimeBean<TI, TG, TE>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractLfTimeBean.class);

	protected final LfFactoryBuilder<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFM,VT,LFC> fbLf;
	protected JeeslLogframeFacade<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFM,VT,LFC> fLf;

	protected IntervalCalendarHandler<TI,TG,TE> intervalCalendarHandler; public IntervalCalendarHandler<TI,TG,TE> getIntervalCalendarHandler() {return intervalCalendarHandler;}
	protected boolean uiGenerate;
	public boolean getUiGenerate()
	{
		if(getUiAllowGenerate() && uiGenerate)
		{
			if(timeGroup.getEndDate()!= null && timeGroup.getStartDate()!= null && timeGroup.getValues() > 0) {return false;}
			return true;
		}
		return false;
	}

	protected boolean uiAllowGenerate;
	public boolean getUiAllowGenerate()
	{
		if(timeGroup!=null && timeGroup.getId() > 0 && timeGroup.getValues()==0 && timeGroup.getInterval() != null)
		{
			logger.info(timeGroup.getInterval().getCode());
			if(timeGroup.getInterval().getCode().equals("none")) {return false;}
			return true;
		}
		return false;
	}
	protected List<TG> timeGroups; public List<TG> getTimeGroups() {return timeGroups;} public void setTimeGroups(List<TG> timeGroups) {this.timeGroups = timeGroups;}
	protected TG timeGroup; public TG getTimeGroup() {return timeGroup;} public void setTimeGroup(TG timeGroup) {this.timeGroup = timeGroup;}
	protected List<TE> elements; public List<TE> getElements() {return elements;} public void setElements(List<TE> elements) {this.elements = elements;}
	protected TE element; public TE getElement() {return element;} public void setElement(TE element) {this.element = element;}
	protected List<TI> intervals; public List<TI> getIntervals() {return intervals;} public void setIntervals(List<TI> intervals) {this.intervals = intervals;}

	public AbstractLfTimeBean(LfFactoryBuilder<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFM,VT,LFC> fbLf)
	{
		super(fbLf.getClassL(),fbLf.getClassD());
		this.fbLf=fbLf;
		this.timeGroups = new ArrayList<TG>();
		this.intervals = new ArrayList<TI>();
	}

	protected void postConstructHd(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslLogframeFacade<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFM,VT,LFC> fLf)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fLf=fLf;
		intervalCalendarHandler = new IntervalCalendarHandler<TI,TG,TE>(this);
		this.intervals = fLf.all(fbLf.getClassTI());
		reloadTimeGroups();
	}

	protected void reloadTimeGroups()
	{
		timeGroups = fLf.all(fbLf.getClassTG());
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbLf.getClassTG(),timeGroups));}
		uiGenerate = false;
	}

	public void addTimeGroup()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbLf.getClassTG()));}
		timeGroup = fbLf.ejbTimeGroup().build();
		elements = new ArrayList<>();
		intervalCalendarHandler.initIntervalSize();
		reset(false,true);
	}

	public void saveTimeGroup() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(timeGroup));}
		timeGroup.setInterval(fLf.find(fbLf.getClassTI(),timeGroup.getInterval()));
		timeGroup = fLf.save(timeGroup);
		reloadTimeGroups();
		reloadElements();
		resetElement();
	}

	public void selectTimeGroup() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(timeGroup));}
		timeGroup = fLf.find(fbLf.getClassTG(),timeGroup);
		reloadElements();
		resetElement();
	}

	public void deleteTimeGroup() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(timeGroup));}
		elements = fLf.allForParent(fbLf.getClassTE(),timeGroup);
		for (TE tte : elements){fLf.rm(tte);}
		elements.clear();
		fLf.rm(timeGroup);
		reloadTimeGroups();
		reset(true,true);
		intervalCalendarHandler.initIntervalSize();
	}

	public void reorderTimeGroups() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fLf, timeGroups);}
	public void addElement()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbLf.getClassTE()));}
		if(element ==null || element.getId()!=0)
		{
			element = fbLf.ejbTimeElement().build();
		}
	}

	public void selectElement()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(timeGroup));}
	}

	public void saveElement() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(element));}
		element.setGroup(timeGroup);
		element = fLf.save(element);
		elements.add(element);
		reloadElements();
		saveElementTimeGroup();
		reloadTimeGroups();
	}

	public void deleteElement() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(timeGroup));}
		fLf.rm(element);

		reloadElements();
		saveElementTimeGroup();

		reloadTimeGroups();
		reset(false,true);
	}

	private void saveElementTimeGroup() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(elements.size() > 0)
		{
			timeGroup.setStartDate(elements.get(0).getRecord());
			timeGroup.setEndDate(elements.get(elements.size()-1).getRecord());
		}
		if(elements.size() == 0)
		{
			timeGroup.setStartDate(null);
			timeGroup.setEndDate(null);
		}
		timeGroup.setValues(elements.size());
		timeGroup = fLf.save(timeGroup);
	}
	public void reloadElements()
	{
		elements = fLf.allForParent(fbLf.getClassTE(),timeGroup);
		if(elements==null) {elements=new ArrayList<>();}
		uiGenerate = false;
		intervalCalendarHandler.initIntervalSize();
	}

	public void generateTimeGroup() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info("generateTimeGroup" + AbstractLogMessage.saveEntity(timeGroup));}

		timeGroup.setInterval(fLf.find(fbLf.getClassTI(),timeGroup.getInterval()));

		if(debugOnInfo) {logger.info("timeGroup : " +timeGroup);}
		elements = intervalCalendarHandler.generateTimeElements(timeGroup);
		for(TE element: elements)
		{
		logger.info("saving element with record  " + element.getRecord().toString());
		element = fLf.save(element);
		}
		timeGroup.setValues(elements.size());
		saveTimeGroup();
		reloadElements();
	}


	private void reset(boolean rTimeGroup, boolean rElement)
	{
		if(rTimeGroup) {timeGroup=null;}
		if(rElement) {element=null;}
	}


	public void resetTimeGroup() {reset(true, false);}
	public void resetElement() {reset(false,true);}


	public void selectIntervalType()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(timeGroup));}
		resetElement();
		if(this.elements.size() > 0)
		{
			timeGroup = fLf.find(fbLf.getClassTG(),timeGroup);
			FacesContext context = FacesContext.getCurrentInstance();
	        FacesMessage message = new FacesMessage("Changing Interval not allowed");
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        context.addMessage("msgTimeGroup", message);
		}
		else
		{
		timeGroup.setInterval(fLf.find(fbLf.getClassTI(),timeGroup.getInterval()));
		updateUiTimeGroup();
		}
	}

	public void updateUiTimeGroup()
	{
		uiGenerate = true;
		TI interval = fLf.find(fbLf.getClassTI(),timeGroup.getInterval());
		intervalCalendarHandler.changeInterval(interval);
	}


/*-----------------------------------------------------------------------*/
    @Override public void updateTimeGroupStartAndEndDate(Date date)
    {
    	timeGroup.setStartDate(date);
    	timeGroup.setEndDate(date);
    }

    @Override public Date getTimeGroupStartDate()
    {
    	return timeGroup.getStartDate();
    }

    @Override public void updateTimeGroupEndDate(Date date)
    {
    	timeGroup.setEndDate(date);
    }

    @Override
	public TE createNewTimeElement()
    {
		return fbLf.ejbTimeElement().build();
	}


}