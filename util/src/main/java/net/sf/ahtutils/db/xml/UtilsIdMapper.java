package net.sf.ahtutils.db.xml;

import java.util.Hashtable;
import java.util.Map;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import org.jeesl.model.xml.io.ssi.sync.Mapper;
import org.jeesl.model.xml.io.ssi.sync.Mappings;
import net.sf.exlp.util.xml.JaxbUtil;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.xml.system.io.sync.XmlMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsIdMapper
{
	final static Logger logger = LoggerFactory.getLogger(XmlMapperFactory.class);
	
	// Mapping of IDs and CODEs
	private Map<Class<?>,Map<Long,Long>>     map;
	private Map<Class<?>,Map<String,String>> mapCode;
	
	// Mapping of Objects
	private Map<Class<?>,Map<String,Object>> mapObjectByCode;
	private Map<Class<?>,Map<Long  ,Object>> mapObjectById;
	
	public UtilsIdMapper()
	{
		map             = new Hashtable<Class<?>,Map<Long,Long>>();
		mapCode         = new Hashtable<Class<?>,Map<String,String>>();
		
		mapObjectByCode = new Hashtable<Class<?>,Map<String,Object>>();
		mapObjectById   = new Hashtable<Class<?>,Map<Long  ,Object>>();
	}
	
	public DataUpdate add(DataUpdate dataUpdate)
	{
		for(Mapper mapper : dataUpdate.getMapper())
		{
			add(mapper);
		}
		return dataUpdate;
	}
	
	public void add(Mappings mappings)
	{
		for(Mapper mapper : mappings.getMapper())
		{
			add(mapper);
		}
	}
	
	public void add(Mapper mapper)
	{
		try
		{
			Class<?> c = Class.forName(mapper.getClazz());
			if(mapper.isSetOldId() && mapper.isSetNewId())
			{
				if(!map.containsKey(c)){map.put(c, new Hashtable<Long,Long>());}
				map.get(c).put(mapper.getOldId(), mapper.getNewId());
			}
			else if(mapper.isSetOldCode() && mapper.isSetNewCode())
			{
				if(!mapCode.containsKey(c)){mapCode.put(c, new Hashtable<String,String>());}
				mapCode.get(c).put(mapper.getOldCode(), mapper.getNewCode());
			}
			else
			{
				logger.warn("No Mapping info!");
				JaxbUtil.warn(mapper);
				
			}
		}
		catch (ClassNotFoundException e) {e.printStackTrace();}
	}
	
	public long getMappedId(Class<?> c,long oldId) throws UtilsConfigurationException
	{
		if(!map.containsKey(c)){throw new UtilsConfigurationException(this.getClass().getSimpleName()+" does contain information for "+c.getSimpleName()+".id="+oldId);}
		if(!map.get(c).containsKey(oldId)){throw new UtilsConfigurationException("Map for "+c.getSimpleName()+" does not have an entry for id="+oldId);}
		return map.get(c).get(oldId);
	}
	
	public String getMappedCode(Class<?> c,String oldCode) throws UtilsConfigurationException
	{
		if(!mapCode.containsKey(c)){throw new UtilsConfigurationException(this.getClass().getSimpleName()+" does contain information for "+c.getSimpleName()+".code="+oldCode);}
		if(!mapCode.get(c).containsKey(oldCode)){throw new UtilsConfigurationException("Map for "+c.getSimpleName()+" does not have an entry for oldCode="+oldCode);}
		return mapCode.get(c).get(oldCode);
	}
	
	public void debug()
	{
		if(map.size()>0)
		{
			logger.info(this.getClass().getSimpleName()+" with "+map.keySet().size()+" classes");
			for(Class<?> c : map.keySet())
			{
				logger.info("\t"+c.getSimpleName()+" "+map.get(c).keySet().size());
			}
		}
		else if(mapCode.size()>0)
		{
			logger.info(this.getClass().getSimpleName()+" with "+mapCode.keySet().size()+" classes");
			for(Class<?> c : mapCode.keySet())
			{
				logger.info("\t"+c.getSimpleName()+" "+mapCode.get(c).keySet().size());
			}
		}
	}
	
	public Boolean isMapped(Class<?> c,Object code)
	{
		if(!mapCode.containsKey(c)){return false;}
		if(!mapCode.get(c).containsKey(code)){return false;}
		return true;
	}
	
	/**
	 * See if an Object is mapped for the given class and the given key.
	 * Method is auto-sensing whether to look for a mapped ID or Code key,
	 * depending on the class of the key parameter
	 * @param c   Class to be searched for
	 * @param key Key to be searched for
	 * @return Is there an object mapped for that key
	 */
	public Boolean isObjectMapped(Class<?> c,Object key)
	{
		if(key.getClass().equals(java.lang.String.class))
		{
			if (mapObjectByCode.get(c) == null)
			{
				return false;
			}
			else
			{
				return mapObjectByCode.get(c).containsKey(key);
			}
		}
		else
		{
			if (mapObjectById.get(c) == null)
			{
				return false;
			}
			else
			{
				return mapObjectById.get(c).containsKey(key);
			}
		}
	}
	
	/**
	 * Get an Object mapped for the given class and the given key.
	 * Method is auto-sensing whether to look for a mapped ID or Code key,
	 * depending on the class of the key parameter
	 * @param c   Class to be searched for
	 * @param key Key to be searched for
	 * @return The mapped object
	 */
	public Object getMappedObject(Class<?> c,Object key)
	{
		logger.trace("Requesting " +key.toString() +" for class " +c.getName());
		Object o = null;
		if(key.getClass().equals(java.lang.String.class))
		{
			o = mapObjectByCode.get(c).get(key);
		}
		else
		{
			o = mapObjectById.get(c).get(key);
		}
		logger.trace("Returning " +o.toString());
		return o;
	}
	
	/*
	 * Add an object with given code to the mapping
	 * @param code  Code to be used for identification
	 * @param o     Object to be mapped with the given code
	 * @return The mapped object
	 */
	public void addObjectForCode(String code, Object o)
	{
                Map<String, Object> objectMap = null;
		if (mapObjectByCode!= null && o !=null && mapObjectByCode.get(o.getClass())!= null)
		{
                    objectMap = mapObjectByCode.get(o.getClass());
                    objectMap.put(code, o);
		}
                else if (o != null)
                {
                    Hashtable<String,Object> map = new Hashtable<String,Object>();
                    mapObjectByCode.put(o.getClass(), map);
                    objectMap = mapObjectByCode.get(o.getClass());
                    objectMap.put(code, o);
                }
	}
	
	/*
	 * Add an object with given id to the mapping
	 * @param id    Id to be used for identification
	 * @param o     Object to be mapped with the given id
	 * @return The mapped object
	 */
	public void addObjectForId(Long id, Object o)
	{
		if (mapObjectById.get(o.getClass()) == null)
		{
			Hashtable<Long,Object> mapForIds = new Hashtable<Long,Object>();
			mapObjectById.put(o.getClass(), mapForIds);
		}
		Hashtable<Long, Object> objectMap = (Hashtable<Long, Object>) mapObjectById.get(o.getClass());
		objectMap.put(id, o);
		mapObjectById.put(o.getClass(), objectMap);
	}
}
