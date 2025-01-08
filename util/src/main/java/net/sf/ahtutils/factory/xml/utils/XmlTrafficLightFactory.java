package net.sf.ahtutils.factory.xml.utils;

import java.util.Objects;

import org.jeesl.api.exception.xml.JeeslXmlStructureException;
import org.jeesl.factory.xml.io.locale.status.XmlScopeFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.model.xml.system.util.TrafficLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;

public class XmlTrafficLightFactory<L extends JeeslLang, D extends JeeslDescription,
									SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>,
									LIGHT extends JeeslTrafficLight<L,D,SCOPE>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlTrafficLightFactory.class);
		
	private TrafficLight q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlScopeFactory<L,D,SCOPE> xfScope;
	
	public XmlTrafficLightFactory(Query query){this(query.getTrafficLight());}
	public XmlTrafficLightFactory(TrafficLight q)
	{
		this.q=q;
		if(Objects.nonNull(q.getScope())) {xfScope = new XmlScopeFactory<>(q.getScope());}
		if(Objects.nonNull(q.getLangs())) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())) {xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public TrafficLight build(LIGHT ejb) throws JeeslXmlStructureException
	{
		TrafficLight xml = new TrafficLight();
		
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getThreshold())) {xml.setThreshold(ejb.getThreshold());}
		if(Objects.nonNull(q.getColorText())) {xml.setColorText(ejb.getColorText());}
		if(Objects.nonNull(q.getColorBackground())) {xml.setColorBackground(ejb.getColorBackground());}
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}
		if(Objects.nonNull(q.getScope())){xml.setScope(xfScope.build(ejb.getScope()));}
		
		return xml;
	}
}