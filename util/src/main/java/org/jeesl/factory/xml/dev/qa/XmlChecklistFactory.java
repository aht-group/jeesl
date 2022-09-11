package org.jeesl.factory.xml.dev.qa;

import org.jeesl.model.xml.system.security.Staff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.qa.Checklist;

public class XmlChecklistFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlChecklistFactory.class);
	
	public static Checklist build(Staff staff)
	{
		Checklist xml = new Checklist();
		xml.setStaff(staff);
		return xml;
	}
}