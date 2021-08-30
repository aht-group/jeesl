package net.sf.ahtutils.monitor.factory;

import java.util.List;

import org.jeesl.api.facade.module.JeeslMonitoringFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.monitoring.Component;
import net.sf.ahtutils.xml.monitoring.Indicator;
import net.sf.ahtutils.xml.monitoring.Transmission;

public class TransmissionFactory extends AbstractTransmissionFactory
{
	final static Logger logger = LoggerFactory.getLogger(TransmissionFactory.class);
	
	public TransmissionFactory(JeeslMonitoringFacade fUm)
	{
		super(fUm);
	}
	
	public Transmission build(List<Indicator> indicators)
	{
		Transmission transmission = new Transmission();		
	
		return transmission;
	}
	
	public Component buildComponent(List<Indicator> indicators)
	{
		Component component = new Component();
		component.getIndicator().addAll(indicators);
		return component;
	}
}