package org.jeesl.controller.db.cache;

import java.util.HashMap;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SsiCodeCache <MAPPING extends JeeslIoSsiMapping<?,ENTITY>,
							ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
							ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
							T extends EjbWithCode>
{
	final static Logger logger = LoggerFactory.getLogger(SsiCodeCache.class);

	private final Class<T> cT;
	
	private final Map<String,T> map;
	
	public SsiCodeCache(IoSsiDataFactoryBuilder<?,?,?,MAPPING,ATTRIBUTE,?,?,ENTITY,?,?> fbSsi,
						JeeslIoSsiFacade<?,?,?,?,MAPPING,ATTRIBUTE,?,?,ENTITY,?,?,?> fSsi,
						Class<T> cT)
	{
		this.cT=cT;
		map = new HashMap<>();
		
		for(ATTRIBUTE a : fSsi.all(fbSsi.getClassAttribute()))
		{
			if(a.getEntity().getCode().equals(cT.getName()))
			{
				try
				{
					T t = fSsi.fByCode(cT, a.getLocalCode());
					map.put(a.getRemoteCode(),t);
				}
				catch (JeeslNotFoundException e) {e.printStackTrace();}
			}
		}
	}
	
	public T ejb(String code) throws JeeslNotFoundException
	{
		if(!map.containsKey(code)) {throw new JeeslNotFoundException();}
		return map.get(code);
	}
}