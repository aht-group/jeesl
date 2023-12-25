package org.jeesl.factory.xml.dev.qa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.qa.UtilsQaGroup;
import net.sf.ahtutils.xml.qa.Group;

public class XmlGroupFactory<GROUP extends UtilsQaGroup<?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlGroupFactory.class);
		
	private Group q;
	
	public XmlGroupFactory(Group q)
	{
		this.q=q;
	}
	
	public Group build(GROUP group)
	{
		Group xml = new Group();
		if(q.isSetId()){xml.setId(group.getId());}
		
		if(q.isSetName()){xml.setName(group.getName());}
		if(q.isSetDescription()){xml.setDescription(org.jeesl.factory.xml.system.lang.XmlDescriptionFactory.build(group.getDescription()));}
		
		return xml;
	}
	
	public static Group build(String name)
	{
		Group xml = new Group();
		xml.setName(name);
		return xml;
	}
}