package net.sf.ahtutils.monitor.util;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.api.facade.module.JeeslMonitoringFacade;
import org.jeesl.model.xml.module.monitoring.Transmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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