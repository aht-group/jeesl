package org.jeesl.factory.xml.system.io.revision;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.factory.xml.system.util.text.XmlRemarkFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.label.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlAttributeFactory <L extends JeeslLang,D extends JeeslDescription,
								RC extends JeeslRevisionCategory<L,D,RC,?>,	
								REM extends JeeslRevisionEntityMapping<?,?,?>,
								RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,										
								RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
								RER extends JeeslStatus<L,D,RER>,
								RAT extends JeeslStatus<L,D,RAT>,
								ERD extends JeeslRevisionDiagram<L,D,RC>>
								
{
	final static Logger logger = LoggerFactory.getLogger(XmlAttributeFactory.class);
	
	private Attribute q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlTypeFactory<L,D,RAT> xfType;
	private XmlRelationFactory<L,D,RC,REM,RE,RA,RER,RAT,ERD> xfRelation;
	
	public XmlAttributeFactory(Attribute q)
	{
		this.q=q;
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<>(q.getDescriptions());}
		if(Objects.nonNull(q.getType())) {xfType = new XmlTypeFactory<>(q.getType());}
		if(Objects.nonNull(q.getRelation())) {xfRelation = new XmlRelationFactory<>(q.getRelation());}
	}
	
	public Attribute build(RA ejb)
	{
		Attribute xml = new Attribute();
		
		if(Objects.nonNull(q.getId())){xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getType())) {xml.setType(xfType.build(ejb.getType()));}
		
		if(Objects.nonNull(q.getXpath())) {xml.setXpath(ejb.getXpath());}
//		if(q.isSetJpa()){xml.setJpa(ejb.get);
		
		if(Objects.nonNull(q.isWeb())) {xml.setWeb(ejb.isShowWeb());}
		if(Objects.nonNull(q.isPrint())) {xml.setPrint(ejb.isShowPrint());}
		if(Objects.nonNull(q.isName())) {xml.setName(ejb.isShowName());}
		if(Objects.nonNull(q.isEnclosure())) {xml.setEnclosure(ejb.isShowEnclosure());}
		if(Objects.nonNull(q.isUi()))
		{
			if(ejb.getUi()==null){xml.setUi(false);}
			else{xml.setUi(ejb.getUi());}
		}
		if(Objects.nonNull(q.isBean()))
		{
			if(ejb.getBean()==null){xml.setBean(false);}
			else {xml.setBean(ejb.getBean());}
		}
		if(Objects.nonNull(q.isConstruction()))
		{
			if(ejb.getConstruction()==null){xml.setConstruction(false);}
			else {xml.setConstruction(ejb.getConstruction());}
		}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}
		if(Objects.nonNull(q.getRemark())) {xml.setRemark(XmlRemarkFactory.build(ejb.getDeveloperInfo()));}
		
		if(Objects.nonNull(q.getRelation()) && ejb.getRelation()!=null){xml.setRelation(xfRelation.build(ejb));}
		
		return xml;
	}
}