package net.sf.ahtutils.factory.xml.sync;

import net.sf.ahtutils.factory.xml.status.XmlStatusFactory;
import net.sf.ahtutils.xml.sync.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlResultFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlResultFactory.class);
		
	public static Result buildOk(){return build("success");}
	public static Result buildFail() {return build("fail");}
	
	public static Result build(String statusCode)
	{
		Result xml = new Result();
		xml.setStatus(XmlStatusFactory.create(statusCode));
		return xml;
	}
}