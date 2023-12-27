package org.jeesl.factory.xml.system.io.sync;

import org.jeesl.model.xml.io.ssi.sync.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlMapperFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlMapperFactory.class);
		
	public static Mapper build(long oldId, long newId)
	{
		Mapper xml = new Mapper();
		xml.setOldId(oldId);
		xml.setNewId(newId);
		return xml;
	}
	
	public static Mapper create(Class<?> c, long oldId, long newId)
	{
		Mapper xml = new Mapper();
		xml.setClazz(c.getName());
		xml.setOldId(oldId);
		xml.setNewId(newId);
		return xml;
	}
	
	public static Mapper create(Class<?> c, String oldCode, String newCode)
	{
		Mapper xml = new Mapper();
		xml.setClazz(c.getName());
		xml.setOldCode(oldCode);
		xml.setNewCode(newCode);
		return xml;
	}
	
	public static Mapper build(String c, String code)
	{
		Mapper xml = new Mapper();
		xml.setClazz(c);
		xml.setCode(code); 
		return xml;
	}
}