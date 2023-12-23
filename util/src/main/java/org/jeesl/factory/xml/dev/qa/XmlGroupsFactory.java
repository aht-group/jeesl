package org.jeesl.factory.xml.dev.qa;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.model.xml.module.dev.qa.Groups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.qa.UtilsQaGroup;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaTest;

public class XmlGroupsFactory<GROUP extends UtilsQaGroup<?,?,?>,
								QAT extends UtilsQaTest<GROUP,?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlGroupsFactory.class);
		
	private Groups q;
	
	private XmlGroupFactory<GROUP> xfGroup;
	
	
	public XmlGroupsFactory(Groups q)
	{
		this.q=q;
		if(ObjectUtils.isNotEmpty(q.getGroup())) {xfGroup  = new XmlGroupFactory<GROUP>(q.getGroup().get(0));}
	}
	
	public Groups build(QAT test)
	{
		Groups xml = new Groups();
		
		if(q.isSetGroup() && test.getGroups()!=null)
		{
			for(GROUP g : test.getGroups())
			{
				xml.getGroup().add(xfGroup.build(g));
			}
		}
		
		return xml;
	}
	
	public static Groups build()
	{
		Groups xml = new Groups();
		
		return xml;
	}
}