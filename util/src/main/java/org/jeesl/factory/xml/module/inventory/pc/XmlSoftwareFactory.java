package org.jeesl.factory.xml.module.inventory.pc;

import org.jeesl.model.xml.module.inventory.pc.Software;
import org.jeesl.model.xml.module.inventory.pc.Updates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSoftwareFactory 
{
	
	final static Logger logger = LoggerFactory.getLogger(XmlSoftwareFactory.class);
	
	public static Software build(Long id){return build(id,null);}
	
	public static Software build(Updates updates){return build(null,updates);}
	
	public static Software build(Long id, Updates updates)
	
	{
		Software xml = new Software();
		
		if(id!=null){xml.setId(id);}
		if(updates!=null){xml.setUpdates(updates);}
		
		return xml;
	}
}


