package net.sf.ahtutils.monitor;

import net.sf.ahtutils.interfaces.rest.UtilsMonitoringRest;
import net.sf.ahtutils.test.UtilsMonitorTestBootstrap;
import net.sf.ahtutils.xml.monitoring.ProcessingResult;
import net.sf.ahtutils.xml.monitoring.Transmission;
import net.sf.exlp.util.xml.JaxbUtil;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliMonitoringRest
{
	final static Logger logger = LoggerFactory.getLogger(CliMonitoringRest.class);
	
	public static void main (String[] args)
	{
		UtilsMonitorTestBootstrap.init();
		
		 ResteasyClient client = new ResteasyClientBuilder().build();
         ResteasyWebTarget target = client.target("http://localhost:8080/lis");

         UtilsMonitoringRest rest = target.proxy(UtilsMonitoringRest.class);
         ProcessingResult result = rest.upload(new Transmission());
         JaxbUtil.info(result);
	  }
}