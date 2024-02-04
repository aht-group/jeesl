package org.jeesl.factory.xml.module.inventory.pc;

import java.util.Date;

import org.exlp.util.system.DateUtil;
import org.jeesl.model.xml.module.inventory.pc.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUpdateFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlUpdateFactory.class);
	
	public static Update build(long id){return build(id,null,null,null);}
	
	public static Update build(String code, Date record, String description){return build(null,code,record,description);}
	
	public static Update build(Date record, String description){return build(null,null,record,description);}
	
	public static Update build(String description){return build(null,null,null,description);}
	
	public static Update build(Long id,String code, Date record, String description)
	{
		Update xml = new Update();
		if(id!=null){xml.setId(id);}
		if(code!=null){xml.setCode(code);}
		if(record!=null){xml.setRecord(DateUtil.toXmlGc(record));}
		if(description!=null){xml.setDescription(description);}
		return xml;
	}
}
