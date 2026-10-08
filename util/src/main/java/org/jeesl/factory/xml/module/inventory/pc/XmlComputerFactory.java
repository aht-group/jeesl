package org.jeesl.factory.xml.module.inventory.pc;

import org.jeesl.model.xml.module.inventory.pc.Computer;
import org.jeesl.model.xml.module.inventory.pc.Hardware;
import org.jeesl.model.xml.module.inventory.pc.Software;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlComputerFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlComputerFactory.class);
		
	public static Computer build(){return build(null,null,null);}
	public static Computer build(Long id){return build(id,null,null);}
	
	public static Computer build(Hardware hardware, Software software){return build(null,hardware,software);}
	
	public static Computer build(Software software){return build(null,null,software);}
	
	public static Computer build(Long id, Hardware hardware, Software software)
	
	{
		Computer xml = new Computer();
		
		if(id!=null){xml.setId(id);}
		if(hardware!=null){xml.setHardware(hardware);}
		if(software!=null){xml.setSoftware(software);}
		
		return xml;
	}
}