package org.jeesl.factory.xml.system.io.revision;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.factory.xml.system.status.XmlCategoryFactory;
import org.jeesl.factory.xml.system.util.text.XmlRemarkFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.jeesl.QueryRevision;
import org.jeesl.model.xml.system.revision.Entity;
import org.jeesl.util.comparator.pojo.BooleanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlEntityFactory <L extends JeeslLang,D extends JeeslDescription,
								RC extends JeeslRevisionCategory<L,D,RC,?>,	
								REM extends JeeslRevisionEntityMapping<?,?,?>,
								RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,	
								RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
								RER extends JeeslStatus<L,D,RER>,
								RAT extends JeeslStatus<L,D,RAT>,
								ERD extends JeeslRevisionDiagram<L,D,RC>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlEntityFactory.class);
	
	private Entity q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlCategoryFactory<L,D,RC> xfCategory;
	private XmlAttributeFactory<L,D,RC,REM,RE,RA,RER,RAT,ERD> xfAttribute;
	private XmlDiagramFactory<L,D,RC,ERD> xfDiagram;
	
	public XmlEntityFactory(QueryRevision q){this(q.getEntity());}
	public XmlEntityFactory(Entity q)
	{
		this.q=q;
		if(q.isSetLangs()){xfLangs = new XmlLangsFactory<>(q.getLangs());}
		if(q.isSetDescriptions()){xfDescriptions = new XmlDescriptionsFactory<>(q.getDescriptions());}
		if(q.isSetCategory()){xfCategory = new XmlCategoryFactory<>(q.getCategory());}
		if(q.isSetAttribute()){xfAttribute = new XmlAttributeFactory<>(q.getAttribute().get(0));}
		if(q.isSetDiagram()) {xfDiagram = new XmlDiagramFactory<>(q.getDiagram());}
	}
	
	public static Entity build(Class<?> c) {Entity xml = build(); xml.setCode(c.getName());return xml;}
	public static Entity build() {return new Entity();}
	
	public Entity build(RE ejb)
	{
		Entity xml = build();
		
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(q.isSetCode()&&ejb.getCode()!=""){xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
		if(q.isSetVisible()){xml.setVisible(ejb.isVisible());}
		if(q.isSetTimeseries()) {xml.setTimeseries(BooleanComparator.active(ejb.getTimeseries()));}
		if(q.isSetDocumentation()) {xml.setDocumentation(BooleanComparator.active(ejb.getDocumentation()));}
		if(q.isSetCategory()){xml.setCategory(xfCategory.build(ejb.getCategory()));}		
		
		if(q.isSetLangs()){xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(q.isSetDescriptions()){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}
		if(q.isSetRemark()){xml.setRemark(XmlRemarkFactory.build(ejb.getDeveloperInfo()));}
		
		if(q.isSetAttribute())
		{
			for(RA attribute : ejb.getAttributes())
			{
				xml.getAttribute().add(xfAttribute.build(attribute));
			}
		}
		if(q.isSetDiagram() && ejb.getDiagram()!=null) {xml.setDiagram(xfDiagram.build(ejb.getDiagram()));}
		
		return xml;
	}
}