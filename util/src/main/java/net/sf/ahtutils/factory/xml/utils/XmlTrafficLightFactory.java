package net.sf.ahtutils.factory.xml.utils;

import net.sf.ahtutils.xml.aht.Query;
import net.sf.ahtutils.xml.utils.TrafficLight;

import org.jeesl.api.exception.xml.JeeslXmlStructureException;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.factory.xml.system.status.XmlScopeFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTrafficLightFactory<L extends JeeslLang, D extends JeeslDescription, SCOPE extends JeeslStatus<L,D,SCOPE>, LIGHT extends JeeslTrafficLight<L,D,SCOPE>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlTrafficLightFactory.class);
		
	private TrafficLight q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlScopeFactory<L,D,SCOPE> xfScope;
	
	public XmlTrafficLightFactory(Query query){this(query.getTrafficLight());}
	public XmlTrafficLightFactory(TrafficLight q)
	{
		this.q=q;
		if(q.isSetScope()) {xfScope = new XmlScopeFactory<>(q.getScope());}
		if(q.isSetLangs()) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
	}
	
	public TrafficLight build(LIGHT ejb) throws JeeslXmlStructureException
	{
		TrafficLight xml = new TrafficLight();
		
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(q.isSetThreshold()){xml.setThreshold(ejb.getThreshold());}
		if(q.isSetColorText()){xml.setColorText(ejb.getColorText());}
		if(q.isSetColorBackground()){xml.setColorBackground(ejb.getColorBackground());}
		
		if(q.isSetLangs()) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(q.isSetDescriptions())
		{
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(q.getDescriptions());
			xml.setDescriptions(f.create(ejb.getDescription()));
		}
		if(q.isSetScope()){xml.setScope(xfScope.build(ejb.getScope()));}
		
		return xml;
	}
}