package org.jeesl.factory.xml.system.io.revision;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.factory.xml.system.status.XmlCategoryFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.jeesl.QueryRevision;
import org.jeesl.model.xml.system.revision.Diagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDiagramFactory <L extends JeeslLang,D extends JeeslDescription,
								RC extends JeeslRevisionCategory<L,D,RC,?>,
								ERD extends JeeslRevisionDiagram<L,D,RC>
							>
{
	final static Logger logger = LoggerFactory.getLogger(XmlDiagramFactory.class);
	
	private Diagram q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlCategoryFactory<L,D,RC> xfCategory;
	
	public XmlDiagramFactory(QueryRevision q){this(q.getDiagram());}
	public XmlDiagramFactory(Diagram q)
	{
		this.q=q;
		
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<>(q.getDescriptions());}
		if(q.isSetCategory()){xfCategory = new XmlCategoryFactory<>(q.getCategory());}
	}
	
	public static Diagram build(String code) {Diagram xml = build();xml.setCode(code);return xml;}
	public static Diagram build() {return new Diagram();}
	
	public Diagram build(ERD ejb)
	{
		Diagram xml = build();
		
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(q.isSetCode()&&ejb.getCode()!=""){xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
		
		if(q.isSetDocumentation()) {xml.setDocumentation(ejb.isDocumentation());}
		if(q.isSetCategory()){xml.setCategory(xfCategory.build(ejb.getCategory()));}		
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}
		
		return xml;
	}

}