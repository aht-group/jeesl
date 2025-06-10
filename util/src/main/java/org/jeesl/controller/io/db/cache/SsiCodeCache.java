package org.jeesl.controller.io.db.cache;

import java.util.HashMap;
import java.util.Map;

import org.exlp.util.io.StringUtil;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SsiCodeCache <CONTEXT extends JeeslIoSsiContext<?,ENTITY>,
							ATTRIBUTE extends JeeslIoSsiAttribute<CONTEXT,ENTITY>,
							ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
							T extends EjbWithCode>
{
	final static Logger logger = LoggerFactory.getLogger(SsiCodeCache.class);
	
	private final JeeslIoSsiFacade<?,?,CONTEXT,ATTRIBUTE,?,?,?,ENTITY,?,?,?> fSsi;
	private final IoSsiDataFactoryBuilder<?,?,?,CONTEXT,ATTRIBUTE,?,?,?,ENTITY,?,?> fbSsi;
	
	private final Map<String,T> map; public Map<String,T> getMap() {return map;}
	
	private final Class<T> cT;

	public static <MAPPING extends JeeslIoSsiContext<?,ENTITY>,
					ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
					ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
					T extends EjbWithCode>
	SsiCodeCache<MAPPING,ATTRIBUTE,ENTITY,T> instance(IoSsiDataFactoryBuilder<?,?,?,MAPPING,ATTRIBUTE,?,?,?,ENTITY,?,?> fbSsi,
			JeeslIoSsiFacade<?,?,MAPPING,ATTRIBUTE,?,?,?,ENTITY,?,?,?> fSsi,
			Class<T> cT)
	{
		return new SsiCodeCache<>(fbSsi,fSsi,cT);
	}
	public SsiCodeCache(IoSsiDataFactoryBuilder<?,?,?,CONTEXT,ATTRIBUTE,?,?,?,ENTITY,?,?> fbSsi,
						JeeslIoSsiFacade<?,?,CONTEXT,ATTRIBUTE,?,?,?,ENTITY,?,?,?> fSsi,
						Class<T> cT)
	{
		this.fbSsi=fbSsi;
		this.fSsi=fSsi;
		this.cT=cT;
		map = new HashMap<>();
		
		this.reload();
	}
	
	public SsiCodeCache<CONTEXT,ATTRIBUTE,ENTITY,T> reload()
	{
		map.clear();
		for(ATTRIBUTE a : fSsi.all(fbSsi.getClassAttribute()))
		{
//			logger.info(a.toString());
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
		return this;
	}
	
	public SsiCodeCache<CONTEXT,ATTRIBUTE,ENTITY,T> reload(CONTEXT context)
	{
		map.clear();
		for(ATTRIBUTE a : fSsi.all(fbSsi.getClassAttribute()))
		{
			
			if(a.getMapping().equals(context) && a.getEntity().getCode().equals(cT.getName()))
			{
				try
				{
					T t = fSsi.fByCode(cT, a.getLocalCode());
					map.put(a.getRemoteCode(),t);
				}
				catch (JeeslNotFoundException e) {e.printStackTrace();}
			}
		}
		return this;
	}
	
	
	public T ejb(String code) throws JeeslNotFoundException
	{
		if(!map.containsKey(code)) {throw new JeeslNotFoundException();}
		return map.get(code);
	}
	
	public void debug()
	{
		logger.info(StringUtil.stars());
		logger.info(cT.getName());
		
		for(String s : map.keySet())
		{
			
			logger.info(s+" - "+map.get(s).getCode());
		}
	}
}