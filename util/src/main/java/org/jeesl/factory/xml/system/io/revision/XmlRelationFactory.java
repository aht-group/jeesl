package org.jeesl.factory.xml.system.io.revision;

import java.util.Objects;

import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.label.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlRelationFactory <L extends JeeslLang,D extends JeeslDescription,
									RC extends JeeslRevisionCategory<L,D,RC,?>,	
									REM extends JeeslRevisionEntityMapping<?,?,?>,
									RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,										
									RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
									RER extends JeeslStatus<L,D,RER>,
									RAT extends JeeslStatus<L,D,RAT>,
									ERD extends JeeslRevisionDiagram<L,D,RC>>

{

	final static Logger logger = LoggerFactory.getLogger(XmlRelationFactory.class);
	
	private Relation q;

	private XmlEntityFactory<L,D,RC,REM,RE,RA,RER,RAT,ERD> xfEntity;
	private XmlTypeFactory<L,D,RER> xfType;
	
	public XmlRelationFactory(Relation q)
	{
		this.q=q;
		if(Objects.nonNull(q.getEntity())) {xfEntity = new XmlEntityFactory<>(q.getEntity());}
		if(Objects.nonNull(q.getType())) {xfType = new XmlTypeFactory<>(q.getType());}
	}
	
	public static Relation build() {return new Relation();}	
	
	public Relation build(RA attribute) 
	{
		Relation xml = new Relation();
		if(Objects.nonNull(q.isDocOptionsTable())) {xml.setDocOptionsTable(BooleanComparator.active(attribute.getStatusTableDoc()));}
		if(Objects.nonNull(q.isDocOptionsInline())) {xml.setDocOptionsInline(BooleanComparator.active(attribute.getDocOptionsInline()));}
		
		if(Objects.nonNull(q.getEntity()))
		{
			if(attribute.getEntity()==null){xml.setEntity(XmlEntityFactory.build());}
			else {xml.setEntity(xfEntity.build(attribute.getEntity()));}
		}	
		
		if(Objects.nonNull(q.getType())) {xml.setType(xfType.build(attribute.getRelation()));}
		
		
		
		return xml;
	}


}
