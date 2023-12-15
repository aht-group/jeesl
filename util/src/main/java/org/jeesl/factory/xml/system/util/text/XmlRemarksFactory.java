package org.jeesl.factory.xml.system.util.text;

import org.jeesl.model.xml.io.cms.text.Remarks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlRemarksFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlRemarksFactory.class);
	
	public static Remarks build() {return new Remarks();}
}