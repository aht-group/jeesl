package org.jeesl.controller.io.db.cache;

import java.util.HashMap;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SsiCache <MAPPING extends JeeslIoSsiContext<?,?>,
						DATA extends JeeslIoSsiData<MAPPING,LINK,?,?>,
						LINK extends JeeslIoSsiStatus<?,?,LINK,?>,
						T extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(SsiCache.class);

	private final JeeslIoSsiFacade<?,?,MAPPING,?,DATA,LINK,?,?,?,?,?> fSsi;
	private final Class<T> cT;
	private final MAPPING mapping;
	
	private final Map<String,T> map;
	
	public SsiCache(IoSsiDataFactoryBuilder<?,?,?,MAPPING,?,?,?,?,?,?,?> fbSsi,
						JeeslIoSsiFacade<?,?,MAPPING,?,DATA,LINK,?,?,?,?,?> fSsi,
						Class<T> cT,
						MAPPING mapping
						)
	{
		this.fSsi=fSsi;
		this.mapping=mapping;
		this.cT=cT;
		map = new HashMap<>();

	}
	
	public T ejb(String code) throws JeeslNotFoundException
	{
		if(!map.containsKey(code))
		{
			DATA data = fSsi.fIoSsiData(mapping,code);
			if(!data.getLink().getCode().equals(JeeslIoSsiStatus.Code.linked.toString())) {throw new JeeslNotFoundException("Not Linked");}
			if(data.getLocalId()==null) {throw new JeeslNotFoundException("No LocalId");}
			T t = fSsi.find(cT,data.getLocalId());
			map.put(code,t);
		}
		return map.get(code);
	}
}