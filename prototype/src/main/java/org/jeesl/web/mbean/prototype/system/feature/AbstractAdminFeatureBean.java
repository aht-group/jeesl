package org.jeesl.web.mbean.prototype.system.feature;

import java.io.Serializable;

import org.jeesl.api.bean.JeeslFeatureManagerBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.system.EjbSystemFeatureFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.property.JeeslSystemFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAdminFeatureBean <F extends JeeslSystemFeature>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminFeatureBean.class);
	
	private JeeslFacade fFeature;
	
	private JeeslFeatureManagerBean<F> bFeature;
	private JeeslFacesMessageBean bMessage;
	
	private Class<F> cFeature;
		
	private F feature; public F getFeature() {return feature;} public void setFeature(F feature) {this.feature = feature;}

	private EjbSystemFeatureFactory<F> efFeature;
	
	public void initSuper(JeeslFacade fFeature, JeeslFeatureManagerBean<F> bFeature, JeeslFacesMessageBean bMessage, final Class<F> cFeature)
	{
		this.fFeature=fFeature;
		this.bFeature=bFeature;
		this.bMessage=bMessage;
		this.cFeature=cFeature;
		
		efFeature = EjbSystemFeatureFactory.factory(cFeature);
	}

	public void selectFeature() throws JeeslNotFoundException
	{
		feature = fFeature.find(cFeature, feature);
	}
	
	public void addFeature()
	{
		feature = efFeature.build();
	}
	
	public void saveFeature() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		feature = fFeature.saveTransaction(feature);
		bFeature.realodFeatures();
		bMessage.growlSuccessSaved(feature);
	}
}