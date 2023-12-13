package org.jeesl.web.mbean.prototype.module.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.MapFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.map.JeeslLocationLevel;
import org.jeesl.interfaces.model.module.map.JeeslStatisticMapStatus;
import org.jeesl.interfaces.model.module.map.JeeslStatisticalMap;
import org.jeesl.interfaces.model.module.map.JeeslStatisticalMapImplementation;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractStatisticalMapBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										MAP extends JeeslStatisticalMap<L,D>,
										IMP extends JeeslStatisticalMapImplementation<MAP,STATUS,LEVEL>,
										STATUS extends JeeslStatisticMapStatus<L,D,STATUS,?>,
										LEVEL extends JeeslLocationLevel<L,D,LEVEL,?>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractStatisticalMapBean.class);
	
	protected JeeslFacade fMap;
	private final MapFactoryBuilder<L,D,MAP,IMP,STATUS,LEVEL> fbMap;
	
	private final Nested2Map<MAP,LEVEL,IMP> nestedMap; public Nested2Map<MAP,LEVEL,IMP> getNestedMap() {return nestedMap;}
	private final List<MAP> maps; public List<MAP> getMaps() {return maps;}
	protected final List<STATUS> status; public List<STATUS> getStatus() {return status;}
	protected final List<LEVEL> levels; public List<LEVEL> getLevels() {return levels;}
	private final List<IMP> implementations; public List<IMP> getImplementations() {return implementations;}
	
	private MAP map; public MAP getMap() {return map;} public void setMap(MAP map) {this.map = map;}
	private IMP implementation; public IMP getImplementation() {return implementation;} public void setImplementation(IMP implementation) {this.implementation = implementation;}
	
	public AbstractStatisticalMapBean(MapFactoryBuilder<L,D,MAP,IMP,STATUS,LEVEL> fbMap)
	{
		super(fbMap.getClassL(),fbMap.getClassD());
		this.fbMap=fbMap;
		maps = new ArrayList<>();
		levels = new ArrayList<>();
		status = new ArrayList<>();
		implementations = new ArrayList<>();
		nestedMap = new Nested2Map<>();
	}

	protected void postConstructMap(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslFacade fMap)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fMap=fMap;
		
		status.addAll(fMap.allOrderedPositionVisible(fbMap.getClassStatus()));
		try
		{
			initPageSettings();
			reloadMaps();
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
	}
	
	protected abstract void initPageSettings() throws JeeslNotFoundException;
	
	public void cancelMap(){reset(true);}
	private void reset(boolean rMap)
	{
		if(rMap) {map=null;}
	}
	
	private void reloadMaps()
	{
		maps.clear();
		maps.addAll(fMap.all(fbMap.getClassMap()));
		
		nestedMap.clear();
		for(IMP i : fMap.all(fbMap.getClassImplementation()))
		{
			nestedMap.put(i.getMap(),i.getLevel(),i);
		}
	}
	
	public void addMap() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbMap.getClassMap()));}
		map = fbMap.ejbMap().build(maps);
		map.setName(efLang.createEmpty(localeCodes));
		map.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void selectMap() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(map));}
		map = fMap.find(fbMap.getClassMap(),map);
		map = efLang.persistMissingLangs(fMap,lp.getLocales(),map);
		map = efDescription.persistMissingLangs(fMap,lp.getLocales(),map);
		reloadImplementations();
	}
	
	public void saveMap() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(map));}
		
		map = fMap.save(map);
		reloadMaps();
		reloadImplementations();
		bMessage.growlSaved(map);
	}
	
	public void deleteMap() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(map));}
		fMap.rm(map);
		bMessage.growlDeleted(map);
		reset(true);
		
		reloadMaps();
	}
	
	private void reloadImplementations()
	{
		implementations.clear();
		implementations.addAll(fMap.allForParent(fbMap.getClassImplementation(), map));
	}
	
	public void addImplementation() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbMap.getClassImplementation()));}
		implementation = fbMap.ejbImplementation().build(map);
	}
	
	public void selectImplementation() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(implementation));}
	}
	
	public void saveImplementation() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(map));}
		implementation.setLevel(fMap.find(fbMap.getClassLevel(), implementation.getLevel()));
		implementation.setStatus(fMap.find(fbMap.getClassStatus(), implementation.getStatus()));
		implementation = fMap.save(implementation);
		reloadImplementations();
		bMessage.growlSaved(implementation);
	}
}