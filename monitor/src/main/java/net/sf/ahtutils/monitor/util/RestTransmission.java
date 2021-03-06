package net.sf.ahtutils.monitor.util;

import org.jeesl.api.facade.module.JeeslMonitoringFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.monitoring.Transmission;
import net.sf.exlp.util.xml.JaxbUtil;

public class RestTransmission
{
	final static Logger logger = LoggerFactory.getLogger(RestTransmission.class);
	
	public RestTransmission(JeeslMonitoringFacade fUm)
	{
		
	}
	
	public void send(Transmission transmission)
	{
		JaxbUtil.info(transmission);
	}
}