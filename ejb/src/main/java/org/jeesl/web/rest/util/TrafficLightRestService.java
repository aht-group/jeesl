package org.jeesl.web.rest.util;

import org.jeesl.api.exception.xml.JeeslXmlStructureException;
import org.jeesl.api.rest.system.traffic.JeeslTrafficLightRestExport;
import org.jeesl.api.rest.system.traffic.JeeslTrafficLightRestImport;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.system.util.EjbTrafficLightFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.util.query.xml.UtilsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.factory.xml.utils.XmlTrafficLightFactory;
import net.sf.ahtutils.factory.xml.utils.XmlTrafficLightsFactory;
import net.sf.ahtutils.xml.sync.DataUpdate;
import net.sf.ahtutils.xml.utils.TrafficLight;
import net.sf.ahtutils.xml.utils.TrafficLights;

public class TrafficLightRestService <L extends JeeslLang, D extends JeeslDescription,
										SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>,
										LIGHT extends JeeslTrafficLight<L,D,SCOPE>>
		implements JeeslTrafficLightRestExport,JeeslTrafficLightRestImport
{
	final static Logger logger = LoggerFactory.getLogger(TrafficLightRestService.class);
	
	private JeeslFacade fUtils;
	
	private final Class<SCOPE> cScope;
	private final Class<LIGHT> cLight;
	
	private XmlTrafficLightFactory<L,D,SCOPE,LIGHT> xfLight;
	private EjbTrafficLightFactory<L,D,SCOPE,LIGHT> efLight;
	
	private TrafficLightRestService(JeeslFacade fUtils,final Class<L> cLang,final Class<D> cDescription,final Class<SCOPE> cScope,final Class<LIGHT> cLight)
	{
		this.fUtils=fUtils;
		this.cScope=cScope;
		this.cLight=cLight;
		
		xfLight = new XmlTrafficLightFactory<L,D,SCOPE,LIGHT>(UtilsQuery.get(UtilsQuery.Key.exTrafficLight));
		efLight = EjbTrafficLightFactory.factory(cLang,cDescription,cLight);
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>,
					LIGHT extends JeeslTrafficLight<L,D,SCOPE>>
		TrafficLightRestService<L,D,SCOPE,LIGHT>
			factory(JeeslFacade fUtils,final Class<L> cL,final Class<D> cD,final Class<SCOPE> cScope,final Class<LIGHT> cLight)
	{
		return new TrafficLightRestService<L,D,SCOPE,LIGHT>(fUtils,cL,cD,cScope,cLight);
	}
	
	@Override
	public TrafficLights exportTrafficLights()
	{
		TrafficLights xml = XmlTrafficLightsFactory.build();
		for(LIGHT light : fUtils.all(cLight))
		{
			try
			{
				xml.getTrafficLight().add(xfLight.build(light));
			}
			catch (JeeslXmlStructureException e) {e.printStackTrace();}
		}
		return xml;
	}

	@Override
	public DataUpdate importTrafficLights(TrafficLights lights)
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		dut.setType(XmlTypeFactory.build(cLight.getName(),"DB Import"));
		for(TrafficLight xLight : lights.getTrafficLight())
		{
			try
			{
				SCOPE scope = fUtils.fByCode(cScope,xLight.getScope().getCode());
				LIGHT eLight = efLight.build(scope,xLight);
				eLight = fUtils.persist(eLight);
				dut.success();
			}
			catch (JeeslNotFoundException e) {dut.fail(e,true);}
			catch (JeeslConstraintViolationException e) {dut.fail(e,true);}
		}
		return dut.toDataUpdate();
	}
}