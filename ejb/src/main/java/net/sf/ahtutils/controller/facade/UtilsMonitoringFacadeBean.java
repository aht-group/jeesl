package net.sf.ahtutils.controller.facade;

import javax.persistence.EntityManager;

import org.jeesl.api.facade.module.JeeslMonitoringFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsMonitoringFacadeBean extends JeeslFacadeBean implements JeeslMonitoringFacade
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(UtilsMonitoringFacadeBean.class);
	
	public UtilsMonitoringFacadeBean(EntityManager em)
	{
		super(em);
	}
}