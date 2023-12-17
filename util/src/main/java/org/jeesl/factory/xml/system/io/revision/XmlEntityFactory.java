package org.jeesl.factory.xml.system.io.revision;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
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
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.jeesl.QueryRevision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlEntityFactory <L extends JeeslLang,D extends JeeslDescription,
								CAT extends JeeslRevisionCategory<L,D,CAT,?>,	
								REM extends JeeslRevisionEntityMapping<?,?,?>,
								RE extends JeeslRevisionEntity<L,D,CAT,REM,RA,ERD>,	
								RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
								RER extends JeeslStatus<L,D,RER>,
								RAT extends JeeslStatus<L,D,RAT>,
								ERD extends JeeslRevisionDiagram<L,D,CAT>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlEntityFactory.class);
	
	private Entity q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlCategoryFactory<L,D,CAT> xfCategory;
	private XmlAttributeFactory<L,D,CAT,REM,RE,RA,RER,RAT,ERD> xfAttribute;
	private XmlDiagramFactory<L,D,CAT,ERD> xfDiagram;
	
	public XmlEntityFactory(QueryRevision q){this(q.getEntity());}
	public XmlEntityFactory(Entity q)
	{
		this.q=q;
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<>(q.getDescriptions());}
		if(Objects.nonNull(q.getCategory())) {xfCategory = new XmlCategoryFactory<>(q.getCategory());}
		if(ObjectUtils.isNotEmpty(q.getAttribute())) {xfAttribute = new XmlAttributeFactory<>(q.getAttribute().get(0));}
		if(Objects.nonNull(q.getDiagram())) {xfDiagram = new XmlDiagramFactory<>(q.getDiagram());}
	}
	
	public static Entity build(Class<?> c) {Entity xml = build(); xml.setCode(c.getName());return xml;}
	public static Entity build() {return new Entity();}
	
	public Entity build(RE ejb)
	{
		Entity xml = build();
		
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode()) && ejb.getCode()!=""){xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(ejb.isVisible());}
		if(Objects.nonNull(q.isTimeseries())) {xml.setTimeseries(BooleanComparator.active(ejb.getTimeseries()));}
		if(Objects.nonNull(q.isDocumentation())) {xml.setDocumentation(BooleanComparator.active(ejb.getDocumentation()));}
		if(Objects.nonNull(q.getCategory())) {xml.setCategory(xfCategory.build(ejb.getCategory()));}		
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}
		if(Objects.nonNull(q.getRemark())) {xml.setRemark(XmlRemarkFactory.build(ejb.getDeveloperInfo()));}
		
		if(ObjectUtils.isNotEmpty(q.getAttribute()))
		{
			for(RA attribute : ejb.getAttributes())
			{
				xml.getAttribute().add(xfAttribute.build(attribute));
			}
		}
		if(ObjectUtils.allNotNull(q.getDiagram(),ejb.getDiagram())) {xml.setDiagram(xfDiagram.build(ejb.getDiagram()));}
		
		return xml;
	}
}