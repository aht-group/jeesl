package org.jeesl.web.mbean.prototype.system.symbol.lights;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslTrafficLightBean;
import org.jeesl.api.facade.system.graphic.JeeslTrafficLightFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.system.LightFactoryBuilder;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.factory.ejb.system.util.EjbTrafficLightFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminTrafficLightBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											LIGHT extends JeeslTrafficLight<L,D,SCOPE>,
											SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminTrafficLightBean.class);
	
	private JeeslTrafficLightFacade<L,D,LIGHT,SCOPE> fTl;
	private final LightFactoryBuilder<L,D,LIGHT,SCOPE> fbLight;
	private JeeslTrafficLightBean<L,D,LIGHT,SCOPE> bLight;
	
    private final EjbLangFactory<L> efLang;
    private final EjbDescriptionFactory<D> efDescription;
    
	private JeeslLocaleProvider<LOC> lp;
	
	private EjbTrafficLightFactory<L,D,SCOPE,LIGHT> efLight;
	
	private List<SCOPE> trafficLightScopes; public List<SCOPE> getTrafficLightScopes(){return trafficLightScopes;}
	protected List<LIGHT> trafficLights; public List<LIGHT> getTrafficLights(){return trafficLights;}
	
	protected SCOPE scope; public SCOPE getScope(){return scope;} public void setScope(SCOPE scope){this.scope = scope;}
	
	public AbstractAdminTrafficLightBean(LightFactoryBuilder<L,D,LIGHT,SCOPE> fbLight)
	{
		this.fbLight=fbLight;
		efLight = EjbTrafficLightFactory.factory(fbLight.getClassL(),fbLight.getClassD(),fbLight.getClassLight());
        efLang = EjbLangFactory.instance(fbLight.getClassL());
        efDescription = EjbDescriptionFactory.factory(fbLight.getClassD());
	}
	
	public void initSuper(JeeslLocaleProvider<LOC> lp, JeeslTrafficLightFacade<L,D,LIGHT,SCOPE> fTl, JeeslTrafficLightBean<L,D,LIGHT,SCOPE> bLight)
	{
		this.lp=lp;
		this.fTl=fTl;
		this.bLight=bLight;
		
		 reloadTrafficLightScopes();
	}
	
	private void reloadTrafficLightScopes()
	{
		trafficLightScopes = fTl.all(fbLight.getClassScope());
		logger.trace("Results: " + trafficLightScopes.size() +" scopes loaded.");
	}
	
	public void selectScope()
	{
		logger.info(AbstractLogMessage.selectEntity(scope));
		reloadTrafficLights();
	}

	private void reloadTrafficLights()
	{
		trafficLights = fTl.allOrderedTrafficLights(scope);
		logger.debug("Results: " + trafficLights.size());
	}
	
	//Light
	protected LIGHT trafficLight;
	public LIGHT getTrafficLight(){return trafficLight;}
	public void setTrafficLight(LIGHT trafficLight){this.trafficLight = trafficLight;}
	
	public void addTrafficLight() throws JeeslConstraintViolationException
	{
		logger.debug(AbstractLogMessage.createEntity(fbLight.getClassLight()));
		trafficLight = efLight.build(scope);
		trafficLight.setName(efLang.buildEmpty(lp.getLocales()));
		trafficLight.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	
	public void selectTrafficLight()
	{
		logger.debug(AbstractLogMessage.selectEntity(trafficLight));
	}
	
	public void save() throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.debug(AbstractLogMessage.saveEntity(trafficLight));
		trafficLight = fTl.save(trafficLight);
		reloadTrafficLights();
		bLight.refreshTrafficLights();
	}
	
	public void rm() throws JeeslConstraintViolationException
	{
		logger.debug(AbstractLogMessage.deleteEntity(trafficLight));
		fTl.rm(trafficLight);
		trafficLight=null;
		reloadTrafficLights();
		bLight.refreshTrafficLights();
	}
}