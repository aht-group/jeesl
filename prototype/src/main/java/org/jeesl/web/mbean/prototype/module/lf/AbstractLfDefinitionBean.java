package org.jeesl.web.mbean.prototype.module.lf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslLogframeFacade;
import org.jeesl.controller.handler.ui.UiSlotWidthHandler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.LfFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
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
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractLfDefinitionBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
												R extends JeeslTenantRealm<L,D,R,?>,
												LF extends JeeslLfLogframe<L,D,R,LFI,IL,IT>,
												LFI extends JeeslLfIndicator<LF,IL,IT,IU,IV,TG,LFV>,
												IL extends JeeslLfIndicatorLevel<L, D,R, IL, ?>,
												IT extends JeeslLfIndicatorType<L, D,R, IT, ?>,
												IU extends JeeslLfUnit<L,D,R,IU,?>,
												IV extends JeeslLfVerificationSource<L,D,R,IV,?>,
												TG extends JeeslLfTimeGroup<L,TI>,
												TI extends JeeslLfTimeInterval<L,D,TI,?>,
												TE extends JeeslLfTimeElement<L,TG>,
												LFV extends JeeslLfValue<LFI,VT,TG,TE>,
												VT extends JeeslLfValueType<L,D,VT,?>,
												LFC extends JeeslLfConfiguration<LF,IT>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractLfDefinitionBean.class);

	private final UiSlotWidthHandler slotHandler; public UiSlotWidthHandler getSlotHandler() {return slotHandler;}
	protected final LfFactoryBuilder<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFV,VT,LFC> fbLf;
	protected JeeslLogframeFacade<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFV,VT,LFC> fLf;

	protected final List<LFI> indicators; public List<LFI> getIndicators() {return indicators;}
	protected LFI indicator; public LFI getIndicator() {return indicator;} public void setIndicator(LFI indicator) {this.indicator = indicator;}

	protected final List<LFV> indicatorValues; public List<LFV> getIndicatorValues() {return indicatorValues;} public void setIndicatorValues(List<LFV> monitorings) {this.indicatorValues.clear(); this.indicatorValues.addAll(monitorings);}
	protected LFV indicatorValue; public LFV getIndicatorValue() {return indicatorValue;} public void setIndicatorValue(LFV monitoring) {this.indicatorValue = monitoring;}

	protected final List<IL> indicatorLevels; public List<IL> getIndicatorLevels() {return indicatorLevels;}
	protected final List<IT> indicatorTypes; public List<IT> getIndicatorTypes() {return indicatorTypes;}
	protected final List<IU> units; public List<IU> getUnits() {return units;}
	protected final List<VT> valueTypes; public List<VT> getValueTypes() {return valueTypes;}

	protected final List<TI> intervals; public List<TI> getIntervals() {return intervals;} public void setIntervals(List<TI> intervalTypes) {this.intervals.clear(); this.intervals.addAll(intervalTypes);}
	protected TI interval; public TI getInterval() {return interval;} public void setInterval(TI interval) {this.interval = interval;}
	protected final List<TG> timeGroups; public List<TG> getTimeGroups() {return timeGroups;} public void setTimeGroups(List<TG> timeGroups) {this.timeGroups.clear(); this.timeGroups.addAll(timeGroups);}
	protected TG timeGroup; public TG getTimeGroup() { return timeGroup;} public void setTimeGroup(TG timeGroup) {this.timeGroup = timeGroup;}
	protected final List<TE> timeElements; public List<TE> getTimeElements() {return timeElements;} public void setTimeElements(List<TE> timeElements) {this.timeElements.clear(); this.timeElements.addAll(timeElements);}
	protected TE timeElement; public TE getTimeElement() {return timeElement;} public void setTimeElement(TE timeElement) {this.timeElement = timeElement;}

	protected Map<IT,Map<IL,List<LFI>>> typesMap; public Map<IT,Map<IL,List<LFI>>> getTypesMap() {return typesMap;}
	private LF logframe; public LF getLogframe() {return logframe;} public void setLogframe(LF logframe) {this.logframe = logframe;}

	public AbstractLfDefinitionBean(LfFactoryBuilder<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFV,VT,LFC> fbLf)
	{
		super(fbLf.getClassL(),fbLf.getClassD());
		this.fbLf=fbLf;

		this.indicators = new ArrayList<LFI>();
		this.indicatorValues = new ArrayList<LFV>();
		this.indicatorLevels = new ArrayList<IL>();
		this.indicatorTypes = new ArrayList<IT>();
		this.units = new ArrayList<IU>();
		this.valueTypes = new ArrayList<VT>();

		this.intervals = new ArrayList<TI>();
		this.timeGroups = new ArrayList<TG>();
		this.timeElements = new ArrayList<TE>();

		slotHandler = new UiSlotWidthHandler();
		slotHandler.set(12);
	}

	protected void postConstructLfDefinition(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
												JeeslLogframeFacade<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFV,VT,LFC> fLf)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fLf = fLf;
		this.indicatorLevels.clear();
		this.indicatorLevels.addAll(fLf.all(fbLf.getClassIL()));
		this.indicatorTypes.clear();
		this.indicatorTypes.addAll(fLf.all(fbLf.getClassIT()));
		this.units.clear();
		this.units.addAll(fLf.all(fbLf.getClassIU()));
		this.valueTypes.addAll(fLf.all(fbLf.getClassVT()));

		intervals.clear();
		intervals.addAll(fLf.all(fbLf.getClassTI()));

		reloadIndicators();
	}

	protected void reloadIndicators()
	{
		if(EjbIdFactory.isSaved(logframe))
		{
			indicators.clear();
			indicators.addAll(fLf.allForParent(fbLf.getClassLFI(), logframe));
		}

		typesMap = new HashMap<>();

		for (LFI lf : indicators)
		{
			IT type = lf.getType();
			IL level = lf.getLevel();
			if(typesMap.get(type)==null)
			{
				typesMap.put(type, new HashMap<>());
				typesMap.get(type).put(level, new ArrayList<>());
				typesMap.get(type).get(level).add(lf);
			}
			else
			{
				if(typesMap.get(type).get(level)==null)
				{
				typesMap.get(type).put(level, new ArrayList<>());
				}
				typesMap.get(type).get(level).add(lf);
			}
		}
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbLf.getClassLFI(),indicators));}
	}

	public void resetIndicatorValue() {reset(false,true);}
	private void reset(boolean rIndicator, boolean rIndicatorValue)
	{
		if(rIndicator) {indicator=null; indicatorValues.clear();}
		if(rIndicatorValue) {indicatorValue=null;}
	}

	public void addIndicator()
	{
		slotHandler.set(8,4);
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbLf.getClassLF()));}
		indicator =  fbLf.ejbLfIndicator().build();
		indicator.setLogframe(logframe);
	}


	public void saveIndicator() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(indicator));}

		logger.info("Saving "+indicator.getLogframe().toString());

		indicator.setLevel(fLf.find(fbLf.getClassIL(),indicator.getLevel()));
		indicator.setType(fLf.find(fbLf.getClassIT(),indicator.getType()));
		indicator.setUnit(fLf.find(fbLf.getClassIU(),indicator.getUnit()));
		indicator = fLf.save(indicator);
		reloadIndicators();
	}

	public void selectIndicator(LFI selectdIndicator) throws JeeslConstraintViolationException
	{
		slotHandler.set(8,4);
		indicator = fLf.find(fbLf.getClassLFI(),selectdIndicator);
		reloadIndicatorValues();
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(selectdIndicator));}
	}

	public void deleteIndicator() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.rmEntity(indicator));}
		fLf.rm(indicator);
		reloadIndicators();
		reset(true,true);
		resetIndicator();
	}

	public void resetIndicator() {reset(true,true);slotHandler.set(12);}


	/**
	 * -----------------------------------------------------------------------------------
	 */
	protected void reloadIndicatorValues()
	{
		indicatorValues.clear();
		indicatorValues.addAll(fLf.allForParent(fbLf.getClassLFM(),indicator));

		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbLf.getClassLFM(),indicatorValues));}
	}

	public void addIndicatorValue()
	{
		slotHandler.set(8,4);
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbLf.getClassLF()));}
		indicatorValue =  fbLf.ejbLfIndicatorValue().build();
		indicatorValue.setIndicator(indicator);
	}

	public void saveIndicatorValue() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(indicatorValue));}
		indicatorValue.setIndicator(fLf.find(fbLf.getClassLFI(),indicator));
		indicatorValue.setType(fLf.find(fbLf.getClassVT(),indicatorValue.getType()));
		indicatorValue = fLf.save(indicatorValue);
		reloadIndicatorValues();
	}

	public void selectIndicatorValue() throws JeeslConstraintViolationException
	{
		indicatorValue = fLf.find(fbLf.getClassLFM(),indicatorValue);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(indicatorValue));}
		timeElement = fLf.find(fbLf.getClassTE(),indicatorValue.getTimeElement());
		timeGroup = fLf.find(fbLf.getClassTG(),timeElement.getGroup());
		interval = fLf.find(fbLf.getClassTI(),timeGroup.getInterval());
		timeGroups.clear();
		timeGroups.addAll(fLf.allForParent(fbLf.getClassTG(), interval));
		timeElements.clear();
		timeElements.addAll(fLf.allForParent(fbLf.getClassTE(), timeGroup));
	}


	public void deleteIndicatorValue() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.rmEntity(indicatorValue));}
		fLf.rm(indicatorValue);
		//Indicator = fLf.save(Indicator);
		reloadIndicatorValues();
		reset(false,true);
	}

	public void resetTimeGroup() {reset(true, false);}
	public void resetElement() {reset(false,true);}


	public void initTimeElementSelection()
	{
		logger.info("initTimeElementSelection..." );
	}

	public void onTimeIntervalChanged()
	{
		interval = fLf.find(fbLf.getClassTI(), interval);
		logger.info("onTimeIntervalChanged..." + interval);

		timeGroups.clear();
		timeGroups.addAll(fLf.allForParent(fbLf.getClassTG(), interval));
		logger.info("timeGroups.size: " + timeGroups.size());
		if(timeGroups.size() > 0)
		{
			timeGroup = timeGroups.get(0);
			timeElements.clear();
			timeElements.addAll(fLf.allForParent(fbLf.getClassTE(), timeGroup));
			timeElement = timeElements.size() > 0 ? timeElements.get(0) : null;
			indicatorValue.setTimeElement(timeElement);

			logger.info("timeGroup: " + timeGroup);
			logger.info("timeElements: " + timeElements);
			logger.info("timeElement: " + timeElement);
		}
		else
		{
			timeGroup = null;
			timeElement = null;
			timeElements.clear();
		}

	}

	public void onTimeGroupChanged()
	{
		timeGroup = fLf.find(fbLf.getClassTG(), timeGroup);
		logger.info("onTimeGroupChanged..." + timeGroup);
		timeElement = null;
		timeElements.clear();
		timeElements.addAll(fLf.allForParent(fbLf.getClassTE(), timeGroup));

		if(timeElements.size() > 0)
		{
			timeElement = timeElements.size() > 0 ? timeElements.get(0) : null;
			indicatorValue.setTimeElement(timeElement);
		}
		logger.info("timeElements.size: " + timeElements.size());
		logger.info("timeElements: " + timeElements);
		logger.info("timeElement: " + timeElement);
	}

	public void onTimeElementChanged()
	{
		logger.info("onTimeElementChanged..." + timeElement );
		timeElement = fLf.find(fbLf.getClassTE(), timeElement);
		indicatorValue.setTimeElement(timeElement);
	}

	/*
	public void selectIndicatorValue(LFV indiValue) throws JeeslConstraintViolationException
	{
		indicatorValue = fLf.find(fbLf.getClassLFM(),indiValue);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(indiValue));}
	}
	*/
}