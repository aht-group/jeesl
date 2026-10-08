package org.jeesl.factory.xml.module.inventory.pc;

import org.jeesl.model.xml.module.inventory.pc.Hardware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlHardwareFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlHardwareFactory.class);
		
	public static Hardware build(Long id){return build(id,null,null,null);}
	
	public static Hardware build(String manufacturer, String model, String serial){return build(null,manufacturer,model,serial);}

	public static Hardware build(String model, String serial){return build(null,null,model,serial);}
	
	public static Hardware build(String serial){return build(null,null,null,serial);}
		
	public static Hardware build(Long id, String manufacturer, String model, String serial)
	{
		Hardware xml = new Hardware();
		if(id!=null){xml.setId(id);}
		if(manufacturer!=null){xml.setManufacturer(manufacturer);}
		if(model!=null){xml.setModel(model);}
		if(serial!=null){xml.setSerial(serial);}
		return xml;
	}
}
