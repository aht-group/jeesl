package org.jeesl.controller.db.cache;

import java.util.HashMap;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SsiAttributeCache <MAPPING extends JeeslIoSsiContext<?,ENTITY>,
							ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
							ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
							T extends EjbWithCode>
{
	final static Logger logger = LoggerFactory.getLogger(SsiAttributeCache.class);
	
	private final JeeslIoSsiFacade<?,?,?,?,MAPPING,ATTRIBUTE,?,?,ENTITY,?,?,?> fSsi;
	private final IoSsiDataFactoryBuilder<?,?,?,MAPPING,ATTRIBUTE,?,?,ENTITY,?,?> fbSsi;
	
	private final Class<T> cT;
	
	private final Map<String,String> mapRemoteError;
	private final Map<String,ATTRIBUTE> mapRemoteAttribute;
	private final Map<ATTRIBUTE,T> mapAttributeEntity;
	private final Map<T,ATTRIBUTE> mapEntityAttribute;
	
	private final MAPPING mapping;
	
	public SsiAttributeCache(IoSsiDataFactoryBuilder<?,?,?,MAPPING,ATTRIBUTE,?,?,ENTITY,?,?> fbSsi,
						JeeslIoSsiFacade<?,?,?,?,MAPPING,ATTRIBUTE,?,?,ENTITY,?,?,?> fSsi,
						MAPPING mapping,
						Class<T> cT)
	{
		this.fSsi=fSsi;
		this.fbSsi=fbSsi;
		this.mapping=mapping;
		this.cT=cT;
		
		mapRemoteError = new HashMap<>();
		mapRemoteAttribute = new HashMap<>();
		mapAttributeEntity = new HashMap<>();
		mapEntityAttribute = new HashMap<>();
	}
	
	public T ejb(String remoteCode) throws JeeslNotFoundException
	{
		if(mapRemoteError.containsKey(remoteCode)) {throw new JeeslNotFoundException(mapRemoteError.get(remoteCode));}
		ATTRIBUTE attribute = findAttribute(remoteCode);
		return findEntity(attribute);
	}
	
	private ATTRIBUTE findAttribute(String remoteCode) throws JeeslNotFoundException
	{
		if(mapRemoteAttribute.containsKey(remoteCode)) {return mapRemoteAttribute.get(remoteCode);}
		else
		{
			for(ATTRIBUTE a : fSsi.allForParent(fbSsi.getClassAttribute(),mapping))
			{
				if(a.getRemoteCode().equals(remoteCode))
				{
					mapRemoteAttribute.put(remoteCode, a);
					return a;
				}
			}
		}
		
		String error = "No Attribute Mapping for "+cT.getSimpleName()+" remoteCode="+remoteCode;
		mapRemoteError.put(remoteCode, error);
		throw new JeeslNotFoundException(error);
	}
	
	public ATTRIBUTE findAttribute(T t) throws JeeslNotFoundException
	{
		if(mapEntityAttribute.containsKey(t)) {return mapEntityAttribute.get(t);}
		else
		{
			for(ATTRIBUTE a : fSsi.allForParent(fbSsi.getClassAttribute(),mapping))
			{
				
				if(a.getLocalCode().equals(t.getCode()))
				{
					mapEntityAttribute.put(t,a);
					return a;
				}
			}
		}
		String error = "No Attribute Mapping for "+cT.getSimpleName()+" and localCode="+t.getCode();
		throw new JeeslNotFoundException(error);
	}
	
	private T findEntity(ATTRIBUTE attribute) throws JeeslNotFoundException
	{
		if(mapAttributeEntity.containsKey(attribute)) {return mapAttributeEntity.get(attribute);}
		else
		{
			try
			{
				T t = fSsi.fByCode(cT,attribute.getLocalCode());
				mapAttributeEntity.put(attribute, t);
				return t;
			}
			catch (JeeslNotFoundException e) {}
		}
		String error = "No "+cT.getSimpleName()+" with remoteCode="+attribute.getRemoteCode()+" localCode="+attribute.getLocalCode();
		mapRemoteError.put(attribute.getRemoteCode(), error);
		throw new JeeslNotFoundException(error);
	}
}